package knn;

import java.util.ArrayList;
import java.util.List;


public class Sample {

	List<List> SampleDatas = new ArrayList<List>();  //行動別にデータを集めたリスト
	int m_label;  //ラベル

	public int getLabel() {
		return m_label;
	}

	public List<List> getDatal() {
		return SampleDatas;
	}

	public void SetLabel(int m_label) {
		this.m_label = m_label;
	}

	public void SetData(List<List> list) {
		this.SampleDatas = list;
	}

	public Sample() {
		for (int i = 1; i < 3; i++) {
			Read data = new Read(text);
			SampleDatas.add(data.getList());

		}
	}

}