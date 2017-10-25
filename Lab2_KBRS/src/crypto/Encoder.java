package crypto;

import java.math.BigInteger;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Encoder {


    public static String encodeText(String text, String keyFull, String initVectorFull ){
        int textLength = text.length();
        text += "    ";
        char[] key = keyFull.substring(0, 8).toCharArray();                     //128 bit
        char[] initVector = initVectorFull.substring(0, 4).toCharArray();       // 64 bit
        char[] currentInitVector = initVector.clone();
        String encodedText = "";
        for (int i = 0; i < Math.floor(text.length()/4); ++i) {
            currentInitVector = Encoder.encodeBlock(currentInitVector, Encoder.findEncodingKeys(key));
            char[] blockOpen = text.substring(i * 4, (i + 1) * 4).toCharArray();
            char[] blockClosed = Encoder.xorCharLines(currentInitVector, blockOpen);
            encodedText += Util.charLineToString(blockClosed);
        }
        return encodedText.substring(0, textLength);
    }


    private static char[] encodeBlock(char[] block, char[] keys) {
        char [] result = block.clone();
        for (int i = 0; i < 8; ++i) {
            round(result, keys, i);
        }
        round9(result, keys);
        return result;
    }

    private static char[] xorCharLines(char[] block1, char[] block2) {
        char [] result = block1.clone();
        for (int i = 0; i < block1.length; ++i) {
            result[i] = (char)(block1[i] ^ block2[i]);
        }
        return result;
    }

    private static char[] round9(char[] block, char[] keys) {
        int a = 0, b = 1, c = 2, d = 3;
        char tempB = block[b];
        block[a] = mulMod(block[a], keys[48]);
        block[b] = addMod(block[c], keys[49]);
        block[c] = addMod(tempB, keys[50]);
        block[d] = mulMod(block[d], keys[51]);
        return block;
    }


    private static char[] round(char[] block, char[] keys, int round) {

        int k1 = round * 6;
        int k2 = k1 + 1;
        int k3 = k1 + 2;
        int k4 = k1 + 3;
        int k5 = k1 + 4;
        int k6 = k1 + 5;
        int a = 0, b = 1, c = 2, d = 3;
        char A = mulMod(block[a], keys[k1]);
        char B = addMod(block[b], keys[k2]);
        char C = addMod(block[c], keys[k3]);
        char D = mulMod(block[d], keys[k4]);
        char E = (char) (A ^ C);
        char F = (char) (B ^ D);
        char G = mulMod(E, keys[k5]);
        char H = addMod(F, G);
        char I = mulMod(H, keys[k6]);
        char J = addMod(G, I);
        block[a] = (char) (A ^ I);
        block[b] = (char) (C ^ I);
        block[c] = (char) (B ^ J);
        block[d] = (char) (D ^ J);

        return block;
    }


    private static char[] findDecodingKeys(char[] key) {

        char[] eKeys = findEncodingKeys(key);
        char[] dKeys = new char[52];
        dKeys[0] = multInver(eKeys[8 * 6]);
        dKeys[1] = addInver(eKeys[8 * 6 + 1]);
        dKeys[2] = addInver(eKeys[8 * 6 + 2]);
        dKeys[3] = multInver(eKeys[8 * 6 + 3]);
        dKeys[4] = eKeys[7 * 6 + 4];
        dKeys[5] = eKeys[7 * 6 + 5];

        for (int r = 1; r < 8; ++r) {
            dKeys[r * 6] = multInver(eKeys[(8 - r) * 6]);
            dKeys[r * 6 + 1] = addInver(eKeys[(8 - r) * 6 + 2]);
            dKeys[r * 6 + 2] = addInver(eKeys[(8 - r) * 6 + 1]);
            dKeys[r * 6 + 3] = multInver(eKeys[(8 - r) * 6 + 3]);
            dKeys[r * 6 + 4] = eKeys[(7 - r) * 6 + 4];
            dKeys[r * 6 + 5] = eKeys[(7 - r) * 6 + 5];
        }

        dKeys[8 * 6] = multInver(eKeys[0]);
        dKeys[8 * 6 + 1] = addInver(eKeys[1]);
        dKeys[8 * 6 + 2] = addInver(eKeys[2]);
        dKeys[8 * 6 + 3] = multInver(eKeys[3]);
        return dKeys;
    }

    private static char[] findEncodingKeys(char[] key) {
        StringBuilder keySB = charLineToSB(key);
        StringBuilder largeKeySB = toLargeKey(keySB);
        char[] keys = splitLargeKeyToCharLine(largeKeySB);
        return keys;
    }


    private static StringBuilder charLineToSB(char[] key) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < key.length; i++) {
            String binaryNum = Integer.toBinaryString(key[i]);
            String fullBinaryNum = "0000000000000000".substring(0, 16 - binaryNum.length()) + binaryNum;
            sb.append(fullBinaryNum);
        }
        return sb;
    }

    private static StringBuilder toLargeKey(StringBuilder key) {
        StringBuilder largeKey = new StringBuilder();
        for (int i = 0; i < 7; ++i) {
            largeKey.append(key);
            key = new StringBuilder(key.substring(25, key.length()) + key.substring(0, 25));
        }
        return largeKey;
    }

    private static char[] splitLargeKeyToCharLine(StringBuilder largeKey) {
        char[] keys = new char[52];
        for (int i = 0; i < 52; ++i) {
            String shortStr = largeKey.substring(16 * i, 16 * (i + 1));
            int parseInt = Integer.parseInt(shortStr, 2);
            keys[i] = (char) parseInt;
        }
        return keys;
    }

    private static char multInver(char x) {
        return (char) new BigInteger(Integer.toString(x)).modInverse(new BigInteger("65537")).intValue();
    }

    private static char addInver(long x) {
        return (char) (65536 - x);
    }

    private static char mulMod(long x, long y) {
        long i = 0;
        if (x == i) {
            x = 65536;
        }
        if (y == i) {
            y = 65536;
        }
        return (char) ((x * y) % 65537);
    }

    private static char addMod(long x, long y) {
        return (char) ((x + y) % 65536);
    }
}
