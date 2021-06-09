package robot;

import utils.Delay;

import java.sql.Time;
import java.util.concurrent.TimeUnit;

/**
 * Created with IntelliJ IDEA.
 * Created by: Hassan Ali
 * User: mhali3112
 * Date: 15/05/18
 */
public class Behaviour {

    int right = 0;
    int left = 0;
    private static final int TURN = 0;
    private static final int MOVE = 1;
    private int step = MOVE;
    public int i = 0;
    public boolean stop = false;
    public static long startTime=0, diffTime=0, estimatedTime=0;
    private static double NODE[][] =
            {

                    {1473,   0},
                    {1324, 2075},
                    {-331,  1852},
                    {-735,  4288},
                    {-115,  4521},
                    {1611, 8208}

            };
    private Robot robot;

    public Behaviour (Robot robot)
    {
        this.robot = robot;
    }

    public boolean shutdown()
    {
        double x  = NODE[i][0];
        double y  = NODE[i][1];


        System.out.println("shut");
        System.out.println("realx="+robot.kinematics.getX() +", y="+ robot.kinematics.getY()+ ", distance= "+robot.control.getTotalDistance(x,y));

        if(robot.control.isLinearDestination(x,y))
        {
            i++;
            if (i== NODE.length)
            {
                stop = true;
                estimatedTime = System.currentTimeMillis();
                diffTime = estimatedTime-startTime;
                diffTime= TimeUnit.SECONDS.convert(diffTime, TimeUnit.MILLISECONDS) ;
                System.out.println("difftime = "+ diffTime);
                System.out.println("stop is true");
                return (false);
            }
        }
        return false;
    }

    public boolean track(double vel)
    {
        System.out.println("track");

        if(robot.sensor.getBlobX() < 210)
        {

            robot.control.turnSpot(-vel/2);
            System.out.println("left");

        }
        else if (robot.sensor.getBlobX() >=210 && robot.sensor.getBlobX()<=430)
        {
            System.out.println("forward");
            robot.control.move(vel);

            if(robot.sensor.getSonarRange(4)<=300 && robot.sensor.getSonarRange(3)<=300)
            {
                robot.control.stop();
                System.out.println("stop");
                Delay.ms(2000);
                return (false) ;
            }

        }
        else if(robot.sensor.getBlobX()> 430)
        {
            System.out.println("right");
            robot.control.turnSpot(vel/2);
        }

        return false;
    }


    public boolean avoid(double vel)
    {
        System.out.println("avoid");

        //System.out.println("moving="+ robot.control.isMoving());

        if(!robot.control.isMoving())
        {
            if (robot.sensor.getSonarRange(3) <300 || robot.sensor.getSonarRange(4)<300)
            {
                robot.control.stop();
                Delay.ms(500);
                robot.control.move(-vel);
                Delay.ms(1000);

                if(robot.sensor.getSonarRange(0)<=300 || robot.sensor.getSonarRange(1)<=300 || robot.sensor.getSonarRange(2)<=300)
                {
                    robot.control.turnSpot(vel);
                    Delay.ms(2500);
                }
                else if(robot.sensor.getSonarRange(7)<=300 || robot.sensor.getSonarRange(6)<=300 || robot.sensor.getSonarRange(5)<=300)
                {
                    robot.control.turnSpot(-vel);
                    Delay.ms(2500);
                }
            }
        }
        else
        {
            robot.control.move(vel);
            if(robot.sensor.getSonarRange(1)<=300 || robot.sensor.getSonarRange(2)<=300 || robot.sensor.getSonarRange(3)<=300)
            {
                robot.control.turnSharp(vel);
                System.out.println("right");
                right ++;
                Delay.ms(500);

                if (right >=2 && left >=1)
                {
                    robot.control.turnSpot(vel);
                    System.out.println("turnaround right");
                    left=0;
                    right=0;
                    Delay.ms(3500);
                }
                else if (left==0 && right>1)
                {
                    right=0;
                }
            }

            else if(robot.sensor.getSonarRange(4)<=300 || robot.sensor.getSonarRange(5)<=300 || robot.sensor.getSonarRange(6)<=300)
            {
                robot.control.turnSharp(-vel);
                left ++;
                System.out.println("left");
                Delay.ms(500);

                if(left>=2 && right>=1)
                {
                    robot.control.turnSpot(-vel);
                    System.out.println("turnaround left");
                    left=0;
                    right=0;
                    Delay.ms(3500);
                }
                else if(right==0 && left >1)
                {
                    left=0;
                }
            }
        }
        return (false);
    }


    public boolean odometryModel(double vel)
    {
        double x  = NODE[i][0];
        double y  = NODE[i][1];
        double th = robot.control.getAngle(x, y);
        double actual = robot.control.get360(robot.kinematics.getTh());
        double real = th-actual;

        System.out.println("i="+i + ", x="+ x + ", y=" + y + ", th="+th);
        System.out.println("realx="+robot.kinematics.getX() +", y="+ robot.kinematics.getY()+ ", distance= "+robot.control.getTotalDistance(x,y));
        switch(step)
        {

            case TURN:
            {

                if(real>0)
                {
                    robot.control.turnSpot(-vel);
                }
                else if(real<0)
                {
                    robot.control.turnSpot(vel);
                }

                if(robot.control.isAngularDestination(th))
                {
                    step = MOVE;
                }

            }break;
            case MOVE:

            {

                robot.control.move(vel);
                if(robot.control.isLinearDestination(x,y))
                {
                    step = TURN;
                    if (i==0)
                    {
                        startTime=estimatedTime;
                    }

                    i++;
                }
            }break;
        }

        if (i== NODE.length-1)
        {
            System.out.println("starttime = "+ startTime);
            estimatedTime = System.currentTimeMillis();
            diffTime = estimatedTime-startTime;
            diffTime= TimeUnit.SECONDS.convert(diffTime, TimeUnit.MILLISECONDS) ;
            System.out.println("difftime = "+ diffTime);

            //start the timer for track
            startTime=estimatedTime;
            System.out.println("esttime = " + estimatedTime);
            robot.control.stop();
            return (true);

        }
        return(false);
    }

}
