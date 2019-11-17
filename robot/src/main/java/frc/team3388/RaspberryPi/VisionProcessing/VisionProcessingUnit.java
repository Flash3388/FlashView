package frc.team3388.RaspberryPi.VisionProcessing;

import com.flash3388.flashlib.time.Clock;
import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;
import frc.team3388.RaspberryPi.ntp.NtpServer;

public class VisionProcessingUnit {
    private static final int DEFAULT_EXPOSURE = 46;
    private static final int VISION_PROCESSING_EXPOSURE = 25;

    private final NetworkTableEntry mRunEntry = NetworkTableInstance.getDefault().getEntry("vision_run");
    private final NetworkTableEntry mWaitEntry = NetworkTableInstance.getDefault().getEntry("vision_wait");
    private final NetworkTableEntry exposureEntry;
    private final NetworkTableEntry distanceEntry;
    private final NetworkTableEntry timeEntry;
    private final NetworkTableEntry angleEntry;

    private double distanceToTarget;

    public VisionProcessingUnit(Clock clock) {
        initVisionTime(clock);

        exposureEntry = NetworkTableInstance.getDefault().getTable("cameraCtrl").getEntry("exposure");
        distanceEntry = NetworkTableInstance.getDefault().getEntry("vision_distance");
        timeEntry = NetworkTableInstance.getDefault().getEntry("vision_time");
        angleEntry = NetworkTableInstance.getDefault().getEntry("vision_angle");
        distanceToTarget = 0;

        setRegularExposure();
    }

    private void initVisionTime(Clock clock) {
        NetworkTable ntpTable = NetworkTableInstance.getDefault().getTable("ntp");

        NtpServer ntpServer = new NtpServer(
                ntpTable.getEntry("client"),
                ntpTable.getEntry("serverRec"),
                ntpTable.getEntry("serverSend"),
                clock);
    }

    public void setVisionProcessingMode(VisionProcessingMode mode) {
        if(mode.equals(VisionProcessingMode.ON)) {
            setVisionProcessingExposure();
            startBadFrameCapture();
        }
        else {
            setRegularExposure();
            resetBadFrameCapture();
        }
    }

    public boolean areFramesBypassed(int numberOfDumpedFrames) {
        return mWaitEntry.getDouble(0) > numberOfDumpedFrames;
    }

    private void resetBadFrameCapture() {
        resetWaitEntry();
        mRunEntry.setBoolean(false);
    }

    private void startBadFrameCapture() {
        resetWaitEntry();
        mRunEntry.setBoolean(true);
    }

    private void resetWaitEntry() {
        mWaitEntry.setDouble(0);
    }

    private void setVisionProcessingExposure() {
        exposureEntry.setDouble(VISION_PROCESSING_EXPOSURE);
    }

    private void setRegularExposure() {
        exposureEntry.setDouble(DEFAULT_EXPOSURE);
    }

    public double getVisionDistanceCm() {
        return distanceEntry.getDouble(0);
    }

    protected void setDistanceToTarget(double distance) {
        distanceToTarget = distance > 0 ? distance : 0;
    }

    public double getDistanceToTarget() {
        return distanceToTarget;
    }

    public long getVisionTime() {
        return (long)timeEntry.getDouble(-1);
    }

    public double getVisionAngle() {
        return angleEntry.getDouble(0);
    }
}
