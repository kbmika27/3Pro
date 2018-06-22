package prj;

import java.io.File;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	public static int ALL = 471;

	public static void main(String[] args) throws Exception {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		File file;
		Mat[] src = new Mat[1]; //入力画像
		Mat[] f_dst = new Mat[ALL]; //フーリエした画像
		Mat[] ci_dst = new Mat[ALL]; //正解画像をフーリエした画像

		//座標リストの作成
		ReadText text = new ReadText("David/groundtruth_rect.txt");

		//画像を読み込み変換する
		DecimalFormat dformat = new DecimalFormat("000"); //数字の表記を変える

		for (int i = 0; i < ALL; i++) {
			String filename = "David/img/0" + dformat.format(i + 1) + ".jpg";

			//入力画像を読み込んでMat型にする
			file = new File(filename);
			src[0] = Imgcodecs.imread(file.getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_COLOR);
			if (src[0] == null) {
				throw new RuntimeException("Can't load image.");
			}

			//Imgcodecs.imwrite("メインクラスの入力画像.jpg", src[0]);  //デバッグ用

			//グレースケール変換後の配列
			Mat[] Grays = new Mat[1];
			GrayImage GI = new GrayImage(src);
			Grays[0] = GI.grayImage;
			//グレースケール画像をフーリエ変換し、配列に読み込む
			Fourier fft = new Fourier(src, Grays);
			f_dst[i] = fft.dst;

			//正解画像の配列を作る
			CorrectImage ci = new CorrectImage(src, text.list.get(i * 4) + text.list.get(i * 4 + 2) / 2,
					text.list.get(i * 4 + 1) + text.list.get(i * 4 + 3) / 2);
			Mat[] CI = new Mat[1];
			CI[0] = ci.dst.clone();
			//Imgcodecs.imwrite("メインクラスの正解画像.jpg", CI[0]); //デバッグ用

			//正解画像をフーリエ変換し、配列に読み込む
			Fourier fci = new Fourier(CI, CI);
			ci_dst[i] = fci.dst;
			//Imgcodecs.imwrite("メインクラスの正解画像のフーリエ.jpg", fci.real);  //デバッグ用
		}

		//画像のサイズを取得
		int width = ci_dst[0].cols(); //320
		int height = ci_dst[0].rows(); //240

		//掛け算したものの出力先
		Mat[] num = new Mat[ALL];    //分子用の配列
		Mat[] den = new Mat[ALL];    //分母用の配列
		Mat NUM = Mat.zeros(height, width, CvType.CV_32FC2);    //分子の和
		Mat DEN = Mat.zeros(height, width, CvType.CV_32FC2);    //分母の和
		Mat ANS = new Mat(height, width, CvType.CV_32FC2);    //分子/分母の和
		Mat IDFT = new Mat();                                 //逆フーリエ後

		for (int f = 0; f < 25; f++) {
			num[f] = new Mat(height, width, CvType.CV_32FC2);    //初期化
			den[f] = new Mat(height, width, CvType.CV_32FC2);    //初期化
			Core.mulSpectrums(ci_dst[f], f_dst[f], num[f], 0, true);   //1枚ずつの分子の計算
			Core.mulSpectrums(f_dst[f], f_dst[f], den[f], 0, true);    //1枚ずつの分母の計算
			Core.add(NUM, num[f], NUM);          //分子の和
			Core.add(DEN, den[f], DEN);          //分母の和
		}

		Core.divide(NUM, DEN, ANS);    //分子/分母

		Mat Test = Mat.zeros(height, width, CvType.CV_32FC2);
		Core.mulSpectrums(f_dst[400], ANS, Test, 0, false);
		Core.idft(Test, IDFT);
		List<Mat> planes = new ArrayList<Mat>();
		Core.split(IDFT, planes);
		Mat test2 = Mat.zeros(240, 320, CvType.CV_8UC1);
		Core.normalize(planes.get(0), test2, 0, 255, Core.NORM_MINMAX);





/*
		List<Mat> conj = new ArrayList<Mat>();
		Core.split(ANS, conj);
		Scalar m = new Scalar(-1);
		Core.multiply(conj.get(1), m, conj.get(1));
		Core.merge(conj, ANS);
		Core.idft(ANS, IDFT);
		List<Mat> planes = new ArrayList<Mat>();
		Core.split(IDFT, planes);
		for (int x = 0; x < planes.get(0).rows(); x++) {
			for (int y = 0; y < planes.get(0).cols(); y++) {
				double[] s = planes.get(0).get(x, y);
				double ans = 0;
				ans = s[0] * 200;
				planes.get(0).put(x, y, ans);
			}
		}*/
		Imgcodecs.imwrite("idft.jpg", test2);
	}
}

