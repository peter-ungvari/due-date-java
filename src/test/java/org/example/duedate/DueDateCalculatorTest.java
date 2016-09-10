package org.example.duedate;

import java.util.Date;
import java.util.Random;
import org.junit.Test;
import static org.junit.Assert.*;

public class DueDateCalculatorTest {
    
    private static final int MAX_RANDOM_TURN_AROUND_HOURS = 500;
    
    private DueDateCalculator dueDateCalculator = new DueDateCalculator();
    private Random random = new Random();
    
    @Test
    public void testCaculateDueDateAfterStartTime() {
	Date startDate = new Date(System.currentTimeMillis() + random.nextLong());
	int turnAroundHours = random.nextInt(MAX_RANDOM_TURN_AROUND_HOURS) + 1; //greater than 0
	
	Date dueDate = dueDateCalculator.calculateDueDate(startDate, turnAroundHours);
	
	assertTrue("dueDate is greater than startDate", dueDate.after(startDate));
    }
    
}
