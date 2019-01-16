package challenge.calculator;

import java.util.Collections;
import org.junit.Assert;
import org.junit.Test;
import challenge.model.DeliveryTest;

/**
 * Tests the Net Promoter Score (NPS) calculation.
 * 
 * @author jeffrey
 */
public final class NPSCalculatorTest {

  /**
   * Passing in a null argument should result in a {@link NullPointerException}.
   */
  @Test(expected = NullPointerException.class)
  public void testNullNPS() {
    NPSCalculator.getNPS(null);
  }

  /**
   * Passing in an empty list should result in an {@link IllegalArgumentException}.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyNPS() {
    NPSCalculator.getNPS(Collections.emptyList());
  }

  /**
   * Calculates the NPS for multiple generated configurations of deliveries.
   */
  @Test
  public void testNPSCalculation() {
    Assert.assertEquals("Only promotors.", 100,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(1, 0, 0)));
    Assert.assertEquals("Only detractors.", -100,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(0, 0, 1)));
    Assert.assertEquals("Equal amount of promoters/detractors.", 0,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(1, 0, 1)));
    Assert.assertEquals("(2 - 1)/4 = 0.25.", 25,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(2, 1, 1)));
    Assert.assertEquals("Rounds from 14.285.", 14,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(3, 10, 1)));
    Assert.assertEquals("Rounds from -23.077.", -23,
        NPSCalculator.getNPS(DeliveryTest.generateDeliveries(5, 10, 11)));
  }

}
