package frc.team3388;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import java.util.function.DoubleSupplier;

public class EncoderSRX implements DoubleSupplier {
    private static final double WHEEL_DIAMETER = 15.24;
    private static final double PPR = 4096.0;
    private static final double REVERSE_PPR = 1.0/PPR;

    private final WPI_TalonSRX talonController;
    private final boolean isReversed;

    public EncoderSRX(WPI_TalonSRX talonController, boolean isReversed) {
        this.talonController = talonController;
        this.isReversed = isReversed;
    }

    @Override
    public double getAsDouble() {
        double multiplyFactor = REVERSE_PPR*WHEEL_DIAMETER*Math.PI;

        return talonController.getSelectedSensorPosition() * multiplyFactor * (isReversed ? -1 : 1);
    }

    public void reset() {
        talonController.setSelectedSensorPosition(0);
    }
}
