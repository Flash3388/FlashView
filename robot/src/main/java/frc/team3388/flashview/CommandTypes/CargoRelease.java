package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.google.gson.JsonObject;
import frc.team3388.subsystems.CargoSystem;

public class CargoRelease implements CommandType {
    private static final String TYPE = "CargoRelease";

    private final CargoSystem cargoSystem;

    public CargoRelease(CargoSystem cargoSystem) {
        this.cargoSystem = cargoSystem;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new frc.team3388.actions.CargoRelease(cargoSystem);
    }
}
