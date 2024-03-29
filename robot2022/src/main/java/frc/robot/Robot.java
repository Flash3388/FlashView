package frc.robot;

import com.flash3388.flashlib.frc.robot.FrcRobotControl;
import com.flash3388.flashlib.frc.robot.base.iterative.IterativeFrcRobot;
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
import frc.robot.actions.commands.ActionCommandType;
import frc.robot.actions.commands.MoveCommand;
import frc.robot.actions.commands.RotateCommand;
import frc.robot.subsystems.Swerve;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Robot extends DelegatingRobotControl implements IterativeFrcRobot {

    private final Swerve swerve;

    public Robot(FrcRobotControl robotControl) {
        super(robotControl);
        swerve = SystemFactory.createSwerveSystem();
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

    }

    @Override
    public void autonomousInit() {
        try {
            Map<String, ActionCommandType> supportedCommands = new HashMap<>();
            supportedCommands.put("move", new MoveCommand(swerve));
            supportedCommands.put("rotate", new RotateCommand(swerve));

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

    }

    @Override
    public void testInit() {

    }

    @Override
    public void testPeriodic() {

    }

    @Override
    public void robotPeriodic() {

    }

    @Override
    public void robotStop() {

    }
}
