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

import org.apache.tomcat.util.net.SSLHostConfig;
import org.apache.tomcat.util.net.SSLHostConfigCertificate;
import org.junit.Ignore;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Alexis Hassler
 */
public class EncryptedSslHttp11NioProtocolTest {

    private static final String CLEAR_PASSWORD = "mypwd";
    private static final String ENCRYPTED_PASSWORD = "XiGY7vFU1Nc=";

    private EncryptedSslHttp11NioProtocol protocol = new EncryptedSslHttp11NioProtocol();

    @Test
    public void keystorePass_should_be_decoded() throws Exception {
        // GIVeN

        // WHeN
        protocol.setKeystorePass(ENCRYPTED_PASSWORD);

        // THeN
        SSLHostConfigCertificate certificate = getSslHostConfigCertificate(protocol);
        assertThat(certificate.getCertificateKeystorePassword()).isEqualTo(CLEAR_PASSWORD);

    }

    @Test
    public void keyPass_should_be_decoded() throws Exception {
        // GIVeN

        // WHeN
        protocol.setKeyPass(ENCRYPTED_PASSWORD);

        // THeN
        SSLHostConfigCertificate certificate = getSslHostConfigCertificate(protocol);
        assertThat(certificate.getCertificateKeyPassword()).isEqualTo(CLEAR_PASSWORD);

    }

    @Test
    public void truststorePass_should_be_decoded() throws Exception {
        // GIVeN

        // WHeN
        protocol.setTruststorePass(ENCRYPTED_PASSWORD);

        // THeN
        assertThat(getSslHostConfig(protocol).getTruststorePassword()).isEqualTo(CLEAR_PASSWORD);

    }

    private SSLHostConfigCertificate getSslHostConfigCertificate(EncryptedSslHttp11NioProtocol protocol) {
        return getSslHostConfig(protocol).getCertificates().iterator().next();
    }

    private SSLHostConfig getSslHostConfig(EncryptedSslHttp11NioProtocol protocol) {
        SSLHostConfig[] sslHostConfigs = protocol.getEndpoint().findSslHostConfigs();
        return sslHostConfigs[0];
    }


}