package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.NavigableMap;
import com.google.common.collect.TreeMultimap;
import challenge.comparator.ManifestPriorityComparator;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Manifest;

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
  public List<Delivery> processManifests(List<Manifest> manifests) {
    NavigableMap<LocalTime, Collection<Manifest>> orderByTimeMap = byOrderTime(manifests).asMap();

    List<Manifest> queue = new ArrayList<>();
    List<Delivery> deliveries = new ArrayList<>();

    LocalTime currentTime = updateQueue(getStartTime(), orderByTimeMap, queue);
    while (!queue.isEmpty()) {
      Manifest manifest = queue.remove(0);
      if (!manifest.getOrderTime().isBefore(getEndTime())) {
        deliveries.add(incompleteDelivery(manifest.getOrderId()));
      } else {
        LocalTime completionTime = manifest.getCompletionTime(currentTime);
        if (completionTime.isBefore(getEndTime())) {
          CustomerSatisfaction rating = manifest.getRating(currentTime);
          deliveries.add(new Delivery(manifest.getOrderId(), currentTime, rating));
          currentTime = completionTime;
        } else {
          deliveries.add(incompleteDelivery(manifest.getOrderId()));
        }
        currentTime = updateQueue(currentTime, orderByTimeMap, queue);
      }
    }
    return deliveries;
  }

  /**
   * Updates the queue. Skips to the next order time if there isn't anything left in the queue.
   * 
   * @param currentTime The current time.
   * @param orderByTimeMap The map of remaining {@link Manifest}s.
   * @param queue The {@link Manifest} queue.
   * @return The updated current time.
   */
  private LocalTime updateQueue(LocalTime currentTime,
      NavigableMap<LocalTime, Collection<Manifest>> orderByTimeMap, List<Manifest> queue) {
    if (!orderByTimeMap.isEmpty()) {
      if (queue.isEmpty()) {
        currentTime = laterTime(currentTime, orderByTimeMap.firstKey());
      }
      NavigableMap<LocalTime, Collection<Manifest>> headMap =
          orderByTimeMap.headMap(currentTime, true);
      if (!headMap.isEmpty()) {
        headMap.values().stream().flatMap(Collection::stream).forEach(queue::add);
        headMap.clear();
        queue.sort(new ManifestPriorityComparator(currentTime));
      }
    }
    return currentTime;
  }

  private TreeMultimap<LocalTime, Manifest> byOrderTime(List<Manifest> manifests) {
    TreeMultimap<LocalTime, Manifest> byOrderTimeMap = TreeMultimap.create();
    manifests.stream().forEach(manifest -> byOrderTimeMap.put(manifest.getOrderTime(), manifest));
    return byOrderTimeMap;
  }

}
