package knn;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

public class labelDouble {
	List<Double> result = new ArrayList<Double>();
	int label;  //ラベル
	
	public labelDouble(List<Double> result, int label) {
		this.label = label;
		this.result = result;
	}
	
	public int getLabel() {
		return label;
	}
	
	public List<Double> getResult(){
		return result;
	}
}
