package frc.robot.subsystems;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.flash3388.flashlib.scheduling.Subsystem;
import frc.robot.RobotMap;

public class Gripper extends Subsystem {
    private WPI_TalonSRX talonSRX;
    public Gripper() {
        this.talonSRX= new WPI_TalonSRX(RobotMap.GRIPPER);

    }
    public void holdCone() {
        talonSRX.set(0.2);
    }
    public void pickCone() {
        talonSRX.set(2);
    }
    public void releaseCone(){
        talonSRX.set(-2);
    }
    public void stop(){
        talonSRX.set(0);
    }

}
