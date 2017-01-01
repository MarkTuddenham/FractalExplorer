package fractal_explorer.util;

import java.awt.Color;

public class MandelbrotSet extends FractalSet{
	
	
	private int power = 2;
	
    @Override
	public int getColour(ComplexNumber z0) {
    	ComplexNumber c = z0.clone();
		int i = 0;
		while (z0.modulusSquared() < divergenceTest && i < convergenceTest) {
			
			z0.power(power);
			z0.add(c);
			i++;
		}
		
		if (i == convergenceTest) {
			return Color.BLACK.getRGB();
		} else {			
			double logRootA = Math.log(z0.modulusSquared())/2.0d;
			double nu = Math.log(logRootA/Math.log(2)) / Math.log(2);
			float colour = (float) (i+1-nu)/edgeDetail;
			return Color.HSBtoRGB( colour+colouringModifier, 1, colour/(colour+darknessScaling));
		}
	}

	public int getPower() {
		return power;
	}

	public void setPower(int power) {
		this.power = power;
	}

	@Override
	public boolean hasJulia() {
		return true;
	}
    
    	
}
