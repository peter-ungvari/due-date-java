package org.example.duedate;

import java.util.Calendar;
import java.util.Date;

public class DueDateCalculator {
    
    public Date calculateDueDate(Date submitDate, int turnAroundHours) {
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(submitDate);
	calendar.add(Calendar.HOUR_OF_DAY, turnAroundHours);
	return calendar.getTime();
    }
}
