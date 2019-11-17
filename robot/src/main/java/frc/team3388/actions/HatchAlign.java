package frc.team3388.actions;

import com.beans.DoubleProperty;
import com.flash3388.flashlib.math.Mathf;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import frc.team3388.RaspberryPi.TimeStamps.TimeStampRecorder;
import frc.team3388.RaspberryPi.VisionProcessing.VisionProcessingMode;
import frc.team3388.RaspberryPi.VisionProcessing.VisionProcessingUnit;
import frc.team3388.subsystems.DriveSystem;
import frc.team3388.subsystems.HatchSystem;

public class HatchAlign extends PidAction{
    private static final double CAMERA_DISTANCE_FROM_FRONT_CM = 20;
    private double initDistanceToTarget;

    private final DriveSystem driveSystem;
    private final HatchSystem hatchSystem;
    private final Clock clock;

    private final TimeStampRecorder recorder;
    private final VisionProcessingUnit visionControl;
    private final DoubleProperty distanceProperty;

    private final double forwardDriveSpeed;
    private final boolean isCapturing;

    public HatchAlign(DriveSystem driveSystem, PidController pidController, double margin, Time timeInThreshold,
                      Clock clock, HatchSystem hatchSystem, VisionProcessingUnit visionControl,
                      DoubleProperty distanceProperty, double forwardDriveSpeed, boolean isCapturing) {
        super(pidController, margin, timeInThreshold, clock);
        requires(driveSystem,hatchSystem);

        this.driveSystem = driveSystem;
        this.hatchSystem = hatchSystem;
        this.visionControl = visionControl;
        this.forwardDriveSpeed = forwardDriveSpeed;
        this.isCapturing = isCapturing;
        this.clock = clock;
        this.distanceProperty = distanceProperty;
        recorder = new TimeStampRecorder();
    }

    @Override
    protected void execute() {
        recorder.append(clock.currentTime(), driveSystem.getAngle());
        setMotorValues(getPidController().calculate(getProcessValue(),getSetpoint()*10));
    }

    @Override
    protected void setMotorValues(double pidValue) {
        System.out.println(pidValue);
        driveSystem.arcadeDrive(forwardDriveSpeed,-pidValue);
    }

    @Override
    protected double getProcessValue() {
        return driveSystem.getAngle();
    }

    @Override
    protected double getSetpoint() {
        double recorderAngle = recorder.getAngleAt(Time.milliseconds(visionControl.getVisionTime()));
        double distance = Mathf.shortestAngularDistance(driveSystem.getAngle(),recorderAngle);

        return visionControl.getVisionAngle() - distance;
    }

    @Override
    protected void end() {
        driveSystem.stop();
        hatchSystem.toggle();
        visionControl.setVisionProcessingMode(VisionProcessingMode.OFF);
    }

    @Override
    protected void resetSensors() {
        driveSystem.resetGyro();
        driveSystem.resetEncoders();

        if (isCapturing)
            hatchSystem.open();
        initDistanceToTarget = visionControl.getDistanceToTarget();
        distanceProperty.set(-initDistanceToTarget);

        if(initDistanceToTarget > 200 || initDistanceToTarget < 0 || visionControl.getVisionAngle() > 12 || visionControl.getVisionAngle() < -12 || visionControl.getVisionAngle() == 0.0)
            cancel();
    }

    @Override
    protected boolean isFinished() {
        return driveSystem.getDistance() >= (initDistanceToTarget - CAMERA_DISTANCE_FROM_FRONT_CM);
    }
}
