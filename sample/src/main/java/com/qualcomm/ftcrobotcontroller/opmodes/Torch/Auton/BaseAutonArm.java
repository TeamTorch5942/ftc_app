package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by taand on 3/10/2016.
 */
public abstract class BaseAutonArm extends LinearOpMode {

    //static means these can't be modified by the program.
    static int BLUE_TURN_ANGLE  = 127;
    static int RED_TURN_ANGLE   = 40;
    static int BLUE_MOVE_DIST   = 124;
    static int RED_MOVE_DIST    = 77;
    static int LIGHT_THRESHOLD  = 100;
    static int BLUE_LAST_DIST   = 30;
    static int RED_LAST_DIST    = 28;
    static int AVOID_RED        = 4;
    static int WAIT_TIME        = 10 * 1000; //ten seconds
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
        //fetty.dumpArm(fetty.DUMP_DOWN);


        if (wait) {sleep(WAIT_TIME);}

        //if alliance.BLUE == true, then dist = 24, else dist = 73
        int firstdist = alliance == alliance.BLUE ? BLUE_MOVE_DIST : RED_MOVE_DIST;
        int turn      = alliance == alliance.BLUE ? BLUE_TURN_ANGLE: RED_TURN_ANGLE;
        int lastdist  = alliance == alliance.BLUE ? BLUE_LAST_DIST : RED_LAST_DIST;
        int red_move  = alliance == alliance.BLUE ? 0 : AVOID_RED;
        double lastdir= alliance == alliance.BLUE ? -0.9 : 0.5;


        fetty.initializeServos();
        fetty.encoderMove(firstdist, 0.7);

        fetty.gTurn(turn, 0.45);

        fetty.encoderMove(red_move, .5);
        
        while (fetty.floorIR.getLightDetectedRaw() < LIGHT_THRESHOLD && opModeIsActive()) {
            waitOneFullHardwareCycle();
            fetty.move(0.2, 0.2);
        }

        fetty.gTurn(90, 0.5);
        fetty.encoderMove(2,0.5);

        fetty.servoController.pwmDisable();
        fetty.move(0, 0);
    }
}