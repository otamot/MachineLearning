import java.util.Random;

import classification.LogisticRegression;

public class LogisticRegressionMain {
	
	private static double h(double x, double y){
		return 5 * x + 3 * y - 1;
	}

	
	public static void main(String[] args){
		
		int N = 10000;
		Random rnd = new Random();
		double[][] x = new double[N][2];
		for(int i = 0; i < x.length;i++){
			for(int j = 0; j < x[i].length; j++){
				x[i][j] = rnd.nextGaussian();
			}
		}
		
		double[][] t = new double[N][1];
		for(int i = 0; i < x.length; i++){
			t[i][0] = (h(x[i][0], x[i][1]) > 0)?1:0;
		}
		
		LogisticRegression lr = new LogisticRegression(x,t);
		lr.cal();
		
		double[][] feature = {{2,-3},{5.0/8.0,3.0/8.0},{0.0,0.0}};
		System.out.println(lr.fit(feature[0]));
		System.out.println(lr.fit(feature[1]));
		System.out.println(lr.fit(feature[2]));
		
		
		
	}

}
