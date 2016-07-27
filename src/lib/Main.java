package lib;

public class Main {
	public static void main(String[] args){
		double[][] a = {{10,20,30},{20,30,40}};
		Matrix A = new Matrix(a);
		A.printMatrix();
		System.out.println();
		double[][] b = {{1,2,3},{2,3,4}};
		Matrix B = new Matrix(b);
		B.printMatrix();
		System.out.println();
		
		(A.add(B)).printMatrix();
		
		A.t().sub(B.t()).printMatrix();
		
		Matrix.getE(4).scalar(2.0).printMatrix();
		
		A.mul(B.t()).printMatrix();
		System.out.println();
		
		double c[][] = {{1,2},{4,2}};
		
		Matrix C = new Matrix(c);
		C.inv().printMatrix();
		C.inv().mul(C).printMatrix();
		
		System.out.println(Matrix.getE(3).scalar(3).det());
	
	}

}
