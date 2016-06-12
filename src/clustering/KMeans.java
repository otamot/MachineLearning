package clustering;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * @author YutaTomomatsu
 * @version 1.0 Mon June 13th,2016
 * 
 */
public class KMeans {
	
	//↓フィールド群
	/**
	 * k-meansのk。何個のクラスタに分割するか
	 */
	private int k;
	/**
	 * クラスタリングするベクトル群
	 */
	private double[][] vec;
	/**
	 * k個の重心ベクトル。
	 */
	private double[][] centroid;
	/**
	 * ベクトル群の所属クラスタを記憶する配列
	 */
	private int[] cluster; 
	/**
	 * 最大イテレーション回数。デフォルトは300
	 */
	private int max_itr;
	/**
	 * 初期値選択アルゴリズムがrandomであればtrue,k-means++であればfalseとなるフラグ
	 */
	private boolean rndFlg;

	/**
	 * k-meansの計算を終わっているかを判定するフラグ
	 */
	private boolean calculated;
	
	// 定数フィールド
	/**
	 * 初期値選択をランダムにしたい時にコンストラクタに渡す文字列
	 */
	public final static String INIT_RND = "random";
	/**
	 * 初期値選択をk-means++にしたい時にコンストラクタに渡す文字列
	 */
	public final static String INIT_K_MEANSPP = "k-means++";
	/**
	 * デフォルトの最大イテレーション回数を記憶する定数フィールド。
	 */
	private final static int DEFAULT_MAX_ITR = 300;
	/**
	 * デフォルトの初期値選択"k-means++"を記憶する定数フィールド。
	 */
	private final static String DEFAULT_INIT_ALGO = INIT_K_MEANSPP;

	
	//↓コンストラクタ群
	/**
	 * 主要コンストラクタ。どのコンストラクタを呼び出しても最終的にこのコンストラクタを呼び出している。
	 * コンストラクタではvecのコピーとkのコピー,初期値選択アルゴリズムの選択,フィールドの配列の初期化のみ行う。
	 * アルゴリズム的な初期化はinit関数内で行う。(初期クラスタの割当て)
	 * @param vec クラスタリングしたいベクトル群をdoubleの2重配列にいれたもの
	 * @param k k-meansのk。(1≦k≦vec.length)の範囲で指定。
	 * @param max_itr オプション。最大イテレーション回数。デフォルトは300
	 * @param initAlgo オプション。初期値選択アルゴリズム。"k-means++"or"random"で指定。デフォルトは"k-means++"
	 * @throws IllegalArgumentException 不正なパラメータを渡した時にエラーを吐く。
	 */
	public KMeans(double[][] vec, int k,int max_itr,String initAlgo){
		//vecのコピー
		if(vec.length == 0 || vec == null)
			throw new IllegalArgumentException("vecが空かnullです");
		this.vec = vec.clone();
		
		//kのコピー
		if(k < 0 || k > vec.length)
			throw new IllegalArgumentException("kの値指定が不正です");
		this.k = k;
		
		//最大イテレーションのコピー
		if(max_itr <= 0)
			throw new IllegalArgumentException("最大イテレーションの指定が正しくありません");
		this.max_itr = max_itr;
		
		//初期値選択アルゴリズムの選択。flgの値を書き換える。
		if(initAlgo.equals(INIT_RND))
			rndFlg = true;
		else if(initAlgo.equals(INIT_K_MEANSPP))
			rndFlg = false;
		else
			throw new IllegalArgumentException("初期値アルゴリズムの指定が正しくありません");
		
		//重心ベクトルを扱う変数の初期化。
		centroid = new double[k][vec[0].length];

		//calcメソッドを呼び出すとtrueになる。kmeansの計算を行っているかを判定するフラグ。
		calculated = false;
		
		//clusteringの結果を格納する配列。
		cluster = new int[vec.length];
	}
	
	/**
	 * 選択されていないパラメータはデフォルト値を入れて主要コンストラクタに投げる。
	 * @param vec クラスタリングしたいベクトル群をdoubleの2重配列にいれたもの
	 * @param k k-meansのk。(1≦k≦vec.length)の範囲で指定。
	 */
	public KMeans(double[][] vec, int k){
		this(vec,k,DEFAULT_MAX_ITR,DEFAULT_INIT_ALGO);
	}
	
