import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;

import observer.Observable;
 
public class Runner extends JFrame implements Observable
{
	public static final int ImageWidth = 400, ImageHeight = 400;
	private MandelbrotPane mPane;
	protected ToolbarPane tPane;
	private ToolboxDialog tbd;
	
	public Runner() throws IOException
	{
		super("Mandelbrot");

		this.setLayout(null);
		this.setSize(ImageWidth, ImageHeight+210);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		mPane = new MandelbrotPane();
		mPane.setSize(ImageWidth, ImageHeight);
		mPane.setLocation(0, 0);
		
		tPane = new ToolbarPane(ImageWidth, 220, mPane, this, true);
		tPane.setLocation(0, mPane.getHeight()+5);
		
		tbd=  new ToolboxDialog(this, false);
		tbd.setSize(tPane.getWidth(), tPane.getHeight());
		
		this.add(tPane);
		this.add(mPane);
		this.repaint();
		
	}
	
	public static void main(String[] args) throws IOException
	{
		Runner win = new Runner();
		win.setResizable(true);
		win.setVisible(true);
	}

	public void update()
	{
		boolean docked = tPane.getDocked();
		if(docked)
		{
			tPane.setLocation(0, 0);
			tPane.setDocked(false);
			tbd.add(tPane);
			this.remove(tPane);
			this.setSize(mPane.getImageWidth(), mPane.getImageHeight()+this.getInsets().top+this.getInsets().bottom);
			this.setPreferredSize(this.getSize());
			tbd.setVisible(true);
		}
		else
		{
			tPane.setLocation(0, mPane.getHeight()+5);
			tPane.setDocked(true);
			this.add(tPane);
			this.setSize(mPane.getImageWidth(), mPane.getImageHeight()+210);
			this.setPreferredSize(this.getSize());
			tbd.setVisible(false);
		}
	}	
	
	class ToolboxDialog extends JDialog implements WindowListener
	{
		Observable obs;
		public ToolboxDialog(JFrame owner, boolean modal)
		{
			super(owner, modal);
			obs = (Observable)owner;
			this.setLayout(null);
			this.repaint();
			this.addWindowListener(this);
			this.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		}
		
		public void windowActivated(WindowEvent arg0) {
		}

		public void windowClosed(WindowEvent arg0) {
		
		}

		@Override
		public void windowClosing(WindowEvent arg0) {
			obs.update();
		}

		@Override
		public void windowDeactivated(WindowEvent arg0) {
		}

		@Override
		public void windowDeiconified(WindowEvent arg0) {
		}

		@Override
		public void windowIconified(WindowEvent arg0) {
		}

		@Override
		public void windowOpened(WindowEvent arg0) {
		}
	}
}


