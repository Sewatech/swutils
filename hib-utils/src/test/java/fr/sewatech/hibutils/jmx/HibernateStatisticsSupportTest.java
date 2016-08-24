package fr.sewatech.hibutils.jmx;

import ch.qos.logback.classic.Level;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.LoggerFactory;

import javax.management.*;
import java.lang.management.ManagementFactory;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class HibernateStatisticsSupportTest {

    private ObjectName beanName;
    private HibernateStatisticsSupport hibernateStatisticsSupport;
    private Statistics statistics;

    @Before
    public void setup() throws Exception {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        statistics = mock(Statistics.class);
        when(sessionFactory.getStatistics()).thenReturn(statistics);
        hibernateStatisticsSupport = new HibernateStatisticsSupport(sessionFactory);

        beanName = new ObjectName("fr.sewatech:type=HibernateStatistics");
    }

    @After
    public void clean() throws Exception {
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        mbeanServer.unregisterMBean(beanName);
    }

    @Test
    public void initStatistics_should_register_mbean() throws Exception {
        // GIVeN

        // WHeN
        hibernateStatisticsSupport.initStatistics();

        // THeN
        MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
        MBeanInfo mBeanInfo = mbeanServer.getMBeanInfo(beanName);
        // Would have thrown an InstanceNotFoundException if not found
        assertThat(mBeanInfo.getClassName()).isEqualTo(HibernateStatistics.class.getName());
    }

    @Test
    public void initStatistics_without_stat_logger_should_not_enable_statistics() throws Exception {
        // GIVeN
        setStatLoggerLevel(Level.OFF);

        // WHeN
        hibernateStatisticsSupport.initStatistics();

        // THeN
        verifyNoMoreInteractions(statistics);
    }

    @Test
    public void initStatistics_with_stat_logger_should_enable_statistics() throws Exception {
        // GIVeN
        setStatLoggerLevel(Level.DEBUG);

        // WHeN
        hibernateStatisticsSupport.initStatistics();

        // THeN
        verify(statistics).setStatisticsEnabled(true);
    }

    private void setStatLoggerLevel(Level level) {
        ch.qos.logback.classic.Logger logger
                = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger(HibernateStatistics.statisticsLogger.getName());
        logger.setLevel(level);
    }

}