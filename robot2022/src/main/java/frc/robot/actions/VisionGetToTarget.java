package frc.robot.actions;

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

public class VisionGetToTarget extends ActionBase {
    private final VisionSystem visionSystem;
    private final Swerve swerve;
    private PidController pidController;
    private final double KP = 3;
    private final double KI = 0.1;
    private final double KD = 0;
    private final double KF = 0.2;
    private final double PID_ERROR = 0.03;
    private final double PID_LIMIT = 1;
    private final double SET_POINT = 0;



    public VisionGetToTarget(VisionSystem visionSystem, Swerve swerve) {
        this.visionSystem = visionSystem;
        this.swerve = swerve;
        SmartDashboard.putNumber("KP", KP);
        SmartDashboard.putNumber("KI", KI);
        SmartDashboard.putNumber("KD", KD);
        SmartDashboard.putNumber("KF", KF);
        pidController = new PidController(RunningRobot.getControl().getClock(),
                ()-> {
                    return SmartDashboard.getNumber("KP", KP);
                },
                ()-> {
                    return SmartDashboard.getNumber("KI", KI);
                },
                ()-> {
                    return SmartDashboard.getNumber("KD", KD);
                },
                ()-> {
                    return SmartDashboard.getNumber("KF", KF);
                }); //something
        pidController.setTolerance(PID_ERROR, Time.milliseconds(500));
        pidController.setOutputLimit(PID_LIMIT);

        configure().setName("VisionGetToTarget").save();

        requires(swerve);
    }

    @Override
    public void initialize(ActionControl control) {
        pidController.reset();
        swerve.resetDistancePassed();
    }

    @Override
    public void execute(ActionControl actionControl) {
        double distance = visionSystem.tomFunction();
        if(!visionSystem.isThereATarget()){
            distance = distance - swerve.getDistancePassedMeters();
        }
        else{
            swerve.resetDistancePassed();
        }
        SmartDashboard.putNumber("distance", distance);
        double speedY = pidController.applyAsDouble(distance * 3, SET_POINT) * swerve.MAX_SPEED;
        SmartDashboard.putNumber("speedY", speedY);
        SmartDashboard.putNumber("pidVal", pidController.applyAsDouble(distance,SET_POINT));
        swerve.drive(-speedY, 0, 0);
    }

    @Override
    public boolean isFinished() {
        double distance = visionSystem.tomFunction() - swerve.getDistancePassedMeters();
        if(ExtendedMath.constrained(distance, -PID_ERROR, PID_ERROR)){
            SmartDashboard.putBoolean("got to target", true);
            return true;}
        else return false;
    }

    @Override
    public void end(FinishReason reason) {
        swerve.stop();
    }





}
