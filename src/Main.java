

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ComboBoxModel;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

import fractal_explorer.files.JuliaFavouritesFile;
import fractal_explorer.gui.MainPanel;
import fractal_explorer.util.ComplexNumber;
import fractal_explorer.util.FractalSettings;

public class Main {
	public static void main(String[] args) {
		/*
		 * Bugs:
		 * 	 	- When Maximising the frame during image calculation - main display is half and half
		 * 		
		 */
		
		SwingUtilities.invokeLater(new Runnable() {
			
			@Override
			public void run() {
				JFrame frame = new JFrame("Fractal Explorer");
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				
				MainPanel mainPanel = new MainPanel();
				frame.setContentPane(mainPanel);

				//create menus
				JMenuBar menuBar = new JMenuBar();
				JMenu fileMenu = new JMenu("File");
				menuBar.add(fileMenu);
				
				//menu item for load the julia set combo box
				JMenuItem openFile = new JMenuItem("Open Julia Sets file");
				openFile.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						ComboBoxModel<ComplexNumber> model = JuliaFavouritesFile.loadFavourites();
						if(model!=null){
							FractalSettings.getFractalSettings().updateJuliaSetPointsComboBox(model);
						}
					}
				});
				
				//menu item for saving julia set combo box
				JMenuItem saveFile = new JMenuItem("Save Julia Sets file");
				saveFile.addActionListener(new ActionListener() {

					@Override
					public void actionPerformed(ActionEvent e) {
						JuliaFavouritesFile.saveFavourites(FractalSettings.getFractalSettings().getJuliaSetPointsComboBox().getModel());
					}
				});

				
				fileMenu.add(saveFile);
				fileMenu.add(openFile);
				
				JMenu viewMenu = new JMenu("View");
				menuBar.add(viewMenu);
				
				//menu item for turning on and off auto update julia on mouse hover
				JCheckBoxMenuItem autoUpdateJulia = new JCheckBoxMenuItem("Auto update Julia Set");
				autoUpdateJulia.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						FractalSettings.getFractalSettings().setAutoUpdateJuliaSet(autoUpdateJulia.isSelected());
						
					}
				});
				
				viewMenu.add(autoUpdateJulia);
				
				//menu item to allow the user to flip the main fractal display vertically
				JMenuItem flipMainPanel = new JMenuItem("Verically flip main fractal");
				flipMainPanel.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						FractalSettings.getFractalSettings().verticallyFlipMainPanel();
						
					}
				});
				
				viewMenu.add(flipMainPanel);
				
				// menu item to save the main fractal as a png image
				JMenuItem export = new JMenuItem("Export Main Fractal as image");
				export.addActionListener(new ActionListener() {
					
					@Override
					public void actionPerformed(ActionEvent e) {
						FractalSettings.getFractalSettings().exportMainDisplay();
						
					}
				});
				fileMenu.add(export);
				
				
				
				//allows user to turn features on and off up pressing CTRL-{someKey}
				autoUpdateJulia.setAccelerator(KeyStroke.getKeyStroke(
				        java.awt.event.KeyEvent.VK_U, 
				        java.awt.Event.CTRL_MASK));
				
				openFile.setAccelerator(KeyStroke.getKeyStroke(
				        java.awt.event.KeyEvent.VK_O, 
				        java.awt.Event.CTRL_MASK));
				
				saveFile.setAccelerator(KeyStroke.getKeyStroke(
				        java.awt.event.KeyEvent.VK_S, 
				        java.awt.Event.CTRL_MASK));
				
				flipMainPanel.setAccelerator(KeyStroke.getKeyStroke(
				        java.awt.event.KeyEvent.VK_F, 
				        java.awt.Event.CTRL_MASK));
				
				export.setAccelerator(KeyStroke.getKeyStroke(
				        java.awt.event.KeyEvent.VK_E, 
				        java.awt.Event.CTRL_MASK));
				
				
				frame.setJMenuBar(menuBar);

				frame.pack();
				frame.setVisible(true);
			}
		});
		
	}
}
