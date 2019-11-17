package frc.team3388.subsystems;

import com.flash3388.flashlib.frc.robot.io.devices.actuators.FrcSpeedController;
import com.flash3388.flashlib.robot.io.devices.actuators.SpeedControllerGroup;
import com.flash3388.flashlib.robot.systems.SingleMotorSystem;
import edu.wpi.first.wpilibj.SpeedController;
import frc.team3388.Switch;

public class LiftSystem extends SingleMotorSystem {
    private static final double LIFT_SPEED = 0.812345;
    private static final double FALL_SPEED = -0.45;
    private static final double STALL_SPEED = 0.075;

    private final Switch upperSwitch;
    private final Switch lowerSwitch;

    public LiftSystem(SpeedController rightLiftController, SpeedController leftLiftController, Switch upperSwitch, Switch lowerSwitch) {
        super(new SpeedControllerGroup(
                new FrcSpeedController(rightLiftController),
                new FrcSpeedController(leftLiftController)));

        this.upperSwitch = upperSwitch;
        this.lowerSwitch = lowerSwitch;
    }

    public void lift() {
        if(!upperSwitch.getAsBoolean())
            rotate(LIFT_SPEED);
        else
            stall();
    }

    public void fall() {
        if(!lowerSwitch.getAsBoolean())
            rotate(FALL_SPEED);
        else
            stop();
    }

    public void stall() {
        if(!lowerSwitch.getAsBoolean())
            rotate(STALL_SPEED);
        else
            stop();
    }

    public boolean isDown() {
        return lowerSwitch.getAsBoolean();
    }

    public boolean isUp() {
        return upperSwitch.getAsBoolean();
    }
}
