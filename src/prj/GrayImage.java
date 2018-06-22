package prj;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

public class GrayImage {

	Mat grayImage = new Mat();

	//グレースケールに変換
	public GrayImage(Mat[] src) {
		grayImage = Mat.zeros(src[0].size(), CvType.CV_32F);
		Imgproc.cvtColor(src[0], grayImage, Imgproc.COLOR_RGB2GRAY);
		grayImage.convertTo(grayImage, CvType.CV_32FC1);

		//Imgcodecs.imwrite("gray.jpg", grayImage); //デバッグ用

		//画像の最大画素値を調べる
		MinMaxLocResult maxv = Core.minMaxLoc(grayImage);
		double ma = maxv.maxVal;

		//画素値を最大値で割って正規化する
		int W = grayImage.cols(); //320
		int H = grayImage.rows(); //240
		for (int x = 0; x < H; x++) {
			for (int y = 0; y < W; y++) {
				double[] s = grayImage.get(x, y);
				double ans = 0;
				ans = s[0] / ma;
				grayImage.put(x, y, ans);
			}
		}
		//Imgcodecs.imwrite("test1.jpg", grayImage); //デバッグ用
	}
}
