package prj;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Size;

public class Fourier {

	Mat src;
	Mat dst;
	Mat real;
	Mat img;
	Mat grayImage;

	//ファイルの読み込み
	public Fourier(Mat[] src, Mat[] grayImage) {

		//this.src = new Mat(240, 320, CvType.CV_8UC3);
		this.src = src[0].clone();

		this.grayImage = new Mat(240, 320, CvType.CV_8UC1);
		grayImage[0].copyTo(this.grayImage);

		//Imgcodecs.imwrite("フーリエクラスの入力画像.jpg", this.src); //デバッグ用
		getDFT();
	}


	private void getDFT() {
		// Convert to gray image.
		int m = Core.getOptimalDFTSize(grayImage.rows());
		int n = Core.getOptimalDFTSize(grayImage.cols());
		Mat padded = new Mat(new Size(n, m), grayImage.type());

		Core.copyMakeBorder(grayImage, padded, 0, m - src.rows(), 0,n - src.cols(), Core.BORDER_CONSTANT);

		// Make complex matrix.
		List<Mat> planes = new ArrayList<Mat>();
		planes.add(padded);
		planes.add(Mat.zeros(padded.size(), padded.type()));
		Mat complexI = Mat.zeros(padded.size(), CvType.CV_32FC1);
		Mat complexI2 = Mat.zeros(padded.size(), CvType.CV_32F);
		Core.merge(planes, complexI);

		// Calculate DFT, and magnitude.
		Core.dft(complexI, complexI2);
		Core.split(complexI2, planes);  //実部と虚部をplanesに分ける
		real = planes.get(0).clone();  //実部
		img = planes.get(1).clone();   //虚部
		dst = complexI2.clone();


/*逆フーリエのテスト
		Mat Test = Mat.zeros(240, 320, CvType.CV_32FC2);
		Core.idft(dst, Test);
		List<Mat> planes1 = new ArrayList<Mat>();
		Core.split(Test, planes1);
		Mat test2 = Mat.zeros(240, 320, CvType.CV_8UC1);
		Core.normalize(planes1.get(0), test2, 0, 255, Core.NORM_MINMAX);
		//Imgcodecs.imwrite("逆フーリエの画像.jpg", test2);  //デバッグ用
*/

/*フーリエ変換の確認テスト
		List<Mat> planes2 = new ArrayList<Mat>();
		planes2.add(real);
		planes2.add(img);
		Mat A = Mat.zeros(padded.size(), CvType.CV_32FC1);
		Mat B = Mat.zeros(padded.size(), CvType.CV_32FC1);
		Core.merge(planes, A);
		List<Mat> planes3 = new ArrayList<Mat>();

		Core.idft(A, B);
		Mat restoredImage = new Mat();
		Core.split(B, planes3);
		Core.normalize(planes3.get(0), restoredImage, 0, 255, Core.NORM_MINMAX);
		Imgcodecs.imwrite("idft.jpg", restoredImage);*/




		//デバッグ用
		//Imgcodecs.imwrite("real.jpg", real);
		//Imgcodecs.imwrite("img.jpg", img);



		/*
		Mat mag = new Mat(planes.get(0).size(), planes.get(0).type());
		Core.magnitude(planes.get(0), planes.get(1), mag);

		Mat magI = mag;
		Mat magI2 = new Mat(magI.size(), magI.type());
		Mat magI3 = new Mat(magI.size(), magI.type());
		Mat magI4 = new Mat(magI.size(), magI.type());
		Mat magI5 = new Mat(magI.size(), magI.type());

		// Normalize.
		Core.add(magI, Mat.ones(padded.size(), CvType.CV_32F), magI2);
		Core.log(magI2, magI3);

		// Swap, swap, swap.
		Mat crop = new Mat(magI3, new Rect(0, 0, magI3.cols() & -2, magI3.rows() & -2));
		magI4 = crop.clone();

		int cx = magI4.cols() / 2;
		int cy = magI4.rows() / 2;

		Rect q0Rect = new Rect(0, 0, cx, cy);
		Rect q1Rect = new Rect(cx, 0, cx, cy);
		Rect q2Rect = new Rect(0, cy, cx, cy);
		Rect q3Rect = new Rect(cx, cy, cx, cy);

		Mat q0 = new Mat(magI4, q0Rect); // Top-Left
		Mat q1 = new Mat(magI4, q1Rect); // Top-Right
		Mat q2 = new Mat(magI4, q2Rect); // Bottom-Left
		Mat q3 = new Mat(magI4, q3Rect); // Bottom-Right

		Mat tmp = new Mat();
		q0.copyTo(tmp);
		q3.copyTo(q0);
		tmp.copyTo(q3);

		q1.copyTo(tmp);
		q2.copyTo(q1);
		tmp.copyTo(q2);

		Core.normalize(magI4, magI5, 0, 255, Core.NORM_MINMAX);

		// Convert image.
		Mat realResult = new Mat(magI5.size(), CvType.CV_32FC1);
		magI5.convertTo(realResult, CvType.CV_32FC1);

		return realResult;
		*/
	}
}
