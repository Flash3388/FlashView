package frc.team3388.RaspberryPi.VisionProcessing;

import com.flash3388.flashlib.robot.scheduling.Action;
import com.jmath.ExtendedMath;
import frc.team3388.subsystems.DriveSystem;

import java.util.Arrays;

public class AverageFrames extends Action {
    private final VisionProcessingUnit visionControl;

    private double[] distances;
    private double prev;
    private int count;
    private final int numberOfAveragedFrames;

    public AverageFrames(DriveSystem driveSystem, VisionProcessingUnit visionControl, int numberOfAveragedFrames) {
        requires(driveSystem);

        this.visionControl = visionControl;
        this.numberOfAveragedFrames = numberOfAveragedFrames;
    }

    @Override
    protected void initialize() {
        distances = new double[7];
        count = 0;
        prev = 0;
    }

    @Override
    protected void execute() {
        double curr = visionControl.getVisionDistanceCm();

        if(prev == 0 ||  curr != prev) {
            distances[count] = curr;
            prev = curr;
            count++;
        }
    }

    @Override
    protected void end() {
        double avg = ExtendedMath.avg(distances);
        visionControl.setDistanceToTarget(findClosest(distances,avg));
    }

    @Override
    protected boolean isFinished() {
        return count >= numberOfAveragedFrames;
    }

    private double findClosest(double[] arr, double target) {
        if (target <= arr[0])
            return arr[0];
        if (target >= arr[arr.length - 1])
            return arr[arr.length - 1];

        int i = 0, j = arr.length, mid = 0;
        while (i < j) {
            mid = (i + j) / 2;

            if (arr[mid] == target)
                return arr[mid];
            if (target < arr[mid]) {
                if (mid > 0 && target > arr[mid - 1])
                    return getClosest(arr[mid - 1], arr[mid], target);
                j = mid;
            } else {
                if (mid < arr.length - 1 && target < arr[mid + 1])
                    return getClosest(arr[mid], arr[mid + 1], target);
                i = mid + 1;
            }
        }

        return arr[mid];
    }

    private double getClosest(double val1, double val2, double target) {
        if (target - val1 >= val2 - target)
            return val2;
        else
            return val1;
    }
}
