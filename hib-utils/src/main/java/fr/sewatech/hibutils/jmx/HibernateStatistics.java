package fr.sewatech.hibutils.jmx;

import org.hibernate.stat.Statistics;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;
import java.util.Arrays;

class HibernateStatistics implements HibernateStatisticsMBean {

    private static final Logger logger = LoggerFactory.getLogger(HibernateStatistics.class);
    static final Logger statisticsLogger = LoggerFactory.getLogger(HibernateStatisticsSupport.class.getPackage().getName() + ".statistics");
    private static final Logger queryStatisticsLogger = LoggerFactory.getLogger(statisticsLogger.getName() + ".query");
    private static final Logger entityStatisticsLogger = LoggerFactory.getLogger(statisticsLogger.getName() + ".entity");
    private static final Logger collectionStatisticsLogger = LoggerFactory.getLogger(statisticsLogger.getName() + ".collection");
    private static final Logger cacheStatisticsLogger = LoggerFactory.getLogger(statisticsLogger.getName() + ".cache");

    private final Statistics delegate;
    private final OpenMBeanSupport openMBeanSupport = new OpenMBeanSupport();

    public HibernateStatistics(Statistics delegate) {
        this.delegate = delegate;
    }

    public void clear() {
        delegate.clear();
    }

    public long getCloseStatementCount() {
        return delegate.getCloseStatementCount();
    }

    public long getCollectionFetchCount() {
        return delegate.getCollectionFetchCount();
    }

    public long getCollectionLoadCount() {
        return delegate.getCollectionLoadCount();
    }

    public long getCollectionRecreateCount() {
        return delegate.getCollectionRecreateCount();
    }

    public long getCollectionRemoveCount() {
        return delegate.getCollectionRemoveCount();
    }

    public String[] getCollectionRoleNames() {
        return delegate.getCollectionRoleNames();
    }

    public CompositeData getCollectionStatistics(String roleName) {
        return openMBeanSupport.toCompositeData(delegate.getCollectionStatistics(roleName), roleName);
    }

    @Override
    public TabularData getCollectionStatistics() {
        return openMBeanSupport.toTabularData(this::getCollectionStatistics, getCollectionRoleNames(), "CollectionStatistics");
    }

    public long getCollectionUpdateCount() {
        return delegate.getCollectionUpdateCount();
    }

    public long getConnectCount() {
        return delegate.getConnectCount();
    }

    public long getEntityDeleteCount() {
        return delegate.getEntityDeleteCount();
    }

    public long getEntityFetchCount() {
        return delegate.getEntityFetchCount();
    }

    public long getEntityInsertCount() {
        return delegate.getEntityInsertCount();
    }

    public long getEntityLoadCount() {
        return delegate.getEntityLoadCount();
    }

    public String[] getEntityNames() {
        return delegate.getEntityNames();
    }

    public CompositeData getEntityStatistics(String entityName) {
        return openMBeanSupport.toCompositeData(delegate.getEntityStatistics(entityName), entityName);
    }

    @Override
    public TabularData getEntityStatistics() {
        return openMBeanSupport.toTabularData(this::getEntityStatistics, getEntityNames(), "EntityStatistics");
    }

    public long getEntityUpdateCount() {
        return delegate.getEntityUpdateCount();
    }

    public long getFlushCount() {
        return delegate.getFlushCount();
    }

    public long getOptimisticFailureCount() {
        return delegate.getOptimisticFailureCount();
    }

    public long getPrepareStatementCount() {
        return delegate.getPrepareStatementCount();
    }

    public String[] getQueries() {
        return delegate.getQueries();
    }

    public long getQueryCacheHitCount() {
        return delegate.getQueryCacheHitCount();
    }

    public long getQueryCacheMissCount() {
        return delegate.getQueryCacheMissCount();
    }

    public long getQueryCachePutCount() {
        return delegate.getQueryCachePutCount();
    }

    public long getQueryExecutionCount() {
        return delegate.getQueryExecutionCount();
    }

    public long getQueryExecutionMaxTime() {
        return delegate.getQueryExecutionMaxTime();
    }

    public String getQueryExecutionMaxTimeQueryString() {
        return delegate.getQueryExecutionMaxTimeQueryString();
    }

    public CompositeData getQueryStatistics(String query) {
        return openMBeanSupport.toCompositeData(delegate.getQueryStatistics(query), query);
    }

