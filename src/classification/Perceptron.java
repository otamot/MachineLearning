package classification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lib.Matrix;


/**
 * パーセプトロンによる分類を行う
 * @author otamot
 * @since 2016/07/27
 * @version 1.0
 */
public class Perceptron {
	/**
	 * データ群の特徴量を保持するMatrix型の変数
	 */
	private Matrix x;
	/**
	 * データ群の正解ラベルを保持するMatrix型の変数
	 */
	private Matrix t;
	/**
	 * パーセプトロンによって求める重みを保持する変数
	 */
	private Matrix w;

	/**
	 * コンストラクタ
	 * @param x 特徴量
	 * @param t 正解ラベル。1か-1のラベルが入っていることを前提とする。
	 */
	public Perceptron(double[][] x, double[][] t){
		this.x = new Matrix(x);
		this.t = new Matrix(t);
		this.w = new Matrix(this.x.col_n()+1,1);
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
	 * xが正なら1,0なら0,負なら-1を返す関数
	 * @param x 引数
	 * @return 符号に対応したint値
	 */
	private int sign(double x){
		return (x>0)?1:(x==0)?0:-1;
	}
	
	/**
	 * パーセプトロンの学習を行う。
	 */
	public void cal(){
		//データの学習する順序をシャッフルするためのリスト
		List<Integer> list = new ArrayList<>();
		for(int i= 0; i < x.row_n(); i++){
			list.add(i);
		}
		while(true){
			//学習順序のシャッフル
			Collections.shuffle(list);
			//分類予想が外れた回数を保持する変数
			int misses = 0;
			for(int n:list){//データを1つずつ取り出して学習。
				//n癌目のデータの正解ラベルを取り出す。
				double t_n = t.getCell(n, 0);
				//n番目の特徴量を基底関数に入れる。
				Matrix feature = phi(x.getRow(n).toArray()[0]);
				//n番目のデータの所属ラベルを予測する
				int predict = sign(feature.mul(w).toArray()[0][0]);
				if((double)predict != t_n){//所属ラベルと正解ラベルが不一致だった時
					//重みの更新を行う。
					w = w.add((feature).scalar(t_n).t());
					//分類予想が外れたので+1する
					misses += 1;
				}
			}
			//分類予想が全て当たったら学習の終了
			if(misses == 0)
				break;
		}
	}
	
	/**
	 * 新しいデータに般化させる
	 * 新しい特徴量を入れた時のそのデータが正解に分類されたら1,不正解に分類されたら-1を返す。
	 * @param feature 特徴量
	 * @return 分類されたラベル
	 */
	public int fit(double[] feature){
		return sign(phi(feature).mul(w).toArray()[0][0]);
	}
	
	/**
	 * パーセプトロンによって求めた重みwをdoubleの1次元配列にて返す。
	 * @return 重みwの1次元配列
	 */
	public double[] getWeight(){
		return (w.t()).getRow(0).toArray()[0];
	}
}

