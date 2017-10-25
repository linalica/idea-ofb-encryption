package crypto;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        String text = Reader.readFromFile();

        String keyFull = generateKey();
        String initVectorStr = generateInitVrctor();

        String encText = Encoder.encodeText(text, keyFull, initVectorStr);
        String decText = Encoder.encodeText(encText, keyFull, initVectorStr);

        System.out.println(text);
        System.out.println(encText);
        System.out.println(decText);

    }

    private static String generateKey(){
        return "GulevichUlyana";
    }

    private static String generateInitVrctor(){
        return "UlyanaGulevich";
    }



}
