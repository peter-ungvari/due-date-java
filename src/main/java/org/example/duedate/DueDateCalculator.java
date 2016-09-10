package org.example.duedate;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DueDateCalculator {

    private static final int FIVE_PM = 17;
    private static final int NINE_AM = 9;

    public Date calculateDueDate(Date submitDate, int turnAroundHours) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(submitDate);

	validateSubmitDate(calendar);
	validateTurnAroundHours(turnAroundHours);

	for (int i = 0; i < turnAroundHours; ++i) {
	    nextWorkHour(calendar);
	}

	return calendar.getTime();
    }

    private void nextWorkHour(Calendar calendar) {
	if (16 == calendar.get(Calendar.HOUR_OF_DAY)) {
	    nextWeekDay(calendar);
	    calendar.set(Calendar.HOUR_OF_DAY, 9);
	} else {
	    calendar.add(Calendar.HOUR_OF_DAY, 1);
	}
    }

    private void nextWeekDay(Calendar calendar) {
	if (Calendar.FRIDAY == calendar.get(Calendar.DAY_OF_WEEK)) {
	    calendar.add(Calendar.DATE, 3); // MONDAY
	} else {
	    calendar.add(Calendar.DATE, 1); // NEXT calendar day
	}
    }

    private void validateSubmitDate(Calendar calendar) {
	List<Integer> weekends = Arrays.asList(Calendar.SUNDAY, Calendar.SATURDAY);

	if (weekends.contains(calendar.get(Calendar.DAY_OF_WEEK))) {
	    throw new IllegalArgumentException("Submit date is not a weekday. Only weekdays accepted");
	}

	if (calendar.get(Calendar.HOUR_OF_DAY) < NINE_AM) {
	    throw new IllegalArgumentException("Submit time is before 9AM. It should be between 9AM and 5PM");
	}

	if (calendar.get(Calendar.HOUR_OF_DAY) > FIVE_PM) {
	    throw new IllegalArgumentException("Submit time is after 5PM. It should be between 9AM and 5PM");
	}
    }

    private void validateTurnAroundHours(int turnAroundHours) {
	if (turnAroundHours <= 0) {
	    throw new IllegalArgumentException("turnAroundHours should be a positive integer");
	}
    }

}
