package surfDetector;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.opencv.calib3d.Calib3d;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfDMatch;
import org.opencv.core.MatOfKeyPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.features2d.DMatch;
import org.opencv.features2d.DescriptorExtractor;
import org.opencv.features2d.DescriptorMatcher;
import org.opencv.features2d.FeatureDetector;
import org.opencv.features2d.Features2d;
import org.opencv.features2d.KeyPoint;
import org.opencv.highgui.Highgui;

public class FeatureDetectorJava {

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

		List<String> games = new ArrayList<String>();
		
		String game1 = "Counter-Strike_Global_Offensive"; 	games.add(game1);
		String game2 = "Dota_2";							games.add(game2);
		String game3 = "Grand_Theft_Auto_V"; 				games.add(game3);
		String game4 = "Hearthstone"; 						games.add(game4);
		String game5 = "League_of_Legends"; 				games.add(game5);
		String game6 = "Minecraft"; 						games.add(game6);
		String game7 = "Overwatch"; 						games.add(game7);
		String game8 = "Path_of_Exile"; 					games.add(game8);
		String game9 = "PLAYERUNKNOWNS_BATTLEGROUNDS"; 		games.add(game9);
		String game10 = "The_Elder_Scrolls_Online"; 		games.add(game10);
		
		List<String> pathTests = new ArrayList<String>();
		List<String> trainingPositives=new ArrayList<String>();
		List<String> trainingNegatives=new ArrayList<String>();
		
		String pathTestGame1 ="D://pedro//0da2//images500//Test_images//Counter-Strike_Global_Offensive"; 	pathTests.add(pathTestGame1);
		String pathTestGame2 ="D://pedro//0da2//images500//Test_images//Dota_2"; 							pathTests.add(pathTestGame2);
		String pathTestGame3 ="D://pedro//0da2//images500//Test_images//Grand_Theft_Auto_V"; 				pathTests.add(pathTestGame3);
		String pathTestGame4 ="D://pedro//0da2//images500//Test_images//Hearthstone"; 						pathTests.add(pathTestGame4);
		String pathTestGame5 ="D://pedro//0da2//images500//Test_images//League_of_Legends"; 				pathTests.add(pathTestGame5);
		String pathTestGame6 ="D://pedro//0da2//images500//Test_images//Minecraft"; 						pathTests.add(pathTestGame6);
		String pathTestGame7 ="D://pedro//0da2//images500//Test_images//Overwatch"; 						pathTests.add(pathTestGame7);
		String pathTestGame8 ="D://pedro//0da2//images500//Test_images//Path_of_Exile"; 					pathTests.add(pathTestGame8);
		String pathTestGame9 ="D://pedro//0da2//images500//Test_images//PLAYERUNKNOWNS_BATTLEGROUNDS";	 	pathTests.add(pathTestGame9);
		String pathTestGame10 ="D://pedro//0da2//images500//Test_images//The_Elder_Scrolls_Online"; 		pathTests.add(pathTestGame10);		
		
