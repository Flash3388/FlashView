package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

public class VisionTask implements Runnable {

    @Override
    public void run() {
        SmartDashboard.putNumber("min R", 0);
        SmartDashboard.putNumber("min G", 0);
        SmartDashboard.putNumber("min B", 0);
        SmartDashboard.putNumber("max R", 255);
        SmartDashboard.putNumber("max G", 255);
        SmartDashboard.putNumber("max B", 255);

        CameraServer.startAutomaticCapture(0);
        CvSink sink = CameraServer.getVideo();
        CvSource output = CameraServer.putVideo("processed", 320, 420);

        Mat mat = new Mat();
        Mat threshold = new Mat();
        while (true) {
            if (sink.grabFrame(mat) == 0) {
                // error
                continue;
            }

            int minR = (int) SmartDashboard.getNumber("min R", 0);
            int minG = (int) SmartDashboard.getNumber("min G", 0);
            int minB = (int) SmartDashboard.getNumber("min B", 0);
            int maxR = (int) SmartDashboard.getNumber("max R", 0);
            int maxG = (int) SmartDashboard.getNumber("max G", 0);
            int maxB = (int) SmartDashboard.getNumber("max B", 0);

            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);

            Core.inRange(mat,
                    new Scalar(minB, minG, minR),
                    new Scalar(maxB, maxG, maxR),
                    threshold);

            //Imgproc.rectangle(mat, new Point(0, 0), new Point(10, 10), new Scalar(255, 50, 50), 2);
            output.putFrame(threshold);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }
}
