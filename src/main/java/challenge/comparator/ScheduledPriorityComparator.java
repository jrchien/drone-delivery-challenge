package challenge.comparator;

import java.time.LocalTime;
import java.util.Comparator;
import java.util.function.Function;
import challenge.model.Scheduled;

/**
 * Pushes detractor schedules to the back otherwise sorts by distance to location.
 * 
 * @author jeffrey
 */
public class ScheduledPriorityComparator implements Comparator<Scheduled> {

  private final LocalTime currentTime;

  /**
   * The constructor.
   * 
   * @param currentTime The current time.
   */
  public ScheduledPriorityComparator(LocalTime currentTime) {
    this.currentTime = currentTime;
  }

  @Override
  public int compare(Scheduled o1, Scheduled o2) {
    int compareResult = compareRatingTimes(o1, o2, Scheduled::getDetractorTime);
    if (compareResult == 0) {
      return o1.getTransitMinutes() - o2.getTransitMinutes();
    }
    return compareResult;
  }

  private int compareRatingTimes(Scheduled o1, Scheduled o2,
      Function<Scheduled, LocalTime> timeFunction) {
    LocalTime time1 = timeFunction.apply(o1);
    LocalTime time2 = timeFunction.apply(o2);
    if (!currentTime.isAfter(time1) && !currentTime.isAfter(time2)) {
      return 0;
    } else if (currentTime.isAfter(time1)) {
      return 1;
    } else {
      return -1;
    }
  }

}
