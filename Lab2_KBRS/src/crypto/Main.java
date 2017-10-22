package crypto;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Main {

    public static void main(String[] args) {

        String text = Reader.readFromFile();
        String keyFull = "GulevichUlyana";

        char [] key = keyFull.substring(0, 8).toCharArray();    //128 bit
        char[] block = text.substring(0, 4).toCharArray();      // 64 bit


        char[] encodedBlock = Encoder.encodeBlock(block, Encoder.findEncodingKeys(key));

        char[] decText = Encoder.encodeBlock(encodedBlock, Encoder.findDecodingKeys(key));
        System.out.println("final dec: ");
        System.out.println(decText);

    }








}
