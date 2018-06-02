package prj;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class Main {

	//グレースケールに変換
	public static Mat getGray(Mat src) {
		Mat grayImage = Mat.zeros(src.size(), CvType.CV_64F);
		Imgproc.cvtColor(src, grayImage, Imgproc.COLOR_RGB2GRAY);
		grayImage.convertTo(grayImage, CvType.CV_32FC1);
		//Imgcodecs.imwrite("gray.jpg", grayImage);  //デバッグ用
		return grayImage;
	}

	//座標テキストの読み込み
	public static List<Integer> loadText() {
		FileReader fr = null;
		BufferedReader br = null;
		List<Integer> list = new ArrayList<>();

		try {
			fr = new FileReader("David/groundtruth_rect.txt");
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
		return list;
	}

	public static void main(String[] args) throws Exception {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		File file;
		Mat src;     //入力画像
		Mat[] f_dst = new Mat[471];        //フーリエした画像
		Mat[] f_real_dst = new Mat[471];    //フーリエ変換した画像の実部
		Mat[] f_img_dst = new Mat[471];     //フーリエ変換した画像の虚部
		Mat[] ci_dst = new Mat[471];        //正解画像をフーリエした画像

		//座標を入れたリストの作成
		List<Integer> list = new ArrayList<>(); //画像の座標テキストをリストに入れたもの
		list = loadText();

		//画像を読み込み変換する
		DecimalFormat dformat = new DecimalFormat("000");   //数字の表記を変える
		for (int i = 0; i < 471; i++) {
			String filename = "David/img/0" + dformat.format(i + 1) + ".jpg";

			//入力画像を読み込んでMat型にする
			file = new File(filename);
			src = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
			if (src == null) {
				throw new RuntimeException("Can't load image.");
			}

			//フーリエ変換させる
			Fourier fft = new Fourier(src, getGray(src));

			//配列に読み込む
			f_dst[i] = fft.dst;
			f_real_dst[i] = fft.real;
			f_img_dst[i] = fft.img;
		    //double[] data = f_img_dst[i].get(1, 1);
		    //System.out.println(data[0]);  //デバッグ用・虚部の中ってどんな数字が入ってるの

			//正解画像を作ってフーリエ変換させる
			CorrectImage ci = new CorrectImage(src, list.get(i * 4) + list.get(i * 4 + 2) / 2,
					list.get(i * 4 + 1) + list.get(i * 4 + 3) / 2);
			Fourier fci = new Fourier(ci.dst, ci.dst);

			//配列に読み込む
			ci_dst[i] = fci.dst;
			//Imgcodecs.imwrite("CIdst" + i + ".jpg", fci.dst);   //デバッグ用
		}

		//画像のサイズを取得
		int width = ci_dst[0].cols(); //320
		int height = ci_dst[0].rows(); //240
		//最終的に出したい画像の作成
		Mat dst = new Mat(height, width, CvType.CV_32FC1);

		Mat[] F1 = new Mat[471];
		Mat[] F2 = new Mat[471];
		Mat[] F3 = new Mat[471];
		//Mat[] F4 = new Mat[471];
		Mat numer = new Mat();    //フーリエ画像の複素共役を保存する配列
		Mat demor = new Mat();

		for (int k = 0; k < 471; k++) {
			//フーリエ画像の複素共益をとる
			Core.subtract(f_real_dst[k], f_img_dst[k], F1[k]);   //ここでぬるぽエラー
			//分子のそれぞれの要素を出す
			Core.gemm(ci_dst[k], F1[k], 1, Mat.zeros(height, width, CvType.CV_32FC1), 0, F2[k], 0);
			//分母のそれぞれの要素を出す
			Core.gemm(f_dst[k], F1[k], 1, Mat.zeros(height, width, CvType.CV_32FC1), 0, F3[k], 0);
			//分子の和
			Core.add(numer, F1[k], numer);
			//分母の和
			Core.add(demor, F3[k], demor);
		}
		 Core.divide(numer, demor, dst, CvType.CV_32FC1);
		 Imgcodecs.imwrite("test.jpg", dst);

	}
}
