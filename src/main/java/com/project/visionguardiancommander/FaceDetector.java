package com.project.visionguardiancommander;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.imageio.ImageIO;
import javax.swing.*;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.javacpp.opencv_objdetect;
import org.bytedeco.javacpp.helper.opencv_core;
import org.bytedeco.javacpp.opencv_core.IplImage;
import org.bytedeco.javacpp.opencv_core.Mat;
import org.bytedeco.javacv.CanvasFrame;
import org.bytedeco.javacv.Frame;
import org.bytedeco.javacv.FrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.bytedeco.javacv.OpenCVFrameConverter;
import org.bytedeco.javacv.OpenCVFrameGrabber;
import static org.bytedeco.javacpp.opencv_core.*;
import static org.bytedeco.javacpp.opencv_imgproc.*;
import static org.bytedeco.javacpp.opencv_imgcodecs.*;
import static org.bytedeco.javacpp.opencv_objdetect.*;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart.Data;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.embed.swing.SwingFXUtils;


import com.project.visionguardiancommander.Database;
//import application.MotionDetector;
//import application.ColoredObjectTracker;
import com.project.visionguardiancommander.MainViewController;
import com.project.visionguardiancommander.FaceRecognizerProcess;



public class FaceDetector implements Runnable {


    private boolean stop = false;
    private OpenCVFrameGrabber grabber;
    private List<CvHaarClassifierCascade> classifiers;
    private CvMemStorage storage;
    private ImageView frames;
    private String framesDirectory; // Directory to save frames
    public static IplImage tempFrame; // Field to store the current frame


    public void init() {
        stop = false;
        classifiers = new ArrayList<>();
        setClassifier("haar/haarcascade_frontalface_alt.xml");
        //setClassifier("haar/haarcascade_eye.xml");
        setClassifier("haar/haarcascade_profileface.xml");
        // setClassifier("haar/haarcascade_eye_tree_eyeglasses.xml");
        storage = CvMemStorage.create();
    }

