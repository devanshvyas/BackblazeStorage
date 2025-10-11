package com.devanshvyas.BackblazeStorage.config.multitenancy;

import com.devanshvyas.BackblazeStorage.util.Constants;
import org.hibernate.engine.jdbc.connections.spi.AbstractDataSourceBasedMultiTenantConnectionProviderImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Component
public class MultiTenanatConnectionProviderImpl extends AbstractDataSourceBasedMultiTenantConnectionProviderImpl {
    public static final Logger logger = LoggerFactory.getLogger(MultiTenanatConnectionProviderImpl.class);
    private final DataSource dataSource;

    public MultiTenanatConnectionProviderImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    protected DataSource selectAnyDataSource() {
        return dataSource;
    }

    @Override
    protected DataSource selectDataSource(Object tenantIdentifier) {
        return dataSource;
    }

    @Override
    public Connection getConnection(Object tenantIdentifier) throws SQLException {
        String tenantId = tenantIdentifier != null ? tenantIdentifier.toString() : Constants.DEFAULT_TENANT;
        logger.info("Acquiring connection for tenant {}", tenantId);
        Connection connection = getAnyConnection();
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format("SET search_path TO %s", tenantId));
        }
        return connection;
    }

    @Override
    public void releaseConnection(Object tenantIdentifier, Connection connection) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.execute(String.format("SET search_path TO %s", Constants.DEFAULT_TENANT));
        }
        releaseAnyConnection(connection);
    }
}
