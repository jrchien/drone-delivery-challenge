package challenge.parser;

import java.time.LocalTime;
import org.junit.Assert;
import org.junit.Test;
import challenge.model.GridCoordinate;

/**
 * Tests order field parsing validation to avoid issues with invalid entries.
 * 
 * @author jeffrey
 */
public class OrderParserTest {

  /**
   * Multiple attempts to parse order IDs.
   */
  @Test
  public void testOrderIdValidation() {
    Assert.assertEquals("Assumes expected format is WM####", "WM0001",
        OrderParser.toOrderId("WM0001"));
    Assert.assertNull("WM10000 has too many numbers.", OrderParser.toOrderId("WM10000"));
    Assert.assertEquals("WM9999", OrderParser.toOrderId("WM9999"));
    Assert.assertNull("Test is invalid.", OrderParser.toOrderId("Test"));
    Assert.assertNull("WMWMWM is invalid.", OrderParser.toOrderId("WMWMWM"));
  }

  /**
   * Multiple attempts to parse/validate time formats.
   */
  @Test
  public void testOrderTimeValidation() {
    Assert.assertEquals("Times should match up.", LocalTime.of(5, 11, 50),
        OrderParser.toOrderTime("05:11:50"));
    Assert.assertEquals(LocalTime.of(15, 11, 50), OrderParser.toOrderTime("15:11:50"));
    Assert.assertNull("24 is outside of the 24-hour clock.", OrderParser.toOrderTime("24:11:50"));
    Assert.assertNull("Expects two digit hours.", OrderParser.toOrderTime("1:11:50"));
    Assert.assertEquals("15:11:50", OrderParser.formatOrderTime(LocalTime.of(15, 11, 50)));
  }

  /**
   * Multiple attempts to parse/validate grid coordinates.
   */
  @Test
  public void testCoordinateValidation() {
    Assert.assertEquals("South and west both parse as negative values.", GridCoordinate.of(-5, -11),
        OrderParser.toGridCoordinates("S11W5"));
    Assert.assertNull("Can only have one x direction and one y direction.",
        OrderParser.toGridCoordinates("N11E5W"));
    Assert.assertNull("Can only have one x direction and one y direction.",
        OrderParser.toGridCoordinates("NS11EW5"));
  }
}
