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
