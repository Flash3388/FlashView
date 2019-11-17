package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import com.google.gson.JsonObject;
import frc.team3388.actions.PidRotate;
import frc.team3388.actions.RotateUntil;
import frc.team3388.subsystems.DriveSystem;

public class Rotate implements CommandType {
    private static final String TYPE = "Rotate";
    private static final String ROTATE_KEY = "Angle";

    private final DriveSystem driveSystem;
    private final Clock clock;
    private final Time timeInTheshold;
    private final double margin;

    public Rotate(DriveSystem driveSystem, double margin, Time timeInTheshold, Clock clock) {
        this.driveSystem = driveSystem;
        this.clock = clock;
        this.timeInTheshold = timeInTheshold;
        this.margin = margin;
    }

    @Override
    public String getCommandType() {
        return TYPE;
    }

    @Override
    public Action getAction(JsonObject object) {
        return new PidRotate(driveSystem, margin, timeInTheshold, getDistance(object), clock);
    }

    private double getDistance(JsonObject object) {
        return object.get(ROTATE_KEY).getAsDouble();
    }
}
