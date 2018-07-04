package prj;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Point;

public class WriteData {

	private List<Point> data = new ArrayList<Point>();
	
	

	WriteData(List<Point> list) {
		this.data = list;
		Write();
	}

	public void Write() {
	  try {
          // FileWriterクラスのオブジェクトを生成する
          FileWriter file = new FileWriter("data1");
          // PrintWriterクラスのオブジェクトを生成する
          PrintWriter pw = new PrintWriter(new BufferedWriter(file));

          //ファイルに書き込む
          for(Point p : data) {
        	  pw.println(p.x);
        	  pw.println(",");
        	  pw.println(p.y);
        	  pw.println(",");
          }

          //ファイルを閉じる
          pw.close();
      } catch (IOException e) {
          e.printStackTrace();
      }

	}
}
