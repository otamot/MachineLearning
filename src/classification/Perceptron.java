package classification;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import lib.Matrix;

public class Perceptron {
	private final int N = 10000;
	double[][] x;
	double[][] t;

	public Perceptron(){
		setX();
		setT();
	}

	private void setX(){
		Random rnd = new Random();
		x = new double[N][2];
		for(int i = 0; i < x.length;i++){
			for(int j = 0; j < x[i].length; j++){
				x[i][j] = rnd.nextGaussian();
			}
		}
	}


	private double h(double x, double y){
		return 5 * x + 3 * y - 1;
	}

	private void setT(){
		t = new double[N][1];
		for(int i = 0; i < x.length; i++){
			t[i][0] = (h(x[i][0], x[i][1]) > 0)?1:-1;
		}
	}

	private Matrix phi(double x, double y){
		double[][] phi_i = new double[1][3];
		phi_i[0][0] = x;
		phi_i[0][1] = y;
		phi_i[0][2] = 1.0;
		return new Matrix(phi_i);
	}


	private int sign(double x){
		return (x>0)?1:(x==0)?0:-1;
	}
	private void cal(){
		Matrix w = new Matrix(3,1);
		List<Integer> list = new ArrayList<>();
		for(int i= 0; i < N; i++){
			list.add(i);
		}
		while(true){
			Collections.shuffle(list);
			int misses = 0;

			for(int n:list){
				double x_n = x[n][0];
				double y_n = x[n][1];
				double t_n = t[n][0];
				int predict = sign(phi(x_n,y_n).mul(w).toArray()[0][0]);
				if((double)predict != t_n){
					w = w.add(phi(x_n, y_n).scalar(t_n).t());
					misses += 1;
				}
			}
			System.out.println();
			w.printMatrix();
			if(misses == 0)
				break;
		}
	}


	//テスト用メソッド
	public static void main(String [] args){
		Perceptron per= new Perceptron();
		per.cal();
	}

}

