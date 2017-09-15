package fr.sewatech.tcutils.headers;

import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HttpResponseHeaderValveTest {

    private static final String HEADER_NAME = "X-TEST";
    private static final String HEADER_VALUE = "VALUE";
    private HttpResponseHeaderValve valve;
    private Request request;
    private Response response;
    private Valve next;

    @Before
    public void init() {
        request = mock(Request.class);
        response = mock(Response.class);
        next = mock(Valve.class);
        valve = new HttpResponseHeaderValve();
        valve.setNext(next);
    }

    @Test
    public void invoke_without_parameters_should_do_nothing() throws Exception {
        // GIVeN

        // WHeN
        valve.invoke(request, response);

        // THeN
        verify(next, times(1)).invoke(request, response);
        assertThat(response.getHeaderNames()).isEmpty();
    }

    @Test
    public void invoke_with_parameters_should_set_header() throws Exception {
        // GIVeN
        valve.setHeaderName(HEADER_NAME);
        valve.setHeaderValue(HEADER_VALUE);

        // WHeN
        valve.invoke(request, response);

        // THeN
        verify(next).invoke(request, response);
        verify(response).setHeader(HEADER_NAME, HEADER_VALUE);
    }

    @Test
    public void invoke_should_override_header() throws Exception {
        // GIVeN
        when(response.getHeader(HEADER_NAME)).thenReturn("xxx");

        valve.setHeaderName(HEADER_NAME);
        valve.setHeaderValue(HEADER_VALUE + "-changed");

        // WHeN
        valve.invoke(request, response);

        // THeN
        verify(next).invoke(request, response);
        verify(response, never()).setHeader(HEADER_NAME, HEADER_VALUE);
    }

    @Test
    public void invoke_forced_should_override_header() throws Exception {
        // GIVeN
        when(response.getHeader(HEADER_NAME)).thenReturn("xxx");

        valve.setHeaderName(HEADER_NAME);
        valve.setHeaderValue(HEADER_VALUE);
        valve.setForce(true);

        // WHeN
        valve.invoke(request, response);

        // THeN
        verify(next).invoke(request, response);
        verify(response).setHeader(HEADER_NAME, HEADER_VALUE);
    }


}