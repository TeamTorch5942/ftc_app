package com.qualcomm.ftcrobotcontroller.opmodes.Torch.newAuton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;

/**
 * Created by taand on 3/10/2016.
 */
public abstract class BaseAuton extends LinearOpMode {

    //static means these can't be modified by the program.
    static int BLUE_TURN_ANGLE  = 130;
    static int RED_TURN_ANGLE   = 40;
    static int BLUE_MOVE_DIST   = 124;
    static int RED_MOVE_DIST    = 75;
    static int LIGHT_THRESHOLD  = 100;
    static int BLUE_LAST_DIST   = 20;
    static int RED_LAST_DIST    = 18;
    static int AVOID_RED        = 4;
    static int WAIT_TIME        = 10 * 1000; //ten seconds
    static int MAX_PUNCHIES     = 5;
    private int PUNCHIES        = 0;
    private int RED_DEADZONE    = 30;

    enum Wait {
        YES,NO
    }
    enum Alliance {
        RED,BLUE
    }
    abstract Wait     getWait();
    abstract Alliance getAlliance();

    boolean wait = getWait() == Wait.YES;

    @Override
    public void runOpMode() throws InterruptedException {
        Alliance alliance = getAlliance();
        RobotSetup fetty = new RobotSetup(hardwareMap,telemetry,this);


        waitForStart();
        fetty.dumpArm(fetty.DUMP_DOWN);


        if (wait) {sleep(WAIT_TIME);}

        //if alliance.BLUE == true, then dist = 24, else dist = 73
        int firstdist = alliance == alliance.BLUE ? BLUE_MOVE_DIST : RED_MOVE_DIST;
        int turn      = alliance == alliance.BLUE ? BLUE_TURN_ANGLE: RED_TURN_ANGLE;
        int lastdist  = alliance == alliance.BLUE ? BLUE_LAST_DIST : RED_LAST_DIST;
        int red_move  = alliance == alliance.BLUE ? 0 : AVOID_RED;
        double lastdir= alliance == alliance.BLUE ? 0.5 : -0.5;


        fetty.initializeServos();
        fetty.encoderMove(firstdist, 0.5);

        fetty.gTurn(turn, 0.4);

        fetty.encoderMove(red_move, .5);
        
        while (fetty.floorIR.getLightDetectedRaw() < LIGHT_THRESHOLD && opModeIsActive()) {
            waitOneFullHardwareCycle();
            fetty.move(0.2, 0.2);
        }
        fetty.allClearL(0.5);
        fetty.allClearR(0.5);
        fetty.encoderMove(2, .2 );
        fetty.dumpArm(fetty.DUMP_UP);
        sleep(1000);
        fetty.dumpArm(fetty.DUMP_DOWN);
        fetty.button(fetty.BUTTON_SEARCH);
        fetty.encoderMove(3.5, .5);
        sleep(1000);
        int color1     = alliance == alliance.BLUE ? fetty.colorSensor.blue() : fetty.colorSensor.red();
        int not_color1 = alliance == alliance.BLUE ? fetty.colorSensor.red() : fetty.colorSensor.blue();

        //Checks if the button is pressed and beacon gets brighter
        if (color1 > not_color1) {
            fetty.punch(1);
            int color2 = alliance == alliance.BLUE ? fetty.colorSensor.blue() : fetty.colorSensor.red();

            while (color1 >= color2 && PUNCHIES < MAX_PUNCHIES) {
                fetty.punch(1);
                PUNCHIES = PUNCHIES + 1;
                color2 = alliance == alliance.BLUE ? fetty.colorSensor.blue() : fetty.colorSensor.red();
            }

        } else if (not_color1 > color1) {
            fetty.encoderMove(4,-.5);
            sleep(100);
            fetty.punch(1);

            int color2 = alliance == alliance.BLUE ? fetty.colorSensor.blue() : fetty.colorSensor.red();

            while (color1 >= color2 && PUNCHIES < MAX_PUNCHIES) {
                fetty.punch(1);
                PUNCHIES = PUNCHIES + 1;
                color2 = alliance == alliance.BLUE ? fetty.colorSensor.blue() : fetty.colorSensor.red();
            }
        }

        sleep(1000);
        fetty.button(0);
        sleep(500);
        fetty.allClearL(fetty.CLEAR_IN);
        fetty.allClearR(fetty.CLEAR_IN);
        fetty.encoderMove(lastdist, lastdir);
        fetty.servoController.pwmDisable();
        fetty.move(0,0);
    }
}