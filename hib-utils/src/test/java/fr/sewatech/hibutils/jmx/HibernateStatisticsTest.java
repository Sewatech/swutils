package fr.sewatech.hibutils.jmx;

import org.hibernate.stat.Statistics;
import org.junit.Before;
import org.junit.Test;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenType;
import javax.management.openmbean.TabularData;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

public class HibernateStatisticsTest {

    private static final Class[] AUTHORIZED_RETURN_TYPES = new Class[]
            {
                    String.class, String[].class,
                    Void.TYPE, Long.TYPE, Integer.TYPE, Boolean.TYPE,
                    CompositeData.class, TabularData.class
            };

    @Test
    public void should_implement_all_public_methods() {
        Method[] delegatePublicMethods = Statistics.class.getMethods();
        Method[] publicMethods = HibernateStatistics.class.getMethods();

        List<Method> otherMethods = Arrays.stream(delegatePublicMethods)
                .filter(method -> !hasElement(publicMethods, method.getName()))
                .collect(Collectors.toList());

        assertThat(otherMethods).isEmpty();
    }

    @Test
    public void all_public_methods_should_return_jmx_type() {
        Method[] publicMethods = HibernateStatistics.class.getMethods();

        List<Method> otherMethods = Arrays.stream(publicMethods)
                .filter(method -> method.getDeclaringClass().equals(HibernateStatistics.class))
                .filter(method -> !isReturnJmxCompliant(method))
                .collect(Collectors.toList());

        assertThat(otherMethods).isEmpty();
    }

    private boolean isReturnJmxCompliant(Method method) {
        return new HashSet<>(Arrays.asList(AUTHORIZED_RETURN_TYPES))
                    .contains(method.getReturnType());
    }

    private boolean hasElement(Method[] publicMethods, String name) {
        return Arrays.stream(publicMethods)
                .filter(method -> method.getName().equals(name))
                .count() > 0;
    }



}