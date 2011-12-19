import java.awt.Color;
import java.awt.Point;
public class ColoredPoint extends Point
{
	private Color color;
	
	public ColoredPoint(int x, int y, Color c)
	{
		color = c;
		this.setLocation(x, y);
	}
	
	public Color getColor()
	{
		return color;
	}
}
