package org.example.duedate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class DueDateCalculatorTest {

    private static final int MAX_RANDOM_TURN_AROUND_HOURS = 500;
    private static final long RANDOM_SEED = 21L;
    private static final int NUM_RANDOM_ITERATIONS = 100;

    private DueDateCalculator dueDateCalculator = new DueDateCalculator();
    private Random random = new Random(RANDOM_SEED);

    @Test
    public void testCaculateDueDateFrom9AMto5PM() {
	for (int i = 0; i < NUM_RANDOM_ITERATIONS; ++i) {
	    Calendar submitCalendar = Calendar.getInstance();
	    submitCalendar.set(Calendar.YEAR, random.nextInt(2000) + 1000);
	    submitCalendar.set(Calendar.MONTH, random.nextInt(12));
	    submitCalendar.set(Calendar.HOUR_OF_DAY, random.nextInt(8) + 9);
	    submitCalendar.set(Calendar.DAY_OF_WEEK, random.nextInt(5) + 2);

	    int turnAroundHours = random.nextInt(MAX_RANDOM_TURN_AROUND_HOURS) + 1; //positive

	    Date dueDate = callAndConvertToUtilDate(submitCalendar, turnAroundHours);

	    Calendar calendar = Calendar.getInstance();
	    calendar.setTime(dueDate);

	    assertTrue("dueDate is greater than startDate", dueDate.after(submitCalendar.getTime()));
	    assertTrue("from 9AM ", calendar.get(Calendar.HOUR_OF_DAY) >= 9);
	    assertTrue("to 5PM", calendar.get(Calendar.HOUR_OF_DAY) <= 17);
	}
    }

    private Date callAndConvertToUtilDate(Calendar submitCalendar, int turnAroundHours) {
	return Date.from(dueDateCalculator.calculateDueDate(LocalDateTime.ofInstant(
		submitCalendar.toInstant(), ZoneId.systemDefault()), Duration.ofHours(turnAroundHours)).atZone(
		ZoneId.systemDefault()).toInstant());
    }

    @Test
    public void testCaculateDueDate1DayOverflow() {
	Calendar submitCalendar = new GregorianCalendar(2016, 8, 7, 14, 0); // 2016-09-07 2PM

	int turnAroundHours = 6;

	Date dueDate = callAndConvertToUtilDate(submitCalendar, turnAroundHours);

	Calendar calendar = new GregorianCalendar(2016, 8, 8, 12, 0); // 2016-09-07 12PM
	calendar.setTime(dueDate);
	assertEquals("2016-09-07 2PM + 6 hours = 2016-09-07 12PM", calendar.getTime(), dueDate);
    }

    @Test
    public void testCaculateDueDateNear5PM() {
	Calendar submitCalendar = Calendar.getInstance();
	submitCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
	submitCalendar.set(Calendar.HOUR_OF_DAY, 16);
	submitCalendar.set(Calendar.MINUTE, 1);

	int turnAroundHours = 1;

	Date dueDate = callAndConvertToUtilDate(submitCalendar, turnAroundHours);

	Calendar calendar = Calendar.getInstance();
	calendar.setTime(dueDate);

	assertEquals("9AM is used consistently isntead of 5PM", 9, calendar.get(Calendar.HOUR_OF_DAY));
    }

    @Test
    public void testCaculateDueDateMoreDaysOverflow() {
	Calendar submitCalendar = new GregorianCalendar(2016, 8, 9, 14, 0); // 2016-09-09 2PM

	int turnAroundHours = 24 * 7 + 4; //7 days, 4 hours

	Date dueDate = callAndConvertToUtilDate(submitCalendar, turnAroundHours);

	Calendar calendar = new GregorianCalendar(2016, 8, 19, 10, 0); // 2016-09-19 10AM
	calendar.setTime(dueDate);
	assertEquals("2016-09-09 2PM + 7 days 4 hours = 2016-09-19 10AM", calendar.getTime(), dueDate);
    }

    @Test
    public void testCaculateDueDateYearOverflow() {
	Calendar submitCalendar = new GregorianCalendar(2015, 11, 31, 14, 0); // 2015-12-31 2PM

	int turnAroundHours = 4;

	Date dueDate = callAndConvertToUtilDate(submitCalendar, turnAroundHours);

	Calendar calendar = new GregorianCalendar(2016, 0, 1, 10, 0); // 2016-01-01 10AM
	calendar.setTime(dueDate);
	assertEquals("2015-12-31 2PM + 4 hours = 2016-01-01 10AM", calendar.getTime(), dueDate);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaculateDueDateValidateSubmitHours() {
	Calendar submitCalendar = Calendar.getInstance();
	submitCalendar.set(Calendar.HOUR_OF_DAY, 0); //midnight
	callAndConvertToUtilDate(submitCalendar, 1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaculateDueDateValidateTurnAroundHours() {
	Calendar submitCalendar = new GregorianCalendar(2016, 8, 7, 14, 0); // 2016-09-07 12PM
	callAndConvertToUtilDate(submitCalendar, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaculateDueDateValidateTurnAroundHours2() {
	Calendar submitCalendar = new GregorianCalendar(2016, 8, 7, 14, 0); // 2016-09-07 12PM
	callAndConvertToUtilDate(submitCalendar, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCaculateDueDateValidateSubmitWeekdays() {
	Calendar submitCalendar = Calendar.getInstance();
	submitCalendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
	callAndConvertToUtilDate(submitCalendar, 1);
    }

}
