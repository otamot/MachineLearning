package linearRegression;
import lib.Matrix;

public class LinearRegression {
	Matrix X;
	Matrix Phi;
	Matrix Y;
	Matrix W;
	
	public LinearRegression(double[][] x, double[] y,BasisFunction phi){
		double[][] y_mat = new double[1][y.length];
		y_mat[0] = y;
		Y = new Matrix(y_mat).t();
		X = new Matrix(x);
	}
	
	
	
	
	/**
	 * 
	 * 線形回帰を行う。
	 * W = (Φ^TΦ)^(-1)ΦY
	 */
	public void cal(){
		W = ((X.t().mul(X)).inv()).mul(X.t()).mul(Y);
		W.printMatrix();
		double[][] w = W.toArray();
		System.out.print("y =");
		for(int i = 0; i < w.length; i++){
			if(i != 0){
				if(w[i][0] > 0)
				System.out.print("+");
			}
			
			System.out.print(" " + w[i][0] + "*Φ[" + i + "] ");
		}
	}
	
}
