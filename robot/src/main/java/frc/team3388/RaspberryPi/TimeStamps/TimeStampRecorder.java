package frc.team3388.RaspberryPi.TimeStamps;

import com.flash3388.flashlib.time.Time;

import java.util.ArrayList;
import java.util.List;

public class TimeStampRecorder {
    private final static int MAX_SIZE = 100;

    private List<AngularFrame> frames;

    public TimeStampRecorder() {
        frames = new ArrayList<AngularFrame>();
    }

    public void append(Time timestamp, double angle) {
        if (frames.size() > MAX_SIZE)
            clear();
        frames.add(new AngularFrame(timestamp, angle));
    }

    public void clear() {
        frames.clear();
    }

    public double getAngleAt(Time timestamp) {
        AngularFrame frame = getCorrespondingAngle(timestamp);

        cleanUp(frames.indexOf(frame));

        return frame.angle;
    }

    public AngularFrame getCorrespondingAngle(Time timestamp) {
        if (timestamp.compareTo(frames.get(0).timestamp) < 0)
            return frames.get(0);
        if (timestamp.compareTo(frames.get(frames.size() - 1).timestamp) > 0)
            return frames.get(frames.size()-1);

        int i = 0, j = frames.size(), mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (frames.get(mid).timestamp == timestamp)
                return frames.get(mid);
            if (timestamp.compareTo(frames.get(mid).timestamp) < 0) {
                if (mid > 0 && timestamp.compareTo(frames.get(mid-1).timestamp) > 0)
                    return getClosest(frames.get(mid-1), frames.get(mid), timestamp);
                j = mid;
            } else {
                if (mid < frames.size() - 1 && timestamp.compareTo(frames.get(mid+1).timestamp) < 0)
                    return getClosest(frames.get(mid), frames.get(mid+1), timestamp);
                i = mid + 1;
            }
        }

        return frames.get(mid);
    }

    private void cleanUp(int index) {
        for (int i = 0; i <= index; ++i) {
            frames.remove(i);
        }
    }

    private static AngularFrame getClosest(AngularFrame frame1, AngularFrame frame2, Time timestamp) {
        if (timestamp.sub(frame1.timestamp).compareTo(frame2.timestamp.sub(timestamp)) >= 0)
            return frame2;
        else
            return frame1;
    }
}
