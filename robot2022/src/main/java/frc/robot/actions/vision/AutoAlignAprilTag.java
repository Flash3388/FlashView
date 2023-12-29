package frc.robot.actions.vision;

import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.ActionControl;
import com.flash3388.flashlib.scheduling.FinishReason;
import com.flash3388.flashlib.scheduling.actions.ActionBase;
import com.flash3388.flashlib.time.Time;
import com.jmath.ExtendedMath;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

public class AutoAlignAprilTag extends ActionBase {
        private VisionSystem visionSystem;
        private Swerve swerve;
    private PidController pid;

    private static final double KP = 1;
    private static final double KI = 0;
    private static final double KD = 0;
    private static final double KF = 0;

    private double firstAngle = 0;
    private double setPoint;
    private static final double ERROR = 2;

        public AutoAlignAprilTag(VisionSystem visionSystem, Swerve swerve) {
            this.visionSystem = visionSystem;
            this.swerve = swerve;
            this.pid = new PidController(RunningRobot.getControl().getClock(), KP, KI, KD, KF);
            pid.setOutputLimit(-1, 1);
            pid.setTolerance(ERROR, Time.milliseconds(500));
            firstAngle = swerve.getHeadingDegrees();
            setPoint = firstAngle;
            requires(swerve, visionSystem);
        }

    @Override
    public void initialize(ActionControl control) {
        visionSystem.setPipelineAprilTag();
    }

    @Override
    public void execute(ActionControl control) {
        boolean specificAprilTag = SmartDashboard.getBoolean("Specific AprilTag?", false);
        double rotation = 0;

        if(specificAprilTag){
            double id = SmartDashboard.getNumber("AprilTag ID:", 0);
            if(visionSystem.isThereSpecificId(id)){
                rotation = pid.applyAsDouble(visionSystem.getAngleToSpecificAprilTag(id), 0);
                setPoint = swerve.getHeadingDegrees() + visionSystem.getAngleToSpecificAprilTag(id);
                if(ExtendedMath.constrained(visionSystem.getAngleToSpecificAprilTag(id), -ERROR, ERROR))
                    control.finish();
            }
            else { // lost the target
                rotation = pid.applyAsDouble(swerve.getHeadingDegrees(), setPoint);
                if(ExtendedMath.constrained(swerve.getHeadingDegrees(), setPoint-ERROR, setPoint+ERROR))
                    control.finish();
            }
        }

        else {
            if(visionSystem.hasTargets()){
                rotation = pid.applyAsDouble(visionSystem.getXAngleToTarget(), 0);
                setPoint = swerve.getHeadingDegrees() + visionSystem.getXAngleToTarget();
                if(ExtendedMath.constrained(visionSystem.getXAngleToTarget(), -ERROR, ERROR))
                    control.finish();
            }
            else {// lost the target
                rotation = pid.applyAsDouble(swerve.getHeadingDegrees(), setPoint);
                if (ExtendedMath.constrained(swerve.getHeadingDegrees(), setPoint - ERROR, setPoint + ERROR))
                    control.finish();
            }

        }

        swerve.drive(0,0,rotation, false);
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }
}
