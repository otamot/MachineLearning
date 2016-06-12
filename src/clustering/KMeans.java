package clustering;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 
 * @author YutaTomomatsu
 * 
 */
public class KMeans {
	
	//↓フィールド群
	private int k; //k-meansのk。何個のクラスタに分割するか
	private double[][] vec;//クラスタリングするベクトル群
	private double[][] centroid;// k個の重心ベクトル。
	private int[] cluster; //ベクトル群の所属クラスタを記憶する配列
	private int max_itr;
	private boolean rndFlg;

	private boolean calculated;//k-meansの計算を終わっているかを判定するフラグ
	
	public final static String INIT_RND = "random";
	public final static String INIT_K_MEANSPP = "k-means++";

	
	//↓コンストラクタ群
	/**
	 * コンストラクタではvecのコピーとkの初期化,フィールドの配列の初期化のみ行う。
	 * アルゴリズム的な初期化はinit関数内で行う。(初期クラスタの割当て)
	 * 
	 * @param vec クラスタリングしたいベクトル群をdoubleの2重配列にいれたもの
	 * @param k k-meansのk。
	 * @param max_itr オプション。最大イテレーション回数。デフォルトは300
	 * @param initAlgo オプション。初期値選択アルゴリズム。"k-means++"or"random"で指定。デフォルトは"k-means++"
	 */
	public KMeans(double[][] vec, int k,int max_itr,String initAlgo){
		this.vec = new double[vec.length][vec[0].length];
		for(int i = 0; i < vec.length; i++){
			for(int j = 0; j < vec[i].length;j++){
				this.vec[i][j] = vec[i][j];
			}
		}
		this.k = k;
		
		centroid = new double[k][vec[0].length];
		calculated = false;// kmeansの計算を行っているかを判定するフラグ
		cluster = new int[vec.length];//各ベクトルの所属ベクトルを
		if(max_itr <= 0){
			throw new IllegalArgumentException("最大イテレーションの指定が正しくありません");
		}
		this.max_itr = max_itr;
		if(initAlgo.equals(INIT_RND))
			rndFlg = true;
		else if(initAlgo.equals(INIT_K_MEANSPP))
			rndFlg = false;
		else
			throw new IllegalArgumentException("初期値アルゴリズムの指定が正しくありません");
	}
	
	public KMeans(double[][] vec, int k){
		this(vec,k,300,INIT_K_MEANSPP);
	}
	
	public KMeans(double[][] vec, int k,int max_itr){
		this(vec,k,max_itr,INIT_K_MEANSPP);
	}
	
	public KMeans(double[][] vec, int k,String initAlgo){
		this(vec,k,300,initAlgo);
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
			int[] clusterPre = new int[vec.length];
			copyArray(cluster,clusterPre);
			
			
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
				continue;
			}
		}
	}
	
	public void printKMeansInfo(){
		System.out.println("手法:k-means");
		System.out.println("初期値選択アルゴリズム:" + ((rndFlg)?INIT_RND:INIT_K_MEANSPP));
		System.out.println("最大イテレーション:" + ((max_itr > 0)?max_itr:"収束するまで"));
		
	}
	
	
	/**
	 * k-means計算後の各クラスタの重心ベクトルを返す。
	 * calc関数実行後に呼び出す。
	 * @return k-meansによって分けられた各クラスタの重心ベクトル
	 * @throws Exception calc関数を実行する前にこの関数を呼び出すとエラーを吐く。
	 */
	public double[][] getCentroid() throws Exception{
		if(!calculated){
			throw new Exception("kmeansの計算を行っていません");
		}
		return centroid;
	}
	
	/**
	 * k-means計算後の各ベクトルの所属クラスタを返す。
	 * calc関数実行後に呼び出す。
	 * @return
	 * @throws Exception calc関数を実行する前に関数を呼び出すとエラーを吐く。
	 */
	public int[] getCluster() throws Exception{
		if(!calculated){
			throw new Exception("kmeansの計算を行っていません。");
		}
		return cluster;
	}

	
	
	//以下privateメソッド群。
	//クラス内からしか参照できない関数。
	
	private void init(){
		if(rndFlg)
			initRandom();
		else
			initKMeansPP();
	}
	
	
	/**最初に所属するクラスタをランダムで割り当てる。
	 * 
	 */
	
	private void initRandom(){
		Random rnd = new Random();
		for(int i = 0; i < cluster.length; i++){
			cluster[i] = rnd.nextInt(k);
		}
	}
	
	
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
	 *
	 * 各ベクトルから最も近い重心ベクトルを計算し、新たな所属クラスタとする。
	 */
	private void calcBelongCluster(){
		for(int i = 0; i < cluster.length; i++){
			double max_sim = Integer.MAX_VALUE;
			int max_c = -1;
			for(int j = 0; j < k; j++){
				double sim = distance(centroid[j],vec[i]);
				if(sim < max_sim){
					max_sim = sim;
					max_c = j;
				}
			}
			cluster[i] = max_c;
		}
	}
	
	/**
	 * 重心を計算する
	 * 
	 */
	private void calcCentroid(){
		double[][] newCentroid = new double[k][vec[0].length];
		int[] count = new int[k];
		for(int i = 0; i < vec.length; i++){
			int c = cluster[i];
			count[c]++;
			for(int j = 0 ; j < vec[i].length; j++){
				newCentroid[c][j] += vec[i][j];
			}
		}
		for(int i = 0; i < centroid.length; i++){
			for(int j = 0; j < centroid[i].length; j++){
				centroid[i][j] = newCentroid[i][j]/count[i];
			}
		}
	}
	
	//aとbが同じベクトルかを計算する
	private boolean isSameArray(int[] a,int[] b){
		for(int i = 0; i < a.length; i++)
			if(b[i] != a[i])
				return false;
		return true;
	}
	
	//配列aをbにコピーする。
	private void copyArray(int[] a, int[] b){
		for(int i = 0; i < a.length; i++){
			b[i] = a[i];	
		}
	}
	
	/**
	 * ベクトルp,qの類似度を計算して返す関数。
	 * @param p
	 * @param q
	 * @return
	 */
	
	private double distance(double[]p, double[] q){
		double distance = 0;
		for(int i = 0; i < p.length; i++){
			distance += Math.pow(p[i]-q[i],2);
		}
		return Math.sqrt(distance);
	}
}
