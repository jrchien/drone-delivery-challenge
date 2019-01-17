package challenge.model;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic {@link Scheduled} tests.
 * 
 * @author jeffrey
 */
public class ScheduledTest {

  /**
   * Tests basic instantiation and getters.
   */
  @Test
  public void testBasicInstantiation() {
    Order order = new Order("WM0001", LocalTime.NOON, GridCoordinate.of(5, 10));
    Scheduled scheduled = new Scheduled(order, GridCoordinate.ZERO);
    Assert.assertNotNull("Expects instantiated Scheduled not to be null.", scheduled);
    Assert.assertEquals("Expects order id to be value declared.", "WM0001", scheduled.getOrderId());
    Assert.assertEquals("Expects order time to be value declared.", LocalTime.NOON,
        scheduled.getOrderTime());
    Assert.assertEquals("Expects transit to be the distance.", 15, scheduled.getTransitMinutes());
    Assert.assertEquals("Expects neutral time.", LocalTime.of(13, 45), scheduled.getNeutralTime());
    Assert.assertEquals("Expects detractor time.", LocalTime.of(15, 45),
        scheduled.getDetractorTime());
  }

  /**
   * Tests completion time. Checks to make sure there isn't wrap around.
   */
  @Test
  public void testCompletionTime() {
    Scheduled scheduled =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.of(10, 5), GridCoordinate.ZERO);
    LocalTime startTime = LocalTime.of(23, 30);

    Assert.assertEquals("Limits to Max Time.", LocalTime.MAX,
        scheduled.getCompletionTime(startTime));
  }

  /**
   * Tests scheduled comparison.
   */
  @Test
  public void testComparable() {
    Scheduled scheduled =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Scheduled differentLocation =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.of(5, 6), GridCoordinate.ZERO);
    Scheduled laterTime = new Scheduled("Test", LocalTime.NOON.plusMinutes(1), GridCoordinate.ZERO,
        GridCoordinate.ZERO);
    Scheduled differentId =
        new Scheduled("Test2", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertEquals("Same time. Compares location.", -1,
        scheduled.compareTo(differentLocation));
    Assert.assertEquals("Later time.", -1, scheduled.compareTo(laterTime));
    Assert.assertEquals("Same time and location. Compares ids.", -1,
        scheduled.compareTo(differentId));
  }

  /**
   * Tests the equals method.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    Scheduled scheduled =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Scheduled same =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Scheduled other =
        new Scheduled("Test", LocalTime.MIDNIGHT, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Scheduled another =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.of(1, 1), GridCoordinate.ZERO);
    Scheduled more =
        new Scheduled("Test2", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertTrue("Two objects with the same addresses should be equal.",
        scheduled.equals(scheduled));
    Assert.assertFalse("Null check.", scheduled.equals(null));
    Assert.assertFalse("Type check.", scheduled.equals(""));
    Assert.assertFalse("Different order times.", scheduled.equals(other));
    Assert.assertFalse("Different customer locations.", scheduled.equals(another));
    Assert.assertFalse("Different order IDs.", scheduled.equals(more));
    Assert.assertTrue("Same values.", scheduled.equals(same));
  }

  /**
   * Tests the toString method. Subject to change.
   */
  @Test
  public void testToString() {
    Scheduled scheduled =
        new Scheduled("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertEquals("Scheduled [orderId=Test, orderTime=12:00, transitMinutes=0]",
        scheduled.toString());
  }

}
