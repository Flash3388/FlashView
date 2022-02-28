package frc.team3388.flashview.CommandTypes;

import com.flash3388.flashlib.robot.scheduling.actions.Action;
import com.flash3388.flashlib.time.Clock;
import com.flash3388.flashlib.time.Time;
import com.google.gson.JsonObject;
import frc.team3388.actions.DriveUntilDistance;
import frc.team3388.actions.PidDrive;
import frc.team3388.subsystems.DriveSystem;

public class Drive implements CommandType {
    private static final String TYPE = "Drive";
    private static final String DRIVE_KEY = "Distance";

    private final DriveSystem driveSystem;
    private final Clock clock;
    private final Time timeInTheshold;
    private final double margin;

    public Drive(DriveSystem driveSystem, double margin, Time timeInTheshold, Clock clock) {
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
        return new PidDrive(driveSystem, margin, timeInTheshold, getDistance(object), clock);
    }

    private double getDistance(JsonObject object) {
        double distance = object.get(DRIVE_KEY).getAsDouble();
        System.out.println(distance);
        return distance;
    }
}
