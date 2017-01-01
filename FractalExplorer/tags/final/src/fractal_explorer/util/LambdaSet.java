package fractal_explorer.util;

import java.awt.Color;

public class LambdaSet extends FractalSet {

	@Override
	public int getColour(ComplexNumber z0) {
		ComplexNumber zn, zn1 = z0.clone();
		int i = 0;
		while (zn1.modulusSquared() < divergenceTest && i < convergenceTest) {
			zn = zn1.clone();
			zn1 = new ComplexNumber(1,0);
			zn1.subtract(zn);
			zn1.multiply(zn);
			zn1.multiply(z0);
		
			i++;
		}
		
		if (i == convergenceTest) {
			return Color.BLACK.getRGB();
		} else {			
			double logRootA = Math.log(zn1.modulusSquared())/2.0d;
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
