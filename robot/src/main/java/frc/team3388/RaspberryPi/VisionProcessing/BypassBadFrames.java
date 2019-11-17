package frc.team3388.RaspberryPi.VisionProcessing;

import com.flash3388.flashlib.robot.scheduling.Action;
import frc.team3388.subsystems.DriveSystem;

public class BypassBadFrames extends Action {
    private final VisionProcessingUnit visionControl;

    private final int numberOfDumpedFrames;

    public BypassBadFrames(DriveSystem driveSystem, VisionProcessingUnit visionControl, int numberOfDumpedFrames) {
        requires(driveSystem);

        this.visionControl = visionControl;
        this.numberOfDumpedFrames = numberOfDumpedFrames;
    }

    @Override
    protected void initialize() {
        visionControl.setVisionProcessingMode(VisionProcessingMode.ON);

    }

    @Override
    protected void execute() {
    }

    @Override
    protected boolean isFinished() {
        return visionControl.areFramesBypassed(numberOfDumpedFrames);
    }

    @Override
    protected void interrupted() {
        visionControl.setVisionProcessingMode(VisionProcessingMode.OFF);
    }

    @Override
    protected void end() {
    }
}
