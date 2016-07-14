package statisticalHypothesisTesting;

public class ChiSquaredTest {
	private double[] probability;
	private int[] observed;
	private static double upperLevelOfSignificance[] = {3.84,5.99,7.81,9.49,11.07,12.6,14.1,15.5,16.9,18.3, 
												19.7,21,22.4,23.7,25,26.3,27.6,28.9,30.1,31.4,
												32.7,33.9,35.2,36.4,37.7,38.9,40.1,41.3,42.6,43.8};
	private static double lowerLevelOfSignificance[] =	{0.0039,0.1026,0.352,0.711,1.15,1.64,2.17,2.73,3.33,3.94,
												4.57,5.23,5.89,6.57,7.26,7.96,8.67,9.39,10.1,10.9,
												11.6,12.3,13.1,13.9,14.6,15.4,16.2,16.9,17.7,18.5};

	private double chi2;
	private int degreeOfFreedom;
	private double alpha;
	private boolean isMatch;
	private double[] expectedValue;
	

	public ChiSquaredTest(double[] probability,int[] observed){
		this.probability = probability.clone();
		this.observed = observed.clone();
		degreeOfFreedom = observed.length - 1;
		alpha = 0.05;
	}
	
	
	public void testing(){
		getExpectedValue();
		chi2 = 0;
		for(int i  = 0; i < observed.length; i++){
			chi2 += Math.pow(observed[i]-expectedValue[i], 2)/expectedValue[i];
		}
		isMatch = (lowerLevelOfSignificance[degreeOfFreedom-1] < chi2) && (chi2 < upperLevelOfSignificance[degreeOfFreedom-1]);

	}
	
	public void printTesting(){
		System.out.println("--------カイ2乗検定--------");
		System.out.println("自由度\t:" + degreeOfFreedom);
		System.out.println("有意確率α\t:" + alpha);
		System.out.print("観測値\t:[");
		for(int i = 0; i < observed.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf("%5d",observed[i]);
		}
		System.out.println("]");
		
		System.out.print("期待確率\t:[");
		for(int i = 0; i < probability.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf("%.3f",probability[i]);
		}
		System.out.println("]");
		
		System.out.print("期待値\t:[");
		for(int i = 0; i < expectedValue.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf("%5.1f",expectedValue[i]);
		}
		System.out.println("]");
		
		System.out.println("χ^2\t:" + chi2);
		System.out.println("上側優位点\t:" + upperLevelOfSignificance[degreeOfFreedom-1]);
		System.out.println("下側優位点\t:" + lowerLevelOfSignificance[degreeOfFreedom-1]);
		System.out.print("検定\t:");
		if(isMatch){
			System.out.println("clear");
		}
		else{
			System.out.println("Misstake");
		}
		System.out.println("-------------------------");
	}
	
	
	
	
	
	
	
	private void getExpectedValue(){
		int sumObserved = 0;
		expectedValue = new double[observed.length];
		for(int n:observed){
			sumObserved+=n;
		}
		for(int i = 0; i < expectedValue.length; i++){
			expectedValue[i] = probability[i]*sumObserved;
		}
	}

}
