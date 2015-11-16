package fr.sewatech.tcutils.connector;

import org.apache.coyote.http11.Http11NioProtocol;

import static fr.sewatech.tcutils.commons.Encryption.decode;

public class EncryptedSslHttp11NioProtocol extends Http11NioProtocol {
    @Override
    public void setKeystorePass(String password) {
        super.setKeystorePass(decode(password));
    }

    @Override
    public void setKeyPass(String password) {
        super.setKeyPass(decode(password));
    }

    @Override
    public void setTruststorePass(String password) {
        super.setTruststorePass(decode(password));
    }
}
