package frc.team3388.actions;

import com.beans.DoubleProperty;
import com.beans.properties.SimpleDoubleProperty;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.robot.scheduling.ActionGroup;
import com.flash3388.flashlib.robot.scheduling.ExecutionOrder;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import frc.team3388.RaspberryPi.VisionProcessing.AverageFrames;
import frc.team3388.RaspberryPi.VisionProcessing.BypassBadFrames;
import frc.team3388.RaspberryPi.VisionProcessing.VisionProcessingUnit;
import frc.team3388.subsystems.DriveSystem;
import frc.team3388.subsystems.HatchSystem;

public class HatchReleaseActionGroup extends ActionGroup {
    private static final double HATCH_ALIGN_MARGIN = 0;
    private static final double HATCH_ALIGN_DRIVE_SPEED = 0.2;
    private static final double HATCH_ALIGN_ROTATE_LIMIT = 0.15;
    private static final PidController alignPid = new PidController(0.2,0,0,0);

    private final DoubleProperty initDistanceProperty;


    public HatchReleaseActionGroup(DriveSystem driveSystem, HatchSystem hatchSystem, VisionProcessingUnit visionProcessingUnit,
                                   Clock clock, int numberOfDumpedFrames, int numberOfAveragedFrames) {
        super(ExecutionOrder.SEQUENTIAL);

        alignPid.setOutputLimit(HATCH_ALIGN_ROTATE_LIMIT);
        initDistanceProperty = new SimpleDoubleProperty();

        add(new BypassBadFrames(driveSystem, visionProcessingUnit, numberOfDumpedFrames),
                new AverageFrames(driveSystem, visionProcessingUnit, numberOfAveragedFrames),
                new HatchAlign(driveSystem, alignPid,
                        HATCH_ALIGN_MARGIN, Time.milliseconds(1000), clock,
                        hatchSystem, visionProcessingUnit, initDistanceProperty, HATCH_ALIGN_DRIVE_SPEED,false));
    }
}
