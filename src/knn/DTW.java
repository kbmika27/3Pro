package knn;

import java.awt.Point;
import java.util.Queue;

public class DTW implements Calc {

	Queue<Point> queue;

	/*Euclidクラスのインスタンスを生成したときに、
	 * そのクラス内の変数としてqueueを持つようにコンストラクタに書く
	 */
	public DTW(Queue<Point> queue) {
		this.queue = queue;
	}

	//実際の計算はここから
	public void Calc() {

		//サンプルデータの取得（できたらインターフェース内にかけると良いかも・・・）

		//実際の計算

		//計算結果をリストに入れる
		DataClass dc = new DataClass(lavel, dtw);  //labelと距離の計算結果(dtw)をクラス化する
		distanceData.add(dc);
	}

}
