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

import org.apache.catalina.Container;
import org.apache.catalina.realm.GenericPrincipal;
import org.apache.catalina.realm.JNDIRealm;
import org.apache.catalina.realm.MessageDigestCredentialHandler;
import org.apache.juli.logging.LogFactory;
import org.apache.naming.NameParserImpl;
import org.apache.tomcat.util.security.MD5Encoder;
import org.junit.Ignore;
import org.junit.Test;

import javax.naming.NamingEnumeration;
import javax.naming.directory.*;
import java.lang.reflect.Field;
import java.security.MessageDigest;
import java.security.Principal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

/**
 * @author Alexis Hassler
 */
public class LdapRealmTest {

    String userPasswordAttribute = "pwd";

    String storedPassword;

    LdapRealm ldapRealm;

    private void setUp() throws Exception {
        setUpWithPassword("PASSWORD");
    }

    private void setUpWithPassword(String password) throws Exception {
        storedPassword = password;
        ldapRealm = new LdapRealm();

        Field field = JNDIRealm.class.getDeclaredField("context");
        field.setAccessible(true);
        field.set(ldapRealm, prepareContext());

        ldapRealm.setContainer(prepareContainer());
        ldapRealm.setUserSearch("");
        ldapRealm.init();
    }

    @Test
    public void password_without_userPassword_attribute_should_be_null() throws Exception {
        // Given
        setUp();

        // When
        String user = ldapRealm.getPassword("NOBODY");

        // Then
        assertThat(user).isNull();
    }

    @Test
    public void password_with_userPassword_attribute_should_not_be_null() throws Exception {
        // Given
        setUp();
        ldapRealm.setUserPassword(userPasswordAttribute);

        // When
        String password = ldapRealm.getPassword("ANYBODY");

        // Then
        assertThat(password).isEqualTo(storedPassword);
    }

    @Test @Ignore
    public void authenticate_simple_should_not_call_getPassword() throws Exception {
        // Given
        setUp();
        ldapRealm.setUserPassword(userPasswordAttribute);
        LdapRealm spiedLdapRealm = spy(ldapRealm);

        // When
        Principal principal = spiedLdapRealm.authenticate("USER", storedPassword);

        // Then
        verify(spiedLdapRealm, never()).getPassword(anyString());
        assertThat(principal).isNotNull();
    }

    @Test
    public void authenticate_with_digest_and_clear_password_should_call_getPassword() throws Exception {
        // Given
        setUp();
        ldapRealm.setUserPassword(userPasswordAttribute);
        LdapRealm spiedLdapRealm = spy(ldapRealm);
        MessageDigest md5Helper = MessageDigest.getInstance("MD5");
        String ha1= MD5Encoder.encode(md5Helper.digest(("user:realm:" + storedPassword).getBytes()));
        String digest = MD5Encoder.encode(md5Helper.digest((ha1 + ":nonce:md5a2").getBytes()));

        // When
        Principal principal = spiedLdapRealm.authenticate("user", digest, "nonce", null, null, null, "realm", "md5a2");

        // Then
        verify(spiedLdapRealm, atLeastOnce()).getPassword(anyString());
        assertThat(principal).isNotNull()
                             .isInstanceOf(GenericPrincipal.class);
        assertThat(((GenericPrincipal)principal).getPassword()).isEqualTo(storedPassword);
    }

    @Test
    public void authenticate_with_digest_and_digested_password_should_call_getPassword() throws Exception {
        // Given
        MessageDigest md5Helper = MessageDigest.getInstance("MD5");
        String ha1= MD5Encoder.encode(md5Helper.digest(("user:realm:" + storedPassword).getBytes()));
        setUpWithPassword(ha1);

        ldapRealm.setUserPassword(userPasswordAttribute);
        MessageDigestCredentialHandler credentialHandler = new MessageDigestCredentialHandler();
        credentialHandler.setAlgorithm("md5");
        ldapRealm.setCredentialHandler(credentialHandler);
        ldapRealm.start();
        LdapRealm spiedLdapRealm = spy(ldapRealm);

        // When
        String digest = MD5Encoder.encode(md5Helper.digest((ha1 + ":nonce:md5a2").getBytes()));
        Principal principal = spiedLdapRealm.authenticate("user", digest, "nonce", null, null, null, "realm", "md5a2");

        // Then
        verify(spiedLdapRealm, atLeastOnce()).getPassword(anyString());
        assertThat(principal).isNotNull()
                             .isInstanceOf(GenericPrincipal.class);
        assertThat(((GenericPrincipal)principal).getPassword()).isEqualTo(storedPassword);
    }

    private DirContext prepareContext() throws javax.naming.NamingException {
        NamingEnumeration namingEnumeration = mock(NamingEnumeration.class);
        when(namingEnumeration.hasMore())
                .thenReturn(true)
                .thenReturn(false)
                .thenReturn(true)
                .thenReturn(false);
        when(namingEnumeration.next())
                .thenReturn(new SearchResult("ANYTHING", "", new BasicAttributes(userPasswordAttribute, storedPassword)));

        DirContext context = mock(InitialDirContext.class);
        when(context.search(anyString(), anyString(), any(SearchControls.class)))
                .thenReturn(namingEnumeration);
        when(context.getNameParser(""))
                .thenReturn(new NameParserImpl());
        when(context.getNameInNamespace())
                .thenReturn("RANDOM");

        return context;
    }

    private Container prepareContainer() {
        Container container = mock(Container.class);
        when(container.getLogger())
                .thenReturn(LogFactory.getLog("TEST"));
        return container;
    }
}