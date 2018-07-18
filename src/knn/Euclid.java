package knn;

import java.awt.Point;
import java.util.List;
import java.util.Queue;

public class Euclid implements Calc {

	double x, y;
	double euclid = 100;
	double ans = 100;
	Queue<Point> queue;

	/*Euclidクラスのインスタンスを生成したときに、
	 * そのクラス内の変数としてqueueを持つようにコンストラクタに書く
	 */
	public Euclid(Queue<Point> queue) {
		this.queue = queue;
	}

	//実際の計算はここから
	public void Calc() {

		//サンプルデータの取得（できたらインターフェース内にかけると良いかも・・・）
		ListData listt = new ListData();
		List<List> list = listt.list;
		Queue<Point> copy = queue;

		//実際の計算
		for (int j = 0; j < list.size(); j++) {
			for (int i = 0; i < 20; i += 2) {//listのlistの中身をユークリッドとる
				Point p = copy.remove();
				x = p.getX();//入力画像のx
				y = p.getY();//入力画像のy
				String objStr1 = list.get(0).get(i).toString();
				String objStr2 = list.get(0).get(i + 1).toString();
				double lx = new Double(objStr1).doubleValue();
				double ly = new Double(objStr2).doubleValue();
				euclid += (x - lx) * (x - lx) + (y - ly) * (y - ly);//足していく

				//計算結果をリストに入れる
				DataClass dc = new DataClass(lavel, euclid);  //labelをどこかのタイミングで取得したい
				distanceData.add(dc);

				//if(euclid>eucli)euclid=eucli;//最小の値を求める
			}
			//if(ans>euclid)ans=euclid;//listの中で最も小さい値
		}
	}

}