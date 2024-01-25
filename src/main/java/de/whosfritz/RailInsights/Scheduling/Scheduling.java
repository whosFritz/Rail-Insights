package de.whosfritz.RailInsights.Scheduling;

//@Configuration
//@EnableScheduling
public class Scheduling {
    //    @Scheduled(fixedDelay = 1000)
    public void scheduleFixedDelayTask() {
        System.out.println(
                "Fixed delay task - " + System.currentTimeMillis() / 1000);
    }
}
