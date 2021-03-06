package com.bs.webusos.config;

import com.bs.webusos.model.LogMessages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.lookup.JndiDataSourceLookup;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

@Configuration
public class DataBaseConfig {
	private static final Logger LOGGER = LoggerFactory.getLogger(DataBaseConfig.class);

	@Value("spring.datasource.jndi-name")
	private String datasourceName;
    @Value("spring.datasource.schema")
    private String schema;
    @Value("spring.datasource.package")
    private String packageDB;
    @Value("spring.datasource.package.03")
	private String package03DB;

    @Autowired
    public void init(Environment env) {
        String profile = env.getActiveProfiles()[0];
        LOGGER.info("init - Profile --> {}", profile);
    }

    @Bean
    DataSource dataSource() {
		LOGGER.info("dataSource: {}", LogMessages.INIT_METHOD_LOG);
		DataSource dataSource;

		JndiDataSourceLookup jndiLookup = new JndiDataSourceLookup();

		Properties jndiEnvironment = new Properties();
		jndiEnvironment.setProperty("check-valid-connection-sql", "SELECT 1 FROM DUAL");
		jndiEnvironment.setProperty("background-validation", "true");
		jndiLookup.setJndiEnvironment(jndiEnvironment);

		dataSource = jndiLookup.getDataSource(this.getDatasourceName());

		LOGGER.info("DataSource => {}", dataSource);
		if (dataSource != null) {
			try (Connection connection = dataSource.getConnection()) {
				LOGGER.info("DataSource Connection => {}", connection);
				LOGGER.info("DataSource Connection valid? => {}", connection.isValid(5));
				LOGGER.info("DataSource Connection closed? => {}", connection.isClosed());
			} catch (SQLException e) {
				LOGGER.error("ERROR Obtaining DataSource's connection => {}", e.getMessage());
			}
		}

		LOGGER.info("dataSource: {}", LogMessages.END_METHOD_LOG);
		return dataSource;
	}
    
	public String getSchema() {
		return schema;
	}

	/**
	 * @return the datasourceName
	 */
	public String getDatasourceName() {
		return datasourceName;
	}

	/**
	 * @param datasourceName the datasourceName to set
	 */
	public void setDatasourceName(String datasourceName) {
		this.datasourceName = datasourceName;
	}

	public void setSchema(String schema) {
		this.schema = schema;
	}

	public String getPackageDB() {
		return packageDB;
	}
	
	public String getPackage03DB() {
		return package03DB;
	}
	
	public void setPackageDB(String packageDB) {
		this.packageDB = packageDB;
	}
	
	public void setPackage03DB(String package03DB) {
		this.package03DB = package03DB;
	}	
}
