package frc.robot.actions;

import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import edu.wpi.first.math.trajectory.Trajectory;
import frc.robot.subsystems.Swerve;

public class FollowTrajectory extends ActionBase {
    private Swerve swerve;
    private Trajectory trajectory;

    public FollowTrajectory(Swerve swerve, Trajectory trajectory){
        this.swerve = swerve;
        this.trajectory = trajectory;

        requires(swerve);
    }

    @Override
    public void initialize(ActionControl control) {

    }

    @Override
    public void execute(ActionControl control) {

    }

    @Override
    public void end(FinishReason reason) {

    }
}
