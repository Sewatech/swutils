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
package fr.sewatech.tcutils.realm;

import javax.naming.NamingException;

/**
 * <p>Implementation of <strong>Realm</strong> that works with a directory
 * server. It works like the JNDIRealm except that it supports Digest
 * authentication.
 *
 *
 * @author Alexis Hassler
 */
public class LdapRealm extends org.apache.catalina.realm.JNDIRealm {

    @Override
    protected String getPassword(String username) {
        // LdapRealm works only with the userPassword attribute
        String userPassword = getUserPassword();
        if (userPassword == null || userPassword.isEmpty()) {
            return null;
        }

        try {
            User user = getUser(open(), username, null);
            if (user == null) {
                // User should be found...
                return null;
            } else {
                // ... and have a password
                return user.getPassword();
            }
        } catch (NamingException e) {
            return null;
        }
    }
}
