package frc.team3388;

import com.beans.DoubleProperty;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkDoubleProperty implements DoubleProperty {
    private final NetworkTableEntry entry;

    public NetworkDoubleProperty(String name) {
        entry = NetworkTableInstance.getDefault().getTable("PID").getEntry(name);
        entry.setDouble(0);
    }

    @Override
    public double getAsDouble() {
        return entry.getDouble(0);
    }

    @Override
    public void setAsDouble(double value) {
        entry.setDouble(value);
    }

    @Override
    public Double get() {
        return entry.getDouble(0);
    }

    @Override
    public void set(Double value) {
        entry.setDouble(value);
    }
}
