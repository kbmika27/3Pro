package knn;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.opencv.core.Point;

public class DTW2 {
	Queue<Point> x1;
	List<Point> xx1;
	List<Point>xx2;
	List<Double>ListResult=new ArrayList<Double>();
	double dtw=Double.MAX_VALUE;
	double d;
	
	public DTW2(Queue<Point>x1) {
		this.x1 = x1;
	}
	
	public static double d(List<Point>input ,List<Point> sample) {
		int n = input.size(); //入力データ
		int m = sample.size(); //サンプルデータ
		double K = (double) n / m;
		
		double X1 = 0.0; //入力データのxの変化量の合計
		double Y1 = 0.0; //入力データのyの変化量の合計
		double X2 = 0.0; //サンプルデータのxの変化量の合計
		double Y2 = 0.0; //サンプルデータのyの変化量の合計
		double returnValue = 0.0;
		
		for(int i=0; i<n-1; i++) {
			X1 += input.get(i).x - input.get(i+1).x;
			Y1 += input.get(i).y - input.get(i+1).y;
		}
		
		for(int i=0; i<m-1; i++) {
			X2 += sample.get(i).x - sample.get(i+1).x;
			Y2 += sample.get(i).y - sample.get(i+1).y;
		}
		
		X2 = X2 * K;
		Y2 = Y2 * K;
		
		returnValue = Math.abs(X1-X2) + Math.abs(Y1-Y2);
		
		return returnValue;
		
	}
	
	public labelDouble Calc(Sample x2) {//x1が入力データのキュー、x2が学習データ
		//System.out.println("samplesize"+x2.SampleDatas.size());
		xx1=new ArrayList<Point>();//入力のコピー
		xx2=new ArrayList<Point>();//サンプルをリストに入れる
		for(int i=0;i<x1.size();i++) {//入力画像をxx1に入れる
			xx1.add(x1.poll());//取り出して削除;
		}
		for(int i=0;i<xx1.size();i++) {//queueに入れ直し
			x1.add(xx1.get(i));
		}
		
		for(int i=0;i<x2.SampleDatas.size();i++) {
	    xx2=x2.getSampleDatas().get(i);//i番目のデータ
	    d=d(xx1,xx2);
	    ListResult.add(d);
	    //if(i==0)dtw=d;
	    //if(d<dtw)dtw=d;
		}
		labelDouble lD=new labelDouble(ListResult,x2.getLabel());
		
		return lD;
	}
}