    @Override
    public TabularData getQueryStatistics() {
        return openMBeanSupport.toTabularData(this::getQueryStatistics, getQueries(), "Query");
    }

    public long getSecondLevelCacheHitCount() {
        return delegate.getSecondLevelCacheHitCount();
    }

    public long getSecondLevelCacheMissCount() {
        return delegate.getSecondLevelCacheMissCount();
    }

    public long getSecondLevelCachePutCount() {
        return delegate.getSecondLevelCachePutCount();
    }

    public String[] getSecondLevelCacheRegionNames() {
        return delegate.getSecondLevelCacheRegionNames();
    }

    public CompositeData getSecondLevelCacheStatistics(String cacheName) {
        return openMBeanSupport.toCompositeData(delegate.getSecondLevelCacheStatistics(cacheName), cacheName);
    }

    public TabularData getSecondLevelCacheStatistics() throws OpenDataException {
        return openMBeanSupport.toTabularData(
                this::getSecondLevelCacheStatistics,
                getSecondLevelCacheRegionNames(),
                "SecondLevelCacheStatistics");
    }

    public long getSessionCloseCount() {
        return delegate.getSessionCloseCount();
    }

    public long getSessionOpenCount() {
        return delegate.getSessionOpenCount();
    }

    public long getStartTime() {
        return delegate.getStartTime();
    }

    public long getSuccessfulTransactionCount() {
        return delegate.getSuccessfulTransactionCount();
    }

    public long getTransactionCount() {
        return delegate.getTransactionCount();
    }

    public boolean isStatisticsEnabled() {
        return delegate.isStatisticsEnabled();
    }

    public void logSummary() {
        delegate.logSummary();
    }

    @Override
    public void logStatistics() {
        if (!statisticsLogger.isDebugEnabled()) {
            return;
        }

        statisticsLogger.debug("========= Hibernate Statistics ==============");
        statisticsLogger.debug("EntityLoadCount: " + delegate.getEntityLoadCount());
        statisticsLogger.debug("EntityFetchCount: " + delegate.getEntityFetchCount());
        statisticsLogger.debug("CollectionLoadCount: " + delegate.getCollectionLoadCount());
        statisticsLogger.debug("CollectionFetchCount: " + delegate.getCollectionFetchCount());
        statisticsLogger.debug("QueryExecutionMaxTime: " + delegate.getQueryExecutionMaxTime() + " // " + delegate.getQueryExecutionMaxTimeQueryString());
        statisticsLogger.debug("QueryExecutionCount: " + delegate.getQueryExecutionCount());
        statisticsLogger.debug("QueryCacheHitCount: " + delegate.getQueryCacheHitCount());

        if (queryStatisticsLogger.isTraceEnabled()) {
            queryStatisticsLogger.trace("=== Query Statistics");
            Arrays.stream(delegate.getQueries())
                    .forEach(query -> queryStatisticsLogger.trace(
                            query + ": " + delegate.getQueryStatistics(query).toString())
                    );
        }
        if (entityStatisticsLogger.isTraceEnabled()) {
            entityStatisticsLogger.trace("=== Entity Statistics");
            Arrays.stream(delegate.getEntityNames())
                    .forEach(name -> entityStatisticsLogger.trace(
                            name + ": " + delegate.getEntityStatistics(name).toString())
                    );
        }
        if (collectionStatisticsLogger.isTraceEnabled()) {
            collectionStatisticsLogger.trace("=== Collection Statistics");
            Arrays.stream(delegate.getCollectionRoleNames())
                    .forEach(name -> collectionStatisticsLogger.trace(
                            name + ": " + delegate.getCollectionStatistics(name).toString())
                    );
        }
        if (cacheStatisticsLogger.isTraceEnabled()) {
            cacheStatisticsLogger.trace("=== 2nd Level Cache Statistics");
            Arrays.stream(delegate.getSecondLevelCacheRegionNames())
                    .forEach(name -> collectionStatisticsLogger.trace(
                            name + ": " + delegate.getSecondLevelCacheStatistics(name).toString())
                    );
        }

        statisticsLogger.debug("========= Hibernate Statistics (end) ========");
    }

    public void setStatisticsEnabled(boolean enabled) {
        delegate.setStatisticsEnabled(enabled);
    }

}
