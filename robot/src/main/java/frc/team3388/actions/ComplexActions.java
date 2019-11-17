package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.actions.Action;
import com.flash3388.flashlib.robot.scheduling.actions.ParallelActionGroup;
import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;

public class ComplexActions {

    public static final Action driveCaptureCargo(DriveSystem driveSystem, CargoSystem cargoSystem, double speed) {
        return new ParallelActionGroup()
                .add(new CargoCapture(cargoSystem))
                .add(new DriveUntilCaptured(driveSystem, cargoSystem, speed));
    }
}
