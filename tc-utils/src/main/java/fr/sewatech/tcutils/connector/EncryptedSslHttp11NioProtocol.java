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

import org.apache.coyote.http11.Http11NioProtocol;

import static fr.sewatech.tcutils.commons.Encryption.decode;

public class EncryptedSslHttp11NioProtocol extends Http11NioProtocol {
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
}
