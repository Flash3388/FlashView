package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import edu.wpi.first.math.util.Units;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSystem extends Subsystem {

    private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");

    private static final double CONE_HEIGHT = 00; //**
    private static final double CAMERA_HEIGHT = 00;//**

    // you need to find it by creating the equation with known constants:
    // place the cone in a known distance and height. know the camera's height as well.
    // using the photon-vision website, find the pitch of the target and then solve the next equation ->
    // tan(a1+a2) = (h2-h1) / d   you need to find "a1"
    // forward explanation at https://docs.limelightvision.io/en/latest/cs_estimating_distance.html
    final double CAMERA_PITCH_RADIANS = Units.degreesToRadians(0);


    public VisionSystem() {
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
            return 0;

        PhotonTrackedTarget bestTarget = pipelineResult.getBestTarget();

        // to know more about the next function press ctrl+b on this function, and you will found out
        // that the equation is similar to the one above
        return PhotonUtils.calculateDistanceToTargetMeters(
                CAMERA_HEIGHT,
                CONE_HEIGHT,
                CAMERA_PITCH_RADIANS,
                Units.degreesToRadians(bestTarget.getPitch()));
    }
}
