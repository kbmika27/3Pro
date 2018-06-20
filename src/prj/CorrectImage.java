package prj;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class CorrectImage {

	Mat src;
	Mat dst;

	//ファイルの読み込み
	public CorrectImage(Mat src, int centerX, int centerY) {
		this.src = src;
		dst = getCI(centerX, centerY);
	}

	private Mat getCI(int centerX, int centerY) {
		int width;
		int height;
		int x, y, s, t = 0;
		double dst;

		width = src.cols();
		height = src.rows();

		Mat dst_mat = new Mat(height, width, CvType.CV_32FC1);
		dst_mat.put(centerX, centerY, 255);

		for (x = 0; x < height; x++) {
			for (y = 0; y < width; y++) {
				dst = 255 * (Math
						.exp((-1) * (((centerX - x) * (centerX - x) + (centerY - y) * (centerY - y)) / (2 * 2 * 2))));
				dst_mat.put(x+1, y+1, dst);
			}
		}

		//Imgcodecs.imwrite("CI.jpg", dst_mat); //デバッグ用

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

		//Imgcodecs.imwrite("CI.jpg", dst_mat); //デバッグ用

		return dst_mat;
	}
}
