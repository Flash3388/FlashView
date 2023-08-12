package frc.robot.subsystems;

import com.flash3388.flashlib.scheduling.Subsystem;

public class VisionSystem extends Subsystem {

    private double distanceX = 0;

    public double getDistanceX() {
        return distanceX;
    }

    public void setDistanceX(double distanceX) {
        this.distanceX = distanceX;
    }
}
