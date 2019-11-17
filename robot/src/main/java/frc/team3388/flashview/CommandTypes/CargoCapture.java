package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.google.gson.JsonObject;
import frc.team3388.actions.DriveAndCapture;
import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;

public class CargoCapture implements CommandType{
    private static final String TYPE = "CargoCapture";

    private final DriveSystem driveSystem;
    private final CargoSystem cargoSystem;
    private final double driveSpeed;

    public CargoCapture(DriveSystem driveSystem, CargoSystem cargoSystem, double driveSpeed) {
        this.driveSystem = driveSystem;
        this.cargoSystem = cargoSystem;
        this.driveSpeed = driveSpeed;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
//        return ComplexActions.driveCaptureCargo(driveSystem, cargoSystem, driveSpeed);
        return new DriveAndCapture(driveSystem, cargoSystem, driveSpeed);
    }
}