	/**
	 * 選択されていないパラメータはデフォルト値を入れて主要コンストラクタに投げる。	 
	 * @param vec クラスタリングしたいベクトル群をdoubleの2重配列にいれたもの
	 * @param k k-meansのk。(1≦k≦vec.length)の範囲で指定。
	 * @param max_itr オプション。最大イテレーション回数。デフォルトは300
	 */
	public KMeans(double[][] vec, int k,int max_itr){
		this(vec,k,DEFAULT_MAX_ITR,DEFAULT_INIT_ALGO);
	}
	
	/**
	 * 選択されていないパラメータはデフォルト値を入れて主要コンストラクタに投げる。
	 * @param vec クラスタリングしたいベクトル群をdoubleの2重配列にいれたもの
	 * @param k k-meansのk。(1≦k≦vec.length)の範囲で指定。
	 * @param initAlgo オプション。初期値選択アルゴリズム。"k-means++"or"random"で指定。デフォルトは"k-means++"
	 */
	public KMeans(double[][] vec, int k,String initAlgo){
		this(vec,k,DEFAULT_MAX_ITR,initAlgo);
	}
	
	//publicなインスタンスメソッド
	
	/**
	 * kmeansの計算を行う。
	 * step0 ランダムに所属クラスタを決める
	 * step1 クラスタの重心ベクトルを求める
	 * step2 所属ベクトルを決める
	 * step3 if(クラスタに変化がない) アルゴリズムの収束 else step1へ
	 */
	public void calc(){
		//step0 ランダムに所属クラスタを決める
		init();
		int iteration=0;
		while(true){
			//あとでクラスタに変化があるか確かめるために計算前の所属クラスタを退避する。
			int[] clusterPre = cluster.clone();
			
			
			//step1 クラスタの重心ベクトルを求める
			calcCentroid();
			
			
			//step2 所属ベクトルを決める
			calcBelongCluster();
			
			
			//step3 if(クラスタに変化がない) アルゴリズムの収束 else step1へ
			if(isSameArray(cluster,clusterPre)||++iteration==max_itr){
				calculated = true;
				return;
			}
			else{
				continue;//必要ないが、step1に戻ることを明示するために記述。
			}
		}
	}
	
	/**
	 * k-meansのメタ情報を標準出力する。
	 */
	public void printKMeansInfo(){
		System.out.println("手法:k-means");
		System.out.println("初期値選択アルゴリズム:" + ((rndFlg)?INIT_RND:INIT_K_MEANSPP));
		System.out.println("最大イテレーション:" + max_itr);
		
	}
	
	
	/**
	 * k-means計算後の各クラスタの重心ベクトルを返す。
	 * calc関数実行後に呼び出す。
	 * @return k-meansによって分けられた各クラスタの重心ベクトルをdoubleの2次元配列で返す。
	 * @throws UnsupportedOperationException calcメソッドを呼び出す前にこのメソッドを実行するとエラーを吐く
	 */
	public double[][] getCentroid(){
		if(!calculated){
			throw new UnsupportedOperationException("kmeansの計算を行っていません");
		}
		return centroid;
	}
	
	/**
	 * k-means計算後の各ベクトルの所属クラスタを返す。
	 * calc関数実行後に呼び出す。
	 * @return 各ベクトルの所属クラスタをintの1次元配列で返す。
	 * @throws UnsupportedOperationException calcメソッドを呼び出す前にこのメソッドを実行するとエラーを吐く
	 */
	public int[] getCluster(){
		if(!calculated){
			throw new UnsupportedOperationException("kmeansの計算を行っていません。");
		}
		return cluster;
	}

	
	
