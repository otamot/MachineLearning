package clustering;

public class KMeansMain {
	public static void main(String[] args){
		double[][] vec = {
				{  80,  85, 100 },
				{  96, 100, 100 },
				{  54,  83,  98 },
				{  80,  98,  98 },
				{  90,  92,  91 },
				{  84,  78,  82 },
				{  79, 100,  96 },
				{  88,  92,  92 },
				{  98,  73,  72 },
				{  75,  84,  85 },
				{  92, 100,  96 },
				{  96,  92,  90 },
				{  99,  76,  91 },
				{  75,  82,  88 },
				{  90,  94,  94 },
				{  54,  84,  87 },
				{  92,  89,  62 },
				{  88,  94,  97 },
				{  42,  99,  80 },
				{  70,  98,  70 },
				{  94,  78,  83 },
				{  52,  73,  87 },
				{  94,  88,  72 },
				{  70,  73,  80 },
				{  95,  84,  90 },
				{  95,  88,  84 },
				{  75,  97,  89 },
				{  49,  81,  86 },
				{  83,  72,  80 },
				{  75,  73,  88 },
				{  79,  82,  76 },
				{ 100,  77,  89 },
				{  88,  63,  79 },
				{ 100,  50,  86 },
				{  55,  96,  84 },
				{  92,  74,  77 },
				{  97,  50,  73 }
		};
		
		KMeans kmeans = new KMeans(vec,3);
		kmeans.calc();	
		int[] cluster = kmeans.getCluster();;
		double[][] centroid = kmeans.getCentroid();
		
		System.out.println("k-meansの情報");
		kmeans.printKMeansInfo();
		System.out.println("\nクラスタid:ベクトルの要素");
		printArray(cluster,vec);
		System.out.println("\n重心ベクトル");
		printArray(centroid);
		
		
		//////////////////////////////////////////////////////////////////
		
		double vec2[][] = {{0,0},{0,1},{1,0},{1,1},{9,0},{9,1},{10,0},{10,1}}; 
		kmeans = new KMeans(vec2,2,KMeans.INIT_RND);
		kmeans.calc();	
		cluster = kmeans.getCluster();;
		centroid = kmeans.getCentroid();
		
		System.out.println("\n\n\n\n");
		System.out.println("k-meansの情報");
		kmeans.printKMeansInfo();
		System.out.println("\nクラスタid:ベクトルの要素");
		printArray(cluster,vec);
		System.out.println("\n重心ベクトル");
		printArray(centroid);
		
	}
	
	
	
	public static void printArray(int[] a,double[][] vec){
		for(int i = 0; i < a.length; i++){
			System.out.printf("%3d:", a[i]);
			for(int j = 0; j < vec[i].length;j++){
				System.out.printf("%6.1f", vec[i][j]);
			}
			System.out.println();
		}
	}
	
	public static void printArray(double[][] a){
		for(int i = 0; i < a.length; i++){
			System.out.printf("%3d:", i);
			for(int j = 0; j < a[i].length; j++){
				System.out.printf("%5.1f ", a[i][j]);
			}
			System.out.println();
		}
		
	}
}
