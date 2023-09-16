package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import com.jmath.ExtendedMath;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import javax.swing.text.Document;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;

public class VisionSystem extends Subsystem {

    double distanceX = 0;
    //private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    private PhotonCamera camera = new PhotonCamera("USB2.0_HD_UVC_WebCam");


    public VisionSystem() {
    }

    public void setPipelineCone() {
        camera.setPipelineIndex(0);
    }

    public double getXAngleToTarget() {
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(pipelineResult.hasTargets()){
            PhotonTrackedTarget bestTarget = pipelineResult.getBestTarget();
            return bestTarget.getYaw();
        }
        else return 6;
    }
    public double tomFunction(){
        double cameraHeightH1 = 10;
        double targetHeightH2 = 2;
        double mountingAngle = -20; //degrees
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(pipelineResult.hasTargets()){
            PhotonTrackedTarget best = pipelineResult.getBestTarget();
            double distance = PhotonUtils.calculateDistanceToTargetMeters(cameraHeightH1,targetHeightH2,mountingAngle,best.getPitch());
            return distance;
        }
        return 0;
    }
    public double usingArea(){
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(pipelineResult.hasTargets()){
            double realArea = 0;
            PhotonTrackedTarget best = pipelineResult.getBestTarget();
            double distance = (findEstimatedFocalLength() * realArea) / best.getArea();
            return distance;
        }
        return 0;
    }
    public double findEstimatedFocalLength(){
        double realArea = 0;
        double targetArea = 0;
        double realHeight = 0;
        double knownDistance = 20; //cm
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(pipelineResult.hasTargets()){
            PhotonTrackedTarget best = pipelineResult.getBestTarget();
            targetArea = best.getArea();
            double targetHeight = (realHeight * targetArea) / realArea ; //pixels
            double focalLength = (knownDistance * targetHeight) / realHeight ;
            return focalLength;
        }
        return 0;
    }
    public double getDistanceX(){
        return this.distanceX;
    }
}
