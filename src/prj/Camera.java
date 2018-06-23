package prj;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.videoio.VideoCapture;

public class Camera extends JPanel {

	private static final long serialVersionUID = 1L;
	private BufferedImage image;
	public List<Mat> src = new ArrayList<Mat>();
	public List<Integer> list = new ArrayList<Integer>();

	// Create a constructor method
	public Camera() {
		super();

	}

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
	}

	public void paintComponent(Graphics g) {
		BufferedImage temp = getimage();
		if (temp != null) {
			g.drawImage(temp, 10, 10, temp.getWidth(), temp.getHeight(), this);
		}
	}

	public void Cam( ) {
		// Load the native library.
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		JFrame frame = new JFrame("CameraImage");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(500, 500);
		Camera panel = new Camera();
		frame.setContentPane(panel);
		frame.setVisible(true);
		Mat webcam_image = new Mat();
		BufferedImage temp;
		VideoCapture capture = new VideoCapture(0);

		if (capture.isOpened()) {
			while (true) {
				capture.read(webcam_image);
				if (!webcam_image.empty()) {
					CascadeClassifier faceDetector = new CascadeClassifier("haarcascade_frontalface_default.xml");
					MatOfRect faceDetections = new MatOfRect();
					faceDetector.detectMultiScale(webcam_image, faceDetections);

					/*
					Mat[] Grays = new Mat[1];
					Mat[] src = new Mat[1];
					src[0] = webcam_image.clone();
					GrayImage GI = new GrayImage(src);
					Grays[0] = GI.grayImage;
					//グレースケール画像をフーリエ変換し、配列に読み込む
					Fourier fft = new Fourier(src, Grays);
					webcam_image = fft.real.clone();
					*/

					src.add(webcam_image);
					for (Rect rect : faceDetections.toArray()) {
						list.add(rect.x);
						list.add(rect.y);
						list.add(rect.width);
						list.add(rect.height);

						Imgproc.rectangle(webcam_image, new Point(rect.x, rect.y),
								new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 0, 255), 5);
					}

					Imgproc.resize(webcam_image, webcam_image,
							new Size(webcam_image.size().width * 0.3, webcam_image.size().height * 0.3));
					frame.setSize(webcam_image.width() + 40, webcam_image.height() + 60);
					temp = matToBufferedImage(webcam_image);
					panel.setimage(temp);
					panel.repaint();
				} else {
					System.out.println(" --(!) No captured frame -- ");
				}
			}
		}
	}
}