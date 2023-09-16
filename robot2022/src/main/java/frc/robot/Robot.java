package frc.robot;

import com.castle.time.Clock;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.IFollower;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.flash3388.flashlib.frc.robot.FrcRobotControl;
import com.flash3388.flashlib.frc.robot.base.iterative.IterativeFrcRobot;
import com.flash3388.flashlib.hid.XboxAxis;
import com.flash3388.flashlib.hid.XboxButton;
import com.flash3388.flashlib.hid.XboxController;
import com.flash3388.flashlib.robot.RunningRobot;
import com.flash3388.flashlib.robot.base.DelegatingRobotControl;
import com.flash3388.flashlib.robot.control.PidController;
import com.flash3388.flashlib.scheduling.actions.Action;
import com.flash3388.flashlib.scheduling.actions.ActionGroup;
import com.flash3388.flashlib.scheduling.actions.Actions;
import com.flash3388.flashlib.time.Time;
import com.flash3388.flashview.Program;
import com.flash3388.flashview.commands.Command;
import com.flash3388.flashview.commands.CommandType;
import com.flash3388.flashview.io.CommandTypesLoader;
import com.flash3388.flashview.io.JsonCommandTypesLoader;
import com.flash3388.flashview.io.JsonProgramLoader;
import com.flash3388.flashview.io.ProgramLoader;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel;
import edu.wpi.first.wpilibj.Filesystem;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import frc.robot.actions.VisionAutoAlign;
import frc.robot.actions.VisionAutoAlign_UsingGyro;
import frc.robot.actions.commands.*;
import frc.robot.subsystems.Elevator;
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
    private Elevator elevator;


    public Robot(FrcRobotControl robotControl) {
        super(robotControl);
        swerve = SystemFactory.createSwerveSystem();
        this.xbox = getHidInterface().newXboxController(RobotMap.XBOX);
        this.gripper = new Gripper();

        visionSystem = new VisionSystem();
       // elevator = new Elevator(new WPI_TalonSRX(7), new WPI_TalonSRX(8));
       // new Thread(new VisionTask(visionSystem)).start();


        xbox.getButton(XboxButton.A).whenActive(new VisionAutoAlign(visionSystem, swerve));
        xbox.getButton(XboxButton.B).whenActive(new VisionAutoAlign_UsingGyro(visionSystem, swerve));
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
       /*  double upY = xbox.getAxis(XboxAxis.LeftStickY).getAsDouble()*4.4196;
        double downX = xbox.getAxis(XboxAxis.LeftStickX).getAsDouble()*4.4196;
        double rotation = xbox.getAxis(XboxAxis.RightStickX).getAsDouble()*4.4196;
       // this.swerve.drive(driveY,driveX,rotation);
        //xbox.getButton(XboxButton.A).whenActive(new VisionAutoAlign(visionSystem, swerve));
        if(upY<0.1 && upY > -0.1) upY = 0;
        */




       // swerve.print();
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

        double driveY = xbox.getAxis(XboxAxis.LeftStickY).getAsDouble();
        double driveX = xbox.getAxis(XboxAxis.LeftStickX).getAsDouble();
        double rotation = xbox.getAxis(XboxAxis.RightStickX).getAsDouble();
        if(driveX< 0.2) driveX =0;
        if(driveY< 0.2) driveX =0;
        if(rotation< 0.2) driveX =0;



        this.swerve.drive(driveY * Swerve.MAX_SPEED,driveX * Swerve.MAX_SPEED,rotation * Swerve.MAX_SPEED);
        //xbox.getButton(XboxButton.A).whenActive(new VisionAutoAlign(visionSystem, swerve));

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
