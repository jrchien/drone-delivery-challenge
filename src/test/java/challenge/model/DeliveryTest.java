package challenge.model;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic {@link Delivery} tests.
 * 
 * @author jeffrey
 */
public class DeliveryTest {

  /**
   * Tests basic instantiation and getters.
   */
  @Test
  public void testBasicInstantiation() {
    Delivery delivery = new Delivery("Test", LocalTime.NOON, CustomerSatisfaction.PROMOTER);
    Assert.assertNotNull("Expects instantiated Delivery not to be null.", delivery);
    Assert.assertEquals("Expects order ID to be value declared.", "Test", delivery.getOrderId());
    Assert.assertEquals("Expects departure time to be value declared.", LocalTime.NOON,
        delivery.getDepartureTime());
    Assert.assertEquals("Expects rating to be value declared.", CustomerSatisfaction.PROMOTER,
        delivery.getRating());
  }

  /**
   * Tests the equals method.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    Delivery delivery = new Delivery("Test", LocalTime.NOON, CustomerSatisfaction.PROMOTER);
    Delivery same = new Delivery("Test", LocalTime.NOON, CustomerSatisfaction.PROMOTER);
    Delivery other = new Delivery("Test", LocalTime.MIDNIGHT, CustomerSatisfaction.PROMOTER);
    Delivery another = new Delivery("Test", LocalTime.NOON, CustomerSatisfaction.DETRACTOR);
    Delivery more = new Delivery("Test2", LocalTime.NOON, CustomerSatisfaction.PROMOTER);

    Assert.assertTrue("Two objects with the same addresses should be equal.",
        delivery.equals(delivery));
    Assert.assertFalse("Null check.", delivery.equals(null));
    Assert.assertFalse("Type check.", delivery.equals(""));
    Assert.assertFalse("Different departure times.", delivery.equals(other));
    Assert.assertFalse("Different ratings.", delivery.equals(another));
    Assert.assertFalse("Different order IDs.", delivery.equals(more));
    Assert.assertTrue("Same values.", delivery.equals(same));
  }

  /**
   * Tests the toString method. Subject to change.
   */
  @Test
  public void testToString() {
    Delivery delivery = new Delivery("Test", LocalTime.NOON, CustomerSatisfaction.PROMOTER);

    Assert.assertEquals("Compare toString.",
        "Delivery [orderId=Test, departureTime=12:00, rating=PROMOTER]", delivery.toString());
  }

}
