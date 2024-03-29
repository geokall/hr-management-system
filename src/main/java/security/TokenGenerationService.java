package security;

import org.jose4j.jws.AlgorithmIdentifiers;
import org.jose4j.jws.JsonWebSignature;
import org.jose4j.jwt.JwtClaims;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.enterprise.context.RequestScoped;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

@RequestScoped
public class TokenGenerationService {

    private static final Logger LOGGER = LoggerFactory.getLogger(JwtClaimService.class);

    public String generateTokenString(JwtClaims claims) throws Exception {
        // Use the private key associated with the public key for a valid signature
        PrivateKey pk = readPrivateKey("/privateKey.pem");

        if (pk == null) {
            LOGGER.info("Could not read private key");
            throw new RuntimeException();
        }

        return generateTokenString(pk, "/privateKey.pem", claims);
    }


    private String generateTokenString(PrivateKey privateKey, String kid, JwtClaims claims) throws Exception {
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

    private String removeBeginEnd(String pem) {
        pem = pem.replaceAll("-----BEGIN (.*)-----", "");
        pem = pem.replaceAll("-----END (.*)----", "");
        pem = pem.replaceAll("\r\n", "");
        pem = pem.replaceAll("\n", "");

        return pem.trim();
    }
}
