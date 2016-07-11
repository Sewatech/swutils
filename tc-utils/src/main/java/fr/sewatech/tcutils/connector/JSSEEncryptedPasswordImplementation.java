package fr.sewatech.tcutils.connector;

import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.apache.tomcat.util.net.SSLUtil;
import org.apache.tomcat.util.net.jsse.JSSEImplementation;

import static fr.sewatech.tcutils.connector.EncryptedPasswordUtil.decodePasswords;

public class JSSEEncryptedPasswordImplementation extends JSSEImplementation {

    @Override
    public SSLUtil getSSLUtil(SSLHostConfigCertificate certificate) {
        return super.getSSLUtil(decodePasswords(certificate));
    }

}
