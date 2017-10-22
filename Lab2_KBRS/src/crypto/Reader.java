package crypto;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Reader {

    public static final String INPUT = "./files/input.txt";


    public static String readFromFile() {
        String lines = "";
        Scanner sc = null;
        try {
            sc = new Scanner(new File(INPUT));
            while (sc.hasNextLine()) {
                String line = sc.nextLine();
                lines = lines + line + "\n";
            }
        } catch (FileNotFoundException e) {
            //
        } finally {
            if (sc != null) {
                sc.close();
            }
        }
        return lines;
    }
}
