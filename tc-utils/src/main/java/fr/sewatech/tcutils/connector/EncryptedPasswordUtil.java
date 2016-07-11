package fr.sewatech.tcutils.connector;

import fr.sewatech.tcutils.commons.Encryption;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;

public class EncryptedPasswordUtil {
    static SSLHostConfigCertificate decodePasswords(SSLHostConfigCertificate certificate) {
        String certificateKeystorePassword = certificate.getCertificateKeystorePassword();
        if (certificateKeystorePassword != null) {
            certificate.setCertificateKeystorePassword(Encryption.decode(certificateKeystorePassword));
        }

        String certificateKeyPassword = certificate.getCertificateKeyPassword();
        if (certificateKeyPassword != null) {
            certificate.setCertificateKeyPassword(Encryption.decode(certificateKeyPassword));
        }

        return certificate;
    }

}
