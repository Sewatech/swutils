/**
 * Copyright 2014 Sewatech
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.sewatech.tcutils.jdbc;

import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.DataSourceFactory;
import org.apache.tomcat.jdbc.pool.PoolConfiguration;
import org.apache.tomcat.jdbc.pool.XADataSource;

import javax.naming.Context;
import java.sql.SQLException;
import java.util.Properties;

import static fr.sewatech.tcutils.commons.Encryption.decode;

/**
 * Factory for Tomcat datasource. Extends Tomcat JDBC DataSourceFactory with password encryption
 *
 * @author Alexis Hassler
 */
public class EncryptedDataSourceFactory extends DataSourceFactory {
    private static final Log logger = LogFactory.getLog(EncryptedDataSourceFactory.class);


    @Override
    public DataSource createDataSource(Properties properties, Context context, boolean xa) throws SQLException {
        PoolConfiguration poolProperties = EncryptedDataSourceFactory.parsePoolProperties(properties);
        poolProperties.setPassword(decode(poolProperties.getPassword()));
        if (poolProperties.getDataSourceJNDI() != null && poolProperties.getDataSource() == null) {
            performJNDILookup(context, poolProperties);
        }
        DataSource dataSource = xa ? new XADataSource(poolProperties) : new DataSource(poolProperties);
        dataSource.createPool();

        return dataSource;
    }

}