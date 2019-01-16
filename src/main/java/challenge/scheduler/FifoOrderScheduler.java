package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Scheduled;

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
  public List<Delivery> scheduleDeliveries(List<Scheduled> scheduledList) {
    List<Delivery> deliveries = new ArrayList<>();
    List<Delivery> incomplete = new ArrayList<>();

    LocalTime currentTime = getStartTime();
    Iterator<Scheduled> scheduledIterator = scheduledList.iterator();
    while (scheduledIterator.hasNext()) {
      Scheduled scheduled = scheduledIterator.next();
      if (!scheduled.getOrderTime().isBefore(getEndTime())) {
        incomplete.add(incompleteDelivery(scheduled.getOrderId()));
      } else {
        currentTime = laterTime(currentTime, scheduled.getOrderTime());
        LocalTime completionTime = scheduled.getCompletionTime(currentTime);
        if (!completionTime.isAfter(getEndTime())) {
          CustomerSatisfaction rating = scheduled.getRating(currentTime);
          deliveries.add(new Delivery(scheduled.getOrderId(), currentTime, rating));
          currentTime = completionTime;
        } else {
          incomplete.add(incompleteDelivery(scheduled.getOrderId()));
        }
      }
    }
    deliveries.addAll(incomplete);

    return deliveries;
  }

}