	//以下privateメソッド群。
	//クラス内からしか参照できない関数。
	
	
	/**
	 * 初期値選択をrndFlgにもとづいて実行する。
	 * rndFlg = trueならばRandomに初期値を選ぶ。
	 * rndFlg = falseならばk-means++で初期値を選ぶ。
	 */
	private void init(){
		if(rndFlg)
			initRandom();
		else
			initKMeansPP();
	}
	
	
	/** 
	 * ランダム割り当てによって初期値選択を行う。
	 */
	private void initRandom(){
		Random rnd = new Random();
		for(int i = 0; i < cluster.length; i++){
			cluster[i] = rnd.nextInt(k);
		}
	}
	
	/**
	 * k-means++法にて初期値選択を行う。
	 */
	private void initKMeansPP(){
		Random rnd = new Random();
		Set<Integer> set = new HashSet<>();
		Set<Integer> centroidSet = new HashSet<>();
		for(int i = 0; i < cluster.length; i++){
			set.add(i);
		}

		int clusterId = rnd.nextInt(cluster.length);
		set.remove(clusterId);
		centroidSet.add(clusterId);
		
		for(int i = 1;i < k; i++){

			double max_dis = -1;
			int max_index = -1;
			for(int j:set){
				
				double min_dis = 10000;
				int min_index = -1;
				
				for(int cent:centroidSet){
					double dis = distance(vec[j],vec[cent]);
					if(min_dis > dis){
						min_dis = dis;
						min_index = j;
					}
				}
				if(max_dis < min_dis){
					max_index = min_index;
					max_dis = min_dis;
				}
				
			}
			centroidSet.add(max_index);
			set.remove(max_index);
		}
		int i = 0;
		for(int cent: centroidSet){
			for(int j = 0; j < centroid[i].length; j++){
				centroid[i][j] = vec[cent][j];
			}
			i++;
		}
		calcBelongCluster();
	}
	
	
	
	
	/** 
	 * 各ベクトルから最も近い重心ベクトルを計算し、新たな所属クラスタとする。
	 */
	private void calcBelongCluster(){
		for(int i = 0; i < cluster.length; i++){
			double minSim = Integer.MAX_VALUE;
			int maxC = -1;
			for(int j = 0; j < k; j++){
				double sim = distance(centroid[j],vec[i]);
				if(minSim > sim){
					minSim = sim;
					maxC = j;
				}
			}
			cluster[i] = maxC;
		}
	}
	
	/**
	 * 各クラスタに所属するベクトルの重心を計算して代表ベクトルとする。
	 */
	private void calcCentroid(){
		double[][] newCentroid = new double[k][vec[0].length];//新たな代表ベクトルの値を一時的に対比しておく配列
		int[] count = new int[k];//各クラスタにいくつのベクトルが割り当てられているか計算する

		/*各クラスタに属するベクトルの和を求める*/
		for(int i = 0; i < vec.length; i++){
			int clusterId = cluster[i];//i番目のベクトルのクラスタidを取り出す
			count[clusterId]++;
			for(int j = 0 ; j < vec[i].length; j++){
				newCentroid[clusterId][j] += vec[i][j];
			}
		}
		
		//各クラスタのベクトル和をベクトルの数で割り、重心を取る。
		for(int i = 0; i < centroid.length; i++){
			for(int j = 0; j < centroid[i].length; j++){
				centroid[i][j] = newCentroid[i][j]/count[i];
			}
		}
	}
	
	/**
	 * aとbが同じ値を持つ配列かどうかを返す
	 * @param a int型の配列
	 * @param b int型の配列
	 * @return aとbが同じ値を持つ配列ならtrue,そうでなけらばelseを返す
	 */
	private boolean isSameArray(int[] a,int[] b){
		if(a.length!=b.length)
			return false;
		for(int i = 0; i < a.length; i++)
			if(b[i] != a[i])
				return false;
		return true;
	}
	

	
	/**
	 * ベクトルp,qのユークリッド距離を計算して返す関数。
	 * @param p doubleのベクトル
	 * @param q doubleのベクトル
	 * @return ベクトルpとベクトルqの距離
	 */
	private double distance(double[]p, double[] q){
		double distance = 0;
		for(int i = 0; i < p.length; i++){
			distance += Math.pow(p[i]-q[i],2);
		}
		return Math.sqrt(distance);
	}
}
