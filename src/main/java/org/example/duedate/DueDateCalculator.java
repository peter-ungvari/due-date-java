package org.example.duedate;

import java.util.Calendar;
import java.util.Date;

public class DueDateCalculator {

    public Date calculateDueDate(Date submitDate, int turnAroundHours) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(submitDate);
	
	for(int i = 0; i < turnAroundHours; ++i) {
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
    
}
