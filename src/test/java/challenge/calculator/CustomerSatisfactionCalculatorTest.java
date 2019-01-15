package challenge.calculator;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import challenge.model.CustomerSatisfaction;

public class CustomerSatisfactionCalculatorTest {

  @Test
  public void testGetRating() {
    LocalTime orderTime = LocalTime.of(5, 11);
    LocalTime neutralLowerBorderTime = orderTime.plusHours(2);
    LocalTime neutralUpperBorderTime = orderTime.plusHours(4);

    Assert.assertEquals(CustomerSatisfaction.PROMOTER,
        CustomerSatisfactionCalculator.getRating(orderTime, neutralLowerBorderTime.minusMinutes(1)));
    Assert.assertEquals(CustomerSatisfaction.NEUTRAL,
        CustomerSatisfactionCalculator.getRating(orderTime, neutralLowerBorderTime));
    Assert.assertEquals(CustomerSatisfaction.DETRACTOR,
        CustomerSatisfactionCalculator.getRating(orderTime, neutralUpperBorderTime));
    Assert.assertEquals(CustomerSatisfaction.NEUTRAL,
        CustomerSatisfactionCalculator.getRating(orderTime, neutralLowerBorderTime.minusMinutes(1), 15));
  }

}
