package linearRegression;



public class BasisFunction {
	
	static double[][] polynomialBasis(double[][] x, int M){
		if(x[0].length != 1 || M < 1)
			throw new IllegalArgumentException();
		double[][] phi = new double[x.length][M];
		for(int i = 0; i < phi.length; i++){
			for(int j = 0; j < phi[i].length; j++){
				phi[i][j] = Math.pow(x[i][0],j);			}
		}
		return phi;
	}
	
	
	
}
