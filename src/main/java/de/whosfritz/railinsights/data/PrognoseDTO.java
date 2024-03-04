package de.whosfritz.railinsights.data;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Objects;

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
        if (!Objects.equals(plannedTime, that.plannedTime)) return false;
        if (!Objects.equals(predictedTime, that.predictedTime))
            return false;
        if (!Objects.equals(line, that.line)) return false;
        return Objects.equals(stop, that.stop);
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
