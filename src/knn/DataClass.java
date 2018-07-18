package knn;

import java.io.Serializable;

public class DataClass implements Serializable {

	public int label;
	public double distance;

	public DataClass(int label, double distance) {
		this.label = label;
		this.distance = distance;
	}

	public int getLabel() {
		return label;
	}

	public double getDistance() {
		return distance;
	}

}