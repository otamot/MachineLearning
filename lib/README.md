# 4. 機械学習演算用ライブラリ
* 機械学習の計算時に必要と思われる計算をライブラリ化した。

## 4.1 Matrix.Javadoc
* 行列演算に関わるデータ構造,計算をライブラリ化したもの

### Javadoc
[コチラ](https://htmlpreview.github.io/?https://raw.githubusercontent.com/otamot/MachineLearning/master/doc/lib/Matrix.html)

### ソースコード

```Java
package lib;

/**
 * @author otamot
 * @version 1.0
 * @since 2016/07/20
 * 行列演算をまとめたライブラリ
 * Matrixはの要素はカプセル化されており一度Newしたら要素を変更することは不可能。(Setterを持たない。)
 */
public class Matrix {
	/**
	 * Matrixの中身。
	 * 内部データ構造として2次元配列で保持する。
	 */
	private double[][] element;
	/**
	 * 行列の行数を保持する変数
	 */
	private int rowLen;
	/**
	 * 行列の列数を保持する変数
	 */
	private int colLen;


	///////////getter群////////////////
	/**
	 * Matrixのi行目,j列目の要素を取り出す
	 * @param i 行数
	 * @param j 列数
	 * @return Matrixのi行目,j列目の要素
	 */
	public double getCell(int i,int j){
		return element[i][j];
	}
	/**
	 * Matrixの行数を返す
	 * @return Matrixの行数
	 */
	public int row_n(){
		return rowLen;
	}
	/**
	 * Matrixの列数を返す
	 * @return Matrixの列数
	 */
	public int col_n(){
		return colLen;
	}

	/**
	 * Matrix型から2次元配列への変換
	 * @return Matrixから変換した2次元配列
	 */
	public double[][] toArray(){
		return element.clone();
	}

	/**
	 * 指定した行Matrixを抜き出す。
	 * @param row_i 抜き出したい行番号
	 * @return Matrix型の行ベクトル
	 */
	public Matrix getRow(int row_i){
		double[][] r = new double[1][colLen];
		r[0] = element[row_i].clone();
		return new Matrix(r);
	}

	/**
	 * 指定した列Matrixを抜き出す。
	 * @param col_j 抜き出したい列番号
	 * @return Matrx型の列ベクトル
	 */
	public Matrix getCol(int col_j){
		double[][] c = new double[1][rowLen];
		c[0] = this.t().toArray()[col_j];
		return new Matrix(c).t();
	}


	////////////コンストラクタ群////////////////
	/**
	 * コンストラクタ。
	 * rowLen*colLenのサイズのゼロ行列を生成する。
	 * @param rowLen 行数
	 * @param colLen 列数
	 */
	public Matrix(int rowLen,int colLen){
		element = new double[rowLen][colLen];
		this.rowLen = rowLen;
		this.colLen = colLen;
	}
	/**
	 * コンストラクタ。
	 * 2次元配列で指定したMatrixを生成する。
	 * @param element 行列化するdoubleの2次元配列
	 */
	public Matrix(double[][] element){
		this.element = element.clone();
		this.rowLen = element.length;
		this.colLen = element[0].length;
	}
	/**
	 * n次の単位行列を生成する。
	 * @param n 次元数
	 * @return n次の単位行列
	 */
	public static Matrix getE(int n){
		double[][] m = new double[n][n];
		for(int i = 0; i < n; i++){
			m[i][i] = 1;
		}
		return new Matrix(m);
	}

	//////////出力部/////////////
	/**
	 * 行列の中身を標準出力する。
	 */
	public void printMatrix(){
		for(double[] row: element){
			for(double cell: row){
				System.out.printf("%8.4f",cell);
			}
			System.out.println();
		}
	}


	//////////////以下全て計算部////////////////

	//////////////和差積////////////////
	/**
	 * 行列の和を計算
	 * @param b 足すMatrix
	 * @return 自身のMatrixにbを足したMatrixを返す。
	 */
	public Matrix add(Matrix b){
		double m[][] = new double[row_n()][col_n()];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < col_n(); j++){
				m[i][j] = this.getCell(i,j)+b.getCell(i, j);
			}
		}
		return new Matrix(m);
	}

	/**
	 * 行列の差を計算
	 * @param b 引くMatrix
	 * @return 自身のMatrixからbを引いたMatrixを返す。
	 */
	public Matrix sub(Matrix b){
		return this.add(b.scalar(-1));
	}


	/**
	 * 行列を定数倍する
	 * @param x 定数
	 * @return 定数倍した行列を返す
	 */
	public Matrix scalar(double x){
		double[][] m = new double[row_n()][col_n()];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < col_n(); j++){
				m[i][j] = x * getCell(i,j);
			}
		}
		return new Matrix(m);
	}

	/**
	 * 行列の乗算を行う
	 * @param b かける行列
	 * @return 乗算した結果のMatrixを返す
	 */
	public Matrix mul(Matrix b){
		if(col_n() != b.row_n()){
			System.err.println("error");
			System.exit(1);
		}
		double[][] m = new double[row_n()][b.col_n()];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < b.col_n(); j++){
				for(int k = 0; k < col_n(); k++){
					m[i][j] += getCell(i,k)*b.getCell(k, j);
				}
			}
		}
		return new Matrix(m);
	}

	//////////////転置/////////////////
	/**
	 * 行列の転置を行う。
	 * @return 転置した行列を返す
	 */
	public Matrix t(){
		double[][] m = new double[col_n()][row_n()];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < col_n(); j++){
				m[j][i] = getCell(i,j);
			}
		}
		return new Matrix(m);
	}

	/////////////行列式/////////////////////
	/**
	 * 行列式を計算する
	 * @return 行列式を返す
	 */
	public double det(){
		double m[][] = element.clone();
	    final int size = m[0].length;
	    for(int i=0; i< m.length; i++){
	      if(m[i].length!=size)
	        throw new IllegalArgumentException();
	    }
	    double det = 0;
	    for(int i=0; i<size; i++){
	      double rightdown = 1;
	      double leftdown = 1;
	      for(int j=0; j<size; j++){
	        rightdown *= m[(i+j)%size][j%size];
	        leftdown  *= m[(i+size-j)%size][j%size];
	      }
	      det += rightdown - leftdown;
	    }
	    return det;
	  }

	////////////////逆行列//////////////////////
	/**
	 * 逆行列を計算する
	 * @return 逆行列を返す
	 */
	public Matrix inv(){
		double[][] m = new double[row_n()][col_n()*2];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < col_n(); j++){
				m[i][j] =getCell(i,j);
				if(i==j){
					m[i][col_n() + j] = 1;
				}
			}
		}
		for(int k=0; k<row_n(); k++){
			pivot(k,m,row_n());
			sweep(k,m,row_n());
		}
		double[][] m_ans = new double[row_n()][row_n()];
		for(int i = 0; i < row_n(); i++){
			for(int j = 0; j < col_n(); j++){
				m_ans[i][j] = m[i][j+ row_n()];
			}
		}
		return new Matrix(m_ans);
	}

	/**
	 * pivotは、消去演算を行う前に、対象となる行を基準とし、それ以降の
	 * 行の中から枢軸要素の絶対値が最大となる行を見つけ出し、対象の行と
	 * その行とを入れ替えることを行う関数である。
	 * @param k
	 * @param a
	 * @param N
	 */
	private void pivot(int k,double a[][],int N){
		double max,copy;
		//ipは絶対値最大となるk列の要素の存在する行を示す変数で、
		//とりあえずk行とする
		int ip=k;
		//k列の要素のうち絶対値最大のものを示す変数maxの値をとりあえず
		//max=|a[k][k]|とする
		max=Math.abs(a[k][k]);
		//k+1行以降、最後の行まで、|a[i][k]|の最大値とそれが存在する行を
		//調べる
		for(int i=k+1; i<N; i++){       
			if(max<Math.abs(a[i][k])){
				ip=i;
				max=Math.abs(a[i][k]);
			}
		}
		if(ip!=k){
			for(int j=0; j<2*N; j++){
				//入れ替え作業
				copy    =a[ip][j];
				a[ip][j]=a[k][j];
				a[k][j] =copy;
			}
		}
	}
	/**
	 * ガウス・ジョルダン法により、消去演算を行う
	 * @param k
	 * @param a
	 * @param N
	 */
	private void sweep(int k,double a[][],int N){
		double piv,mmm;
		//枢軸要素をpivとおく
		piv=a[k][k];
		//k行の要素をすべてpivで割る a[k][k]=1となる
		for(int j=0; j<2*N; j++)
			a[k][j]=a[k][j]/piv;
		//    
		for(int i=0; i<N; i++){
			mmm=a[i][k];
			//a[k][k]=1で、それ以外のk列要素は0となる
			//k行以外
			if(i!=k){
				//i行において、k列から2N-1列まで行う        
				for(int j=k; j<2*N; j++)
					a[i][j]=a[i][j]-mmm*a[k][j];
			}
		}
	}
}



```
