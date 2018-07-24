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


	List<DataClass> list = new ArrayList<DataClass>();
	
	public int ReturnLabel(Queue<Point> queue) {
		ListData listt = new ListData();
		int label = 0;
		for(int i=1; i<13; i+=3) {
			Sample sample = new Sample(listt.Data(i),label);
			//インターフェースに飛ばす
			Euclid euclid = new Euclid(queue);
			//DTW dtw=new DTW(queue);
			DataClass dc = new DataClass(label,euclid.Calc(sample));
			//DataClass dc = new DataClass(label,dtw.Calc(sample));
			System.out.println(dc.distance);
			list.add(dc);
			System.out.println(label+"label");
			label++;
		}
		
		int min = 0;
		for(int i=1; i<list.size(); i++) {
			if(list.get(min).getDistance()>list.get(i).getDistance()) min = i;
		}
		
		return min;
	}

}