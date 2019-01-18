package challenge.scheduler;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;
import com.google.common.base.Preconditions;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Manifest;
import challenge.model.Order;

/**
 * Handles the scheduling of the {@link Order}s and conversion into {@link Delivery}.
 * 
 * @author jeffrey
 */
public abstract class OrderScheduler {

  private static final LocalTime START_TIME = LocalTime.of(6, 0);

  private static final LocalTime END_TIME = LocalTime.of(22, 0);

  private final GridCoordinate warehouseLocation;

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate} used to calculate distance.
   *        Cannot be <code>null</code>.
   */
  public OrderScheduler(GridCoordinate warehouseLocation) {
    Preconditions.checkNotNull(warehouseLocation, "The warehouse location cannot be null.");
    this.warehouseLocation = warehouseLocation;
  }

  /**
   * Parses the orders into a {@link Manifest} list for deliveries. If an order will not complete by
   * the {@link OrderSchedulers#END_TIME}, creates an incomplete delivery entry.
   * 
   * @param orders The {@link Order} list. Cannot be <code>null</code> or empty.
   * @return The resulting {@link Delivery} list.
   */
  public final List<Delivery> schedule(List<Order> orders) {
    Preconditions.checkNotNull(orders, "Orders cannot be null.");
    Preconditions.checkArgument(!orders.isEmpty(), "Orders cannot be empty.");

    return processManifests(orders.stream().map(order -> new Manifest(order, warehouseLocation))
        .collect(Collectors.toList()));
  }

  /**
   * Schedules the deliveries based on the {@link Manifest} list.
   * 
   * @param manifests The {@link Manifest} list.
   * @return The resulting {@link Delivery} list.
   */
  public abstract List<Delivery> processManifests(List<Manifest> manifests);

  /**
   * @return The warehouse {@link GridCoordinate} used to calculate distance. Cannot be
   *         <code>null</code>.
   */
  public final GridCoordinate getWarehouseLocation() {
    return warehouseLocation;
  }

  /**
   * @return The start time for scheduling.
   */
  public LocalTime getStartTime() {
    return START_TIME;
  }

  /**
   * @return The end time for scheduling.
   */
  public LocalTime getEndTime() {
    return END_TIME;
  }

  /**
   * Compares the two times and returns the later of the two.
   * 
   * @param firstTime The first time.
   * @param secondTime The second time.
   * @return The later time.
   */
  protected final LocalTime laterTime(LocalTime firstTime, LocalTime secondTime) {
    return firstTime.isAfter(secondTime) ? firstTime : secondTime;
  }

  /**
   * Creates an incomplete {@link Delivery} at {@link LocalTime#MAX} with a
   * {@link CustomerSatisfaction#DETRACTOR} rating.
   * 
   * @param orderId The order id.
   * @return The incomplete {@link Delivery}.
   */
  protected final Delivery incompleteDelivery(String orderId) {
    return new Delivery(orderId, LocalTime.MAX, CustomerSatisfaction.DETRACTOR);
  }
}
