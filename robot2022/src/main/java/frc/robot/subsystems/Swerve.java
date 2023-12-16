package frc.robot.subsystems;

import com.ctre.phoenix.sensors.WPI_Pigeon2;
import com.flash3388.flashlib.scheduling.Subsystem;
import com.jmath.ExtendedMath;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.ChassisSpeeds;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.SwerveModule;

public class Swerve extends Subsystem {

    private static final double OFFSET = 0.37;
    public static final double MAX_SPEED = 4.4196;

    private final SwerveModule[] swerveModules; //The array should contain the modules by
    // front-left, front-right, back-left, back-right
    private final WPI_Pigeon2 gyro;
    private final SwerveDriveKinematics swerveDriveKinematics;

    public Swerve(SwerveModule[] swerveModules, WPI_Pigeon2 gyro) {
        this.swerveModules = swerveModules;
        this.gyro = gyro;

        Translation2d fL = new Translation2d(OFFSET, OFFSET);
        Translation2d fR = new Translation2d(OFFSET, -OFFSET);
        Translation2d bL = new Translation2d(-OFFSET, OFFSET);
        Translation2d bR = new Translation2d(-OFFSET, -OFFSET);

        gyro.reset();
        swerveDriveKinematics = new SwerveDriveKinematics(fL,fR,bL,bR);
    }


    public double getHeadingDegrees() {
        return gyro.getAngle();
    }

    public void resetDistancePassed() {
        swerveModules[0].resetDistancePassed();
    }

    public double getDistancePassedMeters() {
        return -swerveModules[0].getDistancePassedMeters();
    }

    public void stop(){
        for (int i = 0; i < 4; i++) {
            this.swerveModules[i].stop();
        }
    }

    public void move(double drive, double rotation){
        for (int i = 0; i < 4; i++) {
            this.swerveModules[i].move(drive,rotation);
        }
    }

    public void setDesiredStates(SwerveModuleState[] states){
        for (int i = 0; i < 4; i++) {
            this.swerveModules[i].setDesiredState(states[i]);
        }
    }

    public void resetWheels(){
        this.drive(0,0,0);
        /*
        SwerveModuleState[] startingPosition = new SwerveModuleState[]{
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0)),
                new SwerveModuleState(0, Rotation2d.fromDegrees(0))
        };
        setDesiredStates((startingPosition));
         */






    }

    public void drive(double speedY, double speedX, double rotation){
      /*  if (rot == 0) {
            if (!ExtendedMath.constrained(getAngle(), currentAngle - 1, currentAngle + 1)) {
                rot = ExtendedMath.constrain(pid.applyAsDouble(getAngle(), currentAngle), -0.1, 0.1);
            }
        } else {
            currentAngle = getAngle();
        }


         SwerveModuleState[] swerveModuleStates;
        if(fieldRelative) {
            swerveModuleStates = kinematics.toSwerveModuleStates(
                    ChassisSpeeds.fromFieldRelativeSpeeds(speedX, speedY, rot, Rotation2d.fromDegrees(-getAngle())));
        } else {
            swerveModuleStates = kinematics.toSwerveModuleStates(new ChassisSpeeds(speedX, speedY, rot));
        }

        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, Constants.MAX_SPEED_METERS_PER_SECOND);

        setDesiredStates(swerveModules, swerveModuleStates);
        */




        //Part V: Kinematics of Swerve
        SwerveModuleState[] swerveModuleStates = swerveDriveKinematics.toSwerveModuleStates(new ChassisSpeeds(speedY, speedX, rotation)); //convert kinematics to states[]
        SwerveDriveKinematics.desaturateWheelSpeeds(swerveModuleStates, MAX_SPEED); //limits the speed
        setDesiredStates(swerveModuleStates);
        SmartDashboard.putNumber("LF:", swerveModuleStates[0].speedMetersPerSecond);
    }


    public void print() {
        SmartDashboard.putNumber("FL Heading", swerveModules[0].getHeadingDegrees());
        SmartDashboard.putNumber("FR Heading", swerveModules[1].getHeadingDegrees());
        SmartDashboard.putNumber("RL Heading", swerveModules[2].getHeadingDegrees());
        SmartDashboard.putNumber("RR Heading", swerveModules[3].getHeadingDegrees());
        SmartDashboard.putNumber("FL Velocity", swerveModules[0].getVelocityRpm());
        SmartDashboard.putNumber("FR Velocity", swerveModules[1].getVelocityRpm());
        SmartDashboard.putNumber("RL Velocity", swerveModules[2].getVelocityRpm());
        SmartDashboard.putNumber("RR Velocity", swerveModules[3].getVelocityRpm());
        SmartDashboard.putNumber("FL abs", swerveModules[0].getAbsEncoder());
        SmartDashboard.putNumber("FR abs", swerveModules[1].getAbsEncoder());
        SmartDashboard.putNumber("RL abs", swerveModules[2].getAbsEncoder());
        SmartDashboard.putNumber("RR abs", swerveModules[3].getAbsEncoder());
        SmartDashboard.putNumber("Gyro angle",this.getHeadingDegrees());
    }
}
