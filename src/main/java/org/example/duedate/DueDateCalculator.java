package org.example.duedate;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.EnumSet;

public class DueDateCalculator {

    private static final int FIVE_PM = 17;
    private static final int NINE_AM = 9;

    public LocalDateTime calculateDueDate(LocalDateTime submitDate, Duration turnAround) {

	validateSubmitDate(submitDate);
	validateTurnAround(turnAround);

	LocalDateTime dueDate = submitDate;
	for (int i = 0; i < turnAround.toHours(); ++i) {
	    dueDate = nextWorkHour(submitDate);
	}

	return dueDate;
    }

    private LocalDateTime nextWorkHour(LocalDateTime dateTime) {
	LocalDateTime updatedDateTime;
	if (16 == dateTime.get(ChronoField.HOUR_OF_DAY)) {
	    updatedDateTime = nextWeekDay(dateTime);
	    updatedDateTime = updatedDateTime.withHour(NINE_AM);
	} else {
	    updatedDateTime = dateTime.plusHours(1L);
	}
	return updatedDateTime;
    }

    private LocalDateTime nextWeekDay(LocalDateTime dateTime) {
	if (DayOfWeek.FRIDAY == dateTime.getDayOfWeek()) {
	    return dateTime.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
	} else {
	    return dateTime.plusDays(1L);
	}
    }

    private void validateSubmitDate(LocalDateTime dateTime) {
	EnumSet<DayOfWeek> weekends = EnumSet.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

	if (weekends.contains(dateTime.getDayOfWeek())) {
	    throw new IllegalArgumentException("Submit date is not a weekday. Only weekdays accepted");
	}

	if (dateTime.getHour() < NINE_AM) {
	    throw new IllegalArgumentException("Submit time is before 9AM. It should be between 9AM and 5PM");
	}

	if (dateTime.getHour() > FIVE_PM) {
	    throw new IllegalArgumentException("Submit time is after 5PM. It should be between 9AM and 5PM");
	}
    }

    private void validateTurnAround(Duration turnAround) {
	if (turnAround.isZero() || turnAround.isNegative()) {
	    throw new IllegalArgumentException("turnAround should be a positive");
	}
    }

}