    public void start() {
        try {
            // stop(); // Stop and release resources before starting again
            // Thread.sleep(30); // Add a small delay to control frame rate


            grabber = OpenCVFrameGrabber.createDefault(0); // Use 1 for secondary camera
            grabber.start();

            init();


            new Thread(this).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        stop = true;
        try {
            if (grabber != null) {
                grabber.stop();
                grabber.release();
                grabber = null;
            }
            storage.release();
            storage = null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrame(ImageView frames) {
        this.frames = frames;
    }

    public IplImage getCurrentFrame() {
        return tempFrame;
    }

    public void run() {
        try {
            while (!stop) {
                Frame grabbedFrame = grabber.grab();
                if (grabbedFrame == null) {
                    continue;
                }
                //currentFrame = grabberConverter.convertToIplImage(grabbedFrame); // Update the current frame
                IplImage grabbedImage = grabberConverter.convertToIplImage(grabbedFrame);
                processFrame(grabbedImage);

                //Thread.sleep(30); // Add a small delay to control frame rate
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    private void processFrame(IplImage frame) {
        cvClearMemStorage(storage);

        CvSeq objects = new CvSeq();
        boolean frontalFaceDetected = false;

        // Resize the frame to a smaller size (e.g., half the original size)
        IplImage resizedFrame = cvCreateImage(cvSize(frame.width() / 2, frame.height() / 2), frame.depth(), frame.nChannels());
        cvResize(frame, resizedFrame);

        for (CvHaarClassifierCascade classifier : classifiers) {
            CvSeq faces = cvHaarDetectObjects(resizedFrame, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);

            if (faces != null) {
                for (int i = 0; i < faces.total(); i++) {
                    CvRect detectedFace = new CvRect(cvGetSeqElem(faces, i));

                    // Convert the coordinates back to the original frame size
                    detectedFace.x(detectedFace.x() * 2); // Assuming you scaled by 2
                    detectedFace.y(detectedFace.y() * 2);
                    detectedFace.width(detectedFace.width() * 2);
                    detectedFace.height(detectedFace.height() * 2);

                    // Create a copy of the original frame for drawing
                    IplImage frameCopy = cvCloneImage(frame);
                    tempFrame = cvCloneImage(frameCopy);
                    cvReleaseImage(frameCopy);
                    //frontalFaceDetected = true;

                    // Draw a rectangle around each detected face on the original frame
                    CvPoint center = cvPoint(detectedFace.x() + detectedFace.width() / 2, detectedFace.y() + detectedFace.height() / 2);
                    CvSize axes = cvSize(detectedFace.width() / 2, detectedFace.height() / 2);
                    cvEllipse(frame, center, axes, 0, 0, 360, CvScalar.RED, 2, CV_AA, 0);


                    //frontalFaceDetected = true;

                    // You can save the frame or perform other actions here
                    // Uncomment the line below if you want to save the frame
                    // saveFrame(frame, "userName");

                    // You can break here if you want to stop after detecting any face
                    // break;

                }
            }
        }

        if (frontalFaceDetected) {
            showFrame(frame);
        }

        showFrame(frame);
    }
	    /*
	    private void processFrame(IplImage frame) {
	        cvClearMemStorage(storage);

	        boolean frontalFaceDetected = false;
	        CvRect detectedFace = null;

	     // Resize the frame to a smaller size (e.g., half the original size)
	        IplImage resizedFrame = cvCreateImage(cvSize(frame.width() / 2, frame.height() / 2), frame.depth(), frame.nChannels());
	        cvResize(frame, resizedFrame);

	        for (CvHaarClassifierCascade classifier : classifiers) {
	            CvSeq objects = cvHaarDetectObjects(frame, classifier, storage, 1.1, 3, CV_HAAR_DO_CANNY_PRUNING);
	           // CvPoint[] pts = new CvPoints();
	            if (objects != null) {
	                for (int i = 0; i < objects.total(); i++) {
	                	detectedFace = new CvRect(cvGetSeqElem(objects, i));


	                	// Convert the coordinates back to the original frame size
	                    detectedFace.x(detectedFace.x() * 2); // Assuming you scaled by 2
	                    detectedFace.y(detectedFace.y() * 2);
	                    detectedFace.width(detectedFace.width() * 2);
	                    detectedFace.height(detectedFace.height() * 2);

	                	// Create a copy of the original frame for drawing
	                    IplImage frameCopy = cvCloneImage(frame);
	                    tempFrame = cvCloneImage(frameCopy);
	                    cvReleaseImage(frameCopy);
	                    frontalFaceDetected = true;
		                //break; // Break out of the loop after detecting any face
	                }
	                if (frontalFaceDetected) {
	                	CvPoint center = cvPoint(detectedFace.x() + detectedFace.width() / 2, detectedFace.y() + detectedFace.height() / 2);
	                    CvSize axes = cvSize(detectedFace.width() / 2, detectedFace.height() / 2);
	                    cvEllipse(frame, center, axes, 0, 0, 360, CvScalar.RED, 2, CV_AA, 0);
	                    showFrame(frame);
	                }
	                frontalFaceDetected = false;

	                showFrame(frame);
	            }
	        }
	    }

	    */

    private void showFrame(IplImage frame) {
        Frame convertedFrame = grabberConverter.convert(frame);
        BufferedImage bufferedImage = paintConverter.getBufferedImage(convertedFrame, 1); // Use gamma 1
        WritableImage fxImage = javafx.embed.swing.SwingFX.toFXImage(bufferedImage, null);

        if (frames != null) {
            javafx.application.Platform.runLater(() -> {
                frames.setImage(fxImage);
            });
        }
    }

    public void saveFrame(IplImage frame, String userName) {
        try {
            setFrameDirectory("./facesRecord/" + userName + "/");



            File directory = new File(framesDirectory);

            if (!directory.exists()) {
                boolean created = directory.mkdir(); // Create the directory
                if (created) {
                    System.out.println("Directory created successfully.");
                } else {
                    System.out.println("Failed to create the directory.");
                }
            }

            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = framesDirectory + "frame_" + timeStamp + ".jpg";

            int result = cvSaveImage(fileName, tempFrame);

            if (result == 1) {
                System.out.println("Frame saved successfully to: " + fileName);
            } else {
                System.err.println("Failed to save frame to: " + fileName);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setFrameDirectory(String directoryName) {
        this.framesDirectory = directoryName;
	    	/*
	        // Get the current working directory (the project's root directory)
	        String workingDirectory = System.getProperty("user.dir");

	        // Combine the working directory with the desired directory name
	        this.framesDirectory = workingDirectory + File.separator + directoryName;
	        */
    }

    private void setClassifier(String name) {
        try {
            String classifierPath = Loader.extractResource(name, null, "classifier", ".xml").getAbsolutePath();
            CvHaarClassifierCascade classifier = new CvHaarClassifierCascade(cvLoadImage(classifierPath));

            if (classifier.isNull()) {
                throw new Exception("Could not load the classifier file.");
            }

            classifiers.add(classifier);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private OpenCVFrameConverter.ToIplImage grabberConverter = new OpenCVFrameConverter.ToIplImage();
    private Java2DFrameConverter paintConverter = new Java2DFrameConverter();

}

	/*
    public void start() {
        try {
            grabber = OpenCVFrameGrabber.createDefault(0);
            grabber.start();
            init();

            ExecutorService executor = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());

            while (!stop) {
                Frame grabbedFrame = grabber.grab();
                if (grabbedFrame != null) {
                    final IplImage grabbedImage = grabberConverter.convertToIplImage(grabbedFrame);

                    executor.execute(() -> {
                        processFrame(grabbedImage);
                    });
                }

                // Add a small delay to control frame rate
                Thread.sleep(30);
            }

            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
*/
