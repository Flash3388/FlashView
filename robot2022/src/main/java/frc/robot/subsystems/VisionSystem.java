package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import com.jmath.ExtendedMath;
import com.jmath.vectors.Vector3;
import com.sun.jndi.ldap.spi.LdapDnsProvider;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.first.cscore.CvSink;
import edu.wpi.first.cscore.CvSource;
import edu.wpi.first.math.geometry.Pose3d;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import org.apache.logging.log4j.core.util.datetime.FastDateFormat;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import javax.swing.text.Document;
import java.awt.*;
import java.awt.image.ImageObserver;
import java.awt.image.ImageProducer;
import java.io.IOException;
import java.util.Optional;

public class VisionSystem extends Subsystem {

    double distanceX = 0;
    private double distance = 0;
    private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    //private PhotonCamera camera = new PhotonCamera("USB2.0_HD_UVC_WebCam");

    private static final Transform3d POS_TO_CAMERA = new Transform3d(
            new Translation3d(1, 1, 1),
            new Rotation3d(0,0,0)
            );

    private EstimatedRobotPose estimatedRobotPose;
    PhotonPoseEstimator estimator;
    public VisionSystem() {
        try {
            AprilTagFieldLayout layout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
            estimator = new PhotonPoseEstimator(layout, PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP, camera, POS_TO_CAMERA);
        } catch (IOException e) {
            throw new Error(e);
        }
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
    public boolean isThereATarget(){
        return camera.getLatestResult().hasTargets();
    }
    public double tomFunction(){
        double cameraHeightH1 = 0.485; //m
        double targetHeightH2 = 0.328/2;
        //double mountingAngle = -13.41884; //degree    s
        double mountingAngle =Units.degreesToRadians(-13.41884); //radians
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(pipelineResult.hasTargets()){
            PhotonTrackedTarget best = pipelineResult.getBestTarget();
            this.distance = PhotonUtils.calculateDistanceToTargetMeters(cameraHeightH1,targetHeightH2,mountingAngle, Units.degreesToRadians(best.getPitch()));
            return this.distance;
        }

        return this.distance;
    }
    public double getDistanceX(){
        return this.distanceX;
    }
    public void aprilTagCheck() {

        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        for (PhotonTrackedTarget target : pipelineResult.getTargets()) {


            Transform3d transform3d = target.getBestCameraToTarget();
            Vector3 vector3 = new Vector3(transform3d.getX(), transform3d.getY(), transform3d.getZ());
            System.out.printf("Target %d: %s, dis=%.3f, yaw=%.3f\n", target.getFiducialId(), vector3, vector3.magnitude(), target.getYaw());
        }
    }

    public EstimatedRobotPose getEstimatedRobotPose() {
        return estimatedRobotPose;
    }

    public void update() {
        Optional<EstimatedRobotPose> optional = estimator.update();
        if (optional.isEmpty()) {
            return;
        }

        EstimatedRobotPose pose = optional.get();
        estimatedRobotPose = pose;
    }
}
