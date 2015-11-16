package fr.sewatech.tcutils.connector;

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
        assertThat(protocol.getEndpoint().getKeystorePass()).isEqualTo(CLEAR_PASSWORD);

    }

    @Test
    public void keyPass_should_be_decoded() throws Exception {
        // GIVeN

        // WHeN
        protocol.setKeyPass(ENCRYPTED_PASSWORD);

        // THeN
        assertThat(protocol.getEndpoint().getKeyPass()).isEqualTo(CLEAR_PASSWORD);

    }

    @Test
    public void truststorePass_should_be_decoded() throws Exception {
        // GIVeN

        // WHeN
        protocol.setTruststorePass(ENCRYPTED_PASSWORD);

        // THeN
        assertThat(protocol.getEndpoint().getTruststorePass()).isEqualTo(CLEAR_PASSWORD);

    }


}