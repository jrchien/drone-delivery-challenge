package challenge.calculator;

import java.time.Duration;
import java.time.LocalTime;
import java.util.Arrays;
import challenge.model.CustomerSatisfaction;

public final class CustomerSatisfactionCalculator {

  private CustomerSatisfactionCalculator() {}

  /**
   * Calculates the arrival time by adding the one way delivery duration to the departure time.
   * 
   * @param orderTime The order {@link LocalTime}.
   * @param departureTime The departure {@link LocalTime}.
   * @param oneWayDeliveryDuration The duration a one-way trip takes in minutes.
   * @return The valid {@link CustomerSatisfaction}.
   */
  public static final CustomerSatisfaction getRating(LocalTime orderTime, LocalTime departureTime,
      int oneWayDeliveryDuration) {
    return getRating(orderTime, departureTime.plusMinutes(oneWayDeliveryDuration));
  }

  /**
   * Calculates the duration between the order time and arrival time to get the time to delivery.
   * Compares the hours for that duration to the {@link CustomerSatisfaction} ranges.
   * 
   * @param orderTime The order {@link LocalTime}.
   * @param arrivalTime The arrival {@link LocalTime}.
   * @return The valid {@link CustomerSatisfaction}.
   */
  public static final CustomerSatisfaction getRating(LocalTime orderTime, LocalTime arrivalTime) {
    Duration timeToDelivery = Duration.between(orderTime, arrivalTime);
    long hoursToDelivery = timeToDelivery.toHours();
    return Arrays.stream(CustomerSatisfaction.values())
        .filter(rating -> rating.isValid(hoursToDelivery)).findFirst()
        .orElse(CustomerSatisfaction.DETRACTOR);
  }

}
