package knn;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import org.opencv.core.Point;

public class knn {

	//「歩く」や「走る」のデータごとにサンプルデータのリスト集を作る
	//calcで計算し、距離とラベルの入ったリストを受けとる
	//リストを距離で並び替え、上位k個のラベルを判別
	//動作を判定する

	int numLabel = 4;//ラベルの数
	
	public int ReturnLabel(Queue<Point> queue) {
		ListData[] listD = new ListData[numLabel];
		Sample[] sample = new Sample[numLabel];
		for(int label=0; label<numLabel; label++) {
			listD[label] = new ListData();
			sample[label]= new Sample(listD[label].Data(label),label);
		}
		//インターフェースに飛ばす
		//Euclid euclid = new Euclid(queue);
		DTW dtw=new DTW(queue);
		double min = Double.MAX_VALUE;
		int minLabel = -1;
		for(int label=0; label<numLabel; label++) {
			//double result = euclid.Calc(sample[label]);
			double result = dtw.Calc(sample[label]);
			System.out.println(result);
			if(min>result) {
				min = result;
				minLabel = label;
			}
			System.out.println(label+"label");
		}
		
		return minLabel;
	}

}