package challenge.importer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.model.GridCoordinate;
import challenge.model.Order;
import challenge.parser.OrderParser;

/**
 * The order importer. Assumes the columns in the file are in the following order:
 * <p>
 * ID, LOCATION, TIME.
 * 
 * @author jeffrey
 */
public final class OrderImporter {

  private static final Logger LOG = LoggerFactory.getLogger(OrderImporter.class);

  private static final String DELIMITER = " ";

  private static final int NUMBER_OF_COLUMNS = 3;

  private OrderImporter() {}

  /**
   * Parses the file to get the orders. Ignores invalid lines.
   * 
   * @param filePath The path to the order file to importer.
   * @return The list of {@link Order}.
   */
  public static final List<Order> parseFile(String filePath) {
    List<Order> orders = new ArrayList<>();
    try (BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath))) {
      bufferedReader.lines().map(OrderImporter::toOrder).filter(Objects::nonNull)
          .forEach(orders::add);
    } catch (IOException exception) {
      LOG.error("Unable to read file at path: {}.", filePath);
    }
    return orders;
  }

  /**
   * Parses a String into an {@link Order}. Expects the String to be in the format:
   * <p>
   * ID Coordinate Time
   * 
   * @param line The String to parse.
   * @return The parsed {@link Order} or <code>null</code> if invalid.
   */
  public static final Order toOrder(String line) {
    Order order = null;
    if (line != null) {
      String[] columns = line.trim().split(DELIMITER);
      if (columns.length == NUMBER_OF_COLUMNS) {
        String orderId = OrderParser.toOrderId(columns[0]);
        GridCoordinate coordinate = OrderParser.toGridCoordinates(columns[1]);
        LocalTime orderTime = OrderParser.toOrderTime(columns[2]);
        if (orderId != null && coordinate != null && orderTime != null) {
          order = new Order(orderId, orderTime, coordinate);
        }
      }
    }
    return order;
  }

}
