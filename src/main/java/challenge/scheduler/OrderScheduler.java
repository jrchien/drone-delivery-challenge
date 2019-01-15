package challenge.scheduler;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.calculator.CustomerSatisfactionCalculator;
import challenge.jenetics.ScheduleProblem;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import io.jenetics.EnumGene;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;

/**
 * Handles the scheduling of the {@link Order}s and conversion into {@link Delivery}.
 * 
 * @author jeffrey
 */
public final class OrderScheduler {

  private static final Logger LOG = LoggerFactory.getLogger(OrderScheduler.class);

  private static final LocalTime START_TIME = LocalTime.of(6, 0);

  private static final LocalTime END_TIME = LocalTime.of(22, 0);

  private OrderScheduler() {}

  /**
   * Equivalent to calling {@link OrderScheduler#bestFitSchedule(GridCoordinate, List)} with
   * {@link GridCoordinate#ZERO}
   * 
   * @param orders The {@link Order} list.
   * @return The resulting {@link Delivery} list.
   */
  public static final List<Delivery> bestFitSchedule(List<Order> orders) {
    return bestFitSchedule(GridCoordinate.ZERO, orders);
  }

  /**
   * Uses Jenetics to determine the best fit schedule.
   * 
   * @param warehouseLocation The {@link GridCoordinate} of the warehouse.
   * @param orders The {@link Order} list.
   * @return The resulting {@link Delivery} list.
   */
  public static final List<Delivery> bestFitSchedule(GridCoordinate warehouseLocation,
      List<Order> orders) {
    if (warehouseLocation != null && orders != null) {
      ScheduleProblem scheduleProblem = new ScheduleProblem(warehouseLocation, orders);

      Engine<EnumGene<Order>, Integer> engine = Engine.builder(scheduleProblem)
          .executor(Executors.newFixedThreadPool(5)).maximizing().build();

      Phenotype<EnumGene<Order>, Integer> result =
          engine.stream().limit(Limits.bySteadyFitness(100)).limit(1000)
              .collect(EvolutionResult.toBestPhenotype());

      return basicSchedule(warehouseLocation, result.getGenotype().getChromosome().stream()
          .map(EnumGene::getAllele).collect(Collectors.toList()));
    } else {
      LOG.error("Cannot schedule with a null warehouse location or empty list of orders.");
      return Collections.emptyList();
    }
  }

  /**
   * Equivalent to calling {@link OrderScheduler#basicSchedule(GridCoordinate, List)} with
   * {@link GridCoordinate#ZERO}
   * 
   * @param orders The {@link Order} list.
   * @return The resulting {@link Delivery} list.
   */
  public static final List<Delivery> basicSchedule(List<Order> orders) {
    return basicSchedule(GridCoordinate.ZERO, orders);
  }

  /**
   * Schedules based on iteration order. If an order will not complete by the
   * {@link OrderScheduler#END_TIME}, creates an incomplete delivery entry.
   * 
   * @param warehouseLocation The {@link GridCoordinate} of the warehouse.
   * @param orders The {@link Order} list.
   * @return The resulting {@link Delivery} list.
   */
  public static final List<Delivery> basicSchedule(GridCoordinate warehouseLocation,
      List<Order> orders) {
    List<Delivery> deliveries = new ArrayList<>();

    if (warehouseLocation != null && orders != null) {
      LocalTime currentTime = START_TIME;
      Iterator<Order> orderIterator = orders.iterator();
      while (orderIterator.hasNext() && currentTime.isBefore(END_TIME)) {
        Order order = orderIterator.next();
        int transitMinutes = warehouseLocation.getDistanceTo(order.getCustomerLocation());
        LocalTime completionTime = currentTime.plusMinutes(transitMinutes * 2);
        if (completionTime.isBefore(END_TIME)) {
          CustomerSatisfaction rating = CustomerSatisfactionCalculator
              .getRating(order.getOrderTime(), currentTime, transitMinutes);
          deliveries.add(new Delivery(order.getOrderId(), currentTime, rating));
        } else {
          deliveries.add(incompleteDelivery(order));
        }
        currentTime = completionTime;
      }
      while (orderIterator.hasNext()) {
        Order order = orderIterator.next();
        deliveries.add(incompleteDelivery(order));
      }
    } else {
      LOG.error("Warehouse location {} and orders cannot be null.", warehouseLocation);
    }
    return deliveries;
  }

  private static final Delivery incompleteDelivery(Order order) {
    return new Delivery(order.getOrderId(), LocalTime.MAX, CustomerSatisfaction.DETRACTOR);
  }

}
