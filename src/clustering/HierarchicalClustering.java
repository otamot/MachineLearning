package clustering;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 階層型クラスタリングを行うクラス。
 * @author tomo_otamot
 * @version 0.0
 * @since 2016/06/22
 *
 */
public class HierarchicalClustering {
	private String[] method_arr = {"nearest_neighbor","furthest_neighbor","group_average","Ward’s","centroid","weighted_average","median"};
	private int method_flg; //クラスタ間類似度の測り方を記憶するフラグ
	private double vec[][];//クラスタリングするベクトルのコピー。
	private List<List<List<Integer>>> cluster; 
	
	/**
	 * コンストラクタ。
	 * @param vec クラスタリングしたいベクトル群
	 * @param method  {"nearest_neighbor","furthest_neighbor","group_average","Ward’s","centroid","weighted_average","median"}の中から選択。
	 */
	HierarchicalClustering(double[][] vec,String method){
		this.vec = vec.clone();
		for(int i = 0; i < method_arr.length; i++){
			method_flg = -1;
			if(method.equals(method_arr[i]))
				method_flg = i;
		}
		if(method_flg == -1){
			throw new IllegalArgumentException("methodの指定方法が正しくありません。");
		}
		cluster = new ArrayList<>();
		List<List<Integer>> firstCluster = new ArrayList<>();
		for(int i = 0; i < vec.length; i++){
			List<Integer> list = new ArrayList<>();
			list.add(i);
			firstCluster.add(list);
		}
		cluster.add(firstCluster);
	}
	
	/**
	 * 階層型クラスタリングの計算を行う関数。
	 * 
	 */
	public void calc(){
		while(cluster.size() != vec.length){
			List<List<Integer>> nowHeightCluster = cluster.get(cluster.size()-1);
			double minDist = 100000;
			int minI=0,minJ=0;
			for(int i = 0; i < nowHeightCluster.size();i++){
				for(int j = 0; j < nowHeightCluster.size();j++){
					if(i==j)
						continue;
					double dist = dist(nowHeightCluster.get(i),nowHeightCluster.get(j));
					if(dist>minDist){
						minDist=dist;
						minI = i;
						minJ = j;
					}
				}
			}
			List<Integer> ketsugouCluster = new ArrayList<>();
			List<List<Integer>> newHeightCluster = new ArrayList<>();
			for(int i = 0; i < nowHeightCluster.size();i++){
				if(i==minI||i==minJ){
					nowHeightCluster.get(i).forEach(j->ketsugouCluster.add(j));
				}
				else{
					newHeightCluster.add(nowHeightCluster.get(i));
				}
			}
			newHeightCluster.add(ketsugouCluster);
			cluster.add(newHeightCluster);
		}
	}
	
	private int methodFlg = 0;
	/**
	 * calcメソッドから呼び出される関数。
	 * ここで計算を行うのではなく、newするときに選んだクラスタ間距離の測り方に基づいて、各メソッドに投げる。
	 * @param a ベクトル番号が格納されたクラスタを表すList
	 * @param b ベクトル番号が格納されたクラスタを表すList
	 * @return 選ばれたメソッドで計算したクラスタ間類似度。
	 */
	double dist(List<Integer> a,List<Integer> b){
		switch(methodFlg){
		case 0:
			return distNearestNeighbor(a,b);
		case 1:
			return distFurthestNeighbor(a,b);
		case 2:
			return distGroupAverage(a,b);
		case 3:
			return distWords(a,b);
		case 4:
			return distCentroid(a,b);
		case 5:
			return distWeightedAverage(a,b);
		case 6:
			return distMedian(a,b);
		default:
			throw new RuntimeException("");
		}
	}
	