		String trainingPositiveGame1 ="D://pedro//0da2//images500//Training_Images//Counter-Strike_Global_Offensive//positive400set//400set.jpg"; 	trainingPositives.add(trainingPositiveGame1);
		String trainingPositiveGame2 ="D://pedro//0da2//images500//Training_Images//Dota_2//positive400set//400set.jpg"; 							trainingPositives.add(trainingPositiveGame2);
		String trainingPositiveGame3 ="D://pedro//0da2//images500//Training_Images//Grand_Theft_Auto_V//positive400set//400set.jpg";				trainingPositives.add(trainingPositiveGame3);
		String trainingPositiveGame4 ="D://pedro//0da2//images500//Training_Images//Hearthstone//positive400set//400set.jpg"; 						trainingPositives.add(trainingPositiveGame4);
		String trainingPositiveGame5 ="D://pedro//0da2//images500//Training_Images//League_of_Legends//positive400set//400set.jpg"; 				trainingPositives.add(trainingPositiveGame5);
		String trainingPositiveGame6 ="D://pedro//0da2//images500//Training_Images//Minecraft//positive400set//400set.jpg"; 						trainingPositives.add(trainingPositiveGame6);
		String trainingPositiveGame7 ="D://pedro//0da2//images500//Training_Images//Overwatch//positive400set//400set.jpg"; 						trainingPositives.add(trainingPositiveGame7);
		String trainingPositiveGame8 ="D://pedro//0da2//images500//Training_Images//Path_of_Exile//positive400set//400set.jpg"; 					trainingPositives.add(trainingPositiveGame8);
		String trainingPositiveGame9 ="D://pedro//0da2//images500//Training_Images//PLAYERUNKNOWNS_BATTLEGROUNDS//positive400set//400set.jpg"; 		trainingPositives.add(trainingPositiveGame9);
		String trainingPositiveGame10 ="D://pedro//0da2//images500//Training_Images//The_Elder_Scrolls_Online//positive400set//400set.jpg"; 		trainingPositives.add(trainingPositiveGame10);
		
		String trainingNegativeGame1 ="D://pedro//0da2//images500//Training_Images//Counter-Strike_Global_Offensive//negative400set//400set.jpg"; 	trainingNegatives.add(trainingNegativeGame1);
		String trainingNegativeGame2 ="D://pedro//0da2//images500//Training_Images//Dota_2//negative400set//400set.jpg"; 							trainingNegatives.add(trainingNegativeGame2);
		String trainingNegativeGame3 ="D://pedro//0da2//images500//Training_Images//Grand_Theft_Auto_V//negative400set//400set.jpg"; 				trainingNegatives.add(trainingNegativeGame3);
		String trainingNegativeGame4 ="D://pedro//0da2//images500//Training_Images//Hearthstone//negative400set//400set.jpg"; 						trainingNegatives.add(trainingNegativeGame4); 
		String trainingNegativeGame5 ="D://pedro//0da2//images500//Training_Images//League_of_Legends//negative400set//400set.jpg"; 				trainingNegatives.add(trainingNegativeGame5);
		String trainingNegativeGame6 ="D://pedro//0da2//images500//Training_Images//Minecraft//negative400set//400set.jpg"; 						trainingNegatives.add(trainingNegativeGame6);
		String trainingNegativeGame7 ="D://pedro//0da2//images500//Training_Images//Overwatch//negative400set//400set.jpg"; 						trainingNegatives.add(trainingNegativeGame7);
		String trainingNegativeGame8 ="D://pedro//0da2//images500//Training_Images//Path_of_Exile//negative400set//400set.jpg"; 					trainingNegatives.add(trainingNegativeGame8);
		String trainingNegativeGame9 ="D://pedro//0da2//images500//Training_Images//PLAYERUNKNOWNS_BATTLEGROUNDS//negative400set//400set.jpg"; 		trainingNegatives.add(trainingNegativeGame9);
		String trainingNegativeGame10 ="D://pedro//0da2//images500//Training_Images//The_Elder_Scrolls_Online//negative400set//400set.jpg"; 		trainingNegatives.add(trainingNegativeGame10);
		
		int descriptorExtractorSelection = DescriptorExtractor.SIFT;
		/* The current implementation supports the following types of a descriptor extractor:
			• "SIFT" -- "SIFT" 
			• "SURF" -- "SURF" 
			• "BRIEF" -- "BriefDescriptorExtractor" 
			• "BRISK" -- "BRISK" 
			• "ORB" -- "ORB" 
			• "FREAK" -- "FREAK"
		 */
			
		int featureDetectorSelection = FeatureDetector.SIFT;		
		/*The following detector types are supported:
			• "FAST" -- "FastFeatureDetector" 
			• "STAR" -- "StarFeatureDetector" 
			• "SIFT" -- "SIFT" (nonfree module) 
			• "SURF" -- "SURF" (nonfree module) 
			• "ORB" -- "ORB" 
			• "BRISK" -- "BRISK" 
			• "MSER" -- "MSER" 
			• "GFTT" -- "GoodFeaturesToTrackDetector" 
			• "HARRIS" -- "GoodFeaturesToTrackDetector" with Harris detector enabled 
			• "Dense" -- "DenseFeatureDetector" 
			• "SimpleBlob" -- "SimpleBlobDetector" 
		*/
		
