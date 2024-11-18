package com.uptc.cafmicroservice.service;

import com.uptc.cafmicroservice.dto.InscriptionDTO;
import com.uptc.cafmicroservice.entity.Consent;
import com.uptc.cafmicroservice.entity.Inscription;
import com.uptc.cafmicroservice.enums.ConsentTypeEnum;
import com.uptc.cafmicroservice.repository.ConsentRepository;
import com.uptc.cafmicroservice.repository.InscriptionRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Service
public class ConsentService {

    // Inyección de la propiedad mediaLocation desde el archivo de configuración de la aplicación
    @Value("${media.location}")
    private String mediaLocation;

    // Variable para almacenar la ubicación raíz donde se guardan los archivos
    private Path rootLocation;

    // Inyección de la dependencia de ConsentRepository
    @Autowired
    ConsentRepository consentRepository;

    // Inyección de la dependencia de InscriptionRepository
    @Autowired
    InscriptionRepository inscriptionRepository;

    /**
     * Método de inicialización que se ejecuta después de la construcción del bean.
     * Configura la ruta de la ubicación raíz y crea los directorios si no existen.
     * @throws IOException si ocurre un error al crear los directorios.
     */
    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation); // Crea los directorios en la ubicación especificada si no existen
    }

    /**
     * Guarda archivos de consentimiento para una inscripción específica.
     * @param inscriptionId El ID de la inscripción.
     * @param file El archivo que se va a guardar.
     * @param type El tipo de consentimiento.
     * @return true si el archivo se guarda correctamente, false en caso contrario.
     */
    public boolean saveConsentFiles(int inscriptionId, MultipartFile file, ConsentTypeEnum type) {
        // Busca la inscripción por ID
        Inscription inscription = inscriptionRepository.findInscriptionById(inscriptionId);

        if (inscription != null) {
            // Guarda el archivo en la carpeta correspondiente al tipo de consentimiento
            String filePath = saveFileInConsentTypeFolder(inscription.getInscriptionDate(),
                    inscription.getUserId(), file, type);
            if (!filePath.isEmpty()) {
                // Crea y guarda un objeto Consent en la base de datos
                Consent consent = new Consent();
                consent.setConsentType(type);
                consent.setFilePath(filePath);
                consent.setInscription(inscription);
                consent = consentRepository.save(consent);

                // Verifica si la operación de guardado fue exitosa
                if (consent.getId() == 0) {
                    return false; // Devuelve false si el ID del consentimiento es 0 (error al guardar)
                }
            } else {
                return false; // Devuelve false si no se pudo guardar el archivo
            }
        } else {
            return false; // Devuelve false si la inscripción no existe
        }
        return true; // Devuelve true si el archivo se guarda correctamente
    }

    /**
     * Guarda un archivo en la carpeta correspondiente al tipo de consentimiento.
     * @param inscriptionDate La fecha de inscripción.
     * @param userId El ID del usuario.
     * @param file El archivo a guardar.
     * @param consentType El tipo de consentimiento.
     * @return La ruta del archivo guardado o una cadena vacía si falla.
     */
    private String saveFileInConsentTypeFolder(Date inscriptionDate, int userId, MultipartFile file, ConsentTypeEnum consentType) {
        // Convierte la fecha de inscripción a LocalDate
        LocalDate localDate = inscriptionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        // Construye el nombre de archivo
        String filePath = consentType + "_" + userId + "_" + localDate.getDayOfMonth() + "_" + localDate.getMonthValue() + "_" + localDate.getYear() + "_" + file.getOriginalFilename();
        Path path = rootLocation.resolve(Paths.get(filePath))
                .normalize().toAbsolutePath();
        try {
            // Copia el archivo al sistema de archivos
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            return ""; // Devuelve una cadena vacía si ocurre un error
        }
        return path.toString(); // Devuelve la ruta completa del archivo guardado
    }

    /**
     * Carga un recurso (archivo) basado en el ID de la inscripción y el tipo de consentimiento.
     * @param inscriptionId El ID de la inscripción.
     * @param consentTypeEnum El tipo de consentimiento.
     * @return El recurso cargado o null si no se puede cargar.
     */
    public Resource loadResource(int inscriptionId, ConsentTypeEnum consentTypeEnum) {
        try {
            // Busca la ruta del archivo en la base de datos
            String filePath = consentRepository.findFilePathByInscriptionIdAndConsentType(inscriptionId, consentTypeEnum);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            // Verifica si el recurso existe y es legible
            if (resource.exists() || resource.isReadable()) {
                return resource; // Devuelve el recurso si es válido
            } else {
                return null; // Devuelve null si no se puede leer el recurso
            }
        } catch (MalformedURLException e) {
            return null; // Devuelve null si ocurre un error al construir la URL del recurso
        }
    }
}
