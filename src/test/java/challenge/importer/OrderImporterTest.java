package challenge.importer;

import java.time.LocalTime;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import challenge.model.GridCoordinate;
import challenge.model.Order;

/**
 * Tests import of {@link Order}.
 * 
 * @author jeffrey
 */
public class OrderImporterTest {

  private static final String FIRST_ORDER = "WM0001 N11W5 05:11:50";

  /**
   * Valid file path. Expects file to have the following as the first order:
   * <p>
   * WM0001 N11W5 05:11:50
   */
  @Test
  public void testValidTestFile() {
    List<Order> orders = OrderImporter.parseFile("src/test/resources/test-input-1.txt");
    Assert.assertFalse("Expects orders to have been imported.", orders.isEmpty());
    Order order = OrderImporter.toOrder(FIRST_ORDER);
    Assert.assertEquals("Expects both orders to be equal.", order, orders.iterator().next());
  }

  /**
   * Invalid path to file.
   */
  @Test
  public void testInvalidPath() {
    List<Order> orders = OrderImporter.parseFile("src/test/resources");
    Assert.assertTrue("Invalid path, so no orders should be returned.", orders.isEmpty());
  }

  /**
   * Line parsing with too many columns.
   */
  @Test
  public void testLineColumnCount() {
    Assert.assertNull("Expects three columns.",
        OrderImporter.toOrder("WM0002 S3E2 05:11:55 PROMOTER"));
  }

  /**
   * Line parsing with invalid inputs. Tests different combinations.
   */
  @Test
  public void testInvalidLineContent() {
    Assert.assertNull("All invalid.", OrderImporter.toOrder("Test Test Test"));
    Assert.assertNull("Only ID valid.", OrderImporter.toOrder("WM0001 Test Test"));
    Assert.assertNull("ID and Coordinates valid.", OrderImporter.toOrder("WM0001 N5E2 Test"));
    Assert.assertNull("ID and Time valid.", OrderImporter.toOrder("WM0001 Test 11:22:33"));
  }

  /**
   * Passing in a null argument should result in a null order.
   */
  @Test
  public void testNullLine() {
    Assert.assertNull("Inputs should be null checked.", OrderImporter.toOrder(null));
  }

  /**
   * Parsing with valid inputs.
   */
  @Test
  public void testValidLineContent() {
    Order order = OrderImporter.toOrder(FIRST_ORDER);
    Assert.assertNotNull("Valid orders should not return null.", order);
    Assert.assertEquals("Order ID should remain the same.", "WM0001", order.getOrderId());
    Assert.assertEquals("Customer Location should be parsed correctly.", GridCoordinate.of(-5, 11),
        order.getCustomerLocation());
    Assert.assertEquals("Order Time should be parsed correctly.", LocalTime.of(5, 11, 50),
        order.getOrderTime());
  }

}
