package surfDetector;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.*;
import org.opencv.features2d.*;
import org.opencv.highgui.Highgui;
import org.opencv.ml.CvSVM;
import org.opencv.ml.CvSVMParams;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Kinath on 8/6/2016.
 */
public class SURFDetector {

    public static void main(String[] args) {

        File lib = null;
        String os = System.getProperty("os.name");
        String bitness = System.getProperty("sun.arch.data.model");

        if (os.toUpperCase().contains("WINDOWS")) {
            if (bitness.endsWith("64")) {
                lib = new File("libs//x64//" + System.mapLibraryName("opencv_java2411"));
            } else {
                lib = new File("libs//x86//" + System.mapLibraryName("opencv_java2411"));
            }
        }

        System.out.println(lib.getAbsolutePath());
        System.load(lib.getAbsolutePath());

        String bookObject = "images//cs.jpg";
        String bookScene = "images//setofgames.jpg";

        System.out.println("Started....");
        System.out.println("Loading images...");
        Mat objectImage = Highgui.imread(bookObject, Highgui.CV_LOAD_IMAGE_COLOR);
        Mat sceneImage = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);
        
        MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
        FeatureDetector featureDetector = FeatureDetector.create(FeatureDetector.SURF);
        System.out.println("Detecting key points...");
        featureDetector.detect(objectImage, objectKeyPoints);
        KeyPoint[] keypoints = objectKeyPoints.toArray();
        System.out.println(keypoints);

        MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
        DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(DescriptorExtractor.SURF);
        System.out.println("Computing descriptors...");
        descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

        // Create the matrix for output image.
        Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar newKeypointColor = new Scalar(255, 0, 0);

        System.out.println("Drawing key points on object image...");
        Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

        // Match object image with the scene image
        MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
        MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
        System.out.println("Detecting key points in background image...");
        featureDetector.detect(sceneImage, sceneKeyPoints);
        System.out.println("Computing descriptors in background image...");
        descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

        Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
        Scalar matchestColor = new Scalar(0, 255, 0);

        List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
        DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
        System.out.println("Matching object and scene images...");
        descriptorMatcher.knnMatch(objectDescriptors, sceneDescriptors, matches, 2);

        System.out.println("Calculating good match list...");
        LinkedList<DMatch> goodMatchesList = new LinkedList<DMatch>();

        float nndrRatio = 0.7f;

        for (int i = 0; i < matches.size(); i++) {
            MatOfDMatch matofDMatch = matches.get(i);
            DMatch[] dmatcharray = matofDMatch.toArray();
            DMatch m1 = dmatcharray[0];
            DMatch m2 = dmatcharray[1];

            if (m1.distance <= m2.distance * nndrRatio) {
                goodMatchesList.addLast(m1);

            }
        }

