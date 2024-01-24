package de.whosfritz.RailMetrics;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "railmetrics")
public class RailMetricsApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        SpringApplication.run(RailMetricsApplication.class, args);
    }

}
