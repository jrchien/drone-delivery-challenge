package challenge.model;

/**
 * The customer satisfaction status based on the time to delivery (i.e. delivered - placed). Used
 * for NPS calculation.
 * 
 * @author jeffrey
 */
public enum CustomerSatisfaction {
  /**
   * The time to delivery was under 2 (exclusive) hours.
   */
  PROMOTER(0, 2),
  /**
   * The time to delivery was between 2 (inclusive) and 4 (exclusive) hours.
   */
  NEUTRAL(2, 4),
  /**
   * The time to delivery was 4 or more hours.
   */
  DETRACTOR(4, Integer.MAX_VALUE);

  private final int minimumHours;

  private final int maximumHours;

  /**
   * The constructor.
   * 
   * @param minimumHours The minimum number of hours to stay in the range (inclusive).
   * @param maximumHours The maximum number of hours to stay in the range (exclusive).
   */
  CustomerSatisfaction(int minimumHours, int maximumHours) {
    this.minimumHours = minimumHours;
    this.maximumHours = maximumHours;
  }

  /**
   * @return The minimum number of hours to stay in the range (inclusive).
   */
  public int getMinimumHours() {
    return minimumHours;
  }

  /**
   * @return The maximum number of hours to stay in the range (exclusive).
   */
  public int getMaximumHours() {
    return maximumHours;
  }

  /**
   * @param hours The number of hours.
   * @return Whether the number of hours fits within the range.
   */
  public boolean isValid(long hours) {
    return hours >= getMinimumHours() && hours < getMaximumHours();
  }

}
