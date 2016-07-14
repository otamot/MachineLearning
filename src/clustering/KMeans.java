package clustering;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
	 * 初期値選択アルゴリズムがrandomであれば0,kkzであれば1,k-means++であれば2となるフラグ
	 */
	private int initFlg;

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
	 * 初期値選択をKKZにしたい時にコンストラクタに渡す文字列
	 */
	public final static String INIT_KKZ = "kkz";
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
	 * @param initAlgo オプション。初期値選択アルゴリズム。"k-means++" or "kkz" or "random"で指定。デフォルトは"k-means++"
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
			initFlg = 0;
		else if(initAlgo.equals(INIT_KKZ))
			initFlg = 1;
		else if(initAlgo.equals(INIT_K_MEANSPP))
			initFlg = 2;
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
		System.out.println("初期値選択アルゴリズム:" + ((initFlg==0)?INIT_RND:(initFlg==1)?INIT_KKZ:INIT_K_MEANSPP));
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
	 * rndFlg == 0ならばRandomに初期値を選ぶ。
	 * rndFlg == 1ならばkkzで初期値を選ぶ。
	 * rndFlg == 2ならばk-means++で初期値を選ぶ。
	 */
	private void init(){
		if(initFlg==0)
			initRandom();
		else if(initFlg==1)
			initKKZ();
		else if(initFlg == 2)
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
	 * KKZ法にて初期値選択を行う。
	 * Step0:ランダムに1つベクトルを選び代表ベクトルとする
	 * Step1:代表ベクトルの数がk個になったら終了
	 * Step2:それぞれのベクトル𝑥に関して、最も近い代表ベクトルの距離を求める。
	 * Step3:Step2で求めた距離が最大になるベクトルを新たな代表ベクトルとして選択。Step1へ
	 */
	private void initKKZ(){
		Random rnd = new Random();
		Set<Integer> nonSelectedVecSet = new HashSet<>();
		Set<Integer> centroidSet = new HashSet<>();
		
		//最初は代表ベクトルとして何も選ばれていないので,0からベクトルの数をsetに追加
		for(int i = 0; i < cluster.length; i++){
			nonSelectedVecSet.add(i);//
		}

		//Step0:ランダムに1つベクトルを選び代表ベクトルとする
		int clusterId = rnd.nextInt(cluster.length);
		centroidSet.add(clusterId);//選ばれたベクトルを代表ベクトルSetに追加
		nonSelectedVecSet.remove(clusterId);//選ばれたベクトルを選ばれてないSetから除外
		
		//Step1:代表ベクトルの数がk個になったら終了
		for(int i = 1;i < k; i++){

			double max_dis = -1;//各ベクトルの中で一番近い代表ベクトルの距離が最大となる距離を記憶する変数
			int max_index = -1;//各ベクトルの中で一番近い代表ベクトルの距離が最大となるベクトルのインデックスを記憶する変数
			for(int j:nonSelectedVecSet){
				
				double min_dis = 10000;//注目しているベクトルjから一番近い代表ベクトルまでの距離を記憶する変数
				int min_index = -1;//注目しているベクトルjから一番近い代表ベクトルのインデックスを記憶する変数
				
				for(int cent:centroidSet){
					//Step2:それぞれのベクトル𝑥に関して、最も近い代表ベクトルの距離を求める。
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
			//Step3:Step2で求めた距離が最大になるベクトルを新たな代表ベクトルとして選択。Step1へ
			centroidSet.add(max_index);
			nonSelectedVecSet.remove(max_index);
		}
		int i = 0;
		//選んだ代表ベクトルをフィールドのcentroid変数にコピー
		for(int cent: centroidSet){
			for(int j = 0; j < centroid[i].length; j++){
				centroid[i][j] = vec[cent][j];
			}
			i++;
		}
		calcBelongCluster();
	}
	
	/**
	 * k-means++法にて初期値選択を行う。
	 * Step0:ランダムに1つベクトルを選び代表ベクトルとする
	 * Step1:代表ベクトルの数がk個になったら終了
	 * Step2:それぞれのベクトル𝑥に関して、そのベクトルに一番近い代表ベクトルとの距離𝐷(𝑋)を求める。
	 * Step3:各ベクトルxに関して重み付き確率分布𝜙(x_𝑖)=𝐷(x_𝑖)/∑_𝑘𝐷(𝑥_𝑘)を用いて新たな代表ベクトルをランダムに選ぶ。Step1へ
	 */
	private void initKMeansPP(){
		Random rnd = new Random();
		Set<Integer> nonSelectedVecSet = new HashSet<>();
		Set<Integer> centroidSet = new HashSet<>();
		
		//最初は代表ベクトルとして何も選ばれていないので,0からベクトルの数をsetに追加
		for(int i = 0; i < cluster.length; i++){
			nonSelectedVecSet.add(i);//
		}

		//Step0:ランダムに1つベクトルを選び代表ベクトルとする
		int clusterId = rnd.nextInt(cluster.length);
		centroidSet.add(clusterId);//選ばれたベクトルを代表ベクトルSetに追加
		nonSelectedVecSet.remove(clusterId);//選ばれたベクトルを選ばれてないSetから除外
		
		//Step1:代表ベクトルの数がk個になったら終了
		for(int i = 1;i < k; i++){
			double sumXk = 0;
			Map<Integer,Double> distMap = new HashMap<>();
			for(int j:nonSelectedVecSet){
				
				double min_dis = 10000;//注目しているベクトルjから一番近い代表ベクトルまでの距離を記憶する変数
				int min_index = -1;//注目しているベクトルjから一番近い代表ベクトルのインデックスを記憶する変数
				
				for(int cent:centroidSet){
					//Step2:それぞれのベクトル𝑥に関して、最も近い代表ベクトルの距離を求める。
					double dis = distance(vec[j],vec[cent]);
					if(min_dis > dis){
						min_dis = dis;
						min_index = j;
					}
				}
				sumXk += min_dis;
				distMap.put(min_index, min_dis);
			}
			//Step3:各ベクトルxに関して重み付き確率分布𝜙(x_𝑖)=𝐷(x_𝑖)/∑_𝑘𝐷(𝑥_𝑘)を用いて新たな代表ベクトルをランダムに選ぶ。Step1へ
			int selectedVecIndex = selectVec(distMap,sumXk);
			centroidSet.add(selectedVecIndex);
			nonSelectedVecSet.remove(selectedVecIndex);
		}
		int i = 0;
		//選んだ代表ベクトルをフィールドのcentroid変数にコピー
		for(int cent: centroidSet){
			for(int j = 0; j < centroid[i].length; j++){
				centroid[i][j] = vec[cent][j];
			}
			i++;
		}
		calcBelongCluster();
	}
	
	/**
	 * k-means++のstep3で呼び出されるメソッド。
	 * @param distMap 各ベクトルのインデックスをkeyとして、そのベクトルから一番近い代表ベクトルまでの距離がvalueとして格納
	 * @param sumXk distMapのvalueの和。
	 * @return 確率的に選ばれたベクトルのインデックス
	 */
	private int selectVec(Map<Integer,Double> distMap, Double sumXk){
		Random rnd = new Random();
		double randomValue = rnd.nextDouble()*sumXk;
		
		double nowSum = 0;
		for(int index:distMap.keySet()){
			nowSum += distMap.get(index);
			if(randomValue < nowSum)
				return index;
		}
		return -1;
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
