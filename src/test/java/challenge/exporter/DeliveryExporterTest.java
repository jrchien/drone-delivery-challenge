package challenge.exporter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.model.CustomerSatisfaction;
import challenge.model.Delivery;
import challenge.model.DeliveryTest;

/**
 * Tests export of {@link Delivery}.
 * 
 * @author jeffrey
 */
public class DeliveryExporterTest {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryExporterTest.class);

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
    Delivery delivery = DeliveryTest.generateDelivery(CustomerSatisfaction.PROMOTER);
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
    List<Delivery> deliveries = DeliveryTest.generateDeliveries(5, 2, 3);
    String filePath = DeliveryExporter.exportToFile(deliveries);
    Assert.assertNotNull("File path not null.", filePath);

    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      Assert.assertEquals("Should match expected size.", deliveries.size() + 1,
          bufferedReader.lines().count());
    } catch (IOException exception) {
      LOG.error("Unable to read file at path: {}.", filePath);
    }
  }

}
