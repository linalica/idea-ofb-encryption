package crypto.thread;

import crypto.logic.Encoder;
import crypto.entity.Exchanger;
import crypto.logic.Reader;
import org.apache.commons.lang.RandomStringUtils;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Server implements Runnable {

    private String sessionKey;
    private String initVector;


    @Override
    public void run() {
        System.out.println("[S] Server started");
        Exchanger exchanger = Exchanger.getExchanger();
        try {
            while (exchanger.ePublicKey == null) {
                Thread.sleep(1000);
            }
            System.out.println("[S] Server got ePublicKey");
            System.out.println("[S] ePublicKey: " + exchanger.ePublicKey);

            sessionKey = generateSessionKey();
            System.out.println("[S] Session key generated: " + sessionKey.getBytes());

            initVector = generateInitVector();
            System.out.println("[S] Init vector generated: " + initVector.getBytes());

            String text = Reader.readFromFile();
            String encText = Encoder.encodeText(text, sessionKey, initVector);
            exchanger.encText = encText;
            System.out.println("[S] Text encoded by session key and init vector");

            byte[] encSessionKey = Encoder.encryptRSA(sessionKey.getBytes(), exchanger.ePublicKey, exchanger.N);
            exchanger.encSessionKey = encSessionKey;
            System.out.println("[S] Session key encoded by ePublicKey: " + encSessionKey);

            byte[] encInitVector = Encoder.encryptRSA(initVector.getBytes(), exchanger.ePublicKey, exchanger.N);
            exchanger.encInitVector = encInitVector;
            System.out.println("[S] Init vector encoded by ePublicKey: " + encInitVector);


        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private static String bytesToString(byte[] encrypted) {
        String test = "";
        for (byte b : encrypted) {
            test += Byte.toString(b);
        }
        return test;
    }

 /*   private String generateSessionKey() {
        return "GulevichUlyana";
    }

    private String generateInitVector() {
        return "UlyanaGulevich";
    }
*/

    public static String generateSessionKey() {
        return RandomStringUtils.randomNumeric(8);
    }

    public static String generateInitVector() {
        return RandomStringUtils.randomNumeric(4);
    }


}
