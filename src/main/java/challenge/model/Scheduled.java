package challenge.model;

import java.time.LocalTime;
import com.google.common.base.Preconditions;
import challenge.calculator.CustomerSatisfactionCalculator;

/**
 * Used for scheduling. Holds fields useful for evaluating priority.
 * 
 * @author jeffrey
 */
public class Scheduled implements Comparable<Scheduled> {

  private final String orderId;

  private final LocalTime orderTime;

  private final int transitMinutes;

  private final LocalTime neutralTime;

  private final LocalTime detractorTime;

  /**
   * The constructor.
   * 
   * @param order The {@link Order}. Cannot be <code>null</code>.
   * @param warehouseLocation The warehouse {@link GridCoordinate}. Cannot be <code>null</code>.
   */
  public Scheduled(Order order, GridCoordinate warehouseLocation) {
    Preconditions.checkNotNull(order, "The order cannot be null.");
    Preconditions.checkNotNull(warehouseLocation, "The warehouse location cannot be null.");

    this.orderId = order.getOrderId();
    this.orderTime = order.getOrderTime();
    this.transitMinutes = warehouseLocation.getDistanceTo(order.getCustomerLocation());
    this.neutralTime = toRatingTime(CustomerSatisfaction.NEUTRAL);
    this.detractorTime = toRatingTime(CustomerSatisfaction.DETRACTOR);
  }

  /**
   * The constructor.
   * 
   * @param orderId The id for the order. Cannot be <code>null</code>.
   * @param orderTime The time the order was placed. Cannot be <code>null</code>.
   * @param customerLocation The grid-based location to deliver to. Cannot be <code>null</code>.
   * @param warehouseLocation The warehouse {@link GridCoordinate}. Cannot be <code>null</code>.
   */
  public Scheduled(String orderId, LocalTime orderTime, GridCoordinate customerLocation,
      GridCoordinate warehouseLocation) {
    Preconditions.checkNotNull(orderId, "The order id cannot be null.");
    Preconditions.checkNotNull(orderTime, "The order time cannot be null.");
    Preconditions.checkNotNull(customerLocation, "The customer location cannot be null.");
    Preconditions.checkNotNull(warehouseLocation, "The warehouse location cannot be null.");

    this.orderId = orderId;
    this.orderTime = orderTime;
    this.transitMinutes = warehouseLocation.getDistanceTo(customerLocation);
    this.neutralTime = toRatingTime(CustomerSatisfaction.NEUTRAL);
    this.detractorTime = toRatingTime(CustomerSatisfaction.DETRACTOR);
  }

  private LocalTime toRatingTime(CustomerSatisfaction rating) {
    return orderTime.plusHours(rating.getMinimumHours()).minusMinutes(transitMinutes);
  }

  /**
   * @return The order id.
   */
  public String getOrderId() {
    return orderId;
  }

  /**
   * @return The order time.
   */
  public LocalTime getOrderTime() {
    return orderTime;
  }

  /**
   * @return The minutes it takes to reach the customer location from the warehouse location.
   */
  public int getTransitMinutes() {
    return transitMinutes;
  }

  /**
   * @return The latest time before an order becomes neutral.
   */
  public LocalTime getNeutralTime() {
    return neutralTime;
  }

  /**
   * @return The latest time before an order becomes a detractor.
   */
  public LocalTime getDetractorTime() {
    return detractorTime;
  }

  /**
   * Calculates the {@link CustomerSatisfaction} based on the departure time.
   * 
   * @param departureTime The departure time.
   * @return The {@link CustomerSatisfaction}.
   */
  public CustomerSatisfaction getRating(LocalTime departureTime) {
    return CustomerSatisfactionCalculator.getRating(getOrderTime(), departureTime, transitMinutes);
  }

  /**
   * Adds the delivery and return times to the start time.
   * 
   * @param startTime The start time.
   * @return The delivery completion time.
   */
  public LocalTime getCompletionTime(LocalTime startTime) {
    return startTime.plusMinutes(transitMinutes * 2);
  }

  @Override
  public int compareTo(Scheduled other) {
    return getOrderTime().compareTo(other.getOrderTime());
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    Scheduled other = (Scheduled) obj;
    if (!detractorTime.equals(other.detractorTime)) {
      return false;
    }
    if (!orderId.equals(other.orderId)) {
      return false;
    }
    if (!orderTime.equals(other.orderTime)) {
      return false;
    }
    if (transitMinutes != other.transitMinutes) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Scheduled [orderId=" + orderId + ", orderTime=" + orderTime + ", transitMinutes="
        + transitMinutes + ", detractorTime=" + detractorTime + "]";
  }

}
