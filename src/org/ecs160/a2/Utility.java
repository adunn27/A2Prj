package org.ecs160.a2;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAdjusters;
import java.util.Date;

public final class Utility {
    private Utility() throws Exception {
        throw new Exception("Should not instantiate Util class.");
    }

    public static final DateTimeFormatter timeFormatter12hr =
            DateTimeFormatter.ofPattern("MM/dd/yyyy hh:mm:ss a");
    public static final DateTimeFormatter timeFormatter24hr =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // Decided that since this tool will be mainly for work related purposes
    // a week should start with the work week on Monday rather than the calendar
    // week on Sunday
    public static LocalDateTime getStartOfWeek(LocalDateTime present) {
        return present.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
                      .with(LocalTime.MIN);
    }

    public static LocalDateTime getStartOfCurrentWeek() {
        return getStartOfWeek(LocalDateTime.now());
    }

    public static LocalDateTime getEndOfWeek(LocalDateTime present) {
        return present.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
                .with(LocalTime.MAX);
    }

    public static LocalDateTime getStartOfDay(LocalDate present) {
        return present.atStartOfDay().with(LocalTime.MIN);
    }

    public static LocalDateTime getEndOfDay(LocalDate present) {
        return present.atStartOfDay().with(LocalTime.MAX);
    }

    public static Date convertToDate(LocalDateTime localDateTime) {
        return Date.from(localDateTime.atZone(ZoneId.systemDefault())
                .toInstant()
        );
    }
    public static LocalDate convertToLocalDate(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }
    public static LocalDateTime convertToLocalDateTime(Date date) {
        return date.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
    }
    public static String durationToFormattedString(Duration duration) {
        return String.format("%d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());
    }
}
