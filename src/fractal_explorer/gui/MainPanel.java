
package fractal_explorer.gui;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JPanel;

import fractal_explorer.util.FractalSettings;
import fractal_explorer.util.MandelbrotSet;

public class MainPanel extends JPanel {
	private static final long serialVersionUID = 1L;
	
	private PointDisplayPanel pointDisplayPanel;
	private FractalZoomPanel mainFractalPanel;

	public MainPanel() {
		setPreferredSize(new Dimension(1100, 600));
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		//using observer pattern
		pointDisplayPanel = new PointDisplayPanel();
		mainFractalPanel = new FractalZoomPanel(new MandelbrotSet());
		FractalSettings.getFractalSettings().setMainPanel(mainFractalPanel);
		ControlPanel controlPanel = new ControlPanel();
		FractalSettings.getFractalSettings().setControlPanel(controlPanel);
		FractalSettings.getFractalSettings().setPointDisplayPanel(pointDisplayPanel);
		
		controlPanel.addActionListener(mainFractalPanel);
		mainFractalPanel.addMouseListener(controlPanel);
		mainFractalPanel.addMouseListener(pointDisplayPanel);
		mainFractalPanel.addMouseMotionListener(pointDisplayPanel);
		
		//add a wrapper container to the Mandelbrot Panel so that it stays square (along with overriding the getPreferredSize method)
		JPanel mbParentPanel = new JPanel();
		mbParentPanel.setLayout(new GridBagLayout());
		mbParentPanel.setPreferredSize(new Dimension(500, 500));
		mbParentPanel.add(mainFractalPanel);

		c.fill = GridBagConstraints.BOTH;
		c.weightx = 1.0;
		c.weighty = 1.0;
		c.gridx = 0;
		c.gridy = 0;
		c.gridheight=2;
		add(mbParentPanel, c);
		
		c.weighty=0.5;
		c.weightx = 0.5;
		c.gridheight=1;
		c.gridx = 1;
		c.gridy = 0;
		add(controlPanel, c);
		
		c.weighty=1;
		c.gridx = 1;
		c.gridy = 1;
		add(pointDisplayPanel, c);
				
	}
	


}
