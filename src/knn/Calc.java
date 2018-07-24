package knn;

import java.util.ArrayList;
import java.util.List;

public interface Calc {

	public List<DataClass> distanceData = new ArrayList<DataClass>();    //距離とそのラベルをひとまとまりにしたクラスのリスト

	double Calc(Sample sample);

}