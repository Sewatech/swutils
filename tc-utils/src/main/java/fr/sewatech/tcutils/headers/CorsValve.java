package fr.sewatech.tcutils.headers;

import org.apache.catalina.filters.CorsFilter;

import javax.servlet.ServletException;

import static org.apache.catalina.filters.CorsFilter.*;


/**
 * Adapts the built-in CorsFilter as a Valve
 */
public class CorsValve extends FilterAdapterValve {

    private String allowedOrigins;
    private String allowedMethods;
    private String allowedHeaders;
    private String exposedHeaders;
    private Integer preflightMaxAge;
    private Boolean supportCredentials;
    private Boolean requestDecorate;

    public CorsValve() throws ServletException {
        super(new CorsFilter());
    }

    @Override
    protected void initParameters() {
        initParameters.put(PARAM_CORS_ALLOWED_ORIGINS, allowedOrigins);
        initParameters.put(PARAM_CORS_ALLOWED_METHODS, allowedMethods);
        initParameters.put(PARAM_CORS_ALLOWED_HEADERS, allowedHeaders);
        initParameters.put(PARAM_CORS_EXPOSED_HEADERS, exposedHeaders);
        initParameters.put(PARAM_CORS_PREFLIGHT_MAXAGE, preflightMaxAge == null ? null : preflightMaxAge.toString());
        initParameters.put(PARAM_CORS_SUPPORT_CREDENTIALS, supportCredentials == null ? null : supportCredentials.toString());
        initParameters.put(PARAM_CORS_REQUEST_DECORATE, requestDecorate == null ? null : requestDecorate.toString());
    }

    public void setAllowedOrigins(String allowedOrigins) {
        this.allowedOrigins = allowedOrigins;
    }

    public void setAllowedMethods(String allowedMethods) {
        this.allowedMethods = allowedMethods;
    }

    public void setAllowedHeaders(String allowedHeaders) {
        this.allowedHeaders = allowedHeaders;
    }

    public void setExposedHeaders(String exposedHeaders) {
        this.exposedHeaders = exposedHeaders;
    }

    public void setPreflightMaxAge(int preflightMaxAge) {
        this.preflightMaxAge = preflightMaxAge;
    }

    public void setSupportCredentials(boolean supportCredentials) {
        this.supportCredentials = supportCredentials;
    }

    public void setRequestDecorate(boolean requestDecorate) {
        this.requestDecorate = requestDecorate;
    }
}
