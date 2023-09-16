package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.Subsystem;
import com.flash3388.flashlib.time.Time;

public class Elevator extends Subsystem {

    private WPI_TalonSRX engine1;
    private WPI_TalonSRX engine2;
    private PidController pid;
    private final double KP = 0.02;
    private final double KI = 0;
    private final double KD = 0;
    private final double KF = 0;

    public Elevator(WPI_TalonSRX engine1, WPI_TalonSRX engine2) {
        this.engine1 = engine1;
        this.engine2 = engine2;
        engine2.follow(engine1);
        pid = new PidController(RunningRobot.getControl().getClock(), KP, KI, KD, KF);
        pid.setOutputLimit(1);
        pid.setTolerance(0.1, Time.milliseconds(0));
    }

    public double getHight(double x) {
        return engine1.getSelectedSensorPosition() / 40962 * 0.04 * Math.PI;
    }

    public void up(double x) {
        engine1.set(x);
    }

    public void down(double x) {
        engine1.set(-x);
    }

    public void hold(double x) {
        engine1.set(0.1);
    }

    public void stop(double x) {
        engine1.set(0);
    }


}