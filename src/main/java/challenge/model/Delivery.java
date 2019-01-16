package challenge.model;

import java.time.LocalTime;
import com.google.common.base.Preconditions;

/**
 * Represents a scheduled delivery with an id, departure time, and rating.
 * 
 * @author jeffrey
 */
public class Delivery implements Comparable<Delivery> {

  private final String orderId;

  private final LocalTime departureTime;

  private final CustomerSatisfaction rating;

  /**
   * The constructor.
   * 
   * @param orderId The id for the order. Cannot be <code>null</code>.
   * @param departureTime The time the delivery begins. Cannot be <code>null</code>.
   * @param rating The {@link CustomerSatisfaction}. Cannot be <code>null</code>.
   */
  public Delivery(String orderId, LocalTime departureTime, CustomerSatisfaction rating) {
    Preconditions.checkNotNull(orderId, "The order id cannot be null.");
    Preconditions.checkNotNull(departureTime, "The departure time cannot be null.");
    Preconditions.checkNotNull(rating, "The customer satisfaction cannot be null.");

    this.orderId = orderId;
    this.departureTime = departureTime;
    this.rating = rating;
  }

  /**
   * @return The order ID.
   */
  public String getOrderId() {
    return orderId;
  }

  /**
   * @return The departure time.
   */
  public LocalTime getDepartureTime() {
    return departureTime;
  }

  /**
   * @return The rating.
   */
  public CustomerSatisfaction getRating() {
    return rating;
  }

  @Override
  public int compareTo(Delivery other) {
    return this.getDepartureTime().compareTo(other.getDepartureTime());
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
    Delivery other = (Delivery) obj;
    if (!departureTime.equals(other.departureTime)) {
      return false;
    }
    if (!orderId.equals(other.orderId)) {
      return false;
    }
    if (rating != other.rating) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "Delivery [orderId=" + orderId + ", departureTime=" + departureTime + ", rating="
        + rating + "]";
  }



}
