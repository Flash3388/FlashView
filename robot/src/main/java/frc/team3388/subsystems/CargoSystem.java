package frc.team3388.subsystems;

import com.flash3388.flashlib.frc.robot.io.devices.actuators.FrcSpeedController;
import com.flash3388.flashlib.robot.systems.SingleMotorSystem;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team3388.Switch;
import frc.team3388.actions.GripperStop;

public class CargoSystem extends SingleMotorSystem {
    private static final double CAPTURE_SPEED = -0.8;
    private static final double RELEASE_SPEED = 0.6;
    private static final double GRIPPER_STOP_SPEED_DECLINE = 0.01;
    private static final double GRIPPER_STOP_SPEED_MARGIN = 0.01;

    private final SpeedController gripperController;
    private final Switch captureSwitch;

    public CargoSystem(SpeedController gripperController, Switch captureSwitch) {
        super(new FrcSpeedController(gripperController));

        this.gripperController = gripperController;
        this.captureSwitch = captureSwitch;
    }

    public void capture() {
        rotate(CAPTURE_SPEED);
    }

    public void release() {
        rotate(RELEASE_SPEED);
    }

    public void startSlowStop() {
        new GripperStop(this,GRIPPER_STOP_SPEED_DECLINE, GRIPPER_STOP_SPEED_MARGIN).start();
    }

    public double getCurrentSpeed() {
        return gripperController.get();
    }

    public boolean isCaptured() {
        return captureSwitch.getAsBoolean();
    }
}
