package frc.team3388;

import edu.wpi.first.wpilibj.DigitalInput;

import java.util.function.BooleanSupplier;

public class Switch implements BooleanSupplier {
    private final DigitalInput digitalInput;
    private final boolean isNormalyOpen;

    public Switch(DigitalInput digitalInput, boolean isNormalyOpen) {
        this.isNormalyOpen = isNormalyOpen;
        this.digitalInput = digitalInput;
    }


    @Override
    public boolean getAsBoolean() {
        return isNormalyOpen != digitalInput.get();
    }
}
