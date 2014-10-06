/**
 * Copyright 2014 Sewatech
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
package fr.sewatech.tcutils.jdbc;

import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import javax.naming.Context;
import javax.xml.bind.DatatypeConverter;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Factory for Tomcat datasource. Extends Tomcat JDBC DataSourceFactory with password encryption
 *
 * @author Alexis Hassler
 */
public class EncryptedDataSourceFactory extends DataSourceFactory {

    private static final byte[] KEY = "Sewatech FTW ...".getBytes();

    public static void main(String[] args) throws Exception {
        if ("encode".equals(args[0])) {
            System.out.println(encode(args[1]));
        } else if ("decode".equals(args[0])) {
            System.out.println(decode(args[1]));
        }
    }

    @Override
    public DataSource createDataSource(Properties properties, Context context, boolean xa) throws SQLException {
        try {
            PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);
            poolProperties.setPassword(decode(poolProperties.getPassword()));
            if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {
                performJNDILookup(context, poolProperties);
            }
            DataSource dataSource = xa ? new XADataSource(poolProperties) : new DataSource(poolProperties);
            dataSource.createPool();

            return dataSource;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }


    private static String encode(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return DatatypeConverter.printBase64Binary(getCipher(Cipher.ENCRYPT_MODE).doFinal(text.getBytes()));
    }

    private static String decode(String text) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        return new String(getCipher(Cipher.DECRYPT_MODE).doFinal(DatatypeConverter.parseBase64Binary(text)));
    }

    private static Cipher getCipher(int mode) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException {
        Cipher cipher = Cipher.getInstance("Blowfish");
        cipher.init(mode, new SecretKeySpec(KEY, "Blowfish"));
        return cipher;
    }

}