package crypto.main;

import crypto.logic.Encoder;
import crypto.logic.Reader;
import crypto.thread.Client;
import crypto.thread.Server;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        String text = Reader.readFromFile();

        String keyFull = Server.generateSessionKey();
        String initVectorStr = Server.generateInitVector();

        String encText = Encoder.encodeText(text, keyFull, initVectorStr);
        String decText = Encoder.encodeText(encText, keyFull, initVectorStr);

        System.out.println(text);
        System.out.println(encText);
        System.out.println(decText);

        new Thread(new Client()).start();
        new Thread(new Server()).start();
    }



}
