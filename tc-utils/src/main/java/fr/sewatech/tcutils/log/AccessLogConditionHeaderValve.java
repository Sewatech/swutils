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
package fr.sewatech.tcutils.log;

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
 * </p>
 * <p>
 * Configuration :
 * </p>
 * <pre>
 * &lt;Valve className="fr.sewatech.tcutils.log.AccessLogConditionHeaderValve"
 *        attributeName="sewatech.fr" header="Host" suffix="sewatech.fr" /&gt;
 * </pre>
 * <p>
 * Use in access logs :
 * </p>
 * <pre>
 * &lt;Valve className="org.apache.catalina.log.AccessLogValve"
 * ... conditionIf="sewatech.fr" ... /&gt;
 * </pre>
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

