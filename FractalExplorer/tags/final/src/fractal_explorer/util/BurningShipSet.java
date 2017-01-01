package fractal_explorer.util;

import java.awt.Color;

public class BurningShipSet extends FractalSet{

	@Override
	public int getColour(ComplexNumber c) {
		ComplexNumber z0 = new ComplexNumber(0,0);
		int i = 0;
		while (z0.modulusSquared() < divergenceTest && i < convergenceTest) {

			z0.setReal(Math.abs(z0.getReal()));
			z0.setImaginary(Math.abs(z0.getImaginary()));
			z0.square();
			z0.subtract(c);
			
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
		return false;
	}

}
