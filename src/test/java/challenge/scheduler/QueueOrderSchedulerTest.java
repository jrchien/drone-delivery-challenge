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

/**
 * Test for {@link QueueOrderScheduler}.
 * 
 * @author jeffrey
 */
public class QueueOrderSchedulerTest {

  /**
   * Passing in a <code>null</code> warehouse {@link GridCoordinate}.
   */
  @Test(expected = NullPointerException.class)
  public void testNullWarehouse() {
    OrderSchedulers.queueBased(null);
  }

  /**
   * Passing <code>null</code> {@link Order}s.
   */
  @Test(expected = NullPointerException.class)
  public void testNullOrders() {
    new FifoOrderScheduler(null);
    OrderSchedulers.queueBased().schedule(null);
  }

  /**
   * Passing empty {@link Order}s.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyOrders() {
    OrderSchedulers.queueBased().schedule(Collections.emptyList());
  }

  /**
   * Imports the test files and compares the expected NPS to the actual.
   */
  @Test
  public void testQueueBased() {
    Map<String, Integer> testInputMap = new HashMap<>();
    testInputMap.put("src/test/resources/test-input-1.txt", 75);
    testInputMap.put("src/test/resources/test-input-2.txt", 50);
    testInputMap.put("src/test/resources/test-input-3.txt", 50);

    testInputMap.entrySet().stream()
        .forEach(entry -> testQueueBased(entry.getKey(), entry.getValue()));
  }

  private void testQueueBased(String filePath, int expectedNPS) {
    List<Order> orders = OrderImporter.parseFile(filePath);
    List<Delivery> deliveries = OrderSchedulers.queueBased().schedule(orders);
    Assert.assertEquals("All orders must be scheduled for delivery.", orders.size(),
        deliveries.size());
    Assert.assertEquals(String.format("Queue Based NPS should be %d.", expectedNPS), expectedNPS,
        NPSCalculator.getNPS(deliveries));
  }

}
