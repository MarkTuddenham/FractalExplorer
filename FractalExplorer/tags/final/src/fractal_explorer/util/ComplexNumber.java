package fractal_explorer.util;

import java.io.Serializable;

public class ComplexNumber implements Serializable {
	private static final long serialVersionUID = 1L;
	
	// a + bi
	private double a, b;

	public ComplexNumber(double real, double imaginary) {
		setReal(real);
		setImaginary(imaginary);
	}
	
	public ComplexNumber(){
		super();
	}
	
	public void square(){
		//could use power(2) but would be slower
		double newReal = a*a - b*b;
		double newImaginary = 2*a*b;
		a = newReal;
		b = newImaginary;
	}
	
	public void power(int pow){
		ComplexNumber temp = this.clone();
		for(int i = 0; i<pow/2;i++){
			this.square();
		}
		if(pow%2 == 1){
			this.multiply(temp);
		}
	}
	
	public void multiply(ComplexNumber c){
		double newReal = a*c.getReal() - b*c.getImaginary();
		double newImaginary = c.getReal()*b + a*c.getImaginary();
		a = newReal;
		b = newImaginary;
	}
	
	public double modulusSquared(){
		//modulus = sqrt( a^2 + b^2 )
		return a*a + b*b;
	}
	
	public void add(ComplexNumber i){
		a += i.getReal();
		b += i.getImaginary();
	}
	
	public ComplexNumber clone(){
		ComplexNumber j = new ComplexNumber(a,b);
		return j;
	}
	
	public double getReal() {
		return a;
	}
	public void setReal(double real) {
		this.a = real;
	}
	public double getImaginary() {
		return b;
	}
	public void setImaginary(double imaginary) {
		this.b = imaginary;
	}
	
	public String toString(){
		return this.getReal()+((this.getImaginary()>0)?" + ":" - ")+Math.abs(this.getImaginary())+"i";
	}

	public void subtract(ComplexNumber c) {
		a-=c.getReal();
		b-=c.getImaginary();
		
	}
}
