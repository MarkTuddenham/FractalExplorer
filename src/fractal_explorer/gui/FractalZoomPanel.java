package fractal_explorer.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JTextField;

import fractal_explorer.util.BurningShipSet;
import fractal_explorer.util.FractalSet;
import fractal_explorer.util.FractalSettings;
import fractal_explorer.util.LambdaSet;
import fractal_explorer.util.MandelbrotSet;

public class FractalZoomPanel extends FractalPanel implements ActionListener {
	private static final long serialVersionUID = 1L;

	private FractalZoomListener zoomPanel;

	public FractalZoomPanel(FractalSet fs, Dimension size){
		super(fs, size);

		//add zooming listener
		zoomPanel = new FractalZoomListener(this);
		
		this.addMouseListener(zoomPanel);
		this.addMouseMotionListener(zoomPanel);
	}
	
	public FractalZoomPanel(FractalSet fs){
		this(fs, new Dimension(500,500));
	}
	

	@SuppressWarnings("rawtypes")
	@Override
	public void actionPerformed(ActionEvent e) {
		Object eventSource = e.getSource();
		//when user changes a coord value and hits enter
		if (eventSource instanceof JTextField) {
			ControlPanel cp = (ControlPanel) ((JTextField)eventSource).getParent();
			updateFractalSet(cp);
		//when user hits update or reset
		}else if(eventSource instanceof JButton){
			JButton button = (JButton)eventSource;
			ControlPanel cp = (ControlPanel) button.getParent();
			if( button.getText().equals("Reset")){
				this.reset();
				cp.setMaxX(fs.getXMax());
				cp.setMinX(fs.getXMin());
				cp.setMaxY(fs.getYMax());
				cp.setMinY(fs.getYMin());
				cp.setConvergenceTest(fs.getConvergenceTest());
				shouldUpdate();
				repaint();
			}else if (button.getText().equals("Update")){
				updateFractalSet(cp);
			}
		//when user changes the fractal type i.e mandlebrot to burning ship
		}else if (eventSource instanceof JComboBox){
			ControlPanel cp = (ControlPanel) ((JComboBox)eventSource).getParent();
			String setType = (String)((JComboBox)eventSource).getSelectedItem();
			switch(setType){
			case "Mandelbrot":
				fs = new MandelbrotSet();
				break;
			case "Burning Ship":
				fs = new BurningShipSet();
				break;
			case "Lambda":
				fs = new LambdaSet();
				break;
			default:
				System.err.println("Unknown fractal type: "+setType);
				break;
			}
			reset();
			cp.updateFields(this);
			FractalSettings.getFractalSettings().updateJuliaDisplay(this);
			shouldUpdate();
			repaint();
		}
	}
	private void updateFractalSet(ControlPanel cp){
		fs.setXMin(cp.getMinX());
		fs.setXMax(cp.getMaxX());
		fs.setYMin(cp.getMinY());
		fs.setYMax(cp.getMaxY());
		fs.setConvergenceTest(cp.getConvergenceTest());
		shouldUpdate();
		repaint();
	}

	@Override
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;
		
		Color rectColour = zoomPanel.getColour();
		g2d.setColor(rectColour);
		g2d.draw(zoomPanel);
		g2d.setColor(new Color(rectColour.getRed(), rectColour.getGreen(), rectColour.getBlue(), 80));
		g2d.fill(zoomPanel);
	}



}