		// Test positive image set

		for (int ii = 0; ii < trainingPositives.size(); ii++) {
			Mat sceneImage = Highgui.imread(trainingPositives.get(ii).toString(), Highgui.CV_LOAD_IMAGE_COLOR);

			// Match object image with the scene image
			MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
			MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
			FeatureDetector featureDetector = FeatureDetector.create(featureDetectorSelection);
			DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(descriptorExtractorSelection);

			System.out.println("Detecting key points in background image...");
			featureDetector.detect(sceneImage, sceneKeyPoints);
			System.out.println("Computing descriptors in background image...");
			descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

			Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
			Scalar matchestColor = new Scalar(0, 255, 0);

			List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
			DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);
			
			int attempt = 0;
			for (File file : new File(pathTests.get(ii).toString()).listFiles()) {
				attempt = attempt + 1;
				System.out.println("Game: "+games.get(ii).toString()+" *** POSITIVE TEST *** Attempt........................................" + attempt);
				Mat objectImage = Highgui.imread(pathTests.get(ii).toString() + "\\" + file.getName(),Highgui.CV_LOAD_IMAGE_COLOR);

				System.out.println("Started....");
				System.out.println("Loading images...");

				MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
				System.out.println("Detecting key points...");
				featureDetector.detect(objectImage, objectKeyPoints);
				KeyPoint[] keypoints = objectKeyPoints.toArray();
				System.out.println(keypoints);

				MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
				System.out.println("Computing descriptors...");
				descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

				// Create the matrix for output image.
				Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
				Scalar newKeypointColor = new Scalar(255, 0, 0);

				System.out.println("Drawing key points on object image...");
				Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

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
				
				//The control parameter is min 7 points, it means if there's 7 or more points on the test image AND on the set of images the result is positive

				if (goodMatchesList.size() >= 7) {
					System.out.println("Game Found!!!");

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

					obj_corners.put(0, 0, new double[] { 0, 0 });
					obj_corners.put(1, 0, new double[] { objectImage.cols(), 0 });
					obj_corners.put(2, 0, new double[] { objectImage.cols(), objectImage.rows() });
					obj_corners.put(3, 0, new double[] { 0, objectImage.rows() });

					System.out.println("Transforming object corners to scene corners...");
					Core.perspectiveTransform(obj_corners, scene_corners, homography);

					Mat img = Highgui.imread(trainingPositives.get(ii).toString(), Highgui.CV_LOAD_IMAGE_COLOR);

					Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)),
							new Scalar(0, 255, 0), 4);

					System.out.println("Drawing matches image...");
					MatOfDMatch goodMatches = new MatOfDMatch();
					goodMatches.fromList(goodMatchesList);

					Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches,
							matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);
					
					Highgui.imwrite("outputs//" + games.get(ii).toString() + "//positive//matchoutput_" + attempt + ".jpg",	matchoutput);
					
				} else {
					System.out.println("Object Not Found");
				}
			}
		}

		// Test negative image set
		for (int ii = 0; ii < trainingNegatives.size(); ii++) {
			Mat sceneImage = Highgui.imread(trainingNegatives.get(ii).toString(), Highgui.CV_LOAD_IMAGE_COLOR);

			// Match object image with the scene image
			MatOfKeyPoint sceneKeyPoints = new MatOfKeyPoint();
			MatOfKeyPoint sceneDescriptors = new MatOfKeyPoint();
			FeatureDetector featureDetector = FeatureDetector.create(featureDetectorSelection);
			DescriptorExtractor descriptorExtractor = DescriptorExtractor.create(descriptorExtractorSelection);

			System.out.println("Detecting key points in background image...");
			featureDetector.detect(sceneImage, sceneKeyPoints);
			System.out.println("Computing descriptors in background image...");
			descriptorExtractor.compute(sceneImage, sceneKeyPoints, sceneDescriptors);

			Mat matchoutput = new Mat(sceneImage.rows() * 2, sceneImage.cols() * 2, Highgui.CV_LOAD_IMAGE_COLOR);
			Scalar matchestColor = new Scalar(0, 255, 0);

			List<MatOfDMatch> matches = new LinkedList<MatOfDMatch>();
			DescriptorMatcher descriptorMatcher = DescriptorMatcher.create(DescriptorMatcher.FLANNBASED);

			int attempt = 0;
			for (File file : new File(pathTests.get(ii).toString()).listFiles()) {
				attempt = attempt + 1;
				System.out.println("Game: "+games.get(ii).toString()+" *** NEGATIVE TEST *** Attempt........................................" + attempt);
				Mat objectImage = Highgui.imread(pathTests.get(ii).toString() + "\\" + file.getName(),Highgui.CV_LOAD_IMAGE_COLOR);

				System.out.println("Started....");
				System.out.println("Loading images...");

				MatOfKeyPoint objectKeyPoints = new MatOfKeyPoint();
				System.out.println("Detecting key points...");
				featureDetector.detect(objectImage, objectKeyPoints);
				KeyPoint[] keypoints = objectKeyPoints.toArray();
				System.out.println(keypoints);

				MatOfKeyPoint objectDescriptors = new MatOfKeyPoint();
				System.out.println("Computing descriptors...");
				descriptorExtractor.compute(objectImage, objectKeyPoints, objectDescriptors);

				// Create the matrix for output image.
				Mat outputImage = new Mat(objectImage.rows(), objectImage.cols(), Highgui.CV_LOAD_IMAGE_COLOR);
				Scalar newKeypointColor = new Scalar(255, 0, 0);

				System.out.println("Drawing key points on object image...");
				Features2d.drawKeypoints(objectImage, objectKeyPoints, outputImage, newKeypointColor, 0);

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
				//The control parameter is min 7 points, it means if there's 7 or more points on the test image AND on the set of images the result is positive
				if (goodMatchesList.size() >= 7) {
					System.out.println("Game Found!!!");

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

					obj_corners.put(0, 0, new double[] { 0, 0 });
					obj_corners.put(1, 0, new double[] { objectImage.cols(), 0 });
					obj_corners.put(2, 0, new double[] { objectImage.cols(), objectImage.rows() });
					obj_corners.put(3, 0, new double[] { 0, objectImage.rows() });

					System.out.println("Transforming object corners to scene corners...");
					Core.perspectiveTransform(obj_corners, scene_corners, homography);

					Mat img = Highgui.imread(trainingNegatives.get(ii).toString(), Highgui.CV_LOAD_IMAGE_COLOR);

					Core.line(img, new Point(scene_corners.get(0, 0)), new Point(scene_corners.get(1, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(1, 0)), new Point(scene_corners.get(2, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(2, 0)), new Point(scene_corners.get(3, 0)),
							new Scalar(0, 255, 0), 4);
					Core.line(img, new Point(scene_corners.get(3, 0)), new Point(scene_corners.get(0, 0)),
							new Scalar(0, 255, 0), 4);

					System.out.println("Drawing matches image...");
					MatOfDMatch goodMatches = new MatOfDMatch();
					goodMatches.fromList(goodMatchesList);

					Features2d.drawMatches(objectImage, objectKeyPoints, sceneImage, sceneKeyPoints, goodMatches,
							matchoutput, matchestColor, newKeypointColor, new MatOfByte(), 2);
					Highgui.imwrite("outputs//"+games.get(ii).toString()+"//negative//matchoutput_"+attempt+".jpg",matchoutput);

				} else {
					System.out.println("Object Not Found");
				}
			}
		}		

		System.out.println("Ended....");
	}
}
