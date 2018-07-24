package prj;

public class LabelName {

	public String Name(int label) {
		String[] name = {"歩く", "座る", "立つ","静止"};
		for(int i=0; i<4; i++) {
			if(label==i) return name[i];
		}
		return "error";
	}
}