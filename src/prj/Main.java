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
		Imgcodecs.imwrite("gray.jpg", grayImage);
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
		DecimalFormat dformat = new DecimalFormat("000");
		Mat[] F_DST = new Mat[770]; //フーリエ変換させた画像の配列
		Mat[] CI_DST = new Mat[770]; //正解画像をフーリエ変換させた画像の配列
		List<Integer> list = new ArrayList<>(); //画像の座標テキスト
		File file;
		Mat src;

		//座標を入れたリストの作成
		list = loadText();

		//画像を読み込み変換
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
			F_DST[i] = fft.dst;

			//正解画像を作ってフーリエ変換させる
			CorrectImage ci = new CorrectImage(src, list.get(i * 4) + list.get(i * 4 + 2) / 2,
					list.get(i * 4 + 1) + list.get(i * 4 + 3) / 2);
			Fourier fci = new Fourier(ci.dst, ci.dst);
			//配列に読み込む
			CI_DST[i] = fci.dst;
			//デバッグ用
			//Imgcodecs.imwrite("CIdst" + i + ".jpg", fci.dst);
		}

		//画像のサイズを取得
		int width = F_DST[0].cols(); //320
		int height = F_DST[0].rows(); //240

		Mat dst = new Mat(height, width, CvType.CV_32FC1);  //最終的に出したい画像の作成

		double[] F = new double[1];   //フーリエ変換した画像の画素値を保存する変数
		double[] CI = new double[1];  //正解画像の画素値を保存する変数

		double[][] numer = new double[height][width]; //分子
		double[][] denom = new double[height][width]; //分母
		double[][] quotient = new double[height][width];  //商

		//画素位置ごとに計算
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				for (int k = 0; k < 471; k++) {
					F = F_DST[k].get(x , y );
					CI = CI_DST[k].get(x, y);
					numer[x][y] += (F[0] * CI[0]);
					denom[x][y] += (F[0] * F[0]);
				}
			}
		}

		//最適化した画像のそれぞれの値の計算
		for (int x = 0; x < height; x++) {
			for (int y = 0; y < width; y++) {
				quotient[x][y] = numer[x][y] / denom[x][y];
				dst.put(x, y, quotient[x][y]);
			}
		}
		Imgcodecs.imwrite ("test.jpg",dst);
	}
}
