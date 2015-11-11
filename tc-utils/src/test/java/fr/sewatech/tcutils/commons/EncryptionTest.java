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
package fr.sewatech.tcutils.commons;

import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class EncryptionTest {

    private static final String CLEAR_PASSWORD = "mypwd";
    private static final String ENCRYPTED_PASSWORD = "XiGY7vFU1Nc=";

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
        Encryption.main(new String[] {"encode", CLEAR_PASSWORD});

        // THeN
        assertThat(outputStream.toString(), is(ENCRYPTED_PASSWORD + "\n"));
    }

    @Test
    public void should_main_decode_password() throws Exception {
        // GIVeN

        // WHeN
        Encryption.main(new String[] {"decode", ENCRYPTED_PASSWORD});

        // THeN
        assertThat(outputStream.toString(), is(CLEAR_PASSWORD + "\n"));
    }

}