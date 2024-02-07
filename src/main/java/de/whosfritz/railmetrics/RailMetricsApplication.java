package de.whosfritz.railmetrics;

import de.whosfritz.railmetrics.data.DataDispatcher;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EntityScan(basePackages = {"de.olech2412.adapter.dbadapter.model"}) // scan JPA entities from the dbadapter
@Log4j2
@EnableScheduling
public class RailMetricsApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(RailMetricsApplication.class, args);

        DataDispatcher dataDispatcher = context.getBean(DataDispatcher.class);
        dataDispatcher.fetchData();
    }

}
