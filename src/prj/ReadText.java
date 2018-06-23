package prj;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadText {

	List<Integer> list = new ArrayList<>();

	public ReadText(String x) {
		getText(x);
	}

	public int getMaxWidthSize() {
		int max = 0;
		for (int i = 0; i < Main.ALL; i++) {
			if (max < list.get(i * 4 + 2))
				max = list.get(i * 4 + 2);
		}
		return max;
	}

	public int getMaxHeightSize() {
		int max = 0;
		for (int i = 0; i < Main.ALL; i++) {
			if (max < list.get(i * 4 + 3))
				max = list.get(i * 4 + 3);
		}
		return max;
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
