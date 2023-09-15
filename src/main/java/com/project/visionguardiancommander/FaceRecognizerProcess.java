package com.project.visionguardiancommander;


import java.io.File;
import java.io.FilenameFilter;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.bytedeco.javacpp.opencv_core.*;

import static org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer.*;

import org.bytedeco.javacpp.opencv_core.CvRect;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacpp.opencv_core.MatVector;

import org.bytedeco.javacpp.opencv_face.LBPHFaceRecognizer;
import org.bytedeco.javacpp.opencv_face.*;
import org.bytedeco.javacpp.opencv_objdetect.CascadeClassifier;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfRect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;

import static org.bytedeco.javacpp.opencv_imgcodecs.imread;
import static org.bytedeco.javacpp.opencv_imgcodecs.IMREAD_GRAYSCALE;

import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.DoublePointer;




public class FaceRecognizerProcess {

    LBPHFaceRecognizer lbphRecognizer;
    private Map<Integer, String> labelToPersonMap;

    public FaceRecognizerProcess() {
        lbphRecognizer = LBPHFaceRecognizer.create();
        labelToPersonMap = new HashMap<>();
    }

    public void trainRecognizer(Map<String, List<IplImage>> trainingData) {
        // Train the recognizer with the provided training data
        List<Mat> images = new ArrayList<>();
        List<Integer> labels = new ArrayList<>();

        //debug
        for (Map.Entry<String, List<IplImage>> entry : trainingData.entrySet()) {
            String person = entry.getKey();
            List<IplImage> imagesDebug = entry.getValue();

            System.out.println("Person: " + person);

            for (IplImage image : imagesDebug) {
                System.out.println("Image: " + image);
            }
        }

        for (String person : trainingData.keySet()) {
            // Extract the numeric part (e.g., "u1" becomes 1) from the uniqueCode
            int label = Integer.parseInt(person.substring(1));
            labelToPersonMap.put(label, person);

            for (IplImage image : trainingData.get(person)) {
                // Load the image in grayscale
                // Convert the IplImage to grayscale
                IplImage grayscaleImage = cvCreateImage(cvGetSize(image), IPL_DEPTH_8U, 1);
                cvCvtColor(image, grayscaleImage, CV_BGR2GRAY);

                // Convert the grayscale IplImage to Mat
                Mat img = cvarrToMat(grayscaleImage);
                images.add(img);
                labels.add(label);
            }
        }

        MatVector imagesMat = new MatVector(images.toArray(new Mat[0]));
        Mat labelsMat = new Mat(labels.size(), 1, CV_32SC1, new IntPointer(labels.stream().mapToInt(Integer::intValue).toArray()));

        lbphRecognizer.train(imagesMat, labelsMat);
    }
  /*
    public ArrayList<String> recognizeFace(IplImage testImage) {
        ArrayList<String> resultList = new ArrayList<>(2);
        Mat testImageMat = cvarrToMat(testImage);
        cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

        // Convert Mat to UMat
        UMat testImageUMat = new UMat();
        testImageMat.copyTo(testImageUMat);

        int[] label = new int[1];
        double[] confidence = new double[1]; // Change data type to double

        lbphRecognizer.predict(testImageUMat, label, confidence);

        int predictedLabel = label[0];
        double predictedConfidence = confidence[0]; // Correct data type

        String recognizedPerson = labelToPersonMap.get(predictedLabel);

        System.out.println("Confidence Level: " + (100.0 - predictedConfidence)); // Ensure proper output format
        System.out.println("Predicted Label: " + predictedLabel);

        if (predictedConfidence < CONFIDENCE_THRESHOLD) {
            resultList.add(recognizedPerson);
            resultList.add(Double.toString(100.0 - predictedConfidence));
            return resultList;
        } else {
            resultList.add("unknown");
            resultList.add(Double.toString(100.0 - 1.0));
            System.out.println(resultList.get(0));
            return resultList;
        }
    }
*/

    public ArrayList<String> recognizeFace(IplImage testImage) {
        ArrayList<String> resultList = new ArrayList<>(2);
        Mat testImageMat = cvarrToMat(testImage);
        cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

        // Convert Mat to UMat
        UMat testImageUMat = new UMat();
        testImageMat.copyTo(testImageUMat);

        int[] label = new int[1];
        double[] confidence = new double[1];

        lbphRecognizer.predict(testImageUMat, label, confidence);

        int predictedLabel = label[0];
        int predictedConfidence = (int)confidence[0];

        String recognizedPerson = labelToPersonMap.get(predictedLabel);

        System.out.println("Confidence Level" + (100.0-predictedConfidence));
        System.out.println(predictedLabel);

        if (predictedConfidence < CONFIDENCE_THRESHOLD) {
            resultList.add(recognizedPerson);
            resultList.add(Double.toString(100.0 - predictedConfidence));
            return resultList;
        } else {

            resultList.add("unknown");
            resultList.add(Double.toString(100.0 - 1.0)); //to show understandable confidence level for the layperson
            System.out.println(resultList.get(0));
            return resultList;
        }
    }


    private Mat cvmatFromIplImage(IplImage iplImage) {
        return new Mat(iplImage);
    }

    // Define a confidence threshold for recognition
    private static final int CONFIDENCE_THRESHOLD = 55;

}



/*
public void trainRecognizer(Map<String, List<IplImage>> trainingData) {
    // Train the recognizer with the provided training data
    int label = 0;
    List<Mat> images = new ArrayList<>();
    List<Integer> labels = new ArrayList<>();

    for (String person : trainingData.keySet()) {
        label++;
        labelToPersonMap.put(label, person);

        for (IplImage image : trainingData.get(person)) {
            images.add(cvmatFromIplImage(image));
            labels.add(label);
        }
    }

    MatVector imagesMat = new MatVector(images.toArray(new Mat[0]));
    Mat labelsMat = new Mat(labels.size(), 1, CV_32SC1, new IntPointer(labels.stream().mapToInt(Integer::intValue).toArray()));

    lbphRecognizer.train(imagesMat, labelsMat);
}
*/
/*
public String recognizeFace(IplImage testImage) {

	Mat testImageMat = cvarrToMat(testImage);

	cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

    int[] label = new int[1];

    double[] confidence = new double[1];

    lbphRecognizer.predict(testImageMat, label, confidence);

    int predictedLabel = label[0];
    double predictedConfidence = confidence[0];

    if (predictedConfidence < CONFIDENCE_THRESHOLD) {
        return labelToPersonMap.get(predictedLabel);
    }
    else {
        return "Unknown";
    }
}
*/
/*
public String recognizeFace(IplImage testImage) {
    // Convert IplImage to Mat
    Mat testImageMat = cvarrToMat(testImage);

    // Convert Mat to grayscale
    cvtColor(testImageMat, testImageMat, CV_BGR2GRAY);

    int[] label = new int[1];
    double[] confidence = new double[1];


    if (testImageMat.channels() != 1 || testImageMat.type() != CV_8U) {
        // Handle incorrect image format
        System.err.println("Invalid image format. Ensure it's grayscale and of type CV_8U.");
        return "Unknown";
    }

    lbphRecognizer.predict(testImageMat, label, confidence);

    int predictedLabel = label[0];
    double predictedConfidence = confidence[0];

    if (predictedConfidence < CONFIDENCE_THRESHOLD) {
        return labelToPersonMap.get(predictedLabel);
    }
    else {
        return "Unknown";
    }
}
*/

