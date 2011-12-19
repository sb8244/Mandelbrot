import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.*;


public class MandelbrotPane extends JPanel implements MouseListener
{
	private ArrayList<ColoredPoint> set;
	private Mandelbrot mb = new Mandelbrot();
	private int MAX = 20;
	private Image image;

	public MandelbrotPane()
	{
		set = calculate();
		this.addMouseListener(this);
	}
	
	public boolean getExpMode()
	{
		return mb.getExpMode();
	}
	
	public void useFastExp(boolean b)
	{
		mb.useFastExp(b);
	}

	public int getImageWidth()
	{
		return mb.ImageWidth;
	}
	
	public int getImageHeight()
	{
		return mb.ImageHeight;
	}
	
	public void reset()
	{
		mb.reset();
		render();
	}
	
	public void changeSize(int w, int h)
	{
		mb.changeSize(w, h);
		this.setSize(w, h);
		render();
	}
	
	public void setZoomFactor(int s)
	{
		mb.setZoomFactor(s);
	}

	public int getZoomFactor()
	{
		return mb.getZoomFactor();
	}
	
	public void paint(Graphics g)
	{
		image = createImage();
		g.drawImage(image, 0, 0, null);
		g.drawLine(0, getHeight()-1, getWidth(), getHeight()-1);
	}

	private ArrayList<ColoredPoint> calculate()
	{
		long start = System.currentTimeMillis();
		ArrayList<ColoredPoint> ret = mb.calculate(MAX);
		System.out.println("Total iterations: " + (MAX*mb.ImageHeight*mb.ImageWidth) + " in " + (System.currentTimeMillis() - start) + "ms");
		return ret;
	}

	public int getMaxIterations()
	{
		return MAX;
	}

	public void setMaxIterations(int max)
	{
		MAX = max;
	}

	public void setColorModifier(int s)
	{
		mb.setColorModifier(s);
	}

	public int getColorModifier()
	{
		return mb.getColorModifier();
	}

	public void render()
	{
		set = calculate();
		this.repaint();
	}
	public Image getImage()
	{
		return image;
	}

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

	public void mouseClicked(MouseEvent click) {
		if(click.getButton() == MouseEvent.BUTTON1)
		{
			mb.changeView(click.getPoint(), true);
		}
		else if(click.getButton() == MouseEvent.BUTTON3)
		{
			mb.changeView(click.getPoint(), false);
		}
		//MAX = MAX * 2;
		set = calculate();
		this.repaint();
	}
	public void mouseEntered(MouseEvent arg0) {
	}
	public void mouseExited(MouseEvent arg0) {
	}
	public void mousePressed(MouseEvent arg0) {
	}
	public void mouseReleased(MouseEvent arg0) {
	}
}
