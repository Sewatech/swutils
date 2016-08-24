package fr.sewatech.hibutils.jmx;

import javax.management.openmbean.CompositeData;
import javax.management.openmbean.OpenDataException;
import javax.management.openmbean.TabularData;

public interface HibernateStatisticsMBean {

    boolean isStatisticsEnabled();

    void logStatistics();

    void setStatisticsEnabled(boolean var1);

    void clear();

    long getEntityDeleteCount();

    long getEntityInsertCount();

    long getEntityLoadCount();

    long getEntityFetchCount();

    TabularData getEntityStatistics() throws OpenDataException;

    long getEntityUpdateCount();

    String[] getEntityNames();

    CompositeData getEntityStatistics(String entityName) throws OpenDataException;

    long getCollectionLoadCount();

    long getCollectionFetchCount();

    TabularData getCollectionStatistics() throws OpenDataException;

    long getCollectionUpdateCount();

    long getCollectionRemoveCount();

    long getCollectionRecreateCount();

    String[] getCollectionRoleNames();

    CompositeData getCollectionStatistics(String roleName) throws OpenDataException;

    long getSecondLevelCacheHitCount();

    long getSecondLevelCacheMissCount();

    long getSecondLevelCachePutCount();

    String[] getSecondLevelCacheRegionNames();

    CompositeData getSecondLevelCacheStatistics(String cacheName) throws OpenDataException;

    long getQueryExecutionCount();

    long getQueryExecutionMaxTime();

    String getQueryExecutionMaxTimeQueryString();

    long getQueryCacheHitCount();

    long getQueryCacheMissCount();

    long getQueryCachePutCount();

    String[] getQueries();

    CompositeData getQueryStatistics(String query) throws OpenDataException;

    TabularData getQueryStatistics() throws OpenDataException;

    long getFlushCount();

    long getConnectCount();

    TabularData getSecondLevelCacheStatistics() throws OpenDataException;

    long getSessionCloseCount();

    long getSessionOpenCount();

    long getStartTime();

    long getSuccessfulTransactionCount();

    long getTransactionCount();

    long getPrepareStatementCount();

    long getCloseStatementCount();

    long getOptimisticFailureCount();

    void logSummary();

}