package prj;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Main extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public List<Mat> src = new ArrayList<Mat>(); //入力画像のリスト
	public List<Integer> list = new ArrayList<Integer>(); //
	private static int First = 30;
	private static int now = 0;

	public static List<Point> data = new ArrayList<Point>();

	private BufferedImage getimage() {
		return image;
	}

	private void setimage(BufferedImage newimage) {
		image = newimage;
		return;
	}

	/**
	 * Converts/writes a Mat into a BufferedImage.
	 *
	 * @param matrix Mat of type CV_8UC3 or CV_8UC1
	 * @return BufferedImage of type TYPE_3BYTE_BGR or TYPE_BYTE_GRAY
	 */

	/*
	public static BufferedImage matToBufferedImage(Mat matrix) {
		int cols = matrix.cols();
		int rows = matrix.rows();
		int elemSize = (int) matrix.elemSize();
		byte[] data = new byte[cols * rows * elemSize];
		int type;
		matrix.get(0, 0, data);

		switch (matrix.channels()) {
		case 1:
			type = BufferedImage.TYPE_BYTE_GRAY;
			break;
		case 3:
			type = BufferedImage.TYPE_3BYTE_BGR;
			// bgr to rgb
			byte b;
			for (int i = 0; i < data.length; i = i + 3) {
				b = data[i];
				data[i] = data[i + 2];
				data[i + 2] = b;
			}
			break;
		default:
			return null;
		}


		BufferedImage image2 = new BufferedImage(cols, rows, type);
		image2.getRaster().setDataElements(0, 0, cols, rows, data);
		return image2;
	}*/

	public static BufferedImage convertMatToBufferedImage(Mat m) throws IOException {
		MatOfByte byteMat = new MatOfByte();
		Imgcodecs.imencode(".jpg", m, byteMat);
		InputStream in = new ByteArrayInputStream(byteMat.toArray());
		return ImageIO.read(in);
	}

	public static Mat WriteRec(Mat idft, Mat src, int width, int height) {
		Point p = getPos(idft);
		double x = p.x;
		double y = p.y;
		Imgproc.rectangle(src, new Point(x - width / 2, y - height / 2), new Point(x + width / 2, y + height / 2),
				new Scalar(0, 0, 255), 5);
		return src;
	}

	public static Point getPos(Mat m) {
		MinMaxLocResult max = Core.minMaxLoc(m);
		return max.maxLoc;
	}

	public void paintComponent(Graphics g) {
		BufferedImage temp = getimage();
		if (temp != null) {
			g.drawImage(temp, 10, 10, temp.getWidth(), temp.getHeight(), this);
		}
	}





	public static void main(String args[]) throws IOException {
		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		boolean isFirst = true;
		boolean makeFilter = true;
		List<Mat> planes = new ArrayList<Mat>();
		Mat[] SRC = new Mat[1];
		Mat[] Grays = new Mat[1];
		Mat[] CI = new Mat[1];
		Mat num = new Mat();
		Mat den = new Mat();
		Mat NUM = new Mat();
		Mat DEN = new Mat();
		Mat ANS = new Mat();
		Mat DST = new Mat();
		Scalar m = new Scalar(0.125);
		Scalar n = new Scalar(1 - 0.125);
		int facewidth = 0;
		int faceheight = 0;
		int ave_width = 0;
		int ave_height = 0;
		int count = 0;

		HighGui hi = new HighGui();

		/*JFrame frame = new JFrame("CameraImage");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		Main panel = new Main();
		frame.setContentPane(panel);
		frame.setVisible(true);*/
		hi.namedWindow("Main");
		Mat webcam_image = new Mat();
		//BufferedImage temp;
		VideoCapture capture = new VideoCapture(0);

		 // FileWriterクラスのオブジェクトを生成する
		 FileWriter file = new FileWriter("data1");
         // PrintWriterクラスのオブジェクトを生成する
         PrintWriter pw = new PrintWriter(new BufferedWriter(file));


		if (capture.isOpened()) {

			while (true) {

				//ビデオの読み込み
				capture.read(webcam_image);

				if (!webcam_image.empty()) {

					//顔認識
					CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
					MatOfRect faceDetections = new MatOfRect();

					SRC[0] = webcam_image.clone();
					Imgproc.resize(SRC[0], SRC[0], new Size(SRC[0].size().width * 0.3, SRC[0].size().height * 0.3));

					faceDetector.detectMultiScale(SRC[0], faceDetections);

					if (isFirst) {
						System.out.println("フィルター作り開始");
						num = Mat.zeros(SRC[0].size(), CvType.CV_32FC2); //初期化
						den = Mat.zeros(SRC[0].size(), CvType.CV_32FC2); //初期化
						NUM = Mat.zeros(SRC[0].size(), CvType.CV_32FC2); //初期化
						DEN = Mat.zeros(SRC[0].size(), CvType.CV_32FC2); //初期化
						ANS = Mat.zeros(SRC[0].size(), CvType.CV_32FC2); //初期化
						DST = Mat.zeros(SRC[0].size(), CvType.CV_8UC1); //初期化
						isFirst = false;
					}

					//入力画像のフーリエ変換を作る
					//グレースケール変換
					GrayImage GI = new GrayImage(SRC);
					Grays[0] = GI.grayImage;
					//グレースケール画像をフーリエ変換し、配列に読み込む
					Fourier fft = new Fourier(SRC, Grays);

					/*メモ
					 * フィルター作りとトラッキングのフェーズをわける
					 * 最近傍法で行動のデータを作る
					 * トラッキングの精度をあげる
					 * *正規化の方法を変える：資料参考
					 * *インプシロンかける
					 * 失敗判定を組み込む
					 *
					 * 行動分析フェーズ
					 * K最近傍法で行動のパターンを読み込む
					 */

					if (makeFilter) {
						//正解画像のフーリエ変換を作る
						//顔の範囲取得
						for (Rect rect : faceDetections.toArray()) {
							count++;
							Imgproc.rectangle(SRC[0], new Point(rect.x, rect.y),
									new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(255, 255, 0), 5);

							if (now < First) {

								//正解画像を作る
								CorrectImage ci = new CorrectImage(SRC, rect.x + rect.width / 2,
										rect.y + rect.height / 2);
								//正解画像をフーリエ変換する
								CI[0] = ci.dst.clone();
								Fourier fci = new Fourier(CI, CI);
								Core.mulSpectrums(fci.dst, fft.dst, num, 0, true); //1枚ずつの分子の計算
								Core.mulSpectrums(fft.dst, fft.dst, den, 0, true); //1枚ずつの分母の計算

								//顔のサイズの平均をとる
								facewidth += rect.width;
								faceheight += rect.height;
								ave_width = facewidth / count;
								ave_height = faceheight / count;

								Core.add(NUM, num, NUM); //分子の和
								Core.add(DEN, den, DEN); //分母の和
							} else {
								makeFilter = false;
							}
						}
						now++;

					} else {
						//System.out.println("トラッキング開始");
						Core.divide(NUM, DEN, ANS); //分子/分母

						//フィルターをかける
						Core.mulSpectrums(fft.dst, ANS, DST, 0, false);

						Core.idft(DST, DST);
						Core.split(DST, planes);
						data.add(getPos(planes.get(0)));
						Point p = getPos(planes.get(0));
						pw.print((int)p.x);
						pw.print(",");
						pw.print((int)p.y);
						pw.print(",");
						pw.print("\n");
						Core.normalize(planes.get(0), DST, 0, 255, Core.NORM_MINMAX);
						SRC[0] = WriteRec(DST, SRC[0], ave_width, ave_height);
					}

					//frame.setSize(SRC[0].width() + 40, SRC[0].height() + 40);
					hi.imshow("Main", SRC[0]);
					/*temp = convertMatToBufferedImage(SRC[0]);
					panel.setimage(temp);
					panel.repaint();*/
					if(hi.waitKey(100)!= -1) {
						break;
					}
				} else {
					System.out.println(" --(!) No captured frame -- ");
				}


			}
			capture.release();
		}
		hi.destroyAllWindows();
		pw.close();
	}
}