package com.incture.configurations;
import java.util.Properties;

import javax.annotation.Resource;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
@Configuration
@PropertySource(value= { "classpath:hibernate.properties" })

public class HibernateConfiguration {
		@Resource
		private Environment environment;

		@Bean
		public LocalSessionFactoryBean sessionFactory() {
			LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
			sessionFactory.setDataSource(dataSource());
			sessionFactory.setPackagesToScan(new String[] { "com.incture*" }); //entity package
			sessionFactory.setHibernateProperties(hibernateProperties());
			return sessionFactory;

		}

		private Properties hibernateProperties() {
			Properties properties = new Properties();
			properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
			properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
			properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
			properties.put("hibernate.hbm2ddl.auto", environment.getProperty("hibernate.hbm2ddl.auto"));
			properties.put("hibernate.globally_quoted_identifiers", "true");
			properties.put("hibernate.generate_statistics", true);
			properties.put("hibernate.jdbc.batch_size", environment.getRequiredProperty("hibernate.batch_size"));
			return properties;
		}

		@Bean
		public DataSource dataSource() {
			DriverManagerDataSource dataSource = null;
			dataSource = new DriverManagerDataSource();
			dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
			dataSource.setUrl(environment.getRequiredProperty("jdbc.url"));
			dataSource.setUsername(environment.getRequiredProperty("jdbc.username"));
			dataSource.setPassword(environment.getRequiredProperty("jdbc.password"));
			return dataSource;
		}

		@Bean
		public HibernateTransactionManager transactionManager() {
			HibernateTransactionManager hibernateTransactionManager = new HibernateTransactionManager();
			hibernateTransactionManager.setSessionFactory(sessionFactory().getObject());
			return hibernateTransactionManager;
		}
	}


