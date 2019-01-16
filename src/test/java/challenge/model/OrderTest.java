package challenge.model;

import java.time.LocalTime;
import java.util.Random;
import org.junit.Assert;
import org.junit.Test;

/**
 * Basic {@link Order} tests.
 * 
 * @author jeffrey
 */
public class OrderTest {

  private static final Random RANDOM = new Random();

  /**
   * Tests basic instantiation and getters.
   */
  @Test
  public void testBasicInstantiation() {
    Order order = new Order("Test", LocalTime.NOON, GridCoordinate.ZERO);
    Assert.assertNotNull("Expects instantiated Order not to be null.", order);
    Assert.assertEquals("Expects order ID to be value declared.", "Test", order.getOrderId());
    Assert.assertEquals("Expects order time to be value declared.", LocalTime.NOON,
        order.getOrderTime());
    Assert.assertEquals("Expects customer location to be value declared.", GridCoordinate.ZERO,
        order.getCustomerLocation());
  }

  /**
   * Tests the equals method.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    Order order = new Order("Test", LocalTime.NOON, GridCoordinate.ZERO);
    Order same = new Order("Test", LocalTime.NOON, GridCoordinate.ZERO);
    Order other = new Order("Test", LocalTime.MIDNIGHT, GridCoordinate.ZERO);
    Order another = new Order("Test", LocalTime.NOON, GridCoordinate.of(1, 1));
    Order more = new Order("Test2", LocalTime.NOON, GridCoordinate.ZERO);

    Assert.assertTrue("Two objects with the same addresses should be equal.", order.equals(order));
    Assert.assertFalse("Null check.", order.equals(null));
    Assert.assertFalse("Type check.", order.equals(""));
    Assert.assertFalse("Different order times.", order.equals(other));
    Assert.assertFalse("Different customer locations.", order.equals(another));
    Assert.assertFalse("Different order IDs.", order.equals(more));
    Assert.assertTrue("Same values.", order.equals(same));
  }

  /**
   * Tests the toString method. Subject to change.
   */
  @Test
  public void testToString() {
    Order order = new Order("Test", LocalTime.NOON, GridCoordinate.ZERO);

    Assert.assertEquals("Compare toString.",
        "Order [orderId=Test, orderTime=12:00, customerLocation=GridCoordinate [x=0, y=0]]",
        order.toString());
  }

  /**
   * Generates a valid order id.
   * 
   * @return The generated order id.
   */
  public static final String generateOrderId() {
    return generateOrderId(RANDOM.nextInt(1000));
  }

  /**
   * Generates a valid order id.
   * 
   * @param idNumber The id number to use.
   * @return The generated order id.
   */
  public static final String generateOrderId(int idNumber) {
    return String.format("WM%04d", idNumber);
  }

  /**
   * Generates an order time.
   * 
   * @return The generated order time.
   */
  public static final LocalTime generateOrderTime() {
    return LocalTime.MIN.plusSeconds(RANDOM.nextLong());
  }

  /**
   * Generates an {@link Order} with a grid coordinate in the range.
   * 
   * @param idNumber The id number to use.
   * @param xMax The maximum x coordinate.
   * @param yMax The maximum y coordinate.
   * @return The generated order.
   */
  public static final Order generateOrder(int idNumber, int xMax, int yMax) {
    return generateOrder(generateOrderId(idNumber), xMax, yMax);
  }

  /**
   * Generates an {@link Order} with the id and with a grid coordinate in the range.
   * 
   * @param id Order ID.
   * @param xMax The maximum x coordinate.
   * @param yMax The maximum y coordinate.
   * @return The generated order.
   */
  public static final Order generateOrder(String id, int xMax, int yMax) {
    return new Order(id, generateOrderTime(),
        GridCoordinate.of(RANDOM.nextInt(xMax + 1), RANDOM.nextInt(yMax + 1)));
  }
}
