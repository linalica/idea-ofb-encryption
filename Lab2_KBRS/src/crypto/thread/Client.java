package crypto.thread;

import crypto.logic.Encoder;
import crypto.entity.Exchanger;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Client implements Runnable {

    private BigInteger ePublicKey;
    private BigInteger dPrivateKey;
    private int bitlength = 1024;
    private BigInteger N;

    @Override
    public void run() {
        System.out.println("[C] Client started");
        Exchanger exchanger = Exchanger.getExchanger();

        try {
            generateRsaPairKey();
            System.out.println("[C] RSA key pair generated.");
            System.out.println("[C] ePublicKey: " + ePublicKey);
            exchanger.ePublicKey = ePublicKey;
            exchanger.N = N;

            while (exchanger.encSessionKey == null | exchanger.encInitVector == null | exchanger.encText == null) {
                Thread.sleep(100);
            }
            System.out.println("[C] Client got text, encoded session key and encoded init vector");
            System.out.println("[C] encSessionKey: " + exchanger.encSessionKey);
            System.out.println("[C] encInitVector: " + exchanger.encInitVector);

            String decSessionKey = new String(Encoder.decryptRSA(exchanger.encSessionKey, dPrivateKey, N));
            String decInitVector = new String(Encoder.decryptRSA(exchanger.encInitVector, dPrivateKey, N));

            System.out.println("[C] Client decoded session key and init vector by dPrivateKey");
            System.out.println("[C] decSessionKey: " + decSessionKey);
            System.out.println("[C] decInitVector: " + decInitVector);

            String decText = Encoder.encodeText(exchanger.encText, decSessionKey, decInitVector);
            System.out.println("[C] Client decoded text");
            System.out.println("[C] decText:\n\n" + decText);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    private void generateRsaPairKey() {
        Random r = new Random();
        BigInteger p = BigInteger.probablePrime(bitlength, r);
        BigInteger q = BigInteger.probablePrime(bitlength, r);
        N = p.multiply(q);
        BigInteger phi = p.subtract(BigInteger.ONE).multiply(q.subtract(BigInteger.ONE));
        ePublicKey = BigInteger.probablePrime(bitlength / 2, r);
        while (phi.gcd(ePublicKey).compareTo(BigInteger.ONE) > 0 && ePublicKey.compareTo(phi) < 0) {
            ePublicKey.add(BigInteger.ONE);
        }
        dPrivateKey = ePublicKey.modInverse(phi);
    }

}
