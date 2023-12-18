package frc.robot;

import com.ctre.phoenix.sensors.CANCoder;
import com.flash3388.flashlib.frc.robot.FrcRobotControl;
import com.flash3388.flashlib.frc.robot.base.iterative.IterativeFrcRobot;
import com.flash3388.flashlib.hid.XboxButton;
import com.flash3388.flashlib.hid.XboxController;
import com.flash3388.flashlib.robot.base.DelegatingRobotControl;
import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashlib.scheduling.actions.ActionGroup;
import com.flash3388.flashlib.scheduling.actions.Actions;
import com.flash3388.flashview.Program;
import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.io.CommandTypesLoader;
import com.flash3388.flashview.io.JsonCommandTypesLoader;
import com.flash3388.flashview.io.JsonProgramLoader;
import com.flash3388.flashview.io.ProgramLoader;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.kinematics.SwerveModuleState;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.*;
import frc.robot.actions.commands.*;
import frc.robot.actions.vision.DriveToCone_CameraOnly;
import frc.robot.actions.vision.VisionAutoAlignByDistanceX;
import frc.robot.actions.vision.VisionAutoAlignByPigeon;
import frc.robot.subsystems.Gripper;
import frc.robot.subsystems.Swerve;
import frc.robot.subsystems.VisionSystem;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Robot extends DelegatingRobotControl implements IterativeFrcRobot {

    private Swerve swerve;
    private XboxController xbox;
    private Gripper gripper;
    private VisionSystem visionSystem;
    SwerveModule module;

    public Robot(FrcRobotControl robotControl) {
        super(robotControl);
       // swerve = SystemFactory.createSwerveSystem();
        this.xbox = getHidInterface().newXboxController(RobotMap.XBOX);
        this.gripper = new Gripper();
        visionSystem = new VisionSystem();

      /*  xbox.getButton(XboxButton.Y).whenActive(new VisionAutoAlignByPigeon(visionSystem, swerve));
        xbox.getButton(XboxButton.X).whenActive(new VisionAutoAlignByDistanceX(visionSystem, swerve));
        xbox.getButton(XboxButton.A).whenActive(new DriveToCone_CameraOnly(visionSystem, swerve));
*/
       // swerve.setDefaultAction(new DriveWithXbox(swerve, xbox));

         module = new SwerveModule(new CANSparkMax(RobotMap.SWERVE_DRIVE_RL, CANSparkMaxLowLevel.MotorType.kBrushless),
                new CANSparkMax(RobotMap.SWERVE_STEER_RL, CANSparkMaxLowLevel.MotorType.kBrushless),
                new CANCoder(RobotMap.SWERVE_ABSOLUTE_ENCODER_RL), RobotMap.SWERVE_ABSOLUTE_ENCODER_FL_ZERO_ANGLE);
    }


    @Override
    public void disabledInit() {

    }

    @Override
    public void disabledPeriodic() {

    }

    @Override
    public void teleopInit() {

    }

    @Override
    public void teleopPeriodic() {

        module.setDesiredState(new SwerveModuleState(1,  Rotation2d.fromDegrees(90)));
        SmartDashboard.putNumber("RPM FL", module.getVelocityRpm());

        /*SmartDashboard.putNumber("Distance To Target", this.visionSystem.getDistanceToTarget());
        SmartDashboard.putNumber("Distance passed", swerve.getDistancePassedMeters());*/

        /*ActionGroup group = new PlaceCone(gripper).alongWith(new RotateAngle(swerve,40),
                new MoveDistance(swerve, 10));*/
    }

    @Override
    public void autonomousInit() {
     /*   try {
            Map<String, ActionCommandType> supportedCommands = new HashMap<>();
            supportedCommands.put("move", new MoveCommand(swerve));
            supportedCommands.put("rotate", new RotateCommand(swerve));
            supportedCommands.put("collect", new CollectCommand(gripper));
            supportedCommands.put("place", new PlaceCommand(gripper));


            File rootDeployPath = Filesystem.getDeployDirectory();

            Path commandTypesFile = rootDeployPath.toPath().resolve("commandTypes.json");
            CommandTypesLoader commandTypesLoader = new JsonCommandTypesLoader();
            List<CommandType> commandTypes = commandTypesLoader.load(commandTypesFile);

            Path commandsFile = rootDeployPath.toPath().resolve("flashviewProgram.json");
            ProgramLoader loader = new JsonProgramLoader(commandTypes);
            Program program = loader.load(commandsFile);

            ActionGroup group = Actions.sequential();
            for (Command command : program.getCommands()) {
                String id = command.getCommandType().getId();
                ActionCommandType commandType = supportedCommands.get(id);
                Action action = commandType.createAction(command.getParameters());
                group.add(action);
            }

            getLogger().info("Starting command sequence");
            group.start();
        } catch (IOException e) {
            getLogger().error("Error loading command sequence", e);
        }*/
    }

    @Override
    public void autonomousPeriodic() {
       /* SmartDashboard.putNumber("Angle", swerve.getHeadingDegrees());
        SmartDashboard.putNumber("Distance", swerve.getDistancePassedMeters());*/
    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

    @Override
    public void robotPeriodic() {
        SmartDashboard.getNumber("Distance", 0);
      //  SmartDashboard.getNumber("Distance", swerve.getDistancePassedMeters());
    }

    @Override
    public void robotStop() {

    }
}
