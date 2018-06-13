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
		Mat grayImage = Mat.zeros(src.size(), CvType.CV_32F);
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
		Mat src; //入力画像
		Mat[] f_dst = new Mat[471]; //フーリエした画像
		Mat[] f_real_dst = new Mat[471]; //フーリエ変換した画像の実部
		Mat[] f_img_dst = new Mat[471]; //フーリエ変換した画像の虚部
		Mat[] ci_dst = new Mat[471]; //正解画像をフーリエした画像
		Mat[] ci_real_dst = new Mat[471];
		Mat[] ci_img_dst = new Mat[471];

		//座標を入れたリストの作成
		List<Integer> list = new ArrayList<>(); //画像の座標テキストをリストに入れたもの
		list = loadText();

		//画像を読み込み変換する
		DecimalFormat dformat = new DecimalFormat("000"); //数字の表記を変える
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
			ci_real_dst[i] = fci.real;
			ci_img_dst[i] = fci.img;
			//Imgcodecs.imwrite("CIdst" + i + ".jpg", fci.dst);   //デバッグ用
		}

		//画像のサイズを取得
		int width = ci_dst[0].cols(); //320
		int height = ci_dst[0].rows(); //240
		//最終的に出したい画像の作成
		List<Mat> dst = new ArrayList<Mat>();
		Mat real_dst = new Mat(height, width, CvType.CV_32FC1);
		Mat img_dst = new Mat(height, width, CvType.CV_32FC1);
		Mat a  = new Mat(height, width, CvType.CV_32FC1);
		Mat H = new Mat(height, width, CvType.CV_32FC1);

		double[] F_real = new double[1]; //フーリエ変換した画像の画素値を保存する変数
		double[] CI_real = new double[1]; //正解画像の画素値を保存する変数
		double[] F_img = new double[1]; //フーリエ変換した画像の画素値を保存する変数
		double[] CI_img = new double[1];


				double[][] re_numer = new double[height][width]; //分子
				double[][] im_numer = new double[height][width]; //分子
				double[][] den = new double[height][width]; //分母
				double[][] quotient = new double[height][width]; //商
				double j = Math.sqrt(-1);

				//画素位置ごとに計算
				for (int x = 0; x < height; x++) {
					for (int y = 0; y < width; y++) {
						for (int k = 0; k < 2; k++) {
							F_real = f_real_dst[k].get(x, y);
							CI_real = ci_real_dst[k].get(x, y);
							F_img = f_img_dst[k].get(x, y);
							CI_img = ci_img_dst[k].get(x, y);

							re_numer[x][y] += (CI_real[0] * F_real[0]) + (CI_img[0] * F_img[0]);   //Re[Numi(x, y)]
							im_numer[x][y] += (CI_img[0] * F_real[0]) + (CI_real[0] * F_img[0]);   //Im[Numi(x, y)]
							den[x][y] += (F_real[0] * F_real[0]) + (F_img[0] * F_img[0])+1;  //Den
							/*if((x==1)&&(y==1)){
							System.out.println(re_numer[x][y]);
							System.out.println(im_numer[x][y]);
							System.out.println(den[x][y]);
							}*/
						}
					}
				}

				for (int x = 0; x < height; x++) {
					for (int y = 0; y < width; y++) {
						//System.out.println(re_numer[x][y]);
						//System.out.println(im_numer[x][y]);
						//System.out.println(den[x][y]);
						real_dst.put(x, y, re_numer[x][y]*255/den[x][y]);  //Hの実部部分のmat
						img_dst.put(x, y, (-1*im_numer[x][y]*255)/den[x][y]);   //Hの虚部部分のmat
						//System.out.println(re_numer[x][y]/den[x][y]);
					}
				}
				dst.add(real_dst);
				dst.add(img_dst);

				Core.merge(dst, H);
				Core.idft(H, a);
				Imgcodecs.imwrite("test1.jpg", real_dst);
				Imgcodecs.imwrite("test2.jpg", img_dst);
				Imgcodecs.imwrite("test3.jpg", a);


/*
		Mat[] F1 = new Mat[471];
		Mat[] F2 = new Mat[471];
		Mat[] F3 = new Mat[471];

		for (int i = 0; i < 471; i++) {
			F1[i] = new Mat(height, width, CvType.CV_16UC1);
			F2[i] = new Mat(height, width, CvType.CV_32FC1);
			F3[i] = new Mat(height, width, CvType.CV_32FC1);
		}

		//Mat[] F4 = new Mat[471];
		Mat numer = new Mat(); //フーリエ画像の複素共役を保存する配列
		Mat demor = new Mat();

		for (int k = 0; k < 471; k++) {
			//フーリエ画像の複素共益をとる
			//System.out.println(F1[k]);
			Core.subtract(f_real_dst[k], f_img_dst[k], F1[k]);
			//分子のそれぞれの要素を出す
			Core.gemm(ci_dst[k], F1[k], 1, Mat.zeros(height, width, CvType.CV_32FC1), 0, F2[k], 0);
			//分母のそれぞれの要素を出す
			Core.gemm(f_dst[k], F1[k], 1, Mat.zeros(height, width, CvType.CV_32FC1), 0, F3[k], 0);
			//分子の和
			Core.add(numer, F1[k], numer);
			//分母の和
			Core.add(demor, F3[k], demor);
		}
		Core.divide(numer, demor, H);
		Imgcodecs.imwrite("test.jpg", H);
*/
	}
}
