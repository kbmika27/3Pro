package knn;

import java.util.ArrayList;
import java.util.List;

public interface Calc {

	public List<Double> distanceData = new ArrayList<Double>();    //距離とそのラベルをひとまとまりにしたクラスのリスト

	double Calc(Sample sample);

}