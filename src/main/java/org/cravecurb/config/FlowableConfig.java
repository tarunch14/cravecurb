package org.cravecurb.config;

import javax.sql.DataSource;

import org.flowable.engine.ProcessEngine;
import org.flowable.engine.ProcessEngineConfiguration;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.impl.TaskServiceImpl;
import org.flowable.engine.impl.cfg.StandaloneProcessEngineConfiguration;
import org.flowable.spring.SpringProcessEngineConfiguration;
import org.flowable.spring.job.service.SpringAsyncExecutor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class FlowableConfig {

    // DataSource bean and other beans...
	
	@Bean
	public RuntimeService runtimeService(SpringProcessEngineConfiguration processEngineConfiguration) {
		return processEngineConfiguration.getProcessEngineConfiguration().getRuntimeService();
	}
	
//	@Bean
//	public TaskService taskService() {
//		return new TaskServiceImpl();
//	}

    @Bean
    public ProcessEngine processEngine() {
        ProcessEngineConfiguration processEngineConfiguration = new StandaloneProcessEngineConfiguration();
        processEngineConfiguration.setJdbcUrl("jdbc:mysql://localhost:3306/cravecurbdb");
        processEngineConfiguration.setJdbcUsername("root");
        processEngineConfiguration.setJdbcPassword("2011@Mysql");
        processEngineConfiguration.setJdbcDriver("com.mysql.cj.jdbc.Driver");
        processEngineConfiguration.setDatabaseSchemaUpdate(ProcessEngineConfiguration.DB_SCHEMA_UPDATE_TRUE);
        processEngineConfiguration.setAsyncExecutorActivate(false);  // Disable async executor to avoid conflicts

        return processEngineConfiguration.buildProcessEngine();
    }

    @Bean
    public SpringProcessEngineConfiguration processEngineConfiguration(
            DataSource dataSource,
            PlatformTransactionManager transactionManager,
            LocalContainerEntityManagerFactoryBean entityManagerFactory,
            @Qualifier("processAsyncExecutor") SpringAsyncExecutor asyncExecutor) {  // Use Qualifier here

        SpringProcessEngineConfiguration configuration = new SpringProcessEngineConfiguration();
        configuration.setDataSource(dataSource);
        configuration.setTransactionManager(transactionManager);
        configuration.setJpaEntityManagerFactory(entityManagerFactory.getObject());
        configuration.setJpaHandleTransaction(true);
        configuration.setJpaCloseEntityManager(true);
        configuration.setAsyncExecutor(asyncExecutor);  // Correct executor
        return configuration;
    }
    
}


