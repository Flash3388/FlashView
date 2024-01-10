package frc.robot.subsystems;

import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.math.geometry.Rotation2d;
import edu.wpi.first.math.geometry.Translation2d;
import edu.wpi.first.math.kinematics.SwerveDriveKinematics;
import edu.wpi.first.math.trajectory.Trajectory;
import edu.wpi.first.math.trajectory.TrajectoryConfig;
import edu.wpi.first.math.trajectory.TrajectoryGenerator;
import edu.wpi.first.math.trajectory.constraint.SwerveDriveKinematicsConstraint;
import edu.wpi.first.math.trajectory.constraint.TrajectoryConstraint;
import edu.wpi.first.math.util.Units;

import java.util.ArrayList;
import java.util.List;

public class TrajectoryExample {
    private double maxSpeed;
    private double maxAcceleration;
    private double startVelocity;
    private double endVelocity;
    private boolean reversed;
    private TrajectoryConstraint constraints;
    private SwerveDriveKinematics swerveDriveKinematics;

    public TrajectoryExample(double maxSpeed, double maxAcceleration, SwerveDriveKinematics SwerveDriveKinematics){
        this.maxSpeed = maxSpeed;
        this.maxAcceleration = maxAcceleration;
        this.swerveDriveKinematics = SwerveDriveKinematics;
    }

    public Trajectory generateTrajectory() { // example of trajectory
        var sideStart = new Pose2d(Units.feetToMeters(1.54), Units.feetToMeters(23.23), // start point
                Rotation2d.fromDegrees(-180));
        var crossScale = new Pose2d(Units.feetToMeters(23.7), Units.feetToMeters(6.8), // end point
                Rotation2d.fromDegrees(-160));


        var interiorWaypoints = new ArrayList<Translation2d>(); // points in the way
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(14.54), Units.feetToMeters(23.23)));
        interiorWaypoints.add(new Translation2d(Units.feetToMeters(21.04), Units.feetToMeters(18.23)));


        TrajectoryConfig config = new TrajectoryConfig(Units.feetToMeters(12), Units.feetToMeters(12)); // config
        //config.setReversed(true);

        var trajectory = TrajectoryGenerator.generateTrajectory( // the trajectory
                sideStart,
                interiorWaypoints,
                crossScale,
                config);
        return trajectory;
    }

    public void connectTrajectories(){
        var trajectoryOne =
                TrajectoryGenerator.generateTrajectory(
                        new Pose2d(0, 0, Rotation2d.fromDegrees(0)), // start point
                        List.of(new Translation2d(1, 1), new Translation2d(2, -1)), // points in between
                        new Pose2d(3, 0, Rotation2d.fromDegrees(0)),  // end point
                        new TrajectoryConfig(Units.feetToMeters(3.0), Units.feetToMeters(3.0))); // max velocity and acceleration

        var trajectoryTwo =
                TrajectoryGenerator.generateTrajectory(
                        new Pose2d(3, 0, Rotation2d.fromDegrees(0)),
                        List.of(new Translation2d(4, 4), new Translation2d(6, 3)),
                        new Pose2d(6, 0, Rotation2d.fromDegrees(0)),
                        new TrajectoryConfig(Units.feetToMeters(3.0), Units.feetToMeters(3.0)));

        var concatTraj = trajectoryOne.concatenate(trajectoryTwo); // connect the trajectories

        TrajectoryConstraint j = new TrajectoryConstraint() { // how to create my own constraint
            @Override
            public double getMaxVelocityMetersPerSecond(Pose2d poseMeters, double curvatureRadPerMeter, double velocityMetersPerSecond) {
                return 0;
            }

            @Override
            public MinMax getMinMaxAccelerationMetersPerSecondSq(Pose2d poseMeters, double curvatureRadPerMeter, double velocityMetersPerSecond) {
                return null;
            }
        };

        //Constraint for swerve
        SwerveDriveKinematicsConstraint d = new SwerveDriveKinematicsConstraint(swerveDriveKinematics, 4.19);

    }

    public void ManipulatingTrajectories(){ // Manipulating Trajectories example
        //Once a trajectory has been generated, you can retrieve information from it using certain methods.
        // These methods will be useful when writing code to follow these trajectories.

        // Get the total time of the trajectory in seconds
        Trajectory trajectory = generateTrajectory();
        double duration = trajectory.getTotalTimeSeconds();

        // Sample the trajectory at 1.2 seconds. This represents where the robot
        // should be after 1.2 seconds of traversal.
        Trajectory.State point = trajectory.sample(1.2);
        double t = point.timeSeconds; // the time of this pose
        double v = point.velocityMetersPerSecond; // the velocity of this point
        double a = point.accelerationMetersPerSecondSq; // the acceleration of this point
        Pose2d p = point.poseMeters; // the pose of this point
    }

    public Trajectory createSimpleTrajectory(){
        var start = new Pose2d(0,0, // start point
                Rotation2d.fromDegrees(0));

        var end = new Pose2d(4, 0, // end point
                Rotation2d.fromDegrees(0));



        SwerveDriveKinematicsConstraint d = new SwerveDriveKinematicsConstraint(swerveDriveKinematics, 4.19);

        TrajectoryConfig config = new TrajectoryConfig(4, 2); // config
        config.addConstraint(d);


        var trajectory = TrajectoryGenerator.generateTrajectory( // the trajectory
                start,
                new ArrayList<Translation2d>(),
                end,
                config);

        return trajectory;
    }

}