# 線形回帰モデル

## Javadoc
[コチラ](https://htmlpreview.github.io/?https://raw.githubusercontent.com/otamot/MachineLearning/master/doc/linearRegression/LinearRegression.html)


## ソースコード
### [LinearRegression.java](https://github.com/otamot/MachineLearning/blob/master/src/linearRegression/LinearRegression.java)
```java
package linearRegression;
import lib.Matrix;

public class LinearRegression {
	Matrix X;
	Matrix Phi;
	Matrix Y;
	Matrix W;

	public LinearRegression(double[][] x, double[] y,BasisFunction phi){
		double[][] y_mat = new double[1][y.length];
		y_mat[0] = y;
		Y = new Matrix(y_mat).t();
		X = new Matrix(x);
	}




	/**
	 *
	 * 線形回帰を行う。
	 * W = (Φ^TΦ)^(-1)ΦY
	 */
	public void cal(){
		W = ((X.t().mul(X)).inv()).mul(X.t()).mul(Y);
		W.printMatrix();
		double[][] w = W.toArray();
		System.out.print("y =");
		for(int i = 0; i < w.length; i++){
			if(i != 0){
				if(w[i][0] > 0)
				System.out.print("+");
			}

			System.out.print(" " + w[i][0] + "*Φ[" + i + "] ");
		}
	}

}
```

### [LinearRegressionMain.java](https://github.com/otamot/MachineLearning/blob/master/src/linearRegression/LinearRegressionMain.java)

```java
package linearRegression;

public class LinearRegressionMain {

	public static void main(String[] args) {
		double[][] x = {{100},{200},{300},{400},{500},{600},{700}};
		double[] y = {40,50,50,70,65,65,80};
		LinearRegression lr = new LinearRegression(BasisFunction.polynomialBasis(x, 10),y, new BasisFunction());
		lr.cal();
	}

}
```
