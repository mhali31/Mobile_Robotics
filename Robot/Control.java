package robot;

import sun.org.mozilla.javascript.internal.ast.ReturnStatement;

/**
 * Created by Hassan Ali.
 * Class    : Control
 * Version  : v1.0
 * Date     : 15-05-2018
 * User     : mha3112
 * email    : mhali3112@gmail.com
 * Comments : None.
 **/

public class Control
{
    private Robot robot;

    /**
     * Method     : Control::Control()
     * Purpose    : Secondary Control class constructor that initialises a Robot object.
     * Parameters : robot : An object of Class Robot.
     * Returns    : Nothing.
     * Notes      : This class contains robot control methods.
     **/
    public Control(Robot robot)
    {
        this.robot = robot;
    }

    /**
     * Method     : Control::setVel()
     * Purpose    : To set the robot's velocities.
     * Parameters : - lvel : The left velocity.
     *              - rvel : The right velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void setVel(double lvel, double rvel)
    {
        robot.arRobot.setVel2(lvel, rvel);
    }

    /**
     * Method     : Control::stop()
     * Purpose    : To stop the robot.
     * Parameters : None
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void stop()
    {
        robot.arRobot.stop();
    }

    /**
     * Method     : Control::move()
     * Purpose    : To move forward and backward.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void move(double vel)
    {
        setVel(vel, vel);
    }

    /**
     * Method     : Control::turnSpot()
     * Purpose    : To turn on the spot.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSpot(double vel)
    {
        setVel(vel, -vel);
    }

    /**
     * Method     : Control::turnSharp()
     * Purpose    : To turn on one wheel.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSharp(double vel)
    {
        if(vel >= 0) setVel(vel, 0);
        else         setVel(0, Math.abs(vel));
    }

    /**
     * Method     : Control::turnSmooth()
     * Purpose    : To turn with half speed on one wheel.
     * Parameters : vel : The robot velocity.
     * Returns    : Nothing.
     * Notes      : None.
     **/
    public void turnSmooth(double vel)
    {
        if(vel > 0) setVel(vel, vel/2);
        else        setVel(Math.abs(vel)/2, Math.abs(vel));
    }

    public boolean isMoving()
    {
        if(robot.arRobot.getLeftVel()==0 && robot.arRobot.getRightVel()==0){
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean isAngularDestination(double th)
    {

        if(get360(robot.kinematics.getTh())<=th+10 && get360(robot.kinematics.getTh()) >= th - 10)
        {
           return true;
        }
        else
        {
           return false;
        }
    }


    public boolean isLinearDestination(double x, double y)
    {
        if(getTotalDistance(x,y)<=120)
         {
             return true;
         }
         else
         {
             return false;
         }
    }

   public double getTotalDistance(double x, double y)
   {
       double s = robot.kinematics.getX();
       double t = robot.kinematics.getY();
       double distance1 = ((s-x)*(s-x));
       double distance2 = ((t-y)*(t-y));
       return Math.sqrt(distance1+distance2);

   }

    public double getDegToRad(double th)
    {
      double angle =  get360(th);
      return Math.toRadians(angle);
    }

    public double get360(double th)
    {
        return (th-360 * Math.floor(th/360));
    }

     public double getAngle(double x, double y)
     {

        return (get360(Math.toDegrees(Math.atan2(y-robot.kinematics.getY(),x-robot.kinematics.getX()))));

     }
}
