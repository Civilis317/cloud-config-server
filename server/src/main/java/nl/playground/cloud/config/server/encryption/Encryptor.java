package nl.playground.cloud.config.server.encryption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.rsa.crypto.RsaAlgorithm;
import org.springframework.security.rsa.crypto.RsaSecretEncryptor;
import org.springframework.stereotype.Controller;

import java.security.PublicKey;

@Controller
public class Encryptor {
    private RsaAlgorithm rsaAlgorithm = RsaAlgorithm.DEFAULT;
    private boolean strong = false;
    private String salt = "deadbeef";
    private Keystore keystore;

    @Value("${encrypt.key-store.location}")
    private String keystoreLocation;

    @Value("${encrypt.key-store.password}")
    private String keystorePwd;

    public String encrypt(String alias, String secret, String input) {
        PublicKey publicKey = getKeystore().getPublicKey(alias, secret.toCharArray());
        return encrypt(publicKey, input);
    }

    public String encrypt(String alias, String input) {
        PublicKey publicKey = getKeystore().getPublicKey(alias, keystorePwd.toCharArray());
        return encrypt(publicKey, input);
    }

    private String encrypt(PublicKey publicKey, String input) {
        RsaSecretEncryptor rsaSecretEncryptor = new RsaSecretEncryptor(publicKey, this.rsaAlgorithm, this.salt, this.strong);
        String encrypted = rsaSecretEncryptor.encrypt(input);
        return "{cipher}" + encrypted;
    }

    private Keystore getKeystore() {
        if (keystore == null) {
            keystore = new Keystore(keystoreLocation, keystorePwd);
        }
        return keystore;
    }
}