	/**
	 * 単連結法による実装。
	 * クラスタ同士の比較の際に、両クラスタに所属するベクトルの最も近いベクトル同士の距離をクラスタ缶の距離とする方法。
	 * @param a
	 * @param b
	 * @return
	 */
	double distNearestNeighbor(List<Integer> a,List<Integer> b){
		double dist = 1000000;
		for(int i = 0; i < a.size();i++){
			for(int j = 0; j < b.size(); j++){
				double thisDist = euclidian(vec[i],vec[j]);
				if(dist < thisDist){
					dist = thisDist;
				}
			}
		}
		return dist;
	}
	
	/**
	 * 完全連結法による実装。
	 * クラスタ同士の比較の際に、両クラスタに所属するベクトルの最も遠いベクトル同士の距離をクラスタ缶の距離とする方法。
	 * @param a
	 * @param b
	 * @return
	 */
	double distFurthestNeighbor(List<Integer> a,List<Integer> b){
		double dist = -1;
		for(int i = 0; i < a.size();i++){
			for(int j = 0; j < b.size(); j++){
				double thisDist = euclidian(vec[i],vec[j]);
				if(dist > thisDist){
					dist = thisDist;
				}
			}
		}
		return dist;
	}
	
	/**
	 * 群平均法による実装。
	 * クラスタ同士の比較の際に、両クラスタ間に所属する全てのベクトル同士の距離の平均をクラスタ間の距離とする方法。
	 * @param a
	 * @param b
	 * @return
	 */
	double distGroupAverage(List<Integer> a,List<Integer> b){
		double dist = 0;
		for(int i = 0; i < a.size();i++){
			for(int j = 0; j < b.size(); j++){
				dist += euclidian(vec[i],vec[j]);
			}
		}
		return dist/(a.size()*b.size());
	}
	
	double distWords(List<Integer> a,List<Integer> b){
		double dist = 0;
		
		return dist;
	}
	
	/**
	 * 重心法による実装。
	 * クラスタ同士の比較の際に、両クラスタに所属するベクトルの重心どうしの距離をクラスタ間の距離とする方法
	 * @param a
	 * @param b
	 * @return
	 */
	double distCentroid(List<Integer> a,List<Integer> b){
		double[] centroidA = new double[vec[0].length];
		double[] centroidB = new double[vec[0].length];
		for(int i:a){
			for(int j = 0; j < vec[i].length; j++){
				centroidA[j] += vec[i][j];
			}
		}
		for(int i:b){
			for(int j = 0; j < vec[i].length; j++){
				centroidB[j] += vec[i][j];
			}
		}
		for(int j = 0; j < vec[0].length; j++){
			centroidA[j]/=a.size();
			centroidB[j]/=b.size();
		}
		return euclidian(centroidA,centroidB);
	}
	
	private double distWeightedAverage(List<Integer> a,List<Integer> b){
		double dist = 0;
		
		return dist;
	}
	
	
	/**
	 * median法による実装。
	 * クラスタ同士の距離の比較の際に、両クラスタに属す全てのベクトル同士の距離の中央値をクラスタ間の距離とする方法。
	 * @param a
	 * @param b
	 * @return
	 */
	private double distMedian(List<Integer> a,List<Integer> b){
		double dist[] = new double[a.size()*b.size()];
		int k = 0;
		for(int i:a){
			for(int j:b){
				dist[k++] = euclidian(vec[i],vec[j]);
			}
		}
		Arrays.sort(dist);
		return (dist.length%2==0)?(dist[dist.length/2-1]+dist[dist.length/2])/2:dist[dist.length/2];
	}
	

	
	
	
	/**
	 * ベクトルp,qのユークリッド距離を計算して返す関数。
	 * @param p doubleのベクトル
	 * @param q doubleのベクトル
	 * @return ベクトルpとベクトルqの距離
	 */
	private double euclidian(double[]p, double[] q){
		double distance = 0;
		for(int i = 0; i < p.length; i++){
			distance += Math.pow(p[i]-q[i],2);
		}
		return Math.sqrt(distance);
	}
}
