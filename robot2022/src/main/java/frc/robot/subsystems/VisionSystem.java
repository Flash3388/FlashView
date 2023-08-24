package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;
import org.photonvision.PhotonCamera;
import org.photonvision.targeting.PhotonPipelineResult;
import org.photonvision.targeting.PhotonTrackedTarget;

public class VisionSystem extends Subsystem {

    double distanceX = 0;
    private PhotonCamera camera = new PhotonCamera("Microsoft_LifeCam_HD-3000");

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
    public double getDistanceX(){
        return this.distanceX;
    }
}
