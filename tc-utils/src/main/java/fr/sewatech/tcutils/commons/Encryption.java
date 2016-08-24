/**
 * Copyright 2016 Sewatech
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.sewatech.tcutils.commons;

import javax.crypto.Cipher;
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

    public static String encode(String text) {
        try {
            return DatatypeConverter.printBase64Binary(getCipher(Cipher.ENCRYPT_MODE).doFinal(text.getBytes()));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String decode(String text) {
        try {
            return new String(getCipher(Cipher.DECRYPT_MODE).doFinal(DatatypeConverter.parseBase64Binary(text)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
