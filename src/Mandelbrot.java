import java.awt.Color;
import java.awt.Point;
import java.util.ArrayList;
import JSci.maths.*;

public class Mandelbrot
{
	public int ImageHeight = 400,ImageWidth = 400;

	private double MINRE = -2.0;
	private double MAXRE = 1.0;
	private double MINIM = -1.2;
	private double MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
	protected double RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
	protected double IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
	private int zoom;
	private int colorModifier;
	private boolean useFastExp = true;

	public Mandelbrot()
	{
		colorModifier = 10;
		zoom = 2;
	}
	
	public void useFastExp(boolean b)
	{
		useFastExp = b;
	}
	
	public boolean getExpMode()
	{
		return useFastExp;
	}

	public void reset()
	{
		MINRE = -2.0;
		MAXRE = 1.0;
		MINIM = -1.2;
		refactor();
	}

	public void changeSize(int w, int h)
	{
		ImageHeight = h;
		ImageWidth = w;
		refactor();
	}
	public void setZoomFactor(int s)
	{
		zoom = s;
	}

	public int getZoomFactor()
	{
		return zoom;
	}

	public void setColorModifier(int s)
	{
		colorModifier = s;
	}

	public int getColorModifier()
	{
		return colorModifier;
	}

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

	private void refactor()
	{
		RE_FACTOR = (MAXRE-MINRE)/(ImageWidth-1);
		MAXIM = MINIM+(MAXRE-MINRE)*ImageHeight/ImageWidth;
		IM_FACTOR = (MAXIM-MINIM)/(ImageHeight - 1);
	}

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
	
	private double exp(double val) {
		if(useFastExp)
		{
			final long tmp = (long) (1512775 * val + 1072632447);
			return Double.longBitsToDouble(tmp << 32);
		}
		return Math.exp(val);
	}
}
