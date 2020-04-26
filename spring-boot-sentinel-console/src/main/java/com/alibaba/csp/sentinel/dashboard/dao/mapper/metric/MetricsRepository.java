package com.alibaba.csp.sentinel.dashboard.dao.mapper.metric;

import java.util.List;

/**
 * Repository interface for aggregated metrics data.
 *
 * @author 石头
 * @Date 2020/4/26
 * @Version 1.0
 **/
public interface MetricsRepository<T> {
    /**
     * Save the metric to the storage repository.
     *
     * @param metric metric data to save
     */
    void save(T metric);

    /**
     * Save all metrics to the storage repository.
     *
     * @param metrics metrics to save
     */
    void saveAll(Iterable<T> metrics);

    /**
     * Get all metrics by {@code appName} and {@code resourceName} between a period of time.
     *
     * @param app       application name for Sentinel
     * @param resource  resource name
     * @param startTime start timestamp
     * @param endTime   end timestamp
     * @return all metrics in query conditions
     */
    List<T> queryByAppAndResourceBetween(String app, String resource, long startTime, long endTime);

    /**
     * List resource name of provided application name.
     *
     * @param app application name
     * @return list of resources
     */
    List<String> listResourcesOfApp(String app);
}
