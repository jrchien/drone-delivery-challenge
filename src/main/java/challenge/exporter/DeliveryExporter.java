package challenge.exporter;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.calculator.NPSCalculator;
import challenge.model.Delivery;
import challenge.parser.OrderParser;

/**
 * The utility used to export a {@link Delivery} list.
 * 
 * @author jeffrey
 */
public final class DeliveryExporter {

  private static final Logger LOG = LoggerFactory.getLogger(DeliveryExporter.class);

  private static final String FILE_PATH = "drone-delivery-output.txt";

  private static final String DELIMITER = " ";

  private static final String NPS_FORMAT = "NPS %d";

  private DeliveryExporter() {}

  /**
   * Sorts the deliveries by departure time and writes the deliveries to a file. Formats the
   * deliveries with {@link DeliveryExporter#toLine(Delivery)}. Appends the Net Promoter Score (NPS)
   * at the end of the file.
   * 
   * @param deliveries The {@link Delivery} list to export.
   * @return The path to the export file. <code>null</code> if deliveries invalid.
   */
  public static final String exportToFile(List<Delivery> deliveries) {
    if (deliveries != null && !deliveries.isEmpty()) {
      Collections.sort(deliveries, (a, b) -> a.getDepartureTime().compareTo(b.getDepartureTime()));
      File file = new File(FILE_PATH);
      try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))) {
        deliveries.stream().map(DeliveryExporter::toLine)
            .forEach(line -> writeNewLine(bufferedWriter, line));
        writeNewLine(bufferedWriter, String.format(NPS_FORMAT, NPSCalculator.getNPS(deliveries)));
      } catch (IOException exception) {
        LOG.error("Unable to write to file at path: {}", FILE_PATH);
      }
      return file.getAbsolutePath();
    }
    return null;
  }

  private static final void writeNewLine(BufferedWriter bufferedWriter, String line) {
    try {
      bufferedWriter.write(line);
      bufferedWriter.newLine();
    } catch (IOException exception) {
      LOG.error("Unable to write line.", exception);
    }
  }

  /**
   * Builds a string with two elements: ID and Time delimited by a space. (e.g. WM0001 10:55:20)
   * 
   * @param delivery The {@link Delivery} to format.
   * @return The formatted string.
   */
  public static final String toLine(Delivery delivery) {
    StringBuilder stringBuilder = new StringBuilder();
    stringBuilder.append(delivery.getOrderId());
    stringBuilder.append(DELIMITER);
    stringBuilder.append(OrderParser.formatOrderTime(delivery.getDepartureTime()));
    stringBuilder.append(DELIMITER);
    stringBuilder.append(delivery.getRating());
    return stringBuilder.toString();
  }

}
