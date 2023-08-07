package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxPIDController;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.math.geometry.Rotation2d;

public class SwerveModule {

    private static final double STEER_P = 0.05;
    private static final double STEER_I = 0.;
    private static final double STEER_D = 0.01;
    private static final double STEER_F = 0.;

    private static final double DRIVE_P = 0.0003;
    private static final double DRIVE_I = 5e-7;
    private static final double DRIVE_D = 5e-7;
    private static final double DRIVE_F = 0.0001;

    private static final double GEAR_RATIO_DRIVE = 1/6.75;
    private static final double WHEEL_RADIUS = 0.5005;

    private CANSparkMax drive; //forwards and backwards
    private CANSparkMax steer; // rotates
    private RelativeEncoder driveEncoder;
    private RelativeEncoder steerEncoder;
    private SparkMaxPIDController pidDrive;
    private SparkMaxPIDController pidSteer;
    private CANCoder absoluteEncoder;
    private static final double GEAR_RATIO_STEER = 1 / 12.8;

    public SwerveModule(CANSparkMax drive, CANSparkMax steer, CANCoder absoluteEncoder, double zeroAngle){
        this.drive=drive;
        this.steer=steer;
        driveEncoder = this.drive.getEncoder();
        steerEncoder = this.steer.getEncoder();
        driveEncoder.setPosition(0);
        steerEncoder.setPosition(0);
        pidDrive= this.drive.getPIDController();
        pidSteer= this.steer.getPIDController();
        this.pidDrive.setOutputRange(-1,1);
        this.pidSteer.setOutputRange(-1,1);
        this.absoluteEncoder = absoluteEncoder;
        double positionAbsEncoder = this.absoluteEncoder.getAbsolutePosition(); //divide angles in the creation of the absolute encoder
        steerEncoder.setPosition((positionAbsEncoder - zeroAngle)/360/ GEAR_RATIO_STEER);

        pidSteer.setP(STEER_P);
        pidSteer.setI(STEER_I);
        pidSteer.setD(STEER_D);
        pidSteer.setFF(STEER_F);

        pidDrive.setP(DRIVE_P);
        pidDrive.setI(DRIVE_I);
        pidDrive.setD(DRIVE_D);
        pidDrive.setFF(DRIVE_F);
    }


    public void stop(){
        this.drive.stopMotor();
        this.steer.stopMotor();
    }
    public void move(double drive, double rotation){
        this.drive.set(drive);
        this.steer.set(rotation);
    }

    public double getHeadingDegrees(){
        return this.steerEncoder.getPosition() * (1/12.8) * 360;
    }

    public double getVelocityRpm(){
        return driveEncoder.getVelocity() * GEAR_RATIO_DRIVE; //in RPM units rotation per minute
    }

    public void resetDistancePassed() {
        driveEncoder.setPosition(0);
    }

    public double getDistancePassedMeters() {
        return driveEncoder.getPosition() * GEAR_RATIO_DRIVE * WHEEL_RADIUS;
    }

    public double getAbsEncoder() {
        return absoluteEncoder.getAbsolutePosition();
    }

    public void setDesiredState(SwerveModuleState desiredState){
        SwerveModuleState optimizedState = optimize(desiredState, Rotation2d.fromDegrees(getHeadingDegrees()));
        double velocityRpm = (optimizedState.speedMetersPerSecond * 60 / (WHEEL_RADIUS*2*Math.PI)) / GEAR_RATIO_DRIVE;
        this.pidDrive.setReference(velocityRpm,CANSparkMax.ControlType.kVelocity);
        double steeringValue = optimizedState.angle.getDegrees()/360/ GEAR_RATIO_STEER;
        this.pidSteer.setReference(steeringValue,CANSparkMax.ControlType.kPosition);
    }

    public static SwerveModuleState optimize(SwerveModuleState desiredState, Rotation2d currentAngle) {
        double targetAngle = placeInAppropriate0To360Scope(currentAngle.getDegrees(), desiredState.angle.getDegrees());
        double targetSpeed = desiredState.speedMetersPerSecond;
        double delta = targetAngle - currentAngle.getDegrees();
        if (Math.abs(delta) > 90) {
            targetSpeed = -targetSpeed;
            targetAngle = delta > 90 ? (targetAngle -= 180) : (targetAngle += 180);
        }
        return new SwerveModuleState(targetSpeed, Rotation2d.fromDegrees(targetAngle));
    }

    private static double placeInAppropriate0To360Scope(double scopeReference, double newAngle) {
        double lowerBound;
        double upperBound;
        double lowerOffset = scopeReference % 360;
        if (lowerOffset >= 0) {
            lowerBound = scopeReference - lowerOffset;
            upperBound = scopeReference + (360 - lowerOffset);
        } else {
            upperBound = scopeReference - lowerOffset;
            lowerBound = scopeReference - (360 + lowerOffset);
        }
        while (newAngle < lowerBound) {
            newAngle += 360;
        }
        while (newAngle > upperBound) {
            newAngle -= 360;
        }
        if (newAngle - scopeReference > 180) {
            newAngle -= 360;
        } else if (newAngle - scopeReference < -180) {
            newAngle += 360;
        }
        return newAngle;
    }
/*
    public void MoveWheelTo0() {
        SwerveModuleState optimizedState = optimize(new SwerveModuleState(), Rotation2d.fromDegrees(getHeadingDegrees()));
        double steeringValue = optimizedState.angle.getDegrees() / 360 / GEAR_RATIO;
        this.pidSteer.setReference(steeringValue, CANSparkMax.ControlType.kPosition);
        if(!ExtendedMath.constrained(getHeadingDegrees(), -2, 2))
            MoveWheelTo0();
    }*/
}
