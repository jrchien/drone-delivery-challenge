package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import com.google.common.collect.TreeMultimap;
import challenge.comparator.ScheduledPriorityComparator;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import challenge.model.Scheduled;

/**
 * Uses a sorted queue system to schedule orders.
 * 
 * @author jeffrey
 */
public class QueueOrderScheduler extends OrderScheduler {

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate} used to calculate distance.
   *        Cannot be <code>null</code>.
   */
  public QueueOrderScheduler(GridCoordinate warehouseLocation) {
    super(warehouseLocation);
  }

  @Override
  public List<Delivery> scheduleDeliveries(List<Scheduled> scheduledList) {
    NavigableMap<LocalTime, Collection<Scheduled>> orderByTimeMap =
        byOrderTime(scheduledList).asMap();

    List<Scheduled> queue = new ArrayList<>();
    List<Delivery> deliveries = new ArrayList<>();

    LocalTime currentTime = updateQueue(getStartTime(), orderByTimeMap, queue);
    while (!queue.isEmpty()) {
      Scheduled scheduled = queue.remove(0);
      if (!scheduled.getOrderTime().isBefore(getEndTime())) {
        deliveries.add(incompleteDelivery(scheduled.getOrderId()));
      } else {
        LocalTime completionTime = scheduled.getCompletionTime(currentTime);
        if (completionTime.isBefore(getEndTime())) {
          CustomerSatisfaction rating = scheduled.getRating(currentTime);
          deliveries.add(new Delivery(scheduled.getOrderId(), currentTime, rating));
          currentTime = completionTime;
        } else {
          deliveries.add(incompleteDelivery(scheduled.getOrderId()));
        }
        currentTime = updateQueue(currentTime, orderByTimeMap, queue);
      }
    }
    return deliveries;
  }

  /**
   * Updates the queue. Skips to the next order time if there isn't anything let in the queue.
   * 
   * @param currentTime The current time.
   * @param orderByTimeMap The map of remaining orders.
   * @param queue The {@link Order} queue.
   * @return The updated current time.
   */
  private LocalTime updateQueue(LocalTime currentTime,
      NavigableMap<LocalTime, Collection<Scheduled>> orderByTimeMap, List<Scheduled> queue) {
    if (!orderByTimeMap.isEmpty()) {
      if (queue.isEmpty()) {
        currentTime = laterTime(currentTime, orderByTimeMap.firstKey());
      }
      NavigableMap<LocalTime, Collection<Scheduled>> headMap =
          orderByTimeMap.headMap(currentTime, true);
      if (!headMap.isEmpty()) {
        headMap.values().stream().flatMap(Collection::stream).forEach(queue::add);
        headMap.clear();
        queue.sort(new ScheduledPriorityComparator(currentTime));
      }
    }
    return currentTime;
  }

  private TreeMultimap<LocalTime, Scheduled> byOrderTime(List<Scheduled> scheduledList) {
    TreeMultimap<LocalTime, Scheduled> byOrderTimeMap = TreeMultimap.create();
    scheduledList.stream()
        .forEach(scheduled -> byOrderTimeMap.put(scheduled.getOrderTime(), scheduled));
    return byOrderTimeMap;
  }

}
