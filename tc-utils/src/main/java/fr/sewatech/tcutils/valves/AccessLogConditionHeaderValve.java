package fr.sewatech.tcutils.valves;

import java.io.IOException;

import javax.servlet.ServletException;

import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.valves.ValveBase;

/**
 * AccessLogValve has a condition feature, based on the presence of an attributeName in request. This valve transform
 * conditions on HTTP headers into a boolean attributeName.
 * <p>
 * Conditions are exact value, prefix or suffix
 * <p>
 * Configuration :
 * <Valve className="fr.sewatech.tcutils.valves.AccessLogConditionHeaderValve"
 *        attributeName="sewatech.fr" header="Host" suffix="sewatech.fr" />
 * <p>
 * Use in access logs :
 * <Valve className="org.apache.catalina.valves.AccessLogValve"
 * ... conditionIf="sewatech.fr" ... />
 *
 * @author Alexis Hassler
 */

public class AccessLogConditionHeaderValve extends ValveBase {

    private String attributeName = "";
    private String header = "";
    private String value = "";
    private String suffix = "";
    private String prefix = "";

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
        String headerValue = request.getHeader(this.header);
        if (headerValue != null) {
            if (!value.isEmpty() && headerValue.equals(value)) {
                request.setAttribute(attributeName, true);
            } else if (!suffix.isEmpty() && headerValue.endsWith(suffix)
                    || !prefix.isEmpty() && headerValue.startsWith(prefix)) {
                request.setAttribute(attributeName, true);
            }
        }

        this.getNext().invoke(request, response);
    }

    public void setAttributeName(String attributeName) {
        this.attributeName = attributeName;
    }

    public String getAttributeName() {
        return attributeName;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getHeader() {
        return header;
    }

    public void setSuffix(String suffix) {
        this.suffix = suffix;
    }

    public String getSuffix() {
        return suffix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getPrefix() {
        return prefix;
    }
}

