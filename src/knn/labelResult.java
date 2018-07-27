package knn;

import java.util.ArrayList;
import java.util.List;

public class labelResult {

	double result;
	int label;  //ラベル
	
	public labelResult(double result, int label) {
		this.label = label;
		this.result = result;
	}
	
	public int getLabel() {
		return label;
	}
	
	public double getResult(){
		return result;
	}
}
