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

    @Value("${media.location}")
    private String mediaLocation;

    private Path rootLocation;

    @Autowired
    ConsentRepository consentRepository;

    @Autowired
    InscriptionRepository inscriptionRepository;

    @PostConstruct
    public void init() throws IOException {
        rootLocation = Paths.get(mediaLocation);
        Files.createDirectories(rootLocation);
    }

//    public boolean saveConsentFiles(int inscriptionId, MultipartFile[] files, ConsentTypeEnum[] types) {
//        for (int i = 0; i < files.length; i++) {
//            MultipartFile file = files[i];
//            ConsentTypeEnum type = types[i];
//
//            Inscription inscription = inscriptionRepository.findInscriptionById(inscriptionId);
//
//            if(inscription != null){
//                String filePath = saveFileInConsentTypeFolder(inscription.getInscriptionDate(),
//                        inscription.getUserId(), file, type);
//                if(!filePath.isEmpty()){
//                    Consent consent = new Consent();
//                    consent.setConsentType(type);
//                    consent.setFilePath(filePath);
//                    consent.setInscription(inscription);
//                    consent = consentRepository.save(consent);
//
//                    if(consent.getId() == 0){
//                        return false;
//                    }
//                }else{
//                    return false;
//                }
//            }else{
//                return false;
//            }
//        }
//        return true;
//    }

    public boolean saveConsentFiles(int inscriptionId, MultipartFile file, ConsentTypeEnum type) {
        Inscription inscription = inscriptionRepository.findInscriptionById(inscriptionId);

        if(inscription != null){
            String filePath = saveFileInConsentTypeFolder(inscription.getInscriptionDate(),
                    inscription.getUserId(), file, type);
            if(!filePath.isEmpty()){
                Consent consent = new Consent();
                consent.setConsentType(type);
                consent.setFilePath(filePath);
                consent.setInscription(inscription);
                consent = consentRepository.save(consent);

                if(consent.getId() == 0){
                    return false;
                }
            }else{
                return false;
            }
        }else{
            return false;
        }
        return true;
    }

    private String saveFileInConsentTypeFolder(Date inscriptionDate, int userId, MultipartFile file, ConsentTypeEnum consentType){
        LocalDate localDate = inscriptionDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

        String filePath = consentType + "_" + userId + "_" + localDate.getDayOfMonth() + "_" + localDate.getMonthValue() + "_" + localDate.getYear() + "_" + file.getOriginalFilename();
        Path path = rootLocation.resolve(Paths.get(filePath))
                .normalize().toAbsolutePath();
        try {
            InputStream inputStream = file.getInputStream();
            Files.copy(inputStream, path, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e){
            return "";
        }
        return path.toString();
    }

    public Resource loadResource(int inscriptionId, ConsentTypeEnum consentTypeEnum) {
        try {
            String filePath = consentRepository.findFilePathByInscriptionIdAndConsentType(inscriptionId, consentTypeEnum);
            Path path = Paths.get(filePath);
            Resource resource = new UrlResource(path.toUri());

            if(resource.exists() || resource.isReadable()){
                return resource;
            }else{
                return null;
            }
        }catch (MalformedURLException e){
            return null;
        }
    }
}
