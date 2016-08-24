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
