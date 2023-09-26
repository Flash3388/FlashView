package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.flash3388.flashlib.hid.XboxController;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import frc.robot.subsystems.Swerve;

public class SystemFactory {

    public static Swerve createSwerveSystem(){
        SwerveModule[] swerveModules = new SwerveModule[4];

        CANSparkMax drive = new CANSparkMax(RobotMap.SWERVE_DRIVE_FL, CANSparkMaxLowLevel.MotorType.kBrushless);
        CANSparkMax steer = new CANSparkMax(RobotMap.SWERVE_STEER_FL, CANSparkMaxLowLevel.MotorType.kBrushless);
        CANCoder absoluteEncoder = new CANCoder(RobotMap.SWERVE_ABSOLUTE_ENCODER_FL);
        swerveModules[0] = new SwerveModule(drive,steer,absoluteEncoder, RobotMap.SWERVE_ABSOLUTE_ENCODER_FL_ZERO_ANGLE);

        drive = new CANSparkMax(RobotMap.SWERVE_DRIVE_FR, CANSparkMaxLowLevel.MotorType.kBrushless);
        steer = new CANSparkMax(RobotMap.SWERVE_STEER_FR, CANSparkMaxLowLevel.MotorType.kBrushless);
        absoluteEncoder = new CANCoder(RobotMap.SWERVE_ABSOLUTE_ENCODER_FR);
        swerveModules[1] = new SwerveModule(drive,steer,absoluteEncoder,RobotMap.SWERVE_ABSOLUTE_ENCODER_FR_ZERO_ANGLE);

        drive = new CANSparkMax(RobotMap.SWERVE_DRIVE_RL, CANSparkMaxLowLevel.MotorType.kBrushless);
        steer = new CANSparkMax(RobotMap.SWERVE_STEER_RL, CANSparkMaxLowLevel.MotorType.kBrushless);
        absoluteEncoder = new CANCoder(RobotMap.SWERVE_ABSOLUTE_ENCODER_RL);
        swerveModules[2] = new SwerveModule(drive,steer,absoluteEncoder,RobotMap.SWERVE_ABSOLUTE_ENCODER_RL_ZERO_ANGLE);

        drive = new CANSparkMax(RobotMap.SWERVE_DRIVE_RR, CANSparkMaxLowLevel.MotorType.kBrushless);
        steer = new CANSparkMax(RobotMap.SWERVE_STEER_RR, CANSparkMaxLowLevel.MotorType.kBrushless);
        absoluteEncoder = new CANCoder(RobotMap.SWERVE_ABSOLUTE_ENCODER_RR);
        swerveModules[3] = new SwerveModule(drive,steer,absoluteEncoder,RobotMap.SWERVE_ABSOLUTE_ENCODER_RR_ZERO_ANGLE);

        WPI_Pigeon2 gyro = new WPI_Pigeon2(RobotMap.PIGEON);

        return new Swerve(swerveModules, gyro);
    }
}
