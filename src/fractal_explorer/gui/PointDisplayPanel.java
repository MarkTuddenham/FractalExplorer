package fractal_explorer.gui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import fractal_explorer.util.ComplexNumber;
import fractal_explorer.util.FractalSettings;
import fractal_explorer.util.JuliaSet;

public class PointDisplayPanel extends JPanel implements MouseListener, MouseMotionListener {
	private static final long serialVersionUID = 1L;
	
	private JLabel complexNumberLabel;
	private FractalPanel jp = null;
	private JPanel jpParentPanel;
	private JComboBox<ComplexNumber> favourites = null;
	
	
	public PointDisplayPanel() {

		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0;
		c.weighty = 0;
		c.gridwidth = 2;



		//Create model so we can check what's in the combo box
		DefaultComboBoxModel<ComplexNumber> favModel = new DefaultComboBoxModel<ComplexNumber>();		
		favourites = new JComboBox<ComplexNumber>(favModel);
		favourites.setEditable(false);
		favourites.setMaximumRowCount(10);
		
		addJuliaFavourites(c, favModel);
		FractalSettings.getFractalSettings().setJuliaSetPointsComboBox(favourites);
		
		ComplexNumber i = (ComplexNumber) favourites.getSelectedItem();
		addJuliaSetDisplay(new JuliaSet(i));
		complexNumberLabel = new JLabel("Point: "+i);
		complexNumberLabel.setHorizontalAlignment(SwingConstants.CENTER);
		c.gridx = 0;
		c.gridy = 2;
		add(complexNumberLabel, c);
		
	}
	
	private void addJuliaFavourites(GridBagConstraints c, DefaultComboBoxModel<ComplexNumber> favModel) {
		c.gridwidth=1;
		c.fill = GridBagConstraints.NONE;
		c.gridx=0;
		c.gridy=0;
		c.weightx = 1;
		c.weighty = 0;
		c.anchor = GridBagConstraints.EAST;
		
		JLabel favouriteLabel = new JLabel(" Julia Sets: ");
		add(favouriteLabel, c);
		

		c.anchor = GridBagConstraints.WEST;
		c.gridx=1;
		

		add(favourites, c);
		
		favourites.addItem(new ComplexNumber(0,0));
		

		JButton save = new JButton("Save");
		save.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//jp is null when main panel insn't a Mandelbrot panel
				if(jp==null) return;
				ComplexNumber newItem =((JuliaSet) jp.fs).getC(); 
				if(favModel.getIndexOf(newItem)==-1){
					favourites.addItem(newItem);
					favourites.setSelectedItem(newItem);
				}
			}
		});
		

		JButton load = new JButton("Load");
		load.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				//jp is null when main panel insn't a Mandelbrot panel
				if(jp == null) return;
				setJuliaPanelFromComboBox();

			}
		});

		JButton delete = new JButton("Remove");
		delete.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				//jp is null when main panel insn't a Mandelbrot panel
				if(jp==null) return;
				ComplexNumber i = (ComplexNumber) favourites.getSelectedItem();
				if(i!=null){
					favourites.removeItem(i);
				}
			}
		});
				
		
		c.anchor = GridBagConstraints.CENTER;
		c.gridwidth=2;
		c.gridy=1;
		c.gridx=0;
		
		JPanel buttonWrapper = new JPanel(new GridLayout(1, 2));
		buttonWrapper.add(save);
		buttonWrapper.add(load);
		buttonWrapper.add(delete);
	
		add(buttonWrapper, c);
		
		
	}
	private void addJuliaSetDisplay(JuliaSet js) {
		jp = new FractalPanel(js);
		GridBagConstraints c = new GridBagConstraints();
		c.fill = GridBagConstraints.BOTH;
		c.weightx = 0.5;
		c.weighty = 0.5;
		c.gridwidth = 2;
		c.gridx=0;			
		c.gridy=3;


		jpParentPanel = new JPanel();
		jpParentPanel.setLayout(new GridBagLayout());
		jpParentPanel.setPreferredSize(new Dimension(50, 50));
		jpParentPanel.add(jp);
		
		add(jpParentPanel, c);
		
	}
	
	public void setJuliaPanelFromComboBox(){
		
		ComplexNumber i = (ComplexNumber) favourites.getSelectedItem();
		if(i==null){
			i = new ComplexNumber(0,0);
			favourites.addItem(i);
		}
			JuliaSet js = new JuliaSet(i);
			if(jp == null){
				addJuliaSetDisplay(js);
			}else{
				jp.fs = js;
			}
			
			complexNumberLabel.setText("Point: "+i);
			jp.shouldUpdate();
			jp.repaint();
		
	}


	
	@Override
	public void mouseClicked(MouseEvent e) {
		updateJulia(e);
	}

	private void updateJulia(MouseEvent e) {
		
		Point mouseClickPoint = e.getPoint();

		Object eventSource = e.getSource();
		if (eventSource instanceof FractalPanel) {
			if(((FractalPanel)eventSource).fs.hasJulia()){
				ComplexNumber i = ((FractalPanel)eventSource).getComplexNumberFromPoint(mouseClickPoint);

				JuliaSet js = new JuliaSet(i);

				if(jp == null){			
					addJuliaSetDisplay(js);	
				}else{
					jp.fs = js;				
				}

				complexNumberLabel.setText("Point: "+i);

				jp.shouldUpdate();
				jp.repaint();
		}
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {}
	
	@Override
	public void mouseDragged(MouseEvent e) {}

	@Override
	public void mouseMoved(MouseEvent e) {
		if(FractalSettings.getFractalSettings().doAutoUpdateJuliaSet()){
			updateJulia(e);
		}
		
	}


	public void removeJulia() {
		jp = null;
		this.remove(jpParentPanel);
		complexNumberLabel.setText("No Julia sets for non-Mandelbrot type Fractals");
		repaint();
	}

}

