package de.whosfritz.railinsights.data.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;


@Getter
@Setter
public class TripPercentageDTO {

    private LocalDate tripDate;

    private BigDecimal cancelledPercentage;

    private BigDecimal onTimePercentage;

    private BigDecimal delayedPercentage;
}