package frc.team3388.actions;

import com.flash3388.flashlib.robot.scheduling.ActionGroup;
import com.flash3388.flashlib.robot.scheduling.ExecutionOrder;
import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;

public class ComplexActions {

    public static final ActionGroup driveCaptureCargo(DriveSystem driveSystem, CargoSystem cargoSystem, double speed) {
        return new ActionGroup(ExecutionOrder.PARALLEL)
                .add(new CargoCapture(cargoSystem))
                .add(new DriveUntilCaptured(driveSystem, cargoSystem, speed));
    }
}
