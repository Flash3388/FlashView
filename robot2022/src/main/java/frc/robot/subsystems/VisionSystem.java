package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import com.jmath.vectors.Vector3;
import edu.wpi.first.apriltag.AprilTag;
import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Rotation3d;
import edu.wpi.first.math.geometry.Transform3d;
import edu.wpi.first.math.geometry.Translation3d;
import edu.wpi.first.math.util.Units;
import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

import java.io.IOException;
import java.util.Optional;

public class VisionSystem extends Subsystem {

    double distanceX = 0;
    private double distance = 0;
    private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");
    private static final double CONE_HEIGHT = 0.17; //**
    private static final double CAMERA_HEIGHT = 0.485;//**

    // you need to find it by creating the equation with known constants:
    // place the cone in a known distance and height. know the camera's height as well.
    // using the photon-vision website, find the pitch of the target and then solve the next equation ->
    // tan(a1+a2) = (h2-h1) / d   you need to find "a1"
    // forward explanation at https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
    final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(-13.41884);
    // -8.4

    public static final Transform3d POS_TO_CAMERA = new Transform3d(
            new Translation3d(1,1,1),
            new Rotation3d(0,0,0));


    private EstimatedRobotPose estimatedRobotPose;
    PhotonPoseEstimator estimator;


    public VisionSystem() {
        try {
            AprilTagFieldLayout layout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
            estimator = new PhotonPoseEstimator(layout, PhotonPoseEstimator.PoseStrategy.MULTI_TAG_PNP,
                    camera, POS_TO_CAMERA);
        }
        catch(IOException e) {
            throw new Error(e);
        }
    }



    public void setPipelineCone() {
        camera.setPipelineIndex(0);
    }

    public double getXAngleToTarget(){
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(!pipelineResult.hasTargets())
            return 6;
        PhotonTrackedTarget bestTarget = pipelineResult.getBestTarget();
        return bestTarget.getYaw();
    }

    public double getDistanceToTarget(){
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        if(!pipelineResult.hasTargets())
            return 0.0;

        PhotonTrackedTarget bestTarget = pipelineResult.getBestTarget();

        // to know more about the next function press ctrl+b on this function, and you will found out
        // that the equation is similar to the one above
        return PhotonUtils.calculateDistanceToTargetMeters(
                CAMERA_HEIGHT,
                CONE_HEIGHT,
                CAMERA_PITCH_RADIANS,
                Units.degreesToRadians(bestTarget.getPitch()));
    }

    public boolean hasTargets(){
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        return pipelineResult.hasTargets();
    }

    public void aprilTagCheck(){
        PhotonPipelineResult pipelineResult = camera.getLatestResult();
        for(PhotonTrackedTarget target : pipelineResult.getTargets()){

            Transform3d transform3d = target.getBestCameraToTarget();
            Vector3 vector3 = new Vector3(transform3d.getX(), transform3d.getY(),
                    transform3d.getZ());
            System.out.printf("Target %d: %s, dis= %.3f, yaw= %.3f", target.getFiducialId(),
                    vector3, vector3.magnitude(), target.getYaw());
        }
    }

    public EstimatedRobotPose getEstimatedRobotPose(){
        return estimatedRobotPose;
    }

    public void update(){
        Optional<EstimatedRobotPose> optional = estimator.update();
        if(optional.isEmpty()){
            return;
        }
        EstimatedRobotPose pose = optional.get();
        estimatedRobotPose = pose;
    }
}
