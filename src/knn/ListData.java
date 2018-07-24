package knn;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

import prj.ReadText;

public class ListData {

	int m_label;
	List<List<Point>> list = new ArrayList<List<Point>>();//リストのリスト
	
	public List<List<Point>> Data(int n) {
		for(int i=n;i<n+3;i++) {
			String text="data"+i;
		ReadText data=new ReadText(text);//data i をリストに追加
		list.add(data.list2);//data.listはただのpoint型のlist
		//System.out.println(list.size()+"リストのサイズ");
		}
		return list;
	}
	
}