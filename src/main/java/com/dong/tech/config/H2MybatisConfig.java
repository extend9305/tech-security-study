package com.dong.tech.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.beans.ConstructorProperties;

/**
 * The Class H2MybatisConfig.
 *
 * @author dongsulee
 * @date 2023/09/22
 */
@Slf4j
@Configuration
@MapperScan(value = "com.dong.tech.mapper",sqlSessionFactoryRef = "SqlSessionFactory")
public class H2MybatisConfig {
    @Value("${spring.datasource.mapper-locations}")
    String mPath;

    @Bean(name = "dataSource")
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource DataSource(){
        return DataSourceBuilder.create().build();
    }
    @Bean(name = "SqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dataSource")DataSource dataSource, ApplicationContext applicationContext) throws Exception{
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dataSource);
        sqlSessionFactoryBean.setMapperLocations(applicationContext.getResources(mPath));
        return sqlSessionFactoryBean.getObject();
    }
    @Bean(name = "SessionTemplate")
    public SqlSessionTemplate SqlSessionTemplate(@Qualifier("SqlSessionFactory") SqlSessionFactory firstSqlSessionFactory){
        return new SqlSessionTemplate(firstSqlSessionFactory);
    }

}
