
package fractal_explorer.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.font.TextAttribute;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class ControlPanel extends JPanel implements  MouseListener{
	private static final long serialVersionUID = 1L;
	
	private JTextField xMax, xMin, yMax, yMin, convergenceTest;
	private JButton reset, update;
	private JComboBox<String> fractalList;

	@SuppressWarnings("unchecked")
	public ControlPanel(){
		
		setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		
		JLabel title = new JLabel("Main Fractal Controls");
		//underline title
		Font font = title.getFont();
		
		@SuppressWarnings("rawtypes")
		Map attributes = font.getAttributes();
		attributes.put(TextAttribute.UNDERLINE, TextAttribute.UNDERLINE_ON);
		title.setFont(font.deriveFont(attributes));
		
		xMax = new JTextField("2",20);
		xMin = new JTextField("-2",20);
		yMax = new JTextField("1.6",20);
		yMin = new JTextField("-1.6",20);
		
		JLabel realLabel      = new JLabel("Real Axis ");
		JLabel minLabel 	  = new JLabel("Min Value");
		JLabel maxLabel 	  = new JLabel("Max Value");
		JLabel imaginaryLabel = new JLabel("Imaginary Axis ");
		JLabel fractalListLabel = new JLabel("Fractals: ");
		
		convergenceTest = new JTextField("255",10);

		JLabel convergenceLabel = new JLabel("Convergence Test: ");
		
		reset = new JButton("Reset");
		update = new JButton("Update");
		
		String[] fractals = {"Mandelbrot","Burning Ship", "Lambda"};
		fractalList = new JComboBox<String>(fractals);
		
		//add all the components
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(5,1,5,1);

		c.gridy=0;
		c.gridx=0;
		c.gridwidth=3;
		add(title, c);
		
		c.gridwidth = 1;
		c.gridy++;
		add(fractalListLabel, c);
		c.gridx++;
		add(fractalList, c);
		
		c.insets = new Insets(1,1,1,1);
		
		c.gridx = 1;
		c.gridy++;
		add(realLabel, c);
		c.gridx = 2;
		add(imaginaryLabel,c);
		c.gridx = 0;
		c.gridy++;
		add(maxLabel,c);
		c.gridx=1;
		add(xMax,c);
		c.gridx=2;
		add(yMax,c);
		c.gridx = 0;
		c.gridy++;
		add(minLabel,c);
		c.gridx=1;
		add(xMin,c);
		c.gridx=2;
		add(yMin,c);
		
		c.gridx = 0;
		c.gridy++;
		add(convergenceLabel, c);
		c.gridx = 1;
		add(convergenceTest, c);
		c.gridy++;
		c.gridx=0;
		c.gridwidth = 2;
		c.fill = GridBagConstraints.NONE;
		add(reset, c);
		c.gridx++;
		add(update, c);
	
	}

	public void addActionListener(ActionListener actionListener) {
		//add the action listeners to all components
		xMax.addActionListener(actionListener);
		xMin.addActionListener(actionListener); 
		yMax.addActionListener(actionListener);
		yMin.addActionListener(actionListener);
		convergenceTest.addActionListener(actionListener);
		reset.addActionListener(actionListener);
		update.addActionListener(actionListener);
		fractalList.addActionListener(actionListener);
	}

	public Double getMinX() { return Double.valueOf(xMin.getText()); }
	public Double getMaxX() { return Double.valueOf(xMax.getText()); }
	public Double getMinY() { return Double.valueOf(yMin.getText()); }
	public Double getMaxY() { return Double.valueOf(yMax.getText()); }
	public Integer getConvergenceTest() { return Integer.parseInt(convergenceTest.getText()); }
	
	public void setMinX(Double x) { xMin.setText(""+x); }
	public void setMaxX(Double x) { xMax.setText(""+x); }
	public void setMinY(Double y) { yMin.setText(""+y); }
	public void setMaxY(Double y) { yMax.setText(""+y); }
	public void setConvergenceTest(int i) { convergenceTest.setText(""+i); }

	@Override
	public void mouseClicked(MouseEvent e) {}
	@Override
	public void mouseEntered(MouseEvent e) {}
	@Override
	public void mouseExited(MouseEvent e) {}
	@Override
	public void mousePressed(MouseEvent e) {}
	@Override
	public void mouseReleased(MouseEvent e) {
		updateFields((FractalPanel) e.getSource());
	}
	
	public void updateFields(FractalPanel fp){
		xMin.setText(""+fp.fs.getXMin());
		xMax.setText(""+fp.fs.getXMax());
		yMin.setText(""+fp.fs.getYMin());
		yMax.setText(""+fp.fs.getYMax());
	}

}
