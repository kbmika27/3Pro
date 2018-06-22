package prj;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class CorrectImage {

	Mat src;
	Mat dst;

	//ファイルの読み込み
	public CorrectImage(Mat[] src, int centerX, int centerY) {  //横・たて

		this.src = new Mat(240, 320, CvType.CV_8UC3);
		src[0].copyTo(this.src);
		dst = getCI(centerX, centerY);
	}

	private Mat getCI(int centerX, int centerY) {
		int width;
		int height;
		int x, y, s, t = 0;
		double dst;

		width = src.cols();  //横
		height = src.rows(); //縦

		Mat dst_mat = new Mat(height, width, CvType.CV_32FC1);
		dst_mat.put(centerY, centerX, 255);

		for (y = 0; y < height; y++) {
			for (x = 0; x < width; x++) {
				dst = 255 * (Math
						.exp((-1) * (((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y)) / (2 * 2 * 2))));
				dst_mat.put(y, x, dst);
			}
		}
		Imgcodecs.imwrite("正解クラスの正解画像.jpg", dst_mat); //デバッグ用

		MinMaxLocResult maxv = Core.minMaxLoc(dst_mat);
		double ma = maxv.maxVal;

		for (s = 0; s < height; s++) {
			for (t = 0; t < width; t++) {
				double[] i = dst_mat.get(s, t);
				double ans = 0;
				ans = i[0]/ma;
				dst_mat.put(s, t, ans);
			}
		}
		return dst_mat;
	}
}
