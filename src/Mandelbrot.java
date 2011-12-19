import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;

/**
 * Calculates the Mandelbrot set
 * @author sb8244
 *
 */
public class Mandelbrot
{
	public int ImageHeight = 400,ImageWidth = 400;

	//The Minimum and Maximum real value
	private double MINRE = -2.0;
	private double MAXRE = 1.0;
	//The minimum and maximum imaginary values
	private double MINIM = -1.2;
	private double MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
	//the distance between adjacent x and y coordinates
	protected double RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
	protected double IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
	private int zoom;
	private int colorModifier;
	private boolean useFastExp = true;

	/**
	 * Set default values
	 */
	public Mandelbrot()
	{
		colorModifier = 10;
		zoom = 2;
	}
	
	/**
	 * Sets whether to use fast (approximate) exp or not
	 * @param b
	 */
	public void useFastExp(boolean b)
	{
		useFastExp = b;
	}
	
	/**
	 * 
	 * @return true if fast exp should be used
	 */
	public boolean getExpMode()
	{
		return useFastExp;
	}

	/**
	 * Reset default values
	 */
	public void reset()
	{
		MINRE = -2.0;
		MAXRE = 1.0;
		MINIM = -1.2;
		refactor();
	}

	/**
	 * Set the width and height of the set
	 * @param w The new width
	 * @param h The new Height
	 */
	public void changeSize(int w, int h)
	{
		ImageHeight = h;
		ImageWidth = w;
		refactor();
	}
	
	/**
	 *
	 * @param s The new zoom factor
	 */
	public void setZoomFactor(int s)
	{
		zoom = s;
	}

	/**
	 * 
	 * @return The zoom factor
	 */
	public int getZoomFactor()
	{
		return zoom;
	}

	/**
	 * Sets the color modifier. 
	 * @param s s % 10 will have an effect (effectively 0-9)
	 */
	public void setColorModifier(int s)
	{
		colorModifier = s;
	}

	/**
	 * 
	 * @return The color scheme modifier
	 */
	public int getColorModifier()
	{
		return colorModifier;
	}

	/**
	 * Given a click location, redimension the image so that the click is at the center and zoom (in/out) takes effect
	 * @param clickPoint The location that was clicked
	 * @param zoomIn If true, zoom in
	 */
	public void changeView(Point clickPoint, boolean zoomIn)
	{
		double c_im = MAXIM - clickPoint.y * IM_FACTOR;
		double c_re = MINRE + clickPoint.x * RE_FACTOR;	
		double rediff, imdiff;
		if(zoomIn)
		{
			rediff = (MAXRE - MINRE)/zoom;
			imdiff = (MAXIM - MINIM)/zoom;
		}
		else
		{
			rediff = (MAXRE - MINRE)*zoom;
			imdiff = (MAXIM - MINIM)*zoom;
		}
		MINRE= c_re - (rediff / 2);
		MAXRE = c_re + (rediff / 2);
		MINIM = c_im - imdiff/2;
		refactor();
	}

	/**
	 * Sets the factors and MAXIM values
	 */
	private void refactor()
	{
		RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
		MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
		IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
	}

	/**
	 * Trivial algorithm to calculate the set
	 * @param MaxIterations The maximum iterations before determining a point is in the set
	 * @return An ArrayList of ColoredPoints that is the set
	 */
	public ArrayList<ColoredPoint> calculate(int MaxIterations)
	{
		ArrayList<ColoredPoint> ret = new ArrayList<ColoredPoint>();
		for(int y = 0; y < ImageHeight; y++)
		{
			double c_im = MAXIM - y * IM_FACTOR;
			for(int x = 0; x < ImageWidth; x++)
			{
				double c_re = MINRE + x * RE_FACTOR;
				double Z_re = c_re, Z_im = c_im;
				int n;
				boolean partSet = true;
				double smoothcolor = -exp(-(Z_re*Z_re+Z_im*Z_im));
				for(n = 0; n < MaxIterations; n++)
				{
					double Z_re2 = Z_re*Z_re, Z_im2 = Z_im*Z_im;
					if(Z_re2 + Z_im2 > 4)
					{
						partSet = false;
						break;
					}
					Z_im = 2*Z_re*Z_im + c_im;
					Z_re = Z_re2 - Z_im2 + c_re;
					Z_re2 = Z_re*Z_re; Z_im2 = Z_im*Z_im;
					smoothcolor += exp(-(Z_re2+Z_im2));
				}
				Color color = Color.getHSBColor((float)(.9f * (colorModifier + smoothcolor/MaxIterations)),1.0f,1.0f);
				if(partSet) color = Color.black;
				ret.add(new ColoredPoint(x, y,	color));
			}
		}
		return ret;
	}
	
	/**
	 * Uses either fast or slow exp on a value
	 * @param val
	 * @return 
	 */
	private double exp(double val) {
		if(useFastExp)
		{
			final long tmp = (long) (1512775 * val + 1072632447);
			return Double.longBitsToDouble(tmp << 32);
		}
		return Math.exp(val);
	}
}
