package com.example.persona.demo.Configuration.Datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

public class DynamicRoutingDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getDataSource();
    }
}
