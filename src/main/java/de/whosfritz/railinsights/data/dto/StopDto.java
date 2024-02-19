package de.whosfritz.railinsights.data.dto;

import de.olech2412.adapter.dbadapter.model.station.Station;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StopDto {

    String stopId;

    String stopName;

    Double latitude;

    Double longitude;

    boolean providesFurtherInformation;

    public StopDto(String stopId, String stopName, Double latitude, Double longitude, Station stationObject) {
        this.stopId = stopId;
        this.stopName = stopName;
        this.latitude = latitude;
        this.longitude = longitude;
        this.providesFurtherInformation = stationObject != null;
    }
}
