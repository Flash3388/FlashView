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
    private final double KP = 1;
    private final double KI = 0.03;
    private final double KD = 0;
    private final double KF = 0;
    private final double PID_ERROR = 2;
    private final double PID_LIMIT = 1;
    private final double SET_POINT = 5;



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
    }

    @Override
    public void execute(ActionControl actionControl) {
        double distance = visionSystem.tomFunction();
        SmartDashboard.putNumber("distance", distance);
        //distanceX = contourCenter.x - imageCenter.x;
        double speedX = pidController.applyAsDouble(distance, SET_POINT) * swerve.MAX_SPEED;
        swerve.drive(0, speedX, 0);
    }

    @Override
    public boolean isFinished() {
        double distance = visionSystem.tomFunction();
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
