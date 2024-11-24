package com.uptc.authmicroservice.security;

import com.uptc.authmicroservice.dto.RequestDTO;
import com.uptc.authmicroservice.entity.AuthUser;
import com.uptc.authmicroservice.entity.Role;
import com.uptc.authmicroservice.enums.RoleEnum;
import com.uptc.authmicroservice.validator.*;
import io.jsonwebtoken.Claims;
//import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Componente para la gestión de tokens JWT, proporcionando funcionalidades
 * para la creación, validación y extracción de información de los tokens.
 */
@Component
public class JwtProvider {

    /**
     * Clave secreta utilizada para firmar los tokens.
     */
    @Value("${jwt.secret}")
    private String secret;

    @Autowired
    private AdminRouteValidator adminRouteValidator;

    @Autowired
    private UserRouteValidator userRouteValidator;

    @Autowired
    private CafCoordinatorRouteValidator cafCoordinatorRouteValidator;

    @Autowired
    private WellbeingDirectorRouteValidator wellbeingDirectorRouteValidator;

    @Autowired
    private SportsmanRouteValidator sportsmanRouteValidator;

    /**
     * Crea un token JWT para un usuario autenticado.
     *
     * @param user Usuario autenticado.
     * @return Token JWT firmado y en formato compactado.
     */
    public String createToken(AuthUser user) {
        Date issuedAt = new Date(System.currentTimeMillis());
        Date expirationDate = new Date(new Date().getTime() + 3600000); // Validez de 1 hora.

        return Jwts.builder()
                .header().type("JWT") // Define el tipo de token como JWT.
                .and()
                .subject(user.getUserName()) // Incluye el nombre de usuario como sujeto.
                .claim("Authorities", user.getRoles()) // Añade los roles del usuario como un claim.
                .issuedAt(issuedAt) // Fecha de emisión del token.
                .expiration(expirationDate) // Fecha de expiración del token.
                .signWith(generateSigningKey()) // Firma el token con la clave secreta.
                .compact();
    }

    /**
     * Genera la clave secreta para firmar los tokens.
     *
     * @return Clave secreta como un objeto SecretKey.
     */
    private SecretKey generateSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    /**
     * Inicializa la clave secreta codificándola en Base64.
     */
    @PostConstruct
    protected void init() {
        secret = Base64.getEncoder().encodeToString(secret.getBytes());
    }

    /**
     * Extrae los claims de un token JWT.
     *
     * @param jwtToken Token JWT a analizar.
     * @return Claims extraídos del token.
     */
    public Claims extractClaims(String jwtToken) {
        return Jwts.parser()
                .verifyWith(generateSigningKey())
                .build()
                .parseSignedClaims(jwtToken)
                .getPayload();
    }

    /**
     * Valida un token verificando su expiración y asociación con una ruta específica.
     *
     * @param token      Token a validar.
     * @param requestDTO Información de la solicitud para validar rutas.
     * @return true si el token es válido y está asociado a la ruta, false en caso contrario.
     */
    public boolean validate(String token, RequestDTO requestDTO) {
        try {
            if (!new Date().before(extractExpiration(token))) { // Verifica que el token no haya expirado.
                return false;
            }
        } catch (Exception e) {
            return false;
        }

        // Obtiene los validadores de ruta asociados con la solicitud.
        ArrayList<Integer> pathValidatorNumbers = obtainRouteValidatorFromPath(requestDTO);
        boolean isRoleAssociatedWithPath = false;

        // Verifica si algún rol del usuario está asociado a las rutas validadas.
        for (int i = 0; i < pathValidatorNumbers.size(); i++) {
            if (verifyRoleInRouteValidator(token, pathValidatorNumbers.get(i))) {
                isRoleAssociatedWithPath = true;
                break;
            }
        }

        return isRoleAssociatedWithPath;
    }

    /**
     * Determina los validadores de ruta aplicables a la solicitud.
     *
     * @param requestDTO Información de la solicitud.
     * @return Lista de identificadores de validadores aplicables.
     */
    private ArrayList<Integer> obtainRouteValidatorFromPath(RequestDTO requestDTO) {
        ArrayList<Integer> routeValidators = new ArrayList<>();
        if (adminRouteValidator.isAdminPath(requestDTO)) {
            routeValidators.add(0);
        }
        if (userRouteValidator.isUserPath(requestDTO)) {
            routeValidators.add(1);
        }
        if (wellbeingDirectorRouteValidator.isWellbeingDirectorPath(requestDTO)) {
            routeValidators.add(2);
        }
        if (cafCoordinatorRouteValidator.isCafCoordinatorPath(requestDTO)) {
            routeValidators.add(3);
        }
        if (sportsmanRouteValidator.isSportsmanPaths(requestDTO)) {
            routeValidators.add(4);
        } else {
            routeValidators.add(5);
        }
        return routeValidators;
    }

    /**
     * Verifica si un rol está asociado a un validador de ruta específico.
     *
     * @param token               Token del usuario.
     * @param pathValidatorNumber Identificador del validador de ruta.
     * @return true si el rol está asociado, false en caso contrario.
     */
    private boolean verifyRoleInRouteValidator(String token, int pathValidatorNumber) {
        switch (pathValidatorNumber) {
            case 0:
                return isUserWithRole(token, RoleEnum.ROLE_ADMIN);
            case 1:
                return isUserWithRole(token, RoleEnum.ROLE_USER);
            case 2:
                return isUserWithRole(token, RoleEnum.ROLE_WELLBEING_DIRECTOR);
            case 3:
                return isUserWithRole(token, RoleEnum.ROLE_CAF_COORDINATOR);
            case 4:
                return isUserWithRole(token, RoleEnum.ROLE_SPORTSMAN);
            default:
                return false;
        }
    }

    /**
     * Verifica si el usuario tiene un rol específico.
     *
     * @param token          Token del usuario.
     * @param roleToValidate Rol a validar.
     * @return true si el usuario tiene el rol, false en caso contrario.
     */
    private boolean isUserWithRole(String token, RoleEnum roleToValidate) {
        Set<Role> roles = getAuthoritiesFromToken(token);
        for (Role role : roles) {
            if (role.getRoleName().equals(roleToValidate))
                return true;
        }
        return false;
    }

    /**
     * Obtiene el nombre de usuario desde un token JWT.
     *
     * @param token Token JWT.
     * @return Nombre de usuario o "bad token" si el token es inválido.
     */
    public String getUserNameFromToken(String token) {
        try {
            return extractClaims(token).getSubject();
        } catch (Exception e) {
            return "bad token";
        }
    }

    /**
     * Obtiene los roles del usuario desde un token JWT.
     *
     * @param token Token JWT.
     * @return Conjunto de roles asociados al usuario.
     */
    public Set<Role> getAuthoritiesFromToken(String token) {
        try {
            Claims claims = extractClaims(token);
            List<Map<String, String>> roles = claims.get("Authorities", List.class);

            return roles.stream()
                    .map(roleMap -> Role.builder()
                            .roleName(RoleEnum.valueOf(roleMap.get("roleName")))
                            .build())
                    .collect(Collectors.toSet());
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptySet();
        }
    }

    /**
     * Extrae la fecha de expiración de un token JWT.
     *
     * @param jwtToken Token JWT.
     * @return Fecha de expiración del token.
     */
    public Date extractExpiration(String jwtToken) {
        return extractClaims(jwtToken).getExpiration();
    }
}

