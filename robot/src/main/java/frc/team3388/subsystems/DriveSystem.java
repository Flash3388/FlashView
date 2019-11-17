package frc.team3388.subsystems;

import com.flash3388.flashlib.frc.robot.io.devices.actuators.FrcSpeedController;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.robot.io.devices.actuators.SpeedControllerGroup;
import com.flash3388.flashlib.robot.systems.drive.TankDriveSystem;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.interfaces.Gyro;
import frc.team3388.EncoderSRX;
import frc.team3388.NetworkDoubleProperty;

public class DriveSystem extends TankDriveSystem {
    private static final double DRIVE_PID_LIMIT = 0.4;
    private static final double ROTATE_PID_LIMIT = 0.4;

    private final PidController drivePID;
    private final PidController rotatePID;

    private final EncoderSRX rightEncoder;
    private final EncoderSRX leftEncoder;

    private Gyro gyro;

    public DriveSystem(SpeedController frontRight, SpeedController rearRight, SpeedController frontLeft,
                       SpeedController rearLeft, EncoderSRX rightTalonEncoder, EncoderSRX leftTalonEncoder, Gyro gyro) {
        super(new SpeedControllerGroup(
                new FrcSpeedController(frontRight),
                new FrcSpeedController(rearRight)),

              new SpeedControllerGroup(
                new FrcSpeedController(frontLeft),
                new FrcSpeedController(rearLeft)));


        frontRight.setInverted(true);
        rearRight.setInverted(true);

        drivePID = new PidController(0.08,0,0,0);
        drivePID.setOutputLimit(DRIVE_PID_LIMIT);


        rotatePID = new PidController(0.12, 0, 0, 0);
        rotatePID.setOutputLimit(ROTATE_PID_LIMIT);

        rightEncoder = rightTalonEncoder;
        leftEncoder = leftTalonEncoder;

        this.gyro = gyro;
    }

    public double getDistance() {
        return ExtendedMath.avg(rightEncoder.getAsDouble(),leftEncoder.getAsDouble());
    }

    public void resetEncoders() {
        rightEncoder.reset();
        leftEncoder.reset();
    }

    public PidController getDrivePID() {
        return drivePID;
    }

    public PidController getRotatePID() {
        return rotatePID;
    }

    public double getAngle() {
        return (gyro.getAngle()%360);
    }

    public void resetGyro() {
        gyro.reset();
    }
}
