package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonUtils;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSystem extends Subsystem {

    private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");

    private static final double CONE_HEIGHT = 00; //**
    private static final double FOCAL_LENGTH = 00;//**


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
      //  return (FOCAL_LENGTH*CONE_HEIGHT)/bestTarget.
      //  PhotonUtils.calculateDistanceToTargetMeters()
        return 0;
    }
}
