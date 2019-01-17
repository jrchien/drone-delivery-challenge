package challenge.scheduler;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import challenge.jenetics.ScheduleProblem;
import challenge.model.Delivery;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import challenge.model.Scheduled;
import io.jenetics.EnumGene;
import io.jenetics.LinearRankSelector;
import io.jenetics.Phenotype;
import io.jenetics.engine.Engine;
import io.jenetics.engine.EvolutionResult;
import io.jenetics.engine.Limits;

/**
 * Uses Jenetics to frame a problem and find the best fit {@link Order} schedule.
 * 
 * @author jeffrey
 */
public final class BestFitOrderScheduler extends FifoOrderScheduler {

  private static final int MINIMUM_GENERATION_FACTOR = 50;

  /**
   * The constructor.
   * 
   * @param warehouseLocation The warehouse {@link GridCoordinate} used to calculate distance.
   *        Cannot be <code>null</code>.
   */
  public BestFitOrderScheduler(GridCoordinate warehouseLocation) {
    super(warehouseLocation);
  }

  @Override
  public List<Delivery> scheduleDeliveries(List<Scheduled> scheduledList) {
    int minimumGeneration = MINIMUM_GENERATION_FACTOR * scheduledList.size();
    ScheduleProblem scheduleProblem = new ScheduleProblem(getWarehouseLocation(), scheduledList);

    Engine<EnumGene<Scheduled>, Integer> engine =
        Engine.builder(scheduleProblem).executor(Executors.newCachedThreadPool()).maximizing()
            .offspringSelector(new LinearRankSelector<>())
            .survivorsSelector(new LinearRankSelector<>()).build();

    Phenotype<EnumGene<Scheduled>, Integer> result =
        engine.stream().limit(Limits.bySteadyFitness(minimumGeneration))
            .limit(minimumGeneration * 2).collect(EvolutionResult.toBestPhenotype());

    return super.scheduleDeliveries(result.getGenotype().getChromosome().stream()
        .map(EnumGene::getAllele).collect(Collectors.toList()));
  }

}
