package challenge.parser;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import challenge.model.GridCoordinate;

/**
 * Utility used to parse and validate order fields.
 * 
 * @author jeffrey
 */
public final class OrderParser {

  private static final Logger LOG = LoggerFactory.getLogger(OrderParser.class);

  private static final int ORDER_ID_LENGTH = 6;

  private static final String ORDER_ID_FORMAT = String.format("WM[0-9]{%d}", ORDER_ID_LENGTH - 2);

  private static final String ORDER_TIME_FORMAT = "HH:mm:ss";

  private static final DateTimeFormatter ORDER_TIME_FORMATTER =
      DateTimeFormatter.ofPattern(ORDER_TIME_FORMAT);

  private static final String COORDINATES_FORMAT = "^([N|S][0-9]+)([E|W][0-9]+)$";

  private static final String NEGATIVE_DIRECTIONS = "S|W";

  private static final String POSITIVE_DIRECTIONS = "N|E";

  private OrderParser() {}

  /**
   * Checks to see if the id matches the expected format.
   * 
   * @param id The id to validate.
   * @return The id if it matches the pattern or <code>null</code>.
   */
  public static final String toOrderId(String id) {
    if (id.length() != ORDER_ID_LENGTH || !id.matches(ORDER_ID_FORMAT)) {
      LOG.error("Order Id ({}) does not match format '{}'", id, ORDER_ID_FORMAT);
      return null;
    }
    return id;
  }

  /**
   * Checks to see if the order time matches the expected format.
   * 
   * @param orderTime The order time to validate.
   * @return The parsed LocalTime if it matches the pattern or <code>null</code>.
   */
  public static final LocalTime toOrderTime(String orderTime) {
    LocalTime time = null;
    try {
      time = LocalTime.parse(orderTime, ORDER_TIME_FORMATTER);
    } catch (DateTimeParseException exception) {
      LOG.error("Order Time ({}) does not match format '{}'", orderTime, ORDER_TIME_FORMAT);
    }
    return time;
  }

  /**
   * Formats the time into {@link OrderParser#ORDER_TIME_FORMAT}.
   * 
   * @param orderTime The order time.
   * @return The formatted time string or <code>null</code> if formatting fails.
   */
  public static final String formatOrderTime(LocalTime orderTime) {
    return orderTime.format(ORDER_TIME_FORMATTER);
  }

  /**
   * Checks to see if the coordinate matches the expected format.
   * 
   * @param direction The coordinate to validate.
   * @return The {@link GridCoordinate} or <code>null</code> if invalid.
   */
  public static final GridCoordinate toGridCoordinates(String direction) {
    GridCoordinate coordinate = null;
    Matcher matcher = Pattern.compile(COORDINATES_FORMAT).matcher(direction);
    if (matcher.matches()) {
      int x = toCoordinate(matcher.group(2));
      int y = toCoordinate(matcher.group(1));
      coordinate = GridCoordinate.of(x, y);
    }
    return coordinate;
  }

  private static final Integer toCoordinate(String direction) {
    direction = direction.replaceAll(POSITIVE_DIRECTIONS, "").replaceAll(NEGATIVE_DIRECTIONS, "-");
    return Integer.parseInt(direction);
  }

}
