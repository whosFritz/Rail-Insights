package de.whosfritz.railinsights.data.dto;

import com.vaadin.flow.component.charts.model.DataSeries;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PercentageDataSeriesDTO {

    private DataSeries onPointPercentageSeries;
    private DataSeries delayedPercentageSeries;
    private DataSeries cancelledPercentageSeries;

    public PercentageDataSeriesDTO(DataSeries onTimeSeries, DataSeries delayedSeries, DataSeries cancelledSeries) {
        this.onPointPercentageSeries = onTimeSeries;
        this.delayedPercentageSeries = delayedSeries;
        this.cancelledPercentageSeries = cancelledSeries;
    }
}
