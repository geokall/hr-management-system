package security;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import java.util.Collections;
import java.util.UUID;

@RequestScoped
public class JwtClaimService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtClaimService.class);

    @ConfigProperty(name = "mp.jwt.verify.issuer")
    String jwtIssuer;

    private final TokenGenerationService tokenGenerationService;

    @Inject
    public JwtClaimService(TokenGenerationService tokenGenerationService) {
        this.tokenGenerationService = tokenGenerationService;
    }

    public String generateUserToken(String username, String role) {
        return generateToken(username, role);
    }


    private String generateToken(String username, String role) {
        try {
            JwtClaims jwtClaims = new JwtClaims();
            jwtClaims.setIssuer(jwtIssuer);
            jwtClaims.setJwtId(UUID.randomUUID().toString());
            jwtClaims.setClaim(Claims.upn.name(), username);
            jwtClaims.setClaim(Claims.preferred_username.name(), username);
            jwtClaims.setClaim(Claims.groups.name(), Collections.singletonList(role));
            jwtClaims.setAudience("using-jwt");

            long currentTimeInSeconds = fetchCurrentTimeInSeconds();

            jwtClaims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSeconds));
            jwtClaims.setClaim(Claims.auth_time.name(), NumericDate.fromSeconds(currentTimeInSeconds));

            jwtClaims.setExpirationTimeMinutesInTheFuture(60);

            String token = tokenGenerationService.generateTokenString(jwtClaims);
            LOGGER.info("JWT generated: " + token);

            return token;
        } catch (Exception e) {
            LOGGER.info(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private int fetchCurrentTimeInSeconds() {
        long currentTimeMS = System.currentTimeMillis();

        return (int) (currentTimeMS / 1000);
    }
}
