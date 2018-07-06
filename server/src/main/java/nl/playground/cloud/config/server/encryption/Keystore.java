package nl.playground.cloud.config.server.encryption;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.PublicKey;
import java.security.cert.Certificate;

public class Keystore {
    private static final Logger logger = LoggerFactory.getLogger(Keystore.class);

    private KeyStore store;
    private char[] password;

    public Keystore(String keystoreLocation, String password) {
        try {
            this.password = password.toCharArray();
            store = KeyStore.getInstance("jks");
            store.load(this.getClass().getResourceAsStream(keystoreLocation), this.password);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to instantiate keystore: " + keystoreLocation);
        }
    }

    public PublicKey getPublicKey(String alias, char[] password) {
        try {
            Certificate certificate = store.getCertificate(alias);
            PublicKey publicKey = certificate.getPublicKey();
            return publicKey;
        } catch (KeyStoreException e) {
            logger.error(e.getMessage(), e);
            throw new RuntimeException("Unable to retrieve public key from keystore: " + alias);
        }
    }
}