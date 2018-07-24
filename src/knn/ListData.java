package knn;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

import prj.ReadText;

public class ListData {

	int m_label;
	List<List<Point>> list = new ArrayList<List<Point>>();//リストのリスト
	int numSamples=3;//トレーニングデータの数
	public List<List<Point>> Data(int labelID) {
		for(int i=1;i<numSamples+1;i++) {
			String text="label"+labelID+"/data"+i;
		ReadText data=new ReadText(text);//data i をリストに追加
		list.add(data.list2);//data.listはただのpoint型のlist
		//System.out.println(list.size()+"リストのサイズ");
		}
		return list;
	}
	
}