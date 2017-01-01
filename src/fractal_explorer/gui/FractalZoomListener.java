package fractal_explorer.gui;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import fractal_explorer.util.ComplexNumber;

public class FractalZoomListener extends Rectangle implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	private FractalPanel zoomPanel;
	private boolean hasDragged = false;
	private Color rectColour = Color.BLUE;
	private Point initialMouseDown;
	
	public FractalZoomListener(FractalPanel zoomPanel) {
		this.zoomPanel = zoomPanel;
		
	}
	
	@Override
	public void mousePressed(MouseEvent e) {
		initialMouseDown = new Point(e.getX(), e.getY());
		//so we don't zoom to 1 pixel when creating a julia set
		hasDragged = false;

	}

	@Override
	public void mouseReleased(MouseEvent e){

		if(hasDragged){ 
			//cancel the zoom if the rect goes out of the JPanel
			if(zoomPanel.contains(e.getPoint())){

				ComplexNumber max = zoomPanel.getComplexNumberFromPoint(new Point((int)getMaxX(),(int)getMaxY()));
				ComplexNumber min = zoomPanel.getComplexNumberFromPoint(new Point((int)getX(),(int)getY()));

				zoomPanel.fs.setXMax(max.getReal());
				zoomPanel.fs.setXMin(min.getReal());
				zoomPanel.fs.setYMax(max.getImaginary());
				zoomPanel.fs.setYMin(min.getImaginary());
				
				zoomPanel.shouldUpdate();
			}
			
			setRect(0, 0, 0, 0);
			zoomPanel.repaint();
		}

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		width  =  Math.abs(e.getX() - (int) initialMouseDown.getX());
		height =  Math.abs(e.getY() - (int) initialMouseDown.getY());


		//set to smallest to keep zoom a square
		int size = Math.min(height,width); 

		width=size;
		height=size;

		x = (int) ((e.getX() < initialMouseDown.getX())?initialMouseDown.getX()-width :initialMouseDown.getX());
		y = (int) ((e.getY() < initialMouseDown.getY())?initialMouseDown.getY()-height:initialMouseDown.getY());

		hasDragged = true;

		zoomPanel.repaint();

	}
	
	
	
	@Override
	public void mouseMoved(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}

	@Override
	public void mouseClicked(MouseEvent e) {
	}
	
	public Color getColour(){
		return rectColour;
	}

}
