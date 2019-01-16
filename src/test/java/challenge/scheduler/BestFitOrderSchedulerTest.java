package challenge.scheduler;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import challenge.calculator.NPSCalculator;
import challenge.importer.OrderImporter;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;

public class BestFitOrderSchedulerTest {

  /**
   * Passing in a <code>null</code> warehouse {@link GridCoordinate}.
   */
  @Test(expected = NullPointerException.class)
  public void testNullWarehouse() {
    OrderSchedulers.bestFit(null);
  }

  /**
   * Passing <code>null</code> {@link Order}s.
   */
  @Test(expected = NullPointerException.class)
  public void testNullOrders() {
    OrderSchedulers.bestFit().schedule(null);
  }

  /**
   * Passing empty {@link Order}s.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyOrders() {
    OrderSchedulers.bestFit().schedule(Collections.emptyList());
  }

  /**
   * Imports the test files and compares the expected NPS to the actual.
   */
  @Test
  public void testBestFit() {
    Map<String, Integer> testMap = new HashMap<>();
    testMap.put("src/test/resources/test-input-1.txt", 75);
    testMap.put("src/test/resources/test-input-2.txt", 50);
    testMap.put("src/test/resources/test-input-3.txt", 44);

    testMap.entrySet().stream().forEach(entry -> testBestFit(entry.getKey(), entry.getValue()));
  }

  private void testBestFit(String filePath, int expectedNPS) {
    List<Order> orders = OrderImporter.parseFile(filePath);
    List<Delivery> deliveries = OrderSchedulers.bestFit().schedule(orders);
    Assert.assertEquals("All orders must be scheduled for delivery.", orders.size(),
        deliveries.size());
    Assert.assertEquals(String.format("Best fit NPS should be %d.", expectedNPS), expectedNPS,
        NPSCalculator.getNPS(deliveries));
  }

}
