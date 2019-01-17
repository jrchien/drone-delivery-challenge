package challenge.model;

import java.time.LocalTime;
import org.apache.commons.lang3.builder.CompareToBuilder;
import com.google.common.base.Preconditions;

/**
 * Represents a single placed order with an id, order time, and location.
 * 
 * @author jeffrey
 */
public final class Order implements Comparable<Order> {

  private final String orderId;

  private final LocalTime orderTime;

  private final GridCoordinate customerLocation;

  /**
   * The constructor.
   * 
   * @param orderId The id for the order. Cannot be <code>null</code>.
   * @param orderTime The time the order was placed. Cannot be <code>null</code>.
   * @param customerLocation The grid-based location to deliver to. Cannot be <code>null</code>.
   */
  public Order(String orderId, LocalTime orderTime, GridCoordinate customerLocation) {
    Preconditions.checkNotNull(orderId, "The order id cannot be null.");
    Preconditions.checkNotNull(orderTime, "The order time cannot be null.");
    Preconditions.checkNotNull(customerLocation, "The customer location cannot be null.");

    this.orderId = orderId;
    this.orderTime = orderTime;
    this.customerLocation = customerLocation;
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
   * @return The customer location.
   */
  public GridCoordinate getCustomerLocation() {
    return customerLocation;
  }

  /**
   * Comparison order: order time, customer location, id.
   */
  @Override
  public int compareTo(Order other) {
    return new CompareToBuilder().append(getOrderTime(), other.getOrderTime())
        .append(getCustomerLocation(), other.getCustomerLocation())
        .append(getOrderId(), other.getOrderId()).toComparison();
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
    Order other = (Order) obj;
    if (!customerLocation.equals(other.customerLocation)) {
      return false;
    }
    if (!orderId.equals(other.orderId)) {
      return false;
    }
    if (!orderTime.equals(other.orderTime)) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Order [orderId=" + orderId + ", orderTime=" + orderTime + ", customerLocation="
        + customerLocation + "]";
  }

}
