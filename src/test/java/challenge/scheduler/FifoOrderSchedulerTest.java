package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import org.junit.Assert;
import org.junit.Test;
import challenge.calculator.NPSCalculator;
import challenge.importer.OrderImporter;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import challenge.model.OrderTest;

public class FifoOrderSchedulerTest {

  /**
   * Passing in a <code>null</code> warehouse {@link GridCoordinate}.
   */
  @Test(expected = NullPointerException.class)
  public void testNullWarehouse() {
    OrderSchedulers.fifo(null);
  }

  /**
   * Passing <code>null</code> {@link Order}s.
   */
  @Test(expected = NullPointerException.class)
  public void testNullOrders() {
    new FifoOrderScheduler(null);
    OrderSchedulers.fifo().schedule(null);
  }

  /**
   * Passing empty {@link Order}s.
   */
  @Test(expected = IllegalArgumentException.class)
  public void testEmptyOrders() {
    OrderSchedulers.fifo().schedule(Collections.emptyList());
  }

  /**
   * Ensures deliveries are not scheduled before orders.
   */
  @Test
  public void testDeliveriesAfterOrderTimes() {
    Map<String, Order> orderByIdMap =
        IntStream.range(0, 5).mapToObj(idNumber -> OrderTest.generateOrder(idNumber, 10, 10))
            .collect(Collectors.toMap(Order::getOrderId, Function.identity()));
    List<Delivery> deliveries =
        OrderSchedulers.fifo().schedule(new ArrayList<>(orderByIdMap.values()));
    deliveries.stream().forEach(delivery -> {
      Order order = orderByIdMap.get(delivery.getOrderId());
      Assert.assertFalse("Deliveries cannot be sent before the order is placed.",
          delivery.getDepartureTime().isBefore(order.getOrderTime()));
    });
  }

  /**
   * Imports the test files and compares the expected NPS to the actual.
   */
  @Test
  public void testFifo() {
    Map<String, Integer> testInputMap = new HashMap<>();
    testInputMap.put("src/test/resources/test-input-1.txt", 50);
    testInputMap.put("src/test/resources/test-input-2.txt", -12);
    testInputMap.put("src/test/resources/test-input-3.txt", 10);

    testInputMap.entrySet().stream().forEach(entry -> testFifo(entry.getKey(), entry.getValue()));
  }

  private void testFifo(String filePath, int expectedNPS) {
    List<Order> orders = OrderImporter.parseFile(filePath);
    List<Delivery> deliveries = OrderSchedulers.fifo().schedule(orders);
    Assert.assertEquals("All orders must be scheduled for delivery.", orders.size(),
        deliveries.size());
    Assert.assertEquals(String.format("Fifo NPS should be %d.", expectedNPS), expectedNPS,
        NPSCalculator.getNPS(deliveries));
  }

  /**
   * Tests incomplete orders.
   */
  @Test
  public void testIncompleteOrders() {
    Order lateOrder = new Order("WM0001", LocalTime.of(21, 45), GridCoordinate.of(5, 10));
    Order anotherLateOrder = new Order("WM0002", LocalTime.of(21, 0), GridCoordinate.of(20, 10));
    Order passEndOrder = new Order("WM0003", LocalTime.of(22, 0), GridCoordinate.of(1, 1));
    List<Delivery> deliveries =
        OrderSchedulers.fifo().schedule(Arrays.asList(lateOrder, anotherLateOrder, passEndOrder));
    Assert.assertEquals("Incomplete orders still need to be scheduled.", 3, deliveries.size());
    deliveries.stream().forEach(delivery -> {
      Assert.assertEquals("Incomplete orders should be scheduled with the max time.", LocalTime.MAX,
          delivery.getDepartureTime());
    });
  }
}
