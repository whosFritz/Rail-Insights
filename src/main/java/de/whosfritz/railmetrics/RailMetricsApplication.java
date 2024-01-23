package de.whosfritz.railmetrics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {"de.olech2412.adapter.dbadapter.model"}) // scan JPA entities from the dbadapter
public class RailMetricsApplication {

    public static void main(String[] args) {
        SpringApplication.run(RailMetricsApplication.class, args);
    }

}
