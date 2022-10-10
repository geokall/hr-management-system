package security;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.TokenUtils;

import javax.enterprise.context.RequestScoped;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

@RequestScoped
public class TokenService {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenService.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String jwtIssuer;

    public String generateReaderToken(String username, Date birthDate) {
        return generateToken(username, birthDate, "READER");
    }

    public String generateAdminToken(String username, Date birthDate) {
        return generateToken(username, birthDate, "ADMIN");
    }


    public String generateToken(String username, Date birthDate, String... roles) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer(jwtIssuer); // from properties
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setSubject(username);
            jwtClaims.setClaim(Claims.upn.name(), username);
            jwtClaims.setClaim(Claims.preferred_username.name(), username); //add more
            jwtClaims.setClaim(Claims.groups.name(), Arrays.asList(roles));
            jwtClaims.setAudience("using-jwt");
            jwtClaims.setStringClaim(Claims.birthdate.name(), birthDate.toString());

            jwtClaims.setExpirationTimeMinutesInTheFuture(10); // TODO specify how long do you need

            String token = TokenUtils.generateTokenString(jwtClaims);
            LOGGER.info("TOKEN generated: " + token);

            return token;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
