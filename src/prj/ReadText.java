package prj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;



public class ReadText {

	List<Integer> list = new ArrayList<>();
	public List<Point> list2 = new ArrayList<Point>();
	int xx,yy;

	public ReadText(String x) {
		getText(x);
	}

	public int getMaxWidthSize() {
		int max = 0;
		for (int i = 0; i < tme.ALL; i++) {
			if (max < list.get(i * 4 + 2))
				max = list.get(i * 4 + 2);
		}
		return max;
	}

	public int getMaxHeightSize() {
		int max = 0;
		for (int i = 0; i < tme.ALL; i++) {
			if (max < list.get(i * 4 + 3))
				max = list.get(i * 4 + 3);
		}
		return max;
	}
	public Point getXY(int x,int y) {
		Point pp=new Point(x,y);
		return pp;
	}

	//座標テキストの読み込み
	public void getText(String x) {
		FileReader fr = null;
		BufferedReader br = null;

		try {
			fr = new FileReader(x);
			br = new BufferedReader(fr);

			String line;
			while ((line = br.readLine()) != null) {
				String[] array = line.split(",", 0);
				//ここから
				for(int i=0;i<array.length;i+=2) {
					xx=Integer.parseInt(array[i]);
					yy=Integer.parseInt(array[i+1]);
					list2.add(getXY(xx,yy));
				}

				//ここまで
				for (String elem : array)
					list.add(Integer.parseInt(elem));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				br.close();
				fr.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

}