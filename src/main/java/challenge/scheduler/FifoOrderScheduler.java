package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import com.google.common.base.Preconditions;
import challenge.calculator.CustomerSatisfactionCalculator;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;

/**
 * A basic {@link OrderScheduler} that schedules deliveries in the same order that the orders come
 * in.
 * 
 * @author jeffrey
 */
public class FifoOrderScheduler extends OrderScheduler {

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate} used to calculate distance.
   *        Cannot be <code>null</code>.
   */
  public FifoOrderScheduler(GridCoordinate warehouseLocation) {
    super(warehouseLocation);
  }

  @Override
  public List<Delivery> schedule(List<Order> orders) {
    Preconditions.checkNotNull(orders, "Orders cannot be null.");
    Preconditions.checkArgument(!orders.isEmpty(), "Orders cannot be empty.");

    List<Delivery> deliveries = new ArrayList<>();
    List<Delivery> incomplete = new ArrayList<>();

    LocalTime currentTime = getStartTime();
    Iterator<Order> orderIterator = orders.iterator();
    while (orderIterator.hasNext()) {
      Order order = orderIterator.next();
      if (!order.getOrderTime().isBefore(getEndTime())) {
        incomplete.add(incompleteDelivery(order));
      } else {
        currentTime = laterTime(currentTime, order.getOrderTime());
        int transitMinutes = getWarehouseLocation().getDistanceTo(order.getCustomerLocation());
        LocalTime completionTime = currentTime.plusMinutes(transitMinutes * 2);
        if (completionTime.isBefore(getEndTime())) {
          CustomerSatisfaction rating = CustomerSatisfactionCalculator
              .getRating(order.getOrderTime(), currentTime, transitMinutes);
          deliveries.add(new Delivery(order.getOrderId(), currentTime, rating));
          currentTime = completionTime;
        } else {
          incomplete.add(incompleteDelivery(order));
        }
      }
    }
    deliveries.addAll(incomplete);

    return deliveries;
  }

}
