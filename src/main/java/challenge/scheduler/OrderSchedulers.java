package challenge.scheduler;

import challenge.model.GridCoordinate;

/**
 * Manages {@link OrderScheduler} instance creation.
 * 
 * @author jeffrey
 */
public final class OrderSchedulers {

  private OrderSchedulers() {}

  /**
   * Equivalent to calling {@link OrderSchedulers#bestFit(GridCoordinate)} with
   * {@link GridCoordinate#ZERO}
   * 
   * @return The {@link BestFitOrderScheduler}.
   */
  public static final OrderScheduler bestFit() {
    return bestFit(GridCoordinate.ZERO);
  }

  /**
   * Creates a new instance of {@link BestFitOrderScheduler}.
   * 
   * @param warehouseLocation The {@link GridCoordinate} of the warehouse.
   * @return The {@link BestFitOrderScheduler}.
   */
  public static final OrderScheduler bestFit(GridCoordinate warehouseLocation) {
    return new BestFitOrderScheduler(warehouseLocation);
  }

  /**
   * Equivalent to calling {@link OrderSchedulers#fifo(GridCoordinate)} with
   * {@link GridCoordinate#ZERO}
   * 
   * @return The {@link FifoOrderScheduler}.
   */
  public static final OrderScheduler fifo() {
    return fifo(GridCoordinate.ZERO);
  }

  /**
   * Creates a new instance of {@link FifoOrderScheduler}.
   * 
   * @param warehouseLocation The {@link GridCoordinate} of the warehouse.
   * @return The {@link FifoOrderScheduler}.
   */
  public static final OrderScheduler fifo(GridCoordinate warehouseLocation) {
    return new FifoOrderScheduler(warehouseLocation);
  }

  /**
   * Equivalent to calling {@link OrderSchedulers#queueBased(GridCoordinate)} with
   * {@link GridCoordinate#ZERO}
   * 
   * @return The {@link QueueOrderScheduler}.
   */
  public static final OrderScheduler queueBased() {
    return queueBased(GridCoordinate.ZERO);
  }

  /**
   * Creates a new instance of {@link QueueOrderScheduler}.
   * 
   * @param warehouseLocation The {@link GridCoordinate} of the warehouse.
   * @return The {@link QueueOrderScheduler}.
   */
  public static final OrderScheduler queueBased(GridCoordinate warehouseLocation) {
    return new QueueOrderScheduler(warehouseLocation);
  }

}
