package challenge.jenetics;

import java.util.List;
import java.util.function.Function;
import com.google.common.base.Preconditions;
import challenge.calculator.NPSCalculator;
import challenge.model.GridCoordinate;
import challenge.model.Manifest;
import challenge.scheduler.OrderScheduler;
import challenge.scheduler.OrderSchedulers;
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
public class ScheduleProblem implements Problem<ISeq<Manifest>, EnumGene<Manifest>, Integer> {

  private final ISeq<Manifest> manifestSequence;

  private final OrderScheduler orderScheduler;

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate}. Cannot be <code>null</code>.
   * @param manifests The {@link Manifest}s used for generations. Cannot be <code>null</code>.
   */
  public ScheduleProblem(GridCoordinate warehouseLocation, List<Manifest> manifests) {
    Preconditions.checkNotNull(warehouseLocation, "Warehouse location cannot be null.");
    Preconditions.checkNotNull(manifests, "Manifests cannot be null.");

    this.orderScheduler = OrderSchedulers.fifo(warehouseLocation);
    this.manifestSequence = ISeq.of(manifests);
  }

  @Override
  public Function<ISeq<Manifest>, Integer> fitness() {
    return seq -> NPSCalculator.getNPS(orderScheduler.processManifests(seq.asList()));
  }

  @Override
  public Codec<ISeq<Manifest>, EnumGene<Manifest>> codec() {
    return Codecs.ofPermutation(manifestSequence);
  }

}
