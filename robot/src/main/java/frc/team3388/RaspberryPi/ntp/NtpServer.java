package frc.team3388.RaspberryPi.ntp;

import com.flash3388.flashlib.time.Clock;
import edu.wpi.first.networktables.EntryListenerFlags;
import edu.wpi.first.networktables.EntryNotification;
import edu.wpi.first.networktables.NetworkTableEntry;

public class NtpServer {

    private final NetworkTableEntry mClientEntry;
    private final NetworkTableEntry mServerRecTimeEntry;
    private final NetworkTableEntry mServerSendTimeEntry;
    private final Clock mClock;

    public NtpServer(NetworkTableEntry clientEntry, NetworkTableEntry serverRecTimeEntry, NetworkTableEntry serverSendTimeEntry, Clock clock) {
        mClientEntry = clientEntry;
        mClientEntry.setBoolean(false);

        mServerSendTimeEntry = serverSendTimeEntry;
        mServerSendTimeEntry.setDouble(0.0);

        mServerRecTimeEntry = serverRecTimeEntry;
        mServerRecTimeEntry.setDouble(0.0);

        mClock = clock;

        mClientEntry.addListener(this::onClientRequest, EntryListenerFlags.kUpdate);
    }

    private void onClientRequest(EntryNotification notification) {
        if (!notification.value.getBoolean()) {
            return;
        }

        long receiveTimestamp = mClock.currentTime().valueAsMillis();
        mServerRecTimeEntry.setDouble(receiveTimestamp);

        long sendTimestamp = mClock.currentTime().valueAsMillis();
        mServerSendTimeEntry.setDouble(sendTimestamp);

        mClientEntry.setBoolean(false);
    }
}
