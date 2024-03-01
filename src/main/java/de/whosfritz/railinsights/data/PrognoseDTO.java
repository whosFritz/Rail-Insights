package de.whosfritz.railinsights.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class PrognoseDTO {

    private LocalDateTime plannedTime;

    private int calculatedDelayInMinutes;

    private LocalDateTime predictedTime;

    private String line;

    private String stop;

    private PrognoseStateEnum state;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PrognoseDTO that = (PrognoseDTO) o;

        if (calculatedDelayInMinutes != that.calculatedDelayInMinutes) return false;
        if (plannedTime != null ? !plannedTime.equals(that.plannedTime) : that.plannedTime != null) return false;
        if (predictedTime != null ? !predictedTime.equals(that.predictedTime) : that.predictedTime != null)
            return false;
        if (line != null ? !line.equals(that.line) : that.line != null) return false;
        return stop != null ? stop.equals(that.stop) : that.stop == null;
    }

    @Override
    public int hashCode() {
        int result = plannedTime != null ? plannedTime.hashCode() : 0;
        result = 31 * result + calculatedDelayInMinutes;
        result = 31 * result + (predictedTime != null ? predictedTime.hashCode() : 0);
        result = 31 * result + (line != null ? line.hashCode() : 0);
        result = 31 * result + (stop != null ? stop.hashCode() : 0);
        return result;
    }

}
