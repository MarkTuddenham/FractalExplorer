package fractal_explorer.util;

import java.awt.Color;

public class JuliaSet extends FractalSet{
	private ComplexNumber c;

    public JuliaSet(ComplexNumber c) {
    	super();
    	this.c = c;
    }
    
	public ComplexNumber getC() {
		return c;
	}

	public void setC(ComplexNumber c) {
		this.c = c;
	}
	
	@Override
	public int getColour(ComplexNumber z0) {
		int i = 0;
		while (z0.modulusSquared() < divergenceTest && i < convergenceTest) {
			z0.square();
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

	@Override
	public boolean hasJulia() {
		return true;
	}

}
