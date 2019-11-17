package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.actions.Action;
import com.flash3388.flashlib.time.Clock;
import com.google.gson.JsonObject;
import frc.team3388.RaspberryPi.VisionProcessing.VisionProcessingUnit;
import frc.team3388.actions.HatchCaptureActionGroup;
import frc.team3388.subsystems.DriveSystem;
import frc.team3388.subsystems.HatchSystem;

public class HatchCapture implements CommandType {
    private static final String TYPE = "HatchCapture";

    private final DriveSystem driveSystem;
    private final HatchSystem hatchSystem;
    private final VisionProcessingUnit visionProcessingUnit;
    private final Clock clock;

    private final int numberOfDumpedFrames;
    private final int numberOfAveragedFrames;

    public HatchCapture(DriveSystem driveSystem, HatchSystem hatchSystem, VisionProcessingUnit visionProcessingUnit, Clock clock, int numberOfDumpedFrames, int numberOfAveragedFrames) {
        this.driveSystem = driveSystem;
        this.hatchSystem = hatchSystem;
        this.visionProcessingUnit = visionProcessingUnit;
        this.clock = clock;
        this.numberOfDumpedFrames = numberOfDumpedFrames;
        this.numberOfAveragedFrames = numberOfAveragedFrames;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new HatchCaptureActionGroup(driveSystem, hatchSystem, visionProcessingUnit, clock, numberOfDumpedFrames, numberOfAveragedFrames);
    }
}
