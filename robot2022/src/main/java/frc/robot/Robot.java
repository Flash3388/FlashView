package frc.robot;

import com.flash3388.flashlib.frc.robot.FrcRobotControl;
import com.flash3388.flashlib.frc.robot.base.iterative.IterativeFrcRobot;
import com.flash3388.flashlib.hid.XboxAxis;
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
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.VisionAutoAlignByDistanceX;
import frc.robot.actions.VisionAutoAlignByPigeon;
import frc.robot.actions.commands.*;
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

    private final Swerve swerve;
    private XboxController xbox;
    private Gripper gripper;
    private VisionSystem visionSystem;

    public Robot(FrcRobotControl robotControl) {
        super(robotControl);
        swerve = SystemFactory.createSwerveSystem();
        this.xbox = getHidInterface().newXboxController(RobotMap.XBOX);
        this.gripper = new Gripper();
        visionSystem = new VisionSystem();
     //   new Thread(new VisionTask(visionSystem)).start();
        xbox.getButton(XboxButton.Y).whenActive(new VisionAutoAlignByPigeon(visionSystem, swerve));
        xbox.getButton(XboxButton.X).whenActive(new VisionAutoAlignByDistanceX(visionSystem, swerve));

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

        double driveY = xbox.getAxis(XboxAxis.LeftStickY).getAsDouble()*4.4196;
      /*  double driveX = xbox.getAxis(XboxAxis.LeftStickX).getAsDouble()*4.4196;
        double rotation = xbox.getAxis(XboxAxis.RightStickX).getAsDouble()*4.4196;
        this.swerve.drive(driveY,driveX,rotation);
        swerve.print();*/
       // this.swerve.drive(driveY, 0, 0);


    }

    @Override

    public void autonomousInit() {
        try {
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
        }
    }

    @Override
    public void autonomousPeriodic() {
        SmartDashboard.putNumber("Angle", swerve.getHeadingDegrees());
        SmartDashboard.putNumber("Distance", swerve.getDistancePassedMeters());
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
        SmartDashboard.getNumber("Distance", swerve.getDistancePassedMeters());
    }

    @Override
    public void robotStop() {

    }
}
