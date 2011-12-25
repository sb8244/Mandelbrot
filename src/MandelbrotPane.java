import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;

/**
 * This Pane contains and renders the Mandelbrot set and makes modifications to it
 * @author sb8244
 *
 */
public class MandelbrotPane extends JPanel implements MouseListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -7246875999457604140L;
	private ArrayList<ColoredPoint> set;
	private Mandelbrot mb = new Mandelbrot();
	private int MAX = 20;
	private Image image;

	/**
	 * Initialize this pane
	 */
	public MandelbrotPane()
	{
		set = calculate();
		this.addMouseListener(this);
	}
	
	/**
	 * 
	 * @return What mode of Math.exp is used (true is fast approximate exp, false is slow exact exp)
	 */
	public boolean getExpMode()
	{
		return mb.getExpMode();
	}
	
	/**
	 * 
	 * @param b If true, will use a fast version of Math.exp
	 */
	public void useFastExp(boolean b)
	{
		mb.useFastExp(b);
	}

	/**
	 * 
	 * @return The width of the image in pixels
	 */
	public int getImageWidth()
	{
		return mb.ImageWidth;
	}
	
	/**
	 * 
	 * @return The height of the image in pixels
	 */
	public int getImageHeight()
	{
		return mb.ImageHeight;
	}
	
	/**
	 * Reset the viewport to default and re-render
	 */
	public void reset()
	{
		mb.reset();
		render();
	}
	
	/**
	 * Change the pane's size
	 * @param w The new width
	 * @param h The new height
	 */
	public void changeSize(int w, int h)
	{
		mb.changeSize(w, h);
		this.setSize(w, h);
		render();
	}
	
	/**
	 * Sets the zoom factor
	 * @param s The new zoom factor
	 */
	public void setZoomFactor(int s)
	{
		mb.setZoomFactor(s);
	}

	/**
	 * 
	 * @return The zoom factor the set will use
	 */
	public int getZoomFactor()
	{
		return mb.getZoomFactor();
	}
	
	/**
	 * Creates the image and paints it to the pane
	 * Draws a 1px black border on the bottom of the pane
	 */
	public void paint(Graphics g)
	{
		image = createImage();
		g.drawImage(image, 0, 0, null);
		g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
	}

	/**
	 * 
	 * @return The max iterations used in generating the set
	 */
	public int getMaxIterations()
	{
		return MAX;
	}

	/**
	 * Set the max iterations that will be used in generating the set
	 * @param max
	 */
	public void setMaxIterations(int max)
	{
		MAX = max;
	}

	/**
	 * Set the color scheme
	 * 
	 * @param s s%10 will have an effect on the color (effectively 0-9)
	 */
	public void setColorModifier(int s)
	{
		mb.setColorModifier(s);
	}

	/**
	 * 
	 * @return The integer used to represent the color scheme
	 */
	public int getColorModifier()
	{
		return mb.getColorModifier();
	}

	/**
	 * Calculate the set and repaint it
	 */
	public void render()
	{
		set = calculate();
		this.repaint();
	}
	
	/**
	 * 
	 * @return The image that is painted to the pane
	 */
	public Image getImage()
	{
		return image;
	}

	/**
	 * Creates a bufferedImage and paints the set to it
	 * @return An Image containing the set
	 */
	private Image createImage()
	{
		BufferedImage theImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = (Graphics2D)theImage.createGraphics();
		for(ColoredPoint p: set)
		{
			g2d.setColor(p.getColor());
			g2d.drawRect(p.x, p.y, 1, 1);
		}
		return theImage;
	}

	/**
	 * Zoom in when the left mouse is clicked
	 * Zoom out when the right mouse is clicked
	 */
	public void mouseClicked(MouseEvent click) {
		if(click.getButton() == MouseEvent.BUTTON1)
		{
			mb.changeView(click.getPoint(), true);
		}
		else if(click.getButton() == MouseEvent.BUTTON3)
		{
			mb.changeView(click.getPoint(), false);
		}
		else
		{
			return;
		}
		set = calculate();
		this.repaint();
	}
	
	/**
	 * 
	 * @return An ArrayList containing all of the ColoredPoints to be rendered
	 */
	private ArrayList<ColoredPoint> calculate()
	{
		//long start = System.currentTimeMillis();
		ArrayList<ColoredPoint> ret = mb.calculate(MAX);
		//System.out.println("Total iterations: " + (MAX*mb.ImageHeight*mb.ImageWidth) + " in " + (System.currentTimeMillis() - start) + "ms");
		return ret;
	}

	public void mouseEntered(MouseEvent arg0) {}
	public void mouseExited(MouseEvent arg0) {}
	public void mousePressed(MouseEvent arg0) {}
	public void mouseReleased(MouseEvent arg0) {}
}
