package challenge.model;

import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 * Stores the grid x and y coordinates for a location.
 * 
 * @author jeffrey
 */
public final class GridCoordinate implements Comparable<GridCoordinate> {

  /**
   * The 0, 0 coordinate of the grid.
   */
  public static final GridCoordinate ZERO = GridCoordinate.of(0, 0);

  private final int x;

  private final int y;

  /**
   * The constructor.
   * 
   * @param x The x coordinate.
   * @param y The y coordinate.
   */
  private GridCoordinate(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * @return The x coordinate.
   */
  public int getX() {
    return x;
  }

  /**
   * @return The y coordinate.
   */
  public int getY() {
    return y;
  }

  /**
   * Calculates the distance from this coordinate to the other coordinate.
   * 
   * @param other The other {@link GridCoordinate}.
   * @return The distance.
   */
  public int getDistanceTo(GridCoordinate other) {
    return Math.abs(other.getX() - getX()) + Math.abs(other.getY() - getY());
  }

  /**
   * Obtains an instance of the {@link GridCoordinate} from two coordinates.
   * 
   * @param x The x coordinate.
   * @param y The y coordinate.
   * @return The {@link GridCoordinate}. Cannot be <code>null</code>.
   */
  public static final GridCoordinate of(int x, int y) {
    return new GridCoordinate(x, y);
  }

  /**
   * Comparison order: x, y.
   */
  @Override
  public int compareTo(GridCoordinate other) {
    return new CompareToBuilder().append(getX(), other.getX()).append(getY(), other.getY())
        .toComparison();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    GridCoordinate other = (GridCoordinate) obj;
    if (x != other.x) {
      return false;
    }
    if (y != other.y) {
      return false;
    }
    return true;
  }

  @Override
  public String toString() {
    return "GridCoordinate [x=" + x + ", y=" + y + "]";
  }

}
