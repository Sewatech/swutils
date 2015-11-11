package fr.sewatech.tcutils.commons;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author Alexis Hassler
 */
public class Encryption {
    private static final byte[] KEY = "Sewatech FTW ...".getBytes();

    public static String encode(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return DatatypeConverter.printBase64Binary(getCipher(Cipher.ENCRYPT_MODE).doFinal(text.getBytes()));
    }

    public static String decode(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new String(getCipher(Cipher.DECRYPT_MODE).doFinal(DatatypeConverter.parseBase64Binary(text)));
    }

    private static Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(mode, new SecretKeySpec(KEY, "Blowfish"));
        return cipher;
    }

    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            System.out.println("Waiting for 2 args : encode/decode and password");
        } else if ("encode".equals(args[0])) {
            System.out.println(encode(args[1]));
        } else if ("decode".equals(args[0])) {
            System.out.println(decode(args[1]));
        } else {
            System.out.println("First arg should be 'decode' or 'encode'");
        }
    }

}
