package fractal_explorer.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import fractal_explorer.gui.ControlPanel;
import fractal_explorer.gui.FractalPanel;
import fractal_explorer.gui.PointDisplayPanel;

//Singleton class
public class FractalSettings {
	private static FractalSettings settings = null;
	
	private FractalSettings(){}
	
	public FractalSettings clone(){
		throw new UnsupportedOperationException("Cannot clone singleton - FractalSettings");
	}
	
	public static synchronized FractalSettings getFractalSettings(){
		if(settings == null){
			settings = new FractalSettings();
		}
		return settings;
	}
	
	
	
	private boolean autoUpdateJuliaSet = false;
	private FractalPanel mainPanel = null;
	private ControlPanel controlPanel = null;
	private PointDisplayPanel pointDisplayPanel = null;
	private JComboBox<ComplexNumber> juliaPoints = null;
	
	public synchronized boolean doAutoUpdateJuliaSet(){
		return autoUpdateJuliaSet;	
	}
	
	public synchronized void setAutoUpdateJuliaSet(boolean b){
		this.autoUpdateJuliaSet = b;
	}

	public void verticallyFlipMainPanel() {
		mainPanel.flipVertically();
		controlPanel.updateFields(mainPanel);
	}
	
	public void setMainPanel(FractalPanel panel){
		mainPanel = panel;
	}
	
	public void setControlPanel(ControlPanel panel){
		controlPanel = panel;
	}
	
	public  JComboBox<ComplexNumber> getJuliaSetPointsComboBox() {
		return juliaPoints;
	}

	public void setJuliaSetPointsComboBox(JComboBox<ComplexNumber> comboBox) {
		juliaPoints = comboBox;
		
	}

	public void updateJuliaSetPointsComboBox(ComboBoxModel<ComplexNumber> model) {
		juliaPoints.setModel(model);
		
	}
	
	public void setPointDisplayPanel(PointDisplayPanel panel) {
		this.pointDisplayPanel = panel;
		
	}

	public void updateJuliaDisplay(FractalPanel fractalPanel) {
		if(fractalPanel.hasJulia()){
			pointDisplayPanel.setJuliaPanelFromComboBox();
		}else{
			pointDisplayPanel.removeJulia();
		}
		
	}

	public void exportMainDisplay(){
		JFileChooser fc = new JFileChooser();
		fc.setMultiSelectionEnabled(false);
		String filePath = null;

		fc.setDialogTitle("Saving Main Fractal");
		fc.setApproveButtonText("Save");
		fc.setApproveButtonToolTipText("Save Main Fractal");

		FileFilter ff = new FileNameExtensionFilter("*.png",".png");
		fc.setFileFilter(ff);



		if( fc.showOpenDialog(null) == 	JFileChooser.APPROVE_OPTION){
			filePath = fc.getSelectedFile().getAbsolutePath();
		}

		if(filePath==null){
			JOptionPane.showMessageDialog(null, "Invalid file path, export cancelled.", "Main Fractal Save Cancelled", JOptionPane.INFORMATION_MESSAGE);
			return;
		}
		File outputfile = new File(filePath+".png");
		BufferedImage image = mainPanel.getImage();
		if(image == null)return;
		try {
			ImageIO.write(image, "png", outputfile);
			JOptionPane.showMessageDialog(null, "Successfully saved main fractal to: \n"+outputfile.getAbsolutePath(), "Main Fractal Saved", JOptionPane.INFORMATION_MESSAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	

}
