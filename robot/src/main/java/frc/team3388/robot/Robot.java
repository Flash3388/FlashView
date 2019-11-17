package frc.team3388.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.flash3388.flashlib.frc.robot.TimedFrcRobot;
import com.flash3388.flashlib.robot.hid.scheduling.HidScheduling;
import com.flash3388.flashlib.robot.hid.xbox.XboxButton;
import com.flash3388.flashlib.robot.hid.xbox.XboxController;
import com.flash3388.flashlib.time.Time;
import edu.wpi.first.wpilibj.ADXRS450_Gyro;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DigitalInput;
import frc.team3388.EncoderSRX;
import frc.team3388.Piston;
import frc.team3388.RaspberryPi.VisionProcessing.VisionProcessingUnit;
import frc.team3388.Switch;
import frc.team3388.actions.Edward;
import frc.team3388.actions.ManualLift;
import frc.team3388.flashview.CommandTypes.CargoCapture;
import frc.team3388.flashview.CommandTypes.CargoRelease;
import frc.team3388.flashview.CommandTypes.Drive;
import frc.team3388.flashview.CommandTypes.HatchCapture;
import frc.team3388.flashview.CommandTypes.HatchRelease;
import frc.team3388.flashview.CommandTypes.LiftDown;
import frc.team3388.flashview.CommandTypes.LiftUp;
import frc.team3388.flashview.CommandTypes.Rotate;
import frc.team3388.flashview.CommandTypes.Stop;
import frc.team3388.flashview.CommandTypes.Test;
import frc.team3388.flashview.Interpreter;
import frc.team3388.flashview.ProgramReader;
import frc.team3388.subsystems.CargoSystem;
import frc.team3388.subsystems.DriveSystem;
import frc.team3388.subsystems.HatchSystem;
import frc.team3388.subsystems.LiftSystem;

import java.io.BufferedReader;
import java.io.IOException;

public class Robot extends TimedFrcRobot {
    private DriveSystem driveSystem;
    private LiftSystem liftSystem;
    private CargoSystem cargoSystem;
    private HatchSystem hatchSystem;
    private VisionProcessingUnit visionProcessingUnit;

    private Interpreter interpreter;
    private ProgramReader programReader;

    private XboxController xbox;

    @Override
    protected void robotInit() {
        initSystems();
        setupControllers();
        initFlashview();
    }

    @Override
    protected void disabledInit() {

    }

    @Override
    protected void disabledPeriodic() {

    }

    @Override
    protected void teleopInit() {
    }

    @Override
    protected void teleopPeriodic() {
//        System.out.println("Vision: "+visionProcessingUnit.getVisionAngle()+" Gyro: "+driveSystem.getAngle());
    }

    @Override
    protected void autonomousInit() {
        hatchSystem.open();

        try {
            BufferedReader reader = programReader.tryReading();
            interpreter.generateProgram(reader).start();
            reader.close();
        }
        catch (IOException e) {
            getLogger().error("Flashview failed to read file",e);
        }
    }

    @Override
    protected void autonomousPeriodic() {

    }

    @Override
    protected void testInit() {

    }

    @Override
    protected void testPeriodic() {
    }

    @Override
    protected void robotPeriodic() {
    }

    private void initSystems() {
        WPI_TalonSRX frontRight = new WPI_TalonSRX(RobotMap.FRONT_RIGHT_MOTOR);
        WPI_TalonSRX rearRight = new WPI_TalonSRX(RobotMap.REAR_RIGHT_MOTOR);
        WPI_TalonSRX frontLeft = new WPI_TalonSRX(RobotMap.FRONT_LEFT_MOTOR);
        WPI_TalonSRX rearLeft = new WPI_TalonSRX(RobotMap.REAR_LEFT_MOTOR);

        driveSystem = new DriveSystem(frontRight,
                rearRight,
                frontLeft,
                rearLeft,
                new EncoderSRX(
                        rearRight,true),
                new EncoderSRX(
                        frontLeft,true),
                new ADXRS450_Gyro());

        liftSystem = new LiftSystem(new WPI_VictorSPX(RobotMap.RIGHT_LIFT_MOTOR),
                new WPI_VictorSPX(RobotMap.LEFT_LIFT_MOTOR),
                new Switch(
                        new DigitalInput(RobotMap.UPPER_SWITCH), true),
                new Switch(
                        new DigitalInput(RobotMap.LOWER_SWITCH), true));

        cargoSystem = new CargoSystem(new WPI_TalonSRX(RobotMap.ROLLER_GRIPPER_MOTOR),
                new Switch(
                        new DigitalInput(RobotMap.ROLLER_GRIPPER_SWIRTCH),true));

        hatchSystem = new HatchSystem(new Piston(0,
                RobotMap.HATCH_GRIPPER_CHANNEL_FORWARD,
                RobotMap.HATCH_GRIPPER_CHANNEL_BACKWARD));

        visionProcessingUnit = new VisionProcessingUnit(getClock());
    }

    private void initFlashview() {
        final int NUMBER_OF_FRAMES_TO_BYPASS = 5;
        final int NUMBER_OF_FRAMES_TO_AVERAGE = 7;

        interpreter = new Interpreter(new Test(getLogger()),
                new LiftUp(liftSystem),
                new LiftDown(liftSystem),
//                new Drive(driveSystem, 1.0, Time.seconds(1.0), getClock()),
//                new Rotate(driveSystem, 1.0, Time.seconds(1.0), getClock()),
                new Drive(driveSystem, 1, Time.milliseconds(1000), getClock()),
                new Rotate(driveSystem, 1, Time.milliseconds(1000), getClock()),
                new CargoCapture(driveSystem, cargoSystem, 0.2),
                new CargoRelease(cargoSystem),
                new HatchCapture(driveSystem, hatchSystem, visionProcessingUnit, getClock(), NUMBER_OF_FRAMES_TO_BYPASS, NUMBER_OF_FRAMES_TO_AVERAGE),
                new HatchRelease(driveSystem, hatchSystem, visionProcessingUnit, getClock(), NUMBER_OF_FRAMES_TO_BYPASS, NUMBER_OF_FRAMES_TO_AVERAGE),
                new Stop());

        programReader = new ProgramReader("/home/lvuser/SweetJesus.fv");
    }

    private void setupControllers() {
        xbox = new XboxController(RobotMap.XBOX_PORT);
        HidScheduling.addButtonsUpdateTaskToScheduler(xbox);

        liftSystem.setDefaultAction(new ManualLift(liftSystem,xbox));
        xbox.getButton(XboxButton.A).whenActive(new Edward(hatchSystem));
    }

    private void turnOffCompressor() { new Compressor().stop(); }
}
