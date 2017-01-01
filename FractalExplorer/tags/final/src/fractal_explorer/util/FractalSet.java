package fractal_explorer.util;

public abstract class FractalSet {

	private static final int defaultConvergenceTest = 255;
    protected int convergenceTest = defaultConvergenceTest;
    protected double divergenceTest = 15; //4 - increasing this gets rid of the non smoothed edges when using a blue colour
    protected float edgeDetail = 100f, colouringModifier = 0.5f, darknessScaling = 0.01f;

    private static final double defaultXMax = 2, defaultXMin = -2, defaultYMax = 1.6, defaultYMin = -1.6;
    private double xMax = defaultXMax, xMin = defaultXMin, yMax = defaultYMax, yMin = defaultYMin;
    
      
    public abstract boolean hasJulia();

	public abstract int getColour(ComplexNumber z0);


	public static int getDefaultconvergencetest() {
		return defaultConvergenceTest;
	}


	public  double getDivergenceTest() {
		return divergenceTest;
	}

	public  void setDivergenceTest(double divergenceTest) {
		this.divergenceTest = divergenceTest;
	}
    
	public int getConvergenceTest() {
		return convergenceTest;
	}

	public void setConvergenceTest(int convergenceTest) {
		this.convergenceTest = convergenceTest;
	}

	public double getXMax() {
		return xMax;
	}

	public void setXMax(Double xMax) {
		this.xMax = xMax;
	}

	public double getXMin() {
		return xMin;
	}

	public void setXMin(Double xMin) {
		this.xMin = xMin;
	}

	public double getYMax() {
		return yMax;
	}

	public void setYMax(Double yMax) {
		this.yMax = yMax;
	}

	public double getYMin() {
		return yMin;
	}

	public void setYMin(Double yMin) {
		this.yMin = yMin;
	}


	public static double getDefaultxmax() {
		return defaultXMax;
	}


	public static double getDefaultxmin() {
		return defaultXMin;
	}


	public static double getDefaultymax() {
		return defaultYMax;
	}


	public static double getDefaultymin() {
		return defaultYMin;
	}
}
