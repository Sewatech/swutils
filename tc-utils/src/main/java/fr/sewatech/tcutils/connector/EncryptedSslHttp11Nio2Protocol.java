/**
 * Copyright 2015 Sewatech
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

import org.apache.coyote.http11.Http11Nio2Protocol;
import org.apache.coyote.http11.Http11NioProtocol;
import org.apache.tomcat.util.net.AbstractJsseEndpoint;
import org.apache.tomcat.util.net.Nio2Channel;
import org.apache.tomcat.util.net.NioChannel;

import static fr.sewatech.tcutils.commons.Encryption.decode;

/**
 * Designed for Tomcat 7 & 8.0
 * Deprecated for Tomcat 8.5 & 9
 * Won't work on Tomcat 10+
 *
 * See http://tomcat.apache.org/tomcat-8.5-doc/config/http.html#SSL_Support
 */
@Deprecated
public class EncryptedSslHttp11Nio2Protocol extends Http11Nio2Protocol {
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

    @Override
    protected AbstractJsseEndpoint<Nio2Channel> getEndpoint() {
        return super.getEndpoint();
    }

}
