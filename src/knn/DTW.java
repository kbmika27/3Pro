package knn;

import java.io.Serializable;
import java.util.Queue;

import org.opencv.core.Point;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public  class DTW implements Calc {

	Queue<Point> x1;
	List<Point> xx1;
	List<Point>xx2;
	private static final long serialVersionUID = 1L;
	
    private double width = 1;
	/*Euclidクラスのインスタンスを生成したときに、
	 * そのクラス内の変数としてqueueを持つようにコンストラクタに書く
	 */
	public DTW(Queue<Point>x1) {
		this.x1 = x1;
	}
	 public static  double d(List<Point>xx1,List<Point> x2) {
		        int n1 = xx1.size();
		        int n2 = x2.size();
		        double[][] table = new double[2][n2 + 1];

		        table[0][0] = 0;

		        for (int i = 1; i <= n2; i++) {
		            table[0][i] = Double.POSITIVE_INFINITY;
		        }

		        for (int i = 1; i <= n1; i++) {
		            table[1][0] = Double.POSITIVE_INFINITY;

		            for (int j = 1; j <= n2; j++) {
		              //  double cost = Math.abs(x1[i-1] - x2[j-1]);
                    double costx = Math.abs(xx1.get(i-1).x- x2.get(j-1).x);
	            	 double costy=Math.abs(xx1.get(i-1).y- x2.get(j-1).y);
	            	 double cost=costx+costy;

		                double min = table[0][j - 1];

		                if (min > table[0][j]) {
		                    min = table[0][j];
		                }

		                if (min > table[1][j - 1]) {
		                    min = table[1][j - 1];
		                }

		                table[1][j] = cost + min;
		            }

		            double[] swap = table[0];
		            table[0] = table[1];
		            table[1] = swap;
		        }
		        return table[0][n2];
	 }
	//実際の計算はここから
	public double Calc(Sample x2) {//x1が入力画像のキュー、x2が
		double d,dtw;
		dtw=100;//一旦100にしておく
		xx1=new ArrayList<Point>();//入力のコピー
		xx2=new ArrayList<Point>();//サンプルをリストに入れる
		for(int i=0;i<x2.SampleDatas.size();i++) {		
	    xx1.add(x1.poll());//取り出して削除;
	    xx2=x2.SampleDatas.get(i);
	    d=d(xx1,xx2);
	    if(d<dtw)dtw=d;
		}
		for(int i=0;i<xx1.size();i++) {//queueに入れ直し
			x1.add(xx1.get(i));
		}
		//サンプルデータの取得（できたらインターフェース内にかけると良いかも・・・）
		
		//実際の計算

		//計算結果をリストに入れる
		//DataClass dc = new DataClass(lavel, dtw);  //labelと距離の計算結果(dtw)をクラス化する
		//distanceData.add(dc);
		return dtw;
	}

}
