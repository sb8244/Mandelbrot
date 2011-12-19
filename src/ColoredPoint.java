import java.awt.Color;
import java.awt.Point;

/**
 * Point that also contains a color
 * 
 * @author sb8244
 *
 */
public class ColoredPoint extends Point
{
	private Color color;
	
	/**
	 * Instantiate a ColoredPoint with a given location and color
	 * @param x The x coordinate
	 * @param y the y coordinate
	 * @param c The Color that will be painted
	 */
	public ColoredPoint(int x, int y, Color c)
	{
		color = c;
		this.setLocation(x, y);
	}
	
	/**
	 * 
	 * @return The color of this point
	 */
	public Color getColor()
	{
		return color;
	}
}
