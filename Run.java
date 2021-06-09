import robot.Behaviour;
import robot.Mapping;
import robot.Robot;
import robot.stopwatch;
import utils.Delay;

/**
 * Created by Hassan Ali.
 * Class    : Run
 * Version  : v1.0
 * Date     : 15/05/18
 * User     : mhali3112
 * email    : mhali3112@gmail.com
 * Comments : To run the Aria interface for both, simulator and real robot.
 **/

public class Run
{
    private static Robot robot;

   /**
    * Method     : Run::Run()
    * Purpose    : Secondary Run class constructor.
    * Parameters : args : The program's arguments.
    * Returns    : Nothing.
    * Notes      : None.
    **/
   private static Behaviour behaviour;

    public Run(String args[])
    {
        robot = new Robot();
        robot.init(args, robot);
        behaviour = new Behaviour(robot);

        // [+]Thread setup:
        update.setPriority(Thread.MAX_PRIORITY);
        update.start();
    }

   /**
    * Thread     : Run::update()
    * Purpose    : To run the update thread.
    * Parameters : None.
    * Returns    : Nothing.
    * Notes      : None.
    **/
    Thread update = new Thread()
    {
       public void run()
       {
           while(true)
           {

               Delay.ms(1);
           }
       }
    };



   /**
    * Method     : Run::main()
    * Purpose    : Default main method which runs the Run class.
    * Parameters : - args : Initialization parameters.
    * Returns    : Nothing.
    * Notes      : None.
    **/
    public static void main(String args[])
    {
        boolean trackflag = false;
        boolean avoidflag = false;
        boolean omFlag =  false;
        new Run(args);
        Delay.ms(10000);
        behaviour.startTime = System.currentTimeMillis();
        behaviour.estimatedTime=System.currentTimeMillis();

        while(true)
        {

            if(!omFlag)
            {
                omFlag = behaviour.odometryModel(75);
            }

            else
            {
                avoidflag= behaviour.avoid(75);
                if(!avoidflag)
                {
                     trackflag= behaviour.track(75);
                      if(!trackflag)
                      {
                          behaviour.shutdown();
                          if (behaviour.stop== true)
                          {
                              robot.shutDown();
                              System.out.println("end");
                              break;
                          }

                      }

                }
            }

            Delay.ms(100);

        }
    }
}
