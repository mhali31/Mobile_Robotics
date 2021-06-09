#Programming a Mobile Robot to Navigate Autonomously
This was a university project where a Pioneer robot was used programmed to travel around a room autonomously using a track and avoid subsumption architecture model as well as an odemetry model.

The objective of the project was to test how effective both models were at reaching their destinations. In the experiments, the odometry model was run first, followed by the subsumption architecture.

The files contained in this repository are the original files created by myself as the whole project contained files created by my lecturer.

The first stage of the project was to create the Control script, which contains methods that control the methods to control the desired movement of the robot. Additonally, the Control script contains methods that help us understand the position and orientation of the robot.

The behaviour script contains the different behaviour that is needed to implement the two autonomous navigation methods. The track method uses Blob detection to determine what actions should be taken based on the visible location of the object the robot is following. The avoid method uses the sonar sensor readings to determine how the robot should behave in order to avoid any obstacles in the room. The odemetry method uses the robots orientation and position to determine what actions needs to be taken (turn or move) in order to reach the next destination. The shutdown method was created to end the experiment after the robot has reached it's final destination.

Since the behaviours are booleans, in the Run script, the odemetry behaviour is run until it is true, which then activates the subsumption architecture of track and avoid. 
