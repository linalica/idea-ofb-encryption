package crypto;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Util {


    static String charLineToString(char[] line) {
        String result = "";
        for (int i = 0; i < line.length; ++i) {
            result += line[i];
        }
        return result;
    }

    public static void printCharLineInHex(char[] line) {
        for (int i = 0; i < line.length; i++) {
            System.out.println(i + ": " + Integer.toHexString(line[i]));
        }
    }

    public static void printCharLineInBites(char[] line) {
        for (int i = 0; i < line.length; i++) {
            String binaryNum = Integer.toBinaryString(line[i]);
            System.out.println(i + ": " + "0000000000000000".substring(0, 16 - binaryNum.length()) + binaryNum);
        }
    }

    public static void printCharLineSegmentInBites(char[] line, int start, int end) {
        for (int i = start; i < end; i++) {
            String binaryNum = Integer.toBinaryString(line[i]);
            System.out.println(i + ": " + "0000000000000000".substring(0, 16 - binaryNum.length()) + binaryNum);
        }
    }

}
