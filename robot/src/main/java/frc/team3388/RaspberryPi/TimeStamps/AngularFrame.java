package frc.team3388.RaspberryPi.TimeStamps;

import com.flash3388.flashlib.time.Time;

public class AngularFrame {
    public Time timestamp;
    public double angle;

    public AngularFrame(Time timestamp, double angle) {
        this.timestamp = timestamp;
        this.angle = angle;
    }
}