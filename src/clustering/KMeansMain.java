package clustering;

public class KMeansMain {
	public static void main(String[] args){
//		String[] label = {"国語" ,"数学", "英語"};
		double[][] vec = {
			  //{国語, 数学, 英語 }
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
		
		
	//	double vec[][] = {{0,0},{0,1},{1,0},{1,1},{9,0},{9,1},{10,0},{10,1}}; 
		KMeans kmeans = new KMeans(vec,3);
		kmeans.printKMeansInfo();
		kmeans.calc();
//		System.out.println(KMeans.INIT_ALGO.INIT_K_MEANSPPaa.equals("INIT_K_MEANSPPaa"));
		
		int[] cluster = null;
		double[][] centroid = null;
		try{
			cluster = kmeans.getCluster();
			centroid = kmeans.getCentroid();
		}catch(Exception e){
			e.printStackTrace();
		}
		
		System.out.print("  ");
	/*	for(int j = 0;j < label.length;j++){
			System.out.printf("%4s",label[j]);
		}*/
		System.out.println();
		for(int i = 0; i < cluster.length; i++){
			System.out.print(cluster[i] + ":");
			for(int j = 0; j < vec[i].length; j++){
				System.out.printf("%4d",(int)vec[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		
		System.out.print("  ");
	/*	for(int j = 0;j < label.length;j++){
			System.out.printf("%4s",label[j]);
		}*/
		System.out.println();
		for(int i = 0; i < centroid.length; i++){
			System.out.printf("%2d",i);
			for(int j = 0; j < centroid[i].length; j++){
				System.out.printf("%6.1f",centroid[i][j]);
			}
			System.out.println();
		}
	}

}
