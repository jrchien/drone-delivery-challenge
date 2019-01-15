package challenge.jenetics;

import java.util.List;
import java.util.function.Function;
import com.google.common.base.Preconditions;
import challenge.calculator.NPSCalculator;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import challenge.scheduler.OrderScheduler;
import io.jenetics.EnumGene;
import io.jenetics.engine.Codec;
import io.jenetics.engine.Codecs;
import io.jenetics.engine.Problem;
import io.jenetics.util.ISeq;

/**
 * The schedule problem. Creates a new permutation of the order list for each generation and uses
 * the NPS value to determine best fit.
 * 
 * @author jeffrey
 */
public class ScheduleProblem implements Problem<ISeq<Order>, EnumGene<Order>, Integer> {

  private final GridCoordinate warehouseLocation;

  private final ISeq<Order> orderSequence;

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate}. Cannot be <code>null</code>.
   * @param orders The list of {@link Order}s used for generations. Cannot be <code>null</code>.
   */
  public ScheduleProblem(GridCoordinate warehouseLocation, List<Order> orders) {
    Preconditions.checkNotNull(warehouseLocation, "Warehouse location cannot be null.");
    Preconditions.checkNotNull(orders, "Orders cannot be null.");

    this.warehouseLocation = warehouseLocation;
    this.orderSequence = ISeq.of(orders);
  }

  @Override
  public Function<ISeq<Order>, Integer> fitness() {
    return seq -> NPSCalculator
        .getNPS(OrderScheduler.basicSchedule(warehouseLocation, seq.asList()));
  }

  @Override
  public Codec<ISeq<Order>, EnumGene<Order>> codec() {
    return Codecs.ofPermutation(orderSequence);
  }

}
