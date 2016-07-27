package linearRegression;

public class LinearRegressionMain {

	public static void main(String[] args) {
		double[][] x = {{100},{200},{300},{400},{500},{600},{700}};
		double[] y = {40,50,50,70,65,65,80};
		LinearRegression lr = new LinearRegression(BasisFunction.polynomialBasis(x, 10),y, new BasisFunction());
		lr.cal();
	}

}
