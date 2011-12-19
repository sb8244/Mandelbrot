import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import javax.imageio.ImageIO;
import javax.swing.*;

import observer.Observable;

public class ToolbarPane extends JPanel implements ActionListener
{
	private JButton render, save, resize, reset, popout;
	private JLabel maxLabel, colorLabel, zoomLabel, dimensionLabel, expLabel;
	private JFormattedTextField maxField, zoomField, wField, hField;
	private JSlider colorSlider;
	private MandelbrotPane mPane;
	private JFrame parent;
	private Observable obs;
	private boolean docked;
	private JCheckBox expCheckBox;

	public boolean getDocked()
	{
		return docked;
	}
	
	public void setDocked(boolean d)
	{
		docked = d;
	}
	
	public ToolbarPane(int w, int h, MandelbrotPane mPane, JFrame parent, boolean docked)
	{
		this.docked = docked;
		this.setSize(w, h);
		this.setLayout(null);
		this.mPane = mPane;
		this.parent = parent;
		obs = (Observable)parent;

		render = new JButton("Set");
		render.addActionListener(this);

		save = new JButton("Save");
		save.addActionListener(this);

		reset = new JButton("Reset View");
		reset.addActionListener(this);

		popout = new JButton("^");
		popout.addActionListener(this);

		maxLabel = new JLabel("Max Iterations:");

		colorLabel = new JLabel("Color Modifier:");

		NumberFormat format = NumberFormat.getNumberInstance();
		format.setGroupingUsed(false);
		maxField = new JFormattedTextField(format);
		maxField.setValue(mPane.getMaxIterations());

		colorSlider = new JSlider(JSlider.HORIZONTAL, 1, 10, mPane.getColorModifier());
		colorSlider.setMajorTickSpacing(2);
		colorSlider.setMinorTickSpacing(1);
		colorSlider.setPaintTicks(true);
		colorSlider.setSnapToTicks(true);

		zoomLabel = new JLabel("Zoom Factor:");

		zoomField = new JFormattedTextField(format);
		zoomField.setValue(mPane.getZoomFactor());
		
		expLabel = new JLabel("Fast Exp:");
		expCheckBox = new JCheckBox();

		dimensionLabel = new JLabel("Width - Height");

		wField = new JFormattedTextField(format);
		wField.setValue(mPane.getImageWidth());

		hField = new JFormattedTextField(format);
		hField.setValue(mPane.getImageHeight());

		resize = new JButton("Resize");
		resize.addActionListener(this);

		initialize();

		this.add(popout);
		this.add(render);
		this.add(save);
		this.add(maxLabel);
		this.add(maxField);
		this.add(colorLabel);
		this.add(colorSlider);
		this.add(zoomLabel);
		this.add(zoomField);
		this.add(dimensionLabel);
		this.add(wField);
		this.add(hField);
		this.add(resize);
		this.add(reset);
		this.add(expLabel);
		this.add(expCheckBox);
	}


	public void actionPerformed(ActionEvent event) {
		if(event.getSource() == render)
		{
			mPane.setColorModifier(Math.abs(colorSlider.getValue()));
			mPane.setMaxIterations(Math.abs(Integer.parseInt(maxField.getText())));
			mPane.setZoomFactor(Math.abs(Integer.parseInt(zoomField.getText())));
			mPane.useFastExp(expCheckBox.isSelected());
			mPane.render();
		}
		else if(event.getSource() == save)
		{
			try {
				String name = "output";
				File f;
				for(int i = 0; ; i++)
				{
					f = new File(name + i +".png");
					if(!f.exists())
						break;
				}
				ImageIO.write((RenderedImage) mPane.getImage(), "png", f);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		else if(event.getSource() == resize)
		{
			mPane.changeSize(Math.abs(Integer.parseInt(wField.getText())), Math.abs(Integer.parseInt(hField.getText())));	
			if(docked)
			{
				parent.setSize(mPane.getImageWidth(), mPane.getImageHeight()+210);
				this.setLocation(0, mPane.getHeight()+5);
				initialize();
			}
			else
			{
				parent.setSize(mPane.getImageWidth(), mPane.getImageHeight()+parent.getInsets().top+parent.getInsets().bottom);
			}
		}
		else if(event.getSource() == reset)
		{
			mPane.reset();
		}
		else if(event.getSource() == popout)
		{
			obs.update();
		}
	}
	private void initialize()
	{			
		popout.setSize(60, 25);
		popout.setLocation(getWidth() - popout.getWidth() - 3, 5);

		maxLabel.setSize(125, 25);
		maxLabel.setLocation(0, 5);

		colorLabel.setSize(125, 25);
		colorLabel.setLocation(0, maxLabel.getY() + 35);

		maxField.setSize(125, 25);
		maxField.setLocation(maxLabel.getX() + maxLabel.getWidth(), maxLabel.getY());	

		colorSlider.setSize(125, 25);
		colorSlider.setLocation(colorLabel.getX() + colorLabel.getWidth(), colorLabel.getY());		

		zoomLabel.setSize(125, 25);
		zoomLabel.setLocation(0, colorLabel.getY() + 35);

		zoomField.setSize(125, 25);
		zoomField.setLocation(zoomLabel.getX()+zoomLabel.getWidth(), zoomLabel.getY());

		expLabel.setSize(75, 25);
		expLabel.setLocation(zoomField.getX() + zoomField.getWidth(), zoomField.getY());
		
		expCheckBox.setSize(25, 25);
		expCheckBox.setLocation(expLabel.getX() + expLabel.getWidth(), expLabel.getY());
		expCheckBox.setSelected(mPane.getExpMode());
		
		dimensionLabel.setSize(125, 25);
		dimensionLabel.setLocation(0, zoomLabel.getY() + 35);

		wField.setSize(80, 25);
		wField.setLocation(dimensionLabel.getX() + dimensionLabel.getWidth(), dimensionLabel.getY());

		hField.setSize(80, 25);
		hField.setLocation(wField.getX() + wField.getWidth(), wField.getY());

		resize.setSize(110, 25);
		resize.setLocation(hField.getX() + hField.getWidth(), hField.getY());

		render.setSize(125, 25);
		render.setLocation(0, dimensionLabel.getY() + 35);

		save.setSize(125, 25);
		save.setLocation(render.getX() + save.getWidth(), render.getY());

		reset.setSize(125, 25);
		reset.setLocation(save.getX()+reset.getWidth(), save.getY());
	}

}
