package cat.arsgbm.coral.classes;

import org.apache.commons.codec.digest.DigestUtils;

public class CryptoHelper {

    private static String encripta(String s) {
        return DigestUtils.sha256Hex(s);
    }

    public static boolean testPassword (String clar, String encriptat) {
        String tmp =  encripta(clar);

        return encriptat.equals(tmp);
    }
}
