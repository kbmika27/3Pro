package knn;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;





public class Sample {

	
	List<List<Point>> SampleDatas = new ArrayList<List<Point>>();  //行動別にデータを集めたリスト
	int m_label;  //ラベル
	
	public Sample(List<List<Point>> SampleDatas, int m_label) {
		this.m_label = m_label;
		this.SampleDatas = SampleDatas;
	}
	
	public int getLabel() {
		return m_label;
	}
	
	public List<List<Point>> getSampleDatas(){
		return SampleDatas;
	}

}