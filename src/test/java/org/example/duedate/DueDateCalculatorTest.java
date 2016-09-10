package org.example.duedate;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class DueDateCalculatorTest {
    
    private static final int MAX_RANDOM_TURN_AROUND_HOURS = 500;
    private static final long RANDOM_SEED = 21L;
    
    private DueDateCalculator dueDateCalculator = new DueDateCalculator();
    private Random random = new Random(RANDOM_SEED);
    
    @Test
    public void testCaculateDueDateAfterStartTime() {
	Date startDate = new Date(System.currentTimeMillis() + random.nextLong());
	int turnAroundHours = random.nextInt(MAX_RANDOM_TURN_AROUND_HOURS) + 1; //greater than 0
	
	Date dueDate = dueDateCalculator.calculateDueDate(startDate, turnAroundHours);
	
	assertTrue("dueDate is greater than startDate", dueDate.after(startDate));
    }
    
    @Test
    public void testCaculateDueDateFrom9AMto5PM() {
	Date startDate = new Date(System.currentTimeMillis() + random.nextLong());
	int turnAroundHours = random.nextInt(MAX_RANDOM_TURN_AROUND_HOURS) + 1; //greater than 0
	
	Date dueDate = dueDateCalculator.calculateDueDate(startDate, turnAroundHours);
	
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(dueDate);
	
	assertTrue("from 9AM", calendar.get(Calendar.HOUR_OF_DAY) >= 9);
	assertTrue("to 5PM", calendar.get(Calendar.HOUR_OF_DAY) <= 17);
    }
    
    @Test
    public void testCaculateDueDate1DayOverflow() {
	Calendar submitCalendar = new GregorianCalendar(2016, 8, 7, 14, 0); // 2016-09-07 2PM

	int turnAroundHours = 6;
	
	Date dueDate = dueDateCalculator.calculateDueDate(submitCalendar.getTime(), turnAroundHours);
	
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
	
	Date startDate = submitCalendar.getTime();
	int turnAroundHours = 1;
	
	Date dueDate = dueDateCalculator.calculateDueDate(startDate, turnAroundHours);
	
	Calendar calendar = Calendar.getInstance();
	calendar.setTime(dueDate);
	
	assertEquals("9AM is used consistently isntead of 5PM", 9, calendar.get(Calendar.HOUR_OF_DAY));
    }
    
}
