# カイ二乗検定
## Javadoc
[コチラ](https://htmlpreview.github.io/?https://raw.githubusercontent.com/otamot/MachineLearning/master/doc/statisticalHypothesisTesting/ChiSquaredTest.html)

## ソースコード
### [ChiSquaredTest](https://github.com/otamot/MachineLearning/blob/master/src/statisticalHypothesisTesting/ChiSquaredTest.java)

```java
package statisticalHypothesisTesting;

public class ChiSquaredTest {
	//フィールド宣言
	/**
	 * 期待確率値を保持する変数
	 */
	private double[] probability;
	/**
	 * 観測度数を保持する変数
	 */
	private int[] observed;
	/**
	 * 有意水準5%時の上側有意点
	 * upperLevelOfSignificance[i]で自由度(i+1)の上側パーセント点を得られる。
	 */
	private static double upperLevelOfSignificance[] = {3.84,5.99,7.81,9.49,11.07,12.6,14.1,15.5,16.9,18.3,
												19.7,21,22.4,23.7,25,26.3,27.6,28.9,30.1,31.4,
												32.7,33.9,35.2,36.4,37.7,38.9,40.1,41.3,42.6,43.8};
	/**
	 * 有意水準5%時の下側有意点
	 * lowerLevelOfSignificance[i]で自由度(i+1)の下側パーセント点を得られる。
	 */
	private static double lowerLevelOfSignificance[] =	{0.0039,0.1026,0.352,0.711,1.15,1.64,2.17,2.73,3.33,3.94,
												4.57,5.23,5.89,6.57,7.26,7.96,8.67,9.39,10.1,10.9,
												11.6,12.3,13.1,13.9,14.6,15.4,16.2,16.9,17.7,18.5};

	/**
	 * カイ二乗値を保持する変数
	 */
	private double chi2;
	/**
	 * 自由度を保持する変数
	 */
	private int degreeOfFreedom;
	/**
	 * 有意水準を保持する変数
	 */
	private double alpha;
	/**
	 * 検定がclearかどうかを保持する変数
	 */
	private boolean isMatch;
	/**
	 * 期待値を保持する変数
	 */
	private double[] expectedValue;


	/**
	 * コンストラクタ。
	 * @param probability P(X=0)~P(X=n-1)までのそれぞれの確率値を保持
	 * @param observed 0~n-1の観測値を保持
	 */
	public ChiSquaredTest(double[] probability,int[] observed){
		this.probability = probability.clone();
		this.observed = observed.clone();
		degreeOfFreedom = observed.length - 1;
		alpha = 0.05;
	}

	/**
	 * χ^2値を計算し、χ^2検定を行う。
	 * 検定結果をisMatchに記録する。
	 * 帰無仮説が正しかった場合isMatchはtrue,棄却される場合isMatchはfalseとなり
	 */
	public void testing(){
		getExpectedValue();
		chi2 = 0;
		for(int i  = 0; i < observed.length; i++){
			chi2 += Math.pow(observed[i]-expectedValue[i], 2)/expectedValue[i];
		}
		isMatch = (lowerLevelOfSignificance[degreeOfFreedom-1] < chi2) && (chi2 < upperLevelOfSignificance[degreeOfFreedom-1]);

	}

	/**
	 * 検定結果を出力するメソッド
	 * 自由度,有意確率,観測値,期待値,期待確率,χ^2値,上側優位点,下側優位点,検定結果を出力する。
	 */
	public void printTesting(){
		System.out.println("--------カイ2乗検定--------");
		System.out.println("自由度\t:" + degreeOfFreedom);
		System.out.println("有意確率α\t:" + alpha);		
		System.out.print("期待確率\t:[");
		for(int i = 0; i < probability.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf(" %.5f",probability[i]);
		}
		System.out.println("]");

		System.out.print("期待度数\t:[");
		for(int i = 0; i < expectedValue.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf(" %7.1f",expectedValue[i]);
		}
		System.out.println("]");
		System.out.print("観測度数\t:[");
		for(int i = 0; i < observed.length; i++){
			if(i != 0)
				System.out.print(",");
			System.out.printf(" %5d.0",observed[i]);
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

	/**
	 * 期待度数を計算する
	 */
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
```

### [ChiSquaredTestMain](https://github.com/otamot/MachineLearning/blob/master/src/statisticalHypothesisTesting/ChiSquaredTestMain.java)
```java
package statisticalHypothesisTesting;

import java.util.Random;

public class ChiSquaredTestMain {
	public static void main(String[] args){
		//テストケース①
		/*
		 *  授業と同じテストケース。
		 *  1~6の数を120個ランダムで作り、検定する。
		 */
		System.out.println("テストケース①");
		double[] probability = new double[6];
		int[] observed = new int[6];
		for(int i = 0; i < probability.length; i++){
			probability[i] = 1.0/probability.length;
		}
		Random rnd = new Random();
		for(int i = 0 ; i < 120; i++){
			observed[rnd.nextInt(6)]++;
		}
		ChiSquaredTest chiSquared = new ChiSquaredTest(probability, observed);
		chiSquared.testing();
		chiSquared.printTesting();


		//テストケース②
		/*
		 * 授業でやっていないパターン
		 * 1-10の数を100000個生成検定する。
		 *
		 */
		System.out.println("テストケース②");
		probability = new double[10];
		for(int i = 0; i < probability.length; i++){
			probability[i] = 1.0/probability.length;
		}
		observed = new int[10];
		for(int i = 0; i < probability.length; i++){
			probability[i] = 1.0/probability.length;
		}
		for(int i = 0 ; i < 100000; i++){
			observed[rnd.nextInt(10)]++;
		}
		chiSquared = new ChiSquaredTest(probability, observed);
		chiSquared.testing();
		chiSquared.printTesting();
	}

}

```
