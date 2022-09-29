package app.utilities;
import java.security.KeyPair;
import java.security
        .KeyPairGenerator;
import java.security
        .SecureRandom;

// Class to create an asymmetric key
public class AsymmetricKeyPairGenerator {
    private static final String RSA = "RSA";

    // Generating public and private keys
    // using RSA algorithm.
    public static KeyPair generateRSAKeyPair()
            throws Exception
    {
        SecureRandom secureRandom
                = new SecureRandom();

        KeyPairGenerator keyPairGenerator
                = KeyPairGenerator.getInstance(RSA);

        keyPairGenerator.initialize(
                2048, secureRandom);

        return keyPairGenerator
                .generateKeyPair();
    }
}