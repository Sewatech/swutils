package fr.sewatech.tcutils.proxy;

import fr.sewatech.tcutils.proxy.XForwardedFilter.ForwardedHttpServletRequestWrapper;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;

public class XForwardedFilterTest {

    private static final String CONTEXT_HEADER_VALUE = "VALUE";

    @Test
    public void doFilter_should_pass_a_request_wrapper_to_the_filter_chain() throws Exception {
        // GIVeN
        XForwardedFilter filter = new XForwardedFilter();

        HttpServletRequest request = mock(HttpServletRequest.class);
        FilterChain filterChain = mock(FilterChain.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        // WHeN
        filter.doFilter(request, response, filterChain);

        // THeN
        ArgumentCaptor<HttpServletRequestWrapper> argumentCaptor = ArgumentCaptor.forClass(HttpServletRequestWrapper.class);
        verify(filterChain).doFilter(argumentCaptor.capture(), eq(response));

        assertThat(argumentCaptor.getValue().getRequest()).isSameAs(request);
    }


    @Test
    public void getContextPath_should_call_super_method_when_no_header() {
        // GIVeN
        HttpServletRequest request = mock(HttpServletRequest.class);
        ForwardedHttpServletRequestWrapper requestWrapper = new ForwardedHttpServletRequestWrapper(request);


        // WHeN
        requestWrapper.getContextPath();

        // THeN
        verify(request).getContextPath();
    }

    @Test
    public void getContextPath_should_return_header_value() {
        // GIVeN
        HttpServletRequest request = mock(HttpServletRequest.class);
        ForwardedHttpServletRequestWrapper requestWrapper = new ForwardedHttpServletRequestWrapper(request);

        when(request.getHeader(XForwardedFilter.CONTEXT_HEADER_NAME))
                .thenReturn(CONTEXT_HEADER_VALUE);

        // WHeN
        String contextPath = requestWrapper.getContextPath();

        // THeN
        verify(request, never()).getContextPath();
        verify(request).getHeader(XForwardedFilter.CONTEXT_HEADER_NAME);
        assertThat(contextPath).isEqualTo(CONTEXT_HEADER_VALUE);
    }


}
