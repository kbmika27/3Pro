package knn;

import java.util.ArrayDeque;
import java.util.Queue;

import org.opencv.core.Point;

public class MoveAmount {

	public static int QueueMovement(Queue<Point> queue) { //ラベルを返す
		//Queue<Point> copy = new ArrayDeque<Point>();
		Point p = queue.poll();
		double x1 = p.x;
		double y1 = p.y;
		double X = 0; //xの変化量の合計
		double Y = 0; //yの変化量の合計
		int label = 0; //returnするラベルの値
		for(int i=0; i<queue.size(); i++) {
			p = queue.poll();
			double x2 = p.x;
			double y2 = p.y;
			
			X += x1 - x2;
			Y += y1 - y2;
			
			x1 = x2;
			y1 = y2;
		}
		
		double absX = Math.abs(X);
		double absY = Math.abs(Y);
		if(Math.abs(absX-absY)<20) label = 3; //静止
		else if(absX>absY) label = 0; //歩く
		else if(Y>0) label = 2; //立つ
		else label = 1; //座る

		
		return label;
	}
	
}
