package classification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lib.Matrix;

public class LogisticRegression {
	Matrix x;
	Matrix t;
	Matrix w;
	double init_eta;
	double etaStep;
	int iteration;

	
	/**
	 * 主要コンストラクタ
	 * @param x 特徴量
	 * @param t 正解ラベル
	 * @param iteration イテレーション回数
	 * @param init_eta 学習率の初期値
	 * @param etaStep 学習率の更新幅
	 */
	public LogisticRegression(double[][] x, double[][] t, int iteration,double init_eta,double etaStep){
		this.x = new Matrix(x);
		this.t = new Matrix(t);
		this.iteration = iteration;
		
		double[][] w_array = new double[this.x.col_n()+1][1];
		Random rnd = new Random();
		for(int i = 0; i < w_array.length;i++){
			w_array[i][0]=rnd.nextGaussian();
		}
		this.w = new Matrix(w_array);
		this.init_eta = init_eta;
		this.etaStep = etaStep;
	}
	
	/**
	 * コンストラクタ。イテレーションと学習率はデフォルトで設定される。(iteration = 300,学習率の初期値 = 0.1,学習率の更新幅=0.9)
	 * @param x 特徴量
	 * @param t 正解ラベル
	 */
	public LogisticRegression(double[][] x, double[][] t){
		this(x,t,300,0.1,0.9);
	}
	
	/**
	 * シグモイド関数
	 * sigmoid(x) = 1/1+exp(-x)
	 * となる。
	 * @param x 入力
	 * @return sigmoid(x)の値。
	 */
	private double sigmoid(double x){
		return 1.0/(1.0+Math.exp(-x));
	}

	
	/**
	 * 基底関数。
	 * Φ(x) = ΣΦ(xi)= 1 + x1 + x2 +...+ xn
	 * となるような基底関数。
	 * @param x_i
	 * @return
	 */
	private Matrix phi(double[] x_i){
		double[][] phi = new double[1][x_i.length+1];
		for(int i = 0; i < x_i.length; i++)
			phi[0][i] = x_i[i];
		phi[0][phi[0].length-1] = 1.0;		
		return new Matrix(phi);
	}

	/**
	 * 学習を行う。
	 */
	public void cal(){
		//学習率を初期化
		double eta = init_eta;
		
		//データの学習する順序をシャッフルするためのリスト。
		List<Integer> list = new ArrayList<>();
		for(int i= 0; i < x.row_n(); i++)
			list.add(i);
		for(int i = 0; i < iteration; i++){
			//学習順序のシャッフル
			Collections.shuffle(list);
			for(int n:list){//データを1つずつ取り出して学習。
				//n番目のデータの正解ラベルを取り出す。
				double t_n = t.getCell(n,0);
				//n番目の特徴量を基底関数に入れる。
				Matrix feature = phi(x.getRow(n).toArray()[0]);
				//n番目データの正解ラベルへの所属確率の予測値を計算
				double predict = sigmoid(feature.mul(w).toArray()[0][0]);
				//重みwの更新
				w = w.sub(feature.scalar(eta*(predict-t_n)).t());
			}
			//学習率の更新
			eta*=etaStep;
		}
	}
	
	/**
	 * 新しいデータに汎化させる。
	 * 新しい特徴量を入れた時のそのデータの正解ラベルへの所属確率を返す。
	 * @param feature 特徴量
	 * @return 正解確率
	 */
	public double fit(double[] feature){
		return sigmoid((phi(feature).mul(w)).toArray()[0][0]);
	}
	
	public double[] getWeight(){
		return ((w.t()).getRow(0)).toArray()[0];
	}
}

