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
package fr.sewatech.tcutils.proxy;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;

/**
 * This filter replaces the apparent context path with the path in the X-Forwarded-Context header.
 *
 * @author Alexis Hassler
 */
public class XForwardedFilter implements Filter {

    static final String CONTEXT_HEADER_NAME = "X-Forwarded-Context";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        chain.doFilter(new ForwardedHttpServletRequestWrapper((HttpServletRequest) request), response);
    }

    @Override
    public void destroy() {

    }

    static class ForwardedHttpServletRequestWrapper extends HttpServletRequestWrapper {
        ForwardedHttpServletRequestWrapper(HttpServletRequest request) {
            super(request);
        }

        @Override
        public String getContextPath() {
            String forwardedContextHeader = this.getHeader(CONTEXT_HEADER_NAME);
            String contextPath = forwardedContextHeader == null ? super.getContextPath() : forwardedContextHeader;
            System.out.println("New contextPath=" + contextPath);
            return contextPath;
        }
    }
}