        if (goodMatchesList.size() >= 7) {
            System.out.println("Object Found!!!");

            List<KeyPoint> objKeypointlist = objectKeyPoints.toList();
            List<KeyPoint> scnKeypointlist = sceneKeyPoints.toList();

            LinkedList<Point> objectPoints = new LinkedList<>();
            LinkedList<Point> scenePoints = new LinkedList<>();

            for (int i = 0; i < goodMatchesList.size(); i++) {
                objectPoints.addLast(objKeypointlist.get(goodMatchesList.get(i).queryIdx).pt);
                scenePoints.addLast(scnKeypointlist.get(goodMatchesList.get(i).trainIdx).pt);
            }

            MatOfPoint2f objMatOfPoint2f = new MatOfPoint2f();
            objMatOfPoint2f.fromList(objectPoints);
            MatOfPoint2f scnMatOfPoint2f = new MatOfPoint2f();
            scnMatOfPoint2f.fromList(scenePoints);

            Mat homography = Calib3d.findHomography(objMatOfPoint2f, scnMatOfPoint2f, Calib3d.RANSAC, 3);

            Mat obj_corners = new Mat(4, 1, CvType.CV_32FC2);
            Mat scene_corners = new Mat(4, 1, CvType.CV_32FC2);

            obj_corners.put(0, 0, new double[]{0, 0});
            obj_corners.put(1, 0, new double[]{objectImage.cols(), 0});
            obj_corners.put(2, 0, new double[]{objectImage.cols(), objectImage.rows()});
            obj_corners.put(3, 0, new double[]{0, objectImage.rows()});

            System.out.println("Transforming object corners to scene corners...");
            Core.perspectiveTransform(obj_corners, scene_corners, homography);

            Mat img = Highgui.imread(bookScene, Highgui.CV_LOAD_IMAGE_COLOR);

            Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)), new Scalar(0, 255, 0), 4);
            Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)), new Scalar(0, 255, 0), 4);

            System.out.println("Drawing matches image...");
            MatOfDMatch goodMatches = new MatOfDMatch();
            goodMatches.fromList(goodMatchesList);

            Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches, matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);

            Highgui.imwrite("output//outputImage.jpg", outputImage);
            Highgui.imwrite("output//matchoutput.jpg", matchoutput);
            Highgui.imwrite("output//img.jpg", img);
        } else {
            System.out.println("Object Not Found");
        }
        
        //SVM
        
		Mat classes = new Mat();
        Mat trainingData = new Mat();
        Mat trainingImages = new Mat();
        Mat trainingLabels = new Mat();
        CvSVM clasificador;
        String path="images//cs.jpg";
		for (File file : new File(path).listFiles()) {
            Mat img=new Mat();   
            Mat con = Highgui.imread(path+"\\"+file.getName(),Highgui.CV_LOAD_IMAGE_GRAYSCALE);
            con.convertTo(img, CvType.CV_32FC1,1.0/255.0);

            img.reshape(1, 1);
            trainingImages.push_back(img);
            trainingLabels.push_back(Mat.ones(new Size(1, 75), CvType.CV_32FC1));
        }
        System.out.println("divide");
        path="images//setofgames.jpg";
        for (File file : new File(path).listFiles()) {
                Mat img=new Mat();
                Mat m=new Mat(new Size(640,480),CvType.CV_32FC1);
                Mat con = Highgui.imread(file.getAbsolutePath(),Highgui.CV_LOAD_IMAGE_GRAYSCALE);

                con.convertTo(img, CvType.CV_32FC1,1.0/255.0);
                img.reshape(1, 1);
                trainingImages.push_back(img);

                trainingLabels.push_back(Mat.zeros(new Size(1, 75), CvType.CV_32FC1));

        }

        trainingLabels.copyTo(classes);        
        CvSVMParams params = new CvSVMParams();
        params.set_kernel_type(CvSVM.RBF);
        /*
         * Type of a SVM kernel. Possible values are:
			LINEAR Linear kernel. No mapping is done, linear discrimination (or regression) is done in the original feature space. It is the fastest option. K(x_i, x_j) = x_i^T x_j.
			POLY Polynomial kernel: K(x_i, x_j) = (\gamma x_i^T x_j + coef0)^{degree}, \gamma > 0.
			RBF Radial basis function (RBF), a good choice in most cases. K(x_i, x_j) = e^{-\gamma ||x_i - x_j||^2}, \gamma > 0.
			SIGMOID Sigmoid kernel: K(x_i, x_j) = \tanh(\gamma x_i^T x_j + coef0).
         */
        CvType.typeToString(trainingImages.type());
        CvSVM svm=new CvSVM();
        clasificador = new CvSVM(trainingImages,classes, new Mat(), new Mat(), params);

        clasificador.save("images//svm.xml");
        Mat out=new Mat();

        clasificador.load("mages//svm.xml");
        Mat sample=Highgui.imread("images//cs.jpg",Highgui.CV_LOAD_IMAGE_GRAYSCALE);

        sample.convertTo(out, CvType.CV_32FC1,1.0/255.0);               
        out.reshape(1, 75);
        System.out.println(clasificador.predict(out));

        System.out.println("Ended....");
    }
}
