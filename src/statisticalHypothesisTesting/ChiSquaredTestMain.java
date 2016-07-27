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
