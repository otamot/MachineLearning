# パーセプトロン
## Javadoc
[コチラ](https://htmlpreview.github.io/?https://raw.githubusercontent.com/otamot/MachineLearning/master/doc/classification/Perceptron.html)


## ソースコード
### [Perceptron.java](https://github.com/otamot/MachineLearning/blob/master/src/classification/Perceptron.java)
```java
package classification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import lib.Matrix;


/**
 * パーセプトロンによる分類を行う
 * @author YutaTomomatsu
 * @since 2016/07/27
 * @version 1.0
 */
public class Perceptron {
	//↓フィールド群
	//内部のデータ構造は全てカプセル化されていて外部から変更されることはない。
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

	//↓コンストラクタ
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

	//↓学習を行うメソッド
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

	//汎化を行うためのメソッド群
	//calメソッドを呼び出したあとに実行しないと正しい挙動をしない。
	/**
	 * 新しいデータに般化させる
	 * 新しい特徴量を入れた時のそのデータが正解に分類されたら1,不正解に分類されたら-1を返す。
	 * calメソッド実行後に呼び出す。
	 * @param feature 特徴量
	 * @return 分類されたラベル
	 */
	public int fit(double[] feature){
		return sign(phi(feature).mul(w).toArray()[0][0]);
	}

	/**
	 * パーセプトロンによって求めた重みwをdoubleの1次元配列にて返す。
	 * calメソッド実行後に呼び出す。
	 * @return 重みwの1次元配列
	 */
	public double[] getWeight(){
		return (w.t()).getRow(0).toArray()[0];
	}


	//↓privateメソッド群
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


}


```

### [PerceptronMain.java](https://github.com/otamot/MachineLearning/blob/master/src/classification/PerceptronMain.java)

* Perceptron.javaの確認用クラス。

```java
package classification;

import java.util.Random;

//Perceptronクラスの実行確認用のクラス
public class PerceptronMain {


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
			t[i][0] = (h(x[i][0], x[i][1]) > 0)?1:-1;
		}

		Perceptron p = new Perceptron(x,t);
		p.cal();

		double[][] feature = {{2,3},{5.0/8.0,3.0/8.0},{0.0,0.0}};
		System.out.println(p.fit(feature[0]));
		System.out.println(p.fit(feature[1]));
		System.out.println(p.fit(feature[2]));

	}

}
```
