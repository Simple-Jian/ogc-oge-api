package whu.edu.cn.entity.catalog;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
public class Time {
    private LocalDate date;
    private DateRange interval;

    @Data
    @AllArgsConstructor
    public static class DateRange{
        private LocalDate lte;
        private  LocalDate gte;
    }
}
