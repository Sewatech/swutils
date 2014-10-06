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
package fr.sewatech.tcutils.session;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.security.Principal;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Automatically invalidate session for some users. Can be useful for monitoring users that open sessions but do not
 * reuse them.
 *
 * @author Alexis Hassler
 */
public class SingleRequestSessionFilter implements Filter {

    private static final String USERNAMES_KEY = "userNames";
    private static final String USERNAME_KEY = "userName";

    private Set<String> userNames;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        userNames = new HashSet<>();

        String userNamesParameter = filterConfig.getInitParameter(USERNAMES_KEY);
        if (userNamesParameter != null) {
            Collections.addAll(userNames, userNamesParameter.split(","));
        }

        String userNameParameter = filterConfig.getInitParameter(USERNAME_KEY);
        if (userNameParameter != null) {
            userNames.add(userNameParameter);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(request, response);

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        Principal principal = httpRequest.getUserPrincipal();
        if ( (principal != null) && (userNames.contains(principal.getName())) ) {
            HttpSession session = httpRequest.getSession(false);
            if ( session != null ) {
                session.invalidate();
            }
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public String toString() {
        return Objects.toString(userNames);
    }
}
