package linearRegression;

public class SimpleLinearRegressionMain {
	
	public static void main(String[] args){
		double[] x = {100,200,300,400,500,600,700};
		double[] y = {40,50,50,70,65,65,80};
	
		SimpleLinearRegression slr = new SimpleLinearRegression(x, y);
		slr.learn();
		slr.print();
	}

}
