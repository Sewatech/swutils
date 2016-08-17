package fr.sewatech.tcutils.valves;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.mock;

public class AccessLogConditionHeaderValveTest {

    private static final String HEADER_NAME = "HEADER";
    private static final String ATTRIBUTE_NAME = "ATTR";
    private static final String HEADER_VALUE = "TEST";
    private AccessLogConditionHeaderValve valve = new AccessLogConditionHeaderValve();

    @Before
    public void setUp() throws Exception {
        valve.setAttributeName(ATTRIBUTE_NAME);
        valve.setHeader(HEADER_NAME);
        valve.setValue(HEADER_VALUE);

        Valve next = mock(Valve.class);
        valve.setNext(next);
    }

    @Test
    public void invoke_should_set_attribute_when_header_is_compliant() throws Exception {
        // GIVeN
        Request request = mock(Request.class);
        when(request.getHeader(HEADER_NAME)).thenReturn("TEST");

        // WHeN
        valve.invoke(request, null);

        // THeN
        verify(request).setAttribute("ATTR", true);
    }

    @Test
    public void invoke_should_set_attribute_when_header_is_empty() throws Exception {
        // GIVeN
        Request request = mock(Request.class);
        when(request.getHeader(HEADER_NAME)).thenReturn(null);

        // WHeN
        valve.invoke(request, null);

        // THeN
        verify(request).getHeader(HEADER_NAME);
        verifyNoMoreInteractions(request);
    }

    @Test
    public void invoke_should_set_attribute_when_header_is_not_compliant() throws Exception {
        // GIVeN
        Request request = mock(Request.class);
        when(request.getHeader(HEADER_NAME)).thenReturn("NOT_" + HEADER_VALUE);

        // WHeN
        valve.invoke(request, null);

        // THeN
        verify(request).getHeader(HEADER_NAME);
        verifyNoMoreInteractions(request);
    }

}