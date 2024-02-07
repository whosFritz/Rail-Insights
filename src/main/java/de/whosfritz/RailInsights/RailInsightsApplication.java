package de.whosfritz.RailInsights;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "railinsights")
public class RailInsightsApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(RailInsightsApplication.class, args);
    }

}
