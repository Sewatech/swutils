package fr.sewatech.tcutils.headers;

import org.apache.catalina.Context;
import org.apache.catalina.LifecycleException;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.*;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Adapts a Servlet Filter so that it becomes a Tomcat Valve.<br/>
 * The valve can be configured like any other valve, associated with any Catalina container (Engine, Host, or Context).<br/>
 * If the filter needs access to the servlet context at initialization, the valve can only be configured in a Context
 * container.<br/>
 */
public abstract class FilterAdapterValve extends ValveBase {

    private final Filter filter;
    protected final Map<String, String> initParameters = new HashMap<>();

    private final FilterChain chain = (servletRequest, servletResponse) -> next.invoke((Request) servletRequest, (Response) servletResponse);
    private final FilterConfigAdapter filterConfig;

    public FilterAdapterValve(Filter filter) throws ServletException {
        this.filter = filter;
        this.filterConfig = new FilterConfigAdapter(initParameters.keySet().iterator());
    }

    /**
     * Override this in order to transform the valve parameters into filter init parameters
     */
    protected abstract void initParameters();

    @Override
    protected void initInternal() throws LifecycleException {
        super.initInternal();
        try {
            initParameters();
            if (this.container instanceof Context) {
                Context context = (Context) this.container;
                filterConfig.setServletContext(context.getServletContext());
            }
            filter.init(filterConfig);
        } catch (ServletException e) {
            throw new LifecycleException(e);
        }
    }


    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        filter.doFilter(request, response, chain);
    }

    private class FilterConfigAdapter implements FilterConfig {
        private Iterator<String> initParameterIterator;
        private ServletContext servletContext;

        private FilterConfigAdapter(Iterator<String> iterator) {
            this.initParameterIterator = iterator;
        }

        @Override
        public String getFilterName() {
            return "FilterAdapterValve";
        }

        @Override
        public ServletContext getServletContext() {
            return servletContext;
        }

        public void setServletContext(ServletContext servletContext) {
            this.servletContext = servletContext;
        }

        @Override
        public String getInitParameter(String name) {
            return initParameters.get(name);
        }

        @Override
        public Enumeration<String> getInitParameterNames() {
            return new Enumeration<String>() {
                public boolean hasMoreElements() {
                    return initParameterIterator.hasNext();
                }

                public String nextElement() {
                    return initParameterIterator.next();
                }
            };
        }
    }
}
