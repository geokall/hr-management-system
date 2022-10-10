package security;

import org.eclipse.microprofile.jwt.Claims;
import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.jose4j.jwt.NumericDate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;
import java.util.Map;

@RequestScoped
public class TokenGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtClaimService.class);

    private TokenGenerationService() {
    }

    public String generateTokenString(JwtClaims claims) throws Exception {
        // Use the private key associated with the public key for a valid signature
        PrivateKey pk = readPrivateKey("/privateKey.pem");

        return generateTokenString(pk, "/privateKey.pem", claims);
    }


    private String generateTokenString(PrivateKey privateKey, String kid, JwtClaims claims) throws Exception {

        long currentTimeInSecs = currentTimeInSecs();

        claims.setIssuedAt(NumericDate.fromSeconds(currentTimeInSecs));
        claims.setClaim(Claims.auth_time.name(), NumericDate.fromSeconds(currentTimeInSecs));

        for (Map.Entry<String, Object> entry : claims.getClaimsMap().entrySet()) {
            LOGGER.info("\tAdded claim: {}, value: {}\n", entry.getKey(), entry.getValue());
        }

        JsonWebSignature jws = new JsonWebSignature();
        jws.setPayload(claims.toJson());
        jws.setKey(privateKey);
        jws.setKeyIdHeaderValue(kid);
        jws.setHeader("typ", "JWT");
        jws.setAlgorithmHeaderValue(AlgorithmIdentifiers.RSA_USING_SHA256);

        return jws.getCompactSerialization();
    }

    private PrivateKey readPrivateKey(String pemResName) throws Exception {
        try (InputStream contentIS = TokenGenerationService.class.getResourceAsStream(pemResName)) {
            byte[] tmp = new byte[4096];

            if (contentIS != null) {
                int length = contentIS.read(tmp);

                return decodePrivateKey(new String(tmp, 0, length, StandardCharsets.UTF_8));
            }
        }

        return null;
    }

    private PrivateKey decodePrivateKey(String pemEncoded) throws Exception {
        byte[] encodedBytes = toEncodedBytes(pemEncoded);

        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedBytes);
        KeyFactory kf = KeyFactory.getInstance("RSA");

        return kf.generatePrivate(keySpec);
    }

    private byte[] toEncodedBytes(final String pemEncoded) {
        final String normalizedPem = removeBeginEnd(pemEncoded);

        return Base64.getDecoder().decode(normalizedPem);
    }

    private static String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");

        return pem.trim();
    }

    private int currentTimeInSecs() {
        long currentTimeMS = System.currentTimeMillis();

        return (int) (currentTimeMS / 1000);
    }
}
