package challenge.exporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Stream;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;

/**
 * Tests export of {@link Delivery}.
 * 
 * @author jeffrey
 */
public class DeliveryExporterTest {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryExporterTest.class);

  private static final Random RANDOM = new Random();

  /**
   * Passing in a null argument should result in a null file path.
   */
  @Test
  public void testNullExport() {
    Assert.assertNull("Null check.", DeliveryExporter.exportToFile(null));
  }

  /**
   * Passing in an empty argument should result in a null file path.
   */
  @Test
  public void testEmptyExport() {
    Assert.assertNull("Empty list check.", DeliveryExporter.exportToFile(Collections.emptyList()));
  }

  /**
   * Passing a single delivery.
   */
  @Test
  public void testSingleDeliveryExport() {
    Delivery delivery = DeliveryExporterTest.generateDelivery(CustomerSatisfaction.PROMOTER);
    String filePath = DeliveryExporter.exportToFile(Collections.singletonList(delivery));
    Assert.assertNotNull("File path not null.", filePath);

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      String deliveryLine = bufferedReader.readLine();
      Assert.assertEquals("Format maintained on export.", DeliveryExporter.toLine(delivery),
          deliveryLine);
      Assert.assertEquals("NPS exported correctly.", "NPS 100", bufferedReader.readLine());
    } catch (IOException exception) {
      LOG.error("Unable to read file at path: {}.", filePath);
    }
  }

  /**
   * Passing a valid {@link Delivery} list should produce a file with each of the deliveries along
   * with the Net Promoter Score (NPS).
   */
  @Test
  public void testDeliveriesExport() {
    List<Delivery> deliveries = DeliveryExporterTest.generateDeliveries(5, 2, 3);
    String filePath = DeliveryExporter.exportToFile(deliveries);
    Assert.assertNotNull("File path not null.", filePath);

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      Assert.assertEquals("Should match expected size.", deliveries.size() + 1,
          bufferedReader.lines().count());
    } catch (IOException exception) {
      LOG.error("Unable to read file at path: {}.", filePath);
    }
  }

  /**
   * Creates a {@link Delivery} list filled with randomly generated deliveries following the number
   * of each {@link CustomerSatisfaction} types.
   * 
   * @param numberOfPromoters The number of {@link CustomerSatisfaction#PROMOTER} deliveries to
   *        create.
   * @param numberOfNeutral The number of {@link CustomerSatisfaction#NEUTRAL} deliveries to create.
   * @param numberOfDetractors The number of {@link CustomerSatisfaction#DETRACTOR} deliveries to
   *        create.
   * @return The generated list of deliveries.
   */
  public static final List<Delivery> generateDeliveries(int numberOfPromoters, int numberOfNeutral,
      int numberOfDetractors) {
    List<Delivery> deliveries = new ArrayList<>();
    Stream.generate(() -> generateDelivery(CustomerSatisfaction.PROMOTER)).limit(numberOfPromoters)
        .forEach(deliveries::add);
    Stream.generate(() -> generateDelivery(CustomerSatisfaction.NEUTRAL)).limit(numberOfNeutral)
        .forEach(deliveries::add);
    Stream.generate(() -> generateDelivery(CustomerSatisfaction.DETRACTOR))
        .limit(numberOfDetractors).forEach(deliveries::add);
    return deliveries;
  }

  private static final Delivery generateDelivery(CustomerSatisfaction rating) {
    return new Delivery(String.format("WM%04d", RANDOM.nextInt(1000)),
        LocalTime.MIN.plusSeconds(RANDOM.nextLong()), rating);
  }

}
