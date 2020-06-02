package com.ecut.activiti.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ActivitiDataSourceProperties {
    @Value("jdbc:oracle:thin:@localhost:1521:orcl")
    private String url;

    @Value("t_test")
    private String username;

    @Value("test")
    private String password;

    @Value("oracle.jdbc.driver.OracleDriver")
    private String driverClassName;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDriverClassName() {
        return driverClassName;
    }

    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
