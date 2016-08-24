/**
 * Copyright 2016 Sewatech
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
package fr.sewatech.tcutils.session;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Test;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Field;
import java.security.Principal;
import java.util.*;

import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

public class SingleRequestSessionFilterTest {

    SingleRequestSessionFilter filter = new SingleRequestSessionFilter();

    @Test
    public void should_init_split_userNames_parameter() throws Exception {
        // GIVeN
        FilterConfigFake config = new FilterConfigFake()
                                            .withInitParameter("userNames", "A,B,C,DDD");

        // WHeN
        filter.init(config);

        // THeN
        assertThat(filter, new UserNamesMatcher("A", "B", "C", "DDD"));
    }

    @Test
    public void should_init_not_split_userName_parameter() throws Exception {
        // GIVeN
        FilterConfigFake config = new FilterConfigFake()
                                            .withInitParameter("userName", "A,B,C,DDD");

        // WHeN
        filter.init(config);

        // THeN
        assertThat(filter, new UserNamesMatcher("A,B,C,DDD"));
    }

    @Test
    public void should_init_combine_userNames_and_userName_parameters() throws Exception {
        // GIVeN
        FilterConfigFake config = new FilterConfigFake()
                                            .withInitParameter("userName", "E")
                                            .withInitParameter("userNames", "A,B,C,DDD");

        // WHeN
        filter.init(config);

        // THeN
        assertThat(filter, new UserNamesMatcher("A", "B", "C", "DDD", "E"));
    }


    @Test
    public void should_doFilter_invalidate_session_when_user_matches() throws Exception {
        // GIVeN
        String userName = "A";

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(request.getUserPrincipal()).thenReturn(new FakePrincipal(userName)); // It was the simpler to use

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.init(new FilterConfigFake().withInitParameter("userName", userName));

        // WHeN
        filter.doFilter(request, response, chain);

        // THeN
        verify(session).invalidate();
    }

    @Test
    public void should_doFilter_keep_session_unchanged_when_user_not_matches() throws Exception {
        // GIVeN
        String authenticatedUserName = "A";
        String initParamUserName = "Not A";

        HttpSession session = mock(HttpSession.class);
        HttpServletRequest request = mock(HttpServletRequest.class);
        when(request.getSession()).thenReturn(session);
        when(request.getSession(false)).thenReturn(session);
        when(request.getUserPrincipal()).thenReturn(new FakePrincipal(authenticatedUserName)); // It was the simpler to use

        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain chain = mock(FilterChain.class);

        filter.init(new FilterConfigFake().withInitParameter("userName", initParamUserName));

        // WHeN
        filter.doFilter(request, response, chain);

        // THeN
        verify(session, never()).invalidate();
    }



    //

    private static class FilterConfigFake implements FilterConfig {

        private Map<String, String> parameters = new HashMap<>();

        @Override
        public String getFilterName() {
            return "**FAKE**";
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public String getInitParameter(String key) {
            return parameters.get(key);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return null;
        }

        public FilterConfigFake withInitParameter(String key, String value) {
            parameters.put(key, value);
            return this;
        }

    }

    private static class UserNamesMatcher extends BaseMatcher<SingleRequestSessionFilter> {

        private Set<String> names;

        UserNamesMatcher(String... names) {
            this.names = new HashSet<>(Arrays.asList(names));
        }

        @Override
        public boolean matches(Object item) {
            Set<String> userNames = getUserNames(item);

            return names.equals(userNames);
        }

        private Set<String> getUserNames(Object item) {
            try {
                Field field = SingleRequestSessionFilter.class.getDeclaredField("userNames");
                field.setAccessible(true);
                return (Set<String>) field.get(item);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                return new HashSet<>();
            }
        }

        @Override
        public void describeTo(Description description) {
            description.appendText(names.toString());
        }
    }

    private static class FakePrincipal implements Principal {

        private String name;

        FakePrincipal(String name) {
            this.name = name;
        }

        @Override
        public String getName() {
            return name;
        }
    }
}