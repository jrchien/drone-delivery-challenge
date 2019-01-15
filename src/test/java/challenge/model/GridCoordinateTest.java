package challenge.model;

import org.junit.Assert;
import org.junit.Test;

/**
 * Basic {@link GridCoordinate} tests.
 * 
 * @author jeffrey
 */
public class GridCoordinateTest {

  /**
   * Tests basic instantiation and getters.
   */
  @Test
  public void testBasicInstantiation() {
    GridCoordinate coordinate = GridCoordinate.of(5, 4);
    Assert.assertNotNull("Expects instantiated GridCoordinate not to be null.", coordinate);
    Assert.assertEquals("Expects x coordinate to be value declared.", 5, coordinate.getX());
    Assert.assertEquals("Expects y coordinate to be value declared.", 4, coordinate.getY());
  }

  /**
   * Tests the {@link GridCoordinate#getDistanceTo(GridCoordinate)} method.
   */
  @Test
  public void testDistance() {
    GridCoordinate coordinate = GridCoordinate.of(5, 4);
    GridCoordinate other = GridCoordinate.of(3, -5);
    Assert.assertEquals(
        "Expects distance to be absolute difference between x and y added together.", 9,
        coordinate.getDistanceTo(GridCoordinate.ZERO));
    Assert.assertEquals(
        "Expects distance to be absolute difference between x and y added together.", 11,
        coordinate.getDistanceTo(other));
    Assert.assertEquals("Expects distance to self to be 0.", 0,
        coordinate.getDistanceTo(coordinate));
  }

  /**
   * Tests the equals method.
   */
  @SuppressWarnings("unlikely-arg-type")
  @Test
  public void testEquals() {
    GridCoordinate coordinate = GridCoordinate.of(5, 4);
    GridCoordinate same = GridCoordinate.of(5, 4);
    GridCoordinate other = GridCoordinate.of(5, 7);

    Assert.assertTrue("Two objects with the same addresses should be equal.",
        coordinate.equals(coordinate));
    Assert.assertFalse("Null check.", coordinate.equals(null));
    Assert.assertFalse("Type check.", coordinate.equals(""));
    Assert.assertFalse("Same x, different y.", coordinate.equals(other));
    Assert.assertFalse("Two different GridCoordinates should not be equal.",
        coordinate.equals(GridCoordinate.ZERO));
    Assert.assertTrue("Same values.", coordinate.equals(same));
  }

  /**
   * Tests the toString method. Subject to change.
   */
  @Test
  public void testToString() {
    GridCoordinate coordinate = GridCoordinate.ZERO;

    Assert.assertEquals("Compare toString.", "GridCoordinate [x=0, y=0]", coordinate.toString());
  }

}
