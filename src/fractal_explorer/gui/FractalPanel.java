package fractal_explorer.gui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;

import fractal_explorer.util.ComplexNumber;
import fractal_explorer.util.FractalSet;
import fractal_explorer.util.FractalSettings;
import fractal_explorer.util.JuliaSet;

public class FractalPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	protected FractalSet fs;
	private volatile BufferedImage image;
	private BufferedImage oldImage;
	private boolean newImageReady = true;
	private boolean shouldUpdate = true;
	private volatile List<int[]> nodesLeft;
	private FractalCalculatorWorker[] fractalWorkers;
	private JProgressBar progBar = new JProgressBar();
	private boolean isVerticallyFlipped;

	public FractalPanel(FractalSet fs, Dimension preferedSize) {
		this.fs = fs;
		setPreferredSize(preferedSize);
		
		// create listener to see if the panel has been resized so we can update the image
		addComponentListener(new ComponentListener() {
			
			@Override
			public void componentResized(ComponentEvent e) {
				shouldUpdate();	
			}
			@Override
			public void componentShown(ComponentEvent e) {}
			@Override
			public void componentMoved(ComponentEvent e) {}
			@Override
			public void componentHidden(ComponentEvent e) {}
		});
		
		add(progBar);
		progBar.setStringPainted(true);
		progBar.setVisible(false);
		
	}
	
	public FractalPanel(FractalSet fs){
		this(fs, new Dimension(500,500));
	}
	
	public  boolean hasJulia(){
		return fs.hasJulia();
	}
	
	public void flipVertically() {
		fs.setYMax(-fs.getYMax());
		fs.setYMin(-fs.getYMin());
		isVerticallyFlipped = !isVerticallyFlipped;
		shouldUpdate();
		repaint();
	}
	
	
	public void reset(){
		fs.setXMax(FractalSet.getDefaultxmax());
		fs.setXMin(FractalSet.getDefaultxmin());
		fs.setYMax(isVerticallyFlipped?-FractalSet.getDefaultymax():FractalSet.getDefaultymax());
		fs.setYMin(isVerticallyFlipped?-FractalSet.getDefaultymin():FractalSet.getDefaultymin());
		fs.setConvergenceTest(FractalSet.getDefaultconvergencetest());
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g;

		if(shouldUpdate) {
			updateGraphWithThreads();
		}


		if(newImageReady){
			g2d.drawImage(image, 0, 0, null);
			//hide progress bar when finished with
			progBar.setVisible(false);
		}else{
			g2d.drawImage(oldImage, 0, 0, null);
			//prevent progress bar from displaying on auto update
			if(this.fs instanceof JuliaSet && FractalSettings.getFractalSettings().doAutoUpdateJuliaSet()) return;
			if(image!=null){
				progBar.setMaximum(image.getWidth()*image.getHeight());
				progBar.setVisible(true);
				progBar.setBounds(0, getHeight()-20, getWidth(), 20);
				
				//syncronised as threads will still be updating
				synchronized (nodesLeft){
					progBar.setValue(progBar.getMaximum() - nodesLeft.size());

				}
			}
		}

	}

	private void switchImage() {

		if(newImageReady){
			if(image != null){
				//clone the old image so that its not a reference to the new image
				ColorModel cm = image.getColorModel();
				boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
				WritableRaster raster = image.copyData(null);
				oldImage =  new BufferedImage(cm, raster, isAlphaPremultiplied, null);
			}
		}
		newImageReady = !newImageReady;
	}

	public void shouldUpdate(){
		shouldUpdate = true;
	}

	public ComplexNumber getComplexNumberFromPoint(Point p) {
		double xMin = fs.getXMin();
		double xMax = fs.getXMax();
		double yMin = fs.getYMin();
		double yMax = fs.getYMax();

		//-1 as coords start form 0 (e.g. ig width = 800 then coords are 0-799 so it gets Max by: Min + ((799/799) * width))
		double re = xMin + ((p.getX()/(getWidth()-1))*(xMax-xMin));
		double im = yMin + ((p.getY()/(getHeight()-1))*(yMax-yMin));
		return new ComplexNumber(re, im);
	}

	// makes sure that this stays square (along with a wrapper Panel) to prevent
	// the fractal from warping
	@Override
	public Dimension getPreferredSize() {
		Dimension d;
		Container c = getParent();
		if (c != null) {
			d = c.getSize();
		} else {
			return new Dimension(50, 50);
		}

		int s = (int) Math.min(d.getWidth(), d.getHeight());
		return new Dimension(s, s);
	}

	private void updateGraphWithThreads(){
		shouldUpdate = false;

		//if still calculating the wait but set shouldUpdate
		if(fractalWorkers != null){
			for(int i=0;i<fractalWorkers.length;i++){
				if(fractalWorkers[i]!= null){
					shouldUpdate();
					return;
				}
			}
		}
		
		switchImage();
		// -1 for EDT but if only one core machine then create 1 thread (hence Math.max)
		fractalWorkers = new FractalCalculatorWorker[Math.max(1,Runtime.getRuntime().availableProcessors() - 1)]; 
		//only create the new buffered image if its not doing the julia auto update feature
		//create anyway if the panel is null
		if(image == null || image.getHeight() != getHeight()){
			image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		}
		
		// add all the pixels into the buffered image to the array
		nodesLeft = new ArrayList<int[]>();
		
		for (int x = 0; x < getWidth(); x++) {
			for (int y = 0; y < getHeight(); y++) {
				nodesLeft.add(new int[]{x,y});
			}
		}
		
		//create & execute workers
		for(int i=0;i<fractalWorkers.length;i++){
			fractalWorkers[i] = new FractalCalculatorWorker(i);
			fractalWorkers[i].execute();
		}
	}

	private void setGraphColour(int x, int y, int colour){
			synchronized (image) {
				if(!(x>image.getWidth() || y>image.getHeight())){
					image.setRGB(x, y, colour);
				}
			}
	}
	
	private int[] getGraphNodeToColour(){
		synchronized (nodesLeft) {
			return nodesLeft.isEmpty()?null:nodesLeft.remove(nodesLeft.size()-1);
		}

	}
	
	private void fractalWorkerFinished(int number){
		fractalWorkers[number] = null;
		
		boolean shouldRepaint = true;
		for (int c=0;c<fractalWorkers.length; c++){
			if (fractalWorkers[c]!= null) {
				shouldRepaint = false;
			}
		}
		if(shouldRepaint){
			switchImage();
			repaint();
		}
	}
	
	//extend SwingWorker so we can call repaint from the done method flow
	private class FractalCalculatorWorker extends SwingWorker<Integer, Integer>{
		
		private int number;


		public FractalCalculatorWorker(int number) {
			this.number = number;
		}

		@Override
		protected Integer doInBackground() throws Exception {
			int[] node;
			ComplexNumber i;
			Point p = new Point();
			int count = 0;
			//assignment in while to keep it atomic
			while(!isCancelled() && (node = getGraphNodeToColour())!=null){
				p.x = node[0];
				p.y = node[1];
				i = getComplexNumberFromPoint(p);
				
				setGraphColour(node[0], node[1], fs.getColour(i));
				count++;
				
				//update the display every 500 pixels so that progress bar is updated
				if(count == 500){
					
					SwingUtilities.invokeLater(new Runnable() {
						@Override
						public void run() {
							FractalPanel.this.repaint();
						}
					});
					
					count = 0;
				}
			}
			return null;
		}
		
					
		@Override
		protected void done(){
			fractalWorkerFinished(number);
		}
	}
	
	//for saving image to png
	public BufferedImage getImage() {
		return newImageReady?image:oldImage;
		
	}

}
