package fr.sewatech.hibutils.jmx;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.management.MBeanServer;
import javax.management.ObjectName;
import java.lang.management.ManagementFactory;

public class HibernateStatisticsSupport {

    private static final Logger logger = LoggerFactory.getLogger(HibernateStatisticsSupport.class);

    private String beanName = "fr.sewatech:type=HibernateStatistics";
    private SessionFactory sessionFactory;

    public HibernateStatisticsSupport(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void initStatistics() {
        enableStatistics();
        registerHibernateMBeans();
    }

    private void enableStatistics() {
        if ( HibernateStatistics.statisticsLogger.isDebugEnabled() ) {
            sessionFactory.getStatistics().setStatisticsEnabled(true);
        }
    }

    private void registerHibernateMBeans() {
        try {
            MBeanServer mbeanServer = ManagementFactory.getPlatformMBeanServer();
            mbeanServer.registerMBean(
                    new HibernateStatistics(sessionFactory.getStatistics()),
                    new ObjectName(beanName)
            );
        } catch (Exception ex) {
            logger.warn("Problem when registering HibernateStatistics MBean", ex);
        }

    }
}
