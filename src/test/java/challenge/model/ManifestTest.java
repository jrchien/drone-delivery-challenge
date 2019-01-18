package challenge.model;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic {@link Manifest} tests.
 * 
 * @author jeffrey
 */
public class ManifestTest {

  /**
   * Tests basic instantiation and getters.
   */
  @Test
  public void testBasicInstantiation() {
    Order order = new Order("WM0001", LocalTime.NOON, GridCoordinate.of(5, 10));
    Manifest manifest = new Manifest(order, GridCoordinate.ZERO);
    Assert.assertNotNull("Expects instantiated Scheduled not to be null.", manifest);
    Assert.assertEquals("Expects order id to be value declared.", "WM0001", manifest.getOrderId());
    Assert.assertEquals("Expects order time to be value declared.", LocalTime.NOON,
        manifest.getOrderTime());
    Assert.assertEquals("Expects transit to be the distance.", 15, manifest.getTransitMinutes());
    Assert.assertEquals("Expects neutral time.", LocalTime.of(13, 45), manifest.getNeutralTime());
    Assert.assertEquals("Expects detractor time.", LocalTime.of(15, 45),
        manifest.getDetractorTime());
  }

  /**
   * Tests completion time. Checks to make sure there isn't wrap around.
   */
  @Test
  public void testCompletionTime() {
    Manifest manifest =
        new Manifest("Test", LocalTime.NOON, GridCoordinate.of(10, 5), GridCoordinate.ZERO);
    LocalTime startTime = LocalTime.of(23, 30);

    Assert.assertEquals("Limits to Max Time.", LocalTime.MAX,
        manifest.getCompletionTime(startTime));
  }

  /**
   * Tests {@link Manifest#compareTo}.
   */
  @Test
  public void testComparable() {
    Manifest manifest =
        new Manifest("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Manifest differentLocation =
        new Manifest("Test", LocalTime.NOON, GridCoordinate.of(5, 6), GridCoordinate.ZERO);
    Manifest laterTime = new Manifest("Test", LocalTime.NOON.plusMinutes(1), GridCoordinate.ZERO,
        GridCoordinate.ZERO);
    Manifest differentId =
        new Manifest("Test2", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertEquals("Same time. Compares location.", -1, manifest.compareTo(differentLocation));
    Assert.assertEquals("Later time.", -1, manifest.compareTo(laterTime));
    Assert.assertEquals("Same time and location. Compares ids.", -1,
        manifest.compareTo(differentId));
  }

  /**
   * Tests the equals method.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    Manifest manifest =
        new Manifest("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Manifest same = new Manifest("Test", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Manifest other =
        new Manifest("Test", LocalTime.MIDNIGHT, GridCoordinate.ZERO, GridCoordinate.ZERO);
    Manifest another =
        new Manifest("Test", LocalTime.NOON, GridCoordinate.of(1, 1), GridCoordinate.ZERO);
    Manifest more = new Manifest("Test2", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertTrue("Two objects with the same addresses should be equal.",
        manifest.equals(manifest));
    Assert.assertFalse("Null check.", manifest.equals(null));
    Assert.assertFalse("Type check.", manifest.equals(""));
    Assert.assertFalse("Different order times.", manifest.equals(other));
    Assert.assertFalse("Different customer locations.", manifest.equals(another));
    Assert.assertFalse("Different order IDs.", manifest.equals(more));
    Assert.assertTrue("Same values.", manifest.equals(same));
  }

  /**
   * Tests the toString method. Subject to change.
   */
  @Test
  public void testToString() {
    Manifest scheduled =
        new Manifest("WM0001", LocalTime.NOON, GridCoordinate.ZERO, GridCoordinate.ZERO);

    Assert.assertEquals("Manifest [orderId=WM0001, orderTime=12:00, transitMinutes=0]",
        scheduled.toString());
  }

}
