package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.VisionAutoAlign;
import frc.robot.subsystems.VisionSystem;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.function.Predicate;

public class VisionTask implements Runnable {

    private final VisionSystem visionSystem;

    public VisionTask(VisionSystem visionSystem) {
        this.visionSystem = visionSystem;
    }

    @Override
    public void run() {
        SmartDashboard.putNumber("min H", 0);
        SmartDashboard.putNumber("min S", 0);
        SmartDashboard.putNumber("min V", 0);
        SmartDashboard.putNumber("max H", 180);
        SmartDashboard.putNumber("max S", 255);
        SmartDashboard.putNumber("max V", 255);

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

            int minH = (int) SmartDashboard.getNumber("min H", 0);
            int minS = (int) SmartDashboard.getNumber("min S", 0);
            int minV = (int) SmartDashboard.getNumber("min V", 0);
            int maxH = (int) SmartDashboard.getNumber("max H", 0);
            int maxS = (int) SmartDashboard.getNumber("max S", 0);
            int maxV = (int) SmartDashboard.getNumber("max V", 0);

            // convert the image from BGR to HSV color space
            Imgproc.cvtColor(mat, mat, Imgproc.COLOR_BGR2HSV);

            // filter the image for colors within the range configured on the shuffleboard.
            // this will make threshold a binary image (made up of 0 and 1 pixels) where
            // the 1 pixels are the pixels which were within the color range.
            Core.inRange(mat,
                    new Scalar(minH, minS, minV),
                    new Scalar(maxH, maxS, maxV),
                    threshold);

            // with the binary image, we can now use an algorithm to detect "contours".
            // a contour is basically a collection of close and connected 1 pixels.
            // basically, each "contour" is a different object seen by the camera
            // so after this, we can use these contours to filter out objects until we
            // find the object we want.
            List<MatOfPoint> contours = new ArrayList<>();
            Imgproc.findContours(threshold, contours, new Mat(), Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

            // now we can start filtering out contours we don't want.
            // the easiest way to do so is to start by filtering out contours that are too small to care about
            // play around with this value until it provides a good enough result.
            // remember that the further away objects are, the smaller they will be, so this
            // basically gives a limit on how far our objects can be before we can no longer detect them.
            final int MIN_CONTOUR_PIXEL_SIZE = 50;
            // this function will remove all the contours from the list if the lambda returns true,
            // so we make the lambda return true when the amount of pixels is too small;
            removeContoursByPredicate(contours, (contour)-> {
                return contour.total() <= MIN_CONTOUR_PIXEL_SIZE;
            });

            // let's get rid of all contours that are the wrong shape.
            // this function will get rid of shape that are not close enough to the wanted
            // number of vertices.
            // WANTED_SHAPE_VERTICES indicates the amount of vertices we want from our shape
            // SHAPE_ACCURACY indicates the accuracy of approximation. The way the shape approximation works
            // is by taking a contour and "guessing" what shape it fits, assuming that parts of the shape
            // may be missing. So SHAPE_ACCURACY increases the chance a shape will fit what we want, but make it too big
            // and it will make contours appear like the wrong shape.
            final int WANTED_SHAPE_VERTICES = 4;
            final double SHAPE_ACCURACY = 0.2;
            removeContoursThatAreTheWrongShape(contours, WANTED_SHAPE_VERTICES, SHAPE_ACCURACY);

            // let's draw the shapes we have left to see. To do that, we will make threshold
            // into a colored image so we can draw colors on it
            Imgproc.cvtColor(threshold, threshold, Imgproc.COLOR_GRAY2RGB);
            Imgproc.drawContours(threshold, contours, -1, new Scalar(255, 50, 50), 2);

            // let's get the biggest contour and decide it is the one we want.
            // of course this is an assumption, and there are better ways to find
            // the right contour, but for now it will do.
            MatOfPoint best = findBiggestContour(contours);
            // let's draw this shape on the image in a different color
            drawSingleContour(threshold, best);

            // send the image so we could see it on the shuffleboard
            output.putFrame(threshold);
            output.putFrame(mat);

            // now we can extract information about the contour.
            // for now, let's just get the offset between the contour and the center of the camera
            Point contourCenter = getContourCenter(best);
            Point imageCenter = new Point(threshold.width() / 2.0, threshold.height() / 2.0);
            // you will need to save this value somewhere in the robot which is accessible for an action.
            // the best way is to make a subsystem and give it the value
            double distanceX = contourCenter.x - imageCenter.x;
            visionSystem.setDistanceX(distanceX);

           // new VisionAutoAlign(visionSystem).start(); //***

            // we don't want to force the computer to over-work by running the loop non-stop
            // so we put the thread to sleep for 100ms instead, giving it some break.
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
            }
        }
    }

    private void removeContoursThatAreTheWrongShape(List<MatOfPoint> contours, int vertices, double accuracy){
        MatOfPoint2f matOfPoint2f = new MatOfPoint2f();
        MatOfPoint2f approxCurve = new MatOfPoint2f();

        removeContoursByPredicate(contours, (contour)-> {
            matOfPoint2f.fromList(contour.toList());
            Imgproc.approxPolyDP(
                    matOfPoint2f,
                    approxCurve,
                    Imgproc.arcLength(matOfPoint2f, true) * accuracy,
                    true);
            long total = approxCurve.total();

            return total != vertices;
        });
    }

    private void removeContoursByPredicate(List<MatOfPoint> contours, Predicate<MatOfPoint> predicate) {
        for (Iterator<MatOfPoint> it = contours.iterator(); it.hasNext();) {
            MatOfPoint contour = it.next();
            if (predicate.test(contour)) {
                it.remove();
            }
        }
    }

    private MatOfPoint findBiggestContour(List<MatOfPoint> contours) {
        return contours.stream().max(Comparator.comparingLong(MatOfPoint::total)).get();
    }

    private void drawSingleContour(Mat image, MatOfPoint contour) {
        Imgproc.drawContours(image, Collections.singletonList(contour), -1, new Scalar(50, 255, 50), 2);
    }

    private Point getContourCenter(MatOfPoint contour) {
        Rect rect = Imgproc.boundingRect(contour);
        return new Point(
                rect.x + rect.width / 2.0,
                rect.y + rect.height / 2.0
        );
    }
}
