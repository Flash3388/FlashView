package frc.team3388.subsystems;

import com.flash3388.flashlib.robot.scheduling.Subsystem;
import frc.team3388.Piston;

public class HatchSystem extends Subsystem {
    private final Piston edward;

    public HatchSystem(Piston edward) {
        this.edward = edward;
    }

    public void toggle() {
        edward.toggle();
    }

    public void open() {
        edward.open();
    }

    public void close() {
        edward.close();
    }

    public boolean isOpen() { return edward.isOpen(); }
}
