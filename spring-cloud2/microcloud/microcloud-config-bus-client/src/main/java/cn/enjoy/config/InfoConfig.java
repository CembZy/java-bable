package cn.enjoy.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

@Component
@RefreshScope
public class InfoConfig {
    @Value("${info.app.name}")
    private String appName ;
    @Value("${info.company.name}")
    private String companyName ;

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    @Override
    public String toString() {
        return "InfoConfig{" +
                "appName='" + appName + '\'' +
                ", companyName='" + companyName + '\'' +
                '}';
    }
}