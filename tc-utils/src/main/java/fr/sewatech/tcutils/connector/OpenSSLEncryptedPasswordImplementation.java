package fr.sewatech.tcutils.connector;

import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtil;
import org.apache.tomcat.util.net.openssl.OpenSSLImplementation;

import static fr.sewatech.tcutils.connector.EncryptedPasswordUtil.decodePasswords;

public class OpenSSLEncryptedPasswordImplementation extends OpenSSLImplementation {

    @Override
    public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate) {
        return super.getSSLUtil(decodePasswords(certificate));
    }

}
