package challenge.comparator;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.function.Function;
import challenge.model.Manifest;

/**
 * Pushes detractor schedules to the back otherwise sorts by distance to location.
 * 
 * @author jeffrey
 */
public class ManifestPriorityComparator implements Comparator<Manifest> {

  private final LocalTime currentTime;

  /**
   * The constructor.
   * 
   * @param currentTime The current time.
   */
  public ManifestPriorityComparator(LocalTime currentTime) {
    this.currentTime = currentTime;
  }

  @Override
  public int compare(Manifest firstManifest, Manifest secondManifest) {
    int compareResult =
        compareRatingTimes(firstManifest, secondManifest, Manifest::getDetractorTime);
    if (compareResult == 0) {
      return firstManifest.getTransitMinutes() - secondManifest.getTransitMinutes();
    }
    return compareResult;
  }

  private int compareRatingTimes(Manifest firstManifest, Manifest secondManifest,
      Function<Manifest, LocalTime> timeFunction) {
    LocalTime firstManifestTime = timeFunction.apply(firstManifest);
    LocalTime secondManifestTime = timeFunction.apply(secondManifest);
    if (!currentTime.isAfter(firstManifestTime) && !currentTime.isAfter(secondManifestTime)) {
      return 0;
    } else if (currentTime.isAfter(firstManifestTime)) {
      return 1;
    } else {
      return -1;
    }
  }

}
