package app.properties;

import app.utilities.AsymmetricKeyPairGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.*;
import java.security.spec.ECGenParameterSpec;

public class Wallet {
    private final Logger LOGGER = LoggerFactory.getLogger(Wallet.class.getName());
    private int coinBalance;
    private PublicKey publicKey;
    private PrivateKey privateKey;


    public Wallet() {
        this.coinBalance = 0;
        generateKeyPair();
    }


    public void generateKeyPair() {
        try {
            KeyPair keyPair = AsymmetricKeyPairGenerator.generateRSAKeyPair();
            this.privateKey = keyPair.getPrivate();
            this.publicKey = keyPair.getPublic();
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
    }
}
