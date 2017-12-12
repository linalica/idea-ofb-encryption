package crypto.entity;

import java.math.BigInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author Gulevich Ulyana
 * @version 1.0
 */
public class Exchanger {

    private static Lock lockInst = new ReentrantLock();
    private static Exchanger exchanger = null;

    public BigInteger ePublicKey = null;
    public BigInteger N = null;
    public byte[] encSessionKey;
    public byte[] encInitVector;
    public String encText;


    private Exchanger() {
    }

    public static Exchanger getExchanger() {
        lockInst.lock();
        try {
            if (exchanger == null) {
                exchanger = new Exchanger();
            }
        } finally {
            lockInst.unlock();
        }
        return exchanger;
    }

}
