package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Manifest;

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
  public List<Delivery> processManifests(List<Manifest> manifests) {
    List<Delivery> deliveries = new ArrayList<>();
    List<Delivery> incomplete = new ArrayList<>();

    LocalTime currentTime = getStartTime();
    Iterator<Manifest> manifestIterator = manifests.iterator();
    while (manifestIterator.hasNext()) {
      Manifest manifest = manifestIterator.next();
      if (!manifest.getOrderTime().isBefore(getEndTime())) {
        incomplete.add(incompleteDelivery(manifest.getOrderId()));
      } else {
        currentTime = laterTime(currentTime, manifest.getOrderTime());
        LocalTime completionTime = manifest.getCompletionTime(currentTime);
        if (!completionTime.isAfter(getEndTime())) {
          CustomerSatisfaction rating = manifest.getRating(currentTime);
          deliveries.add(new Delivery(manifest.getOrderId(), currentTime, rating));
          currentTime = completionTime;
        } else {
          incomplete.add(incompleteDelivery(manifest.getOrderId()));
        }
      }
    }
    deliveries.addAll(incomplete);

    return deliveries;
  }

}
