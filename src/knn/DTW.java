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
	List<Double>ListResult=new ArrayList<Double>();
	double dtw=Double.MAX_VALUE;
	double d;
	private static final long serialVersionUID = 1L;
	
    private double width = 1;
	/*Euclidクラスのインスタンスを生成したときに、
	 * そのクラス内の変数としてqueueを持つようにコンストラクタに書く
	 */
	public DTW(Queue<Point>x1) {
		this.x1 = x1;
	}
	
	 public static  double d(List<Point>xx1,List<Point> x2) {
		        int n1 = xx1.size();//入力画像のリスト
		        int n2 = x2.size();//学習データのリスト
		        double[][] table = new double[2][n2 + 1];

		        table[0][0] = 0;

		        for (int i = 1; i <= n2; i++) {
		            table[0][i] = Double.POSITIVE_INFINITY;
		        }

		        for (int i = 1; i <= n1; i++) {
		            table[1][0] = Double.POSITIVE_INFINITY;

		            for (int j = 1; j <= n2; j++) {
		            	double costx = Math.abs(xx1.get(i-1).x- x2.get(j-1).x);
		            	double costy=Math.abs(xx1.get(i-1).y- x2.get(j-1).y);
		            	//double cost=Math.sqrt(costx*costx+costy*costy);
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
	public labelDouble Calc(Sample x2) {//x1が入力画像のキュー、x2が学習データ
		//System.out.println("samplesize"+x2.SampleDatas.size());
		xx1=new ArrayList<Point>();//入力のコピー
		xx2=new ArrayList<Point>();//サンプルをリストに入れる
		for(int i=0;i<x1.size();i++) {//入力画像をxx1に入れる
			xx1.add(x1.poll());//取り出して削除;
		}
		for(int i=0;i<x2.SampleDatas.size();i++) {	//ここは合ってる	 18まで回してる
	    xx2=x2.getSampleDatas().get(i);//i番目のデータ
	    d=d(xx1,xx2);
	    ListResult.add(d);
	   // if(i==0)dtw=d;
	    //if(d<dtw)dtw=d;
		}
		for(int i=0;i<xx1.size();i++) {//queueに入れ直し
			x1.add(xx1.get(i));
		}
		labelDouble lD=new labelDouble(ListResult,x2.getLabel());//ex)data1の結果が18個入っている
		//サンプルデータの取得（できたらインターフェース内にかけると良いかも・・・）
		
		//実際の計算

		//計算結果をリストに入れる
		//DataClass dc = new DataClass(lavel, dtw);  //labelと距離の計算結果(dtw)をクラス化する
		//distanceData.add(dc);
		//return dtw;
		return lD;
	}

}
