package com.ibarhatov.newswebapp;

import com.ibarhatov.newswebapp.model.Article;
import com.ibarhatov.newswebapp.model.Category;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;

/**
 * Created by Ivan on 28.02.2017.
 */
@Configuration
@ComponentScan
public class AppConfig {
    @Bean(name = "dataSource")
    public DriverManagerDataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(
                "jdbc:mysql://localhost:3306/news?useSSL=false", "root", "admin");
        return dataSource;
    }

    @Bean(name = "sessionFactory")
    public LocalSessionFactoryBean sessionFactory(@Qualifier("dataSource") DriverManagerDataSource dataSource) {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setAnnotatedClasses(Article.class, Category.class);
        sessionFactory.getHibernateProperties().put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        return sessionFactory;
    }
}
