package knn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Comparator;
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
		//DTW dtw=new DTW(queue);
	    DTW2 dtw2=new DTW2(queue);
		double min = Double.MAX_VALUE;
		int minLabel = -1;
		List<labelResult> resultAll = new ArrayList<labelResult>();
		for(int label=0; label<numLabel; label++) {
			//labelDouble result = euclid.Calc(sample[label]);
			//labelDouble result = dtw.Calc(sample[label]);
			labelDouble result = dtw2.Calc(sample[label]);
			for(int numResult=0; numResult<result.getResult().size(); numResult++) {
				labelResult lr = new labelResult(result.getResult().get(numResult),result.getLabel());
				resultAll.add(lr);
			}
		}
		Collections.sort(resultAll, new Comparator<labelResult>() {
			@Override
			public int compare(labelResult a, labelResult b) {
				return (int) (a.getResult() - b.getResult());
			}
		});
		int[] count = new int[4];
		for(int i=0; i<5; i++) {
			int correct = resultAll.get(i).getLabel();
			count[correct]++;
			if(count[correct]==2) {
				minLabel = correct;
				break;
			}
		}
		
		return minLabel;
	}

}