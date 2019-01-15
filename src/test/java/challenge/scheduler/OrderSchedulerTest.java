package challenge.scheduler;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;
import challenge.calculator.NPSCalculator;
import challenge.importer.OrderImporter;
import challenge.model.Delivery;
import challenge.model.Order;

/**
 * Tests the scheduling to ensure it is ordering the deliveries properly.
 * 
 * @author jeffrey
 */
public class OrderSchedulerTest {

  /**
   * Passing in a null argument should result in an empty list.
   */
  @Test
  public void testNull() {
    Assert.assertTrue("Null check basic schedule.",
        OrderScheduler.basicSchedule(null, null).isEmpty());
    Assert.assertTrue("Null check best fit schedule.",
        OrderScheduler.bestFitSchedule(null, null).isEmpty());
  }

  /**
   * Tests the basic scheduling to ensure it creates deliveries based on iteration order.
   */
  @Test
  public void testBasicSchedule() {
    List<Order> orders = OrderImporter.parseFile("src/test/resources/test-input-2.txt");
    List<Delivery> deliveries = OrderScheduler.basicSchedule(orders);
    Assert.assertEquals("All orders must be scheduled for delivery.", orders.size(),
        deliveries.size());
    Assert.assertEquals("Basic NPS is -12.", -12, NPSCalculator.getNPS(deliveries));
  }

  /**
   * Imports the test files and compares the expected NPS to the actual.
   */
  @Test
  public void testBestFit() {
    Map<String, Integer> testMap = new HashMap<>();
    testMap.put("src/test/resources/test-input-1.txt", 75);
    testMap.put("src/test/resources/test-input-2.txt", 50);

    testMap.entrySet().stream().forEach(entry -> testBestFit(entry.getKey(), entry.getValue()));
  }

  private void testBestFit(String filePath, int expectedNPS) {
    List<Order> orders = OrderImporter.parseFile(filePath);
    List<Delivery> deliveries = OrderScheduler.bestFitSchedule(orders);
    Assert.assertEquals("All orders must be scheduled for delivery.", orders.size(),
        deliveries.size());
    Assert.assertEquals(String.format("Best fit NPS should be %d.", expectedNPS), expectedNPS,
        NPSCalculator.getNPS(deliveries));
  }

}
