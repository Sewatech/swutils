package fr.sewatech.tcutils.headers;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.util.logging.Logger;

public class HttpResponseHeaderValve extends ValveBase {

    // Don't use JULI logger as it's not available in JBoss Web
    private static final Logger logger = Logger.getLogger(HttpResponseHeaderValve.class.getName());

    private String headerName;
    private String headerValue;
    private boolean force = false;

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        next.invoke(request, response);
        if (headerName == null) {
            logger.config("No header to set");
            return;
        }
        if (force || response.getHeader(headerName) == null) {
            logger.config("Setting header " + headerName);
            response.setHeader(headerName, headerValue);
        } else {
            logger.config("Header already set");
        }
    }

    public void setHeaderName(String headerName) {
        this.headerName = headerName;
    }

    public void setHeaderValue(String headerValue) {
        this.headerValue = headerValue;
    }

    public void setForce(boolean force) {
        this.force = force;
    }
}
