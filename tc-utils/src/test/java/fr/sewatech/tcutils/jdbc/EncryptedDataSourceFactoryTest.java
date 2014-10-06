package fr.sewatech.tcutils.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Properties;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EncryptedDataSourceFactoryTest {

    private static final String CLEAR_PASSWORD = "mypwd";
    private static final String ENCRYPTED_PASSWORD = "XiGY7vFU1Nc=";

    private EncryptedDataSourceFactory factory = new EncryptedDataSourceFactory();
    private ByteArrayOutputStream outputStream;

    @Before
    public void init() {
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void should_main_encode_password() throws Exception {
        // GIVeN

        // WHeN
        factory.main(new String[] {"encode", CLEAR_PASSWORD});

        // THeN
        assertThat(outputStream.toString(), is(ENCRYPTED_PASSWORD + "\n"));
    }

    @Test
    public void should_main_decode_password() throws Exception {
        // GIVeN

        // WHeN
        factory.main(new String[] {"decode", ENCRYPTED_PASSWORD});

        // THeN
        assertThat(outputStream.toString(), is(CLEAR_PASSWORD + "\n"));
    }

    @Test
    public void should_createDataSource_decode_password() throws Exception {
        // GIVeN
        Properties properties = new Properties();
        properties.setProperty("password", ENCRYPTED_PASSWORD);
        properties.setProperty("initialSize", "0");

        // WHeN
        DataSource dataSource = factory.createDataSource(properties, null, false);

        // THeN
        assertThat(dataSource.getPoolProperties().getPassword(), is(CLEAR_PASSWORD));
    }
}