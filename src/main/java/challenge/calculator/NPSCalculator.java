package challenge.calculator;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import com.google.common.base.Preconditions;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;

/**
 * Utility for calculating the Net Promoter Score (NPS) based on the {@link CustomerSatisfaction}
 * ranges.
 * 
 * @author jeffrey
 */
public final class NPSCalculator {

  private static final int FULL_PERCENTAGE = 100;

  private NPSCalculator() {}

  /**
   * Counts the {@link CustomerSatisfaction} of the collection of {@link Delivery}. Computes the
   * score based on the percentage of promoters minus the percentage of detractors. The score has a
   * minimum value of -100 and a maximum value of 100.
   * 
   * @param deliveries The collection of {@link Delivery}. Cannot be <code>null</code> or empty.
   * @return The Net Promoter Score (NPS).
   */
  public static final int getNPS(Collection<Delivery> deliveries) {
    Preconditions.checkNotNull(deliveries, "Deliveries cannot be null.");
    Preconditions.checkArgument(!deliveries.isEmpty(), "Deliveries cannot be empty.");

    float size = deliveries.size();
    Map<CustomerSatisfaction, Long> count = deliveries.stream().map(Delivery::getRating)
        .collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
    float promoters = count.getOrDefault(CustomerSatisfaction.PROMOTER, 0L);
    float detractors = count.getOrDefault(CustomerSatisfaction.DETRACTOR, 0L);

    float nps = (promoters - detractors) / size;

    return Math.round(nps * FULL_PERCENTAGE);
  }

}
