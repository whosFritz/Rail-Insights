package de.whosfritz.railinsights;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.theme.Theme;
import de.whosfritz.railinsights.ui.services.DataProviderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@Theme(value = "railinsights")
@EntityScan(basePackages = {"de.olech2412.adapter.dbadapter.model"}) // scan JPA entities from the dbadapter
@Log4j2
@EnableScheduling
public class RailInsightsApplication implements AppShellConfigurator {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RailInsightsApplication.class, args);

        DataProviderService dataProviderService = context.getBean(DataProviderService.class);
        dataProviderService.calculateData();
    }

}
