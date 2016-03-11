package com.qualcomm.ftcrobotcontroller.opmodes.Torch.newAuton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by taand on 3/10/2016.
 */
public abstract class BaseAuton extends LinearOpMode {

    //static means these can't be modified by the program.
    static int BLUE_TURN_ANGLE  = 130;
    static int RED_TURN_ANGLE   = 40;
    static int BLUE_MOVE_DIST   = 124;
    static int RED_MOVE_DIST    = 73;
    static int LIGHT_THRESHOLD  = 15;
    static int BLUE_LAST_DIST   = 13;
    static int RED_LAST_DIST    = 13;
    static int WAIT_TIME        = 10 * 1000; //ten seconds

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
        fetty.initializeServos();

        if (wait) {sleep(WAIT_TIME);}

        //if alliance.BLUE == true, then dist = 24, else dist = 73
        int firstdist = alliance == alliance.BLUE ? BLUE_MOVE_DIST : RED_MOVE_DIST;
        int turn      = alliance == alliance.BLUE ? BLUE_TURN_ANGLE: RED_TURN_ANGLE;
        int lastdist  = alliance == alliance.BLUE ? BLUE_LAST_DIST : RED_LAST_DIST;
        double lastdir= alliance == alliance.BLUE ? 0.5 : -0.5;


        //start off slow, then speed up
        fetty.encoderMove(24, 0.3);
        fetty.encoderMove(firstdist, 0.7);

        fetty.gTurn(turn,0.3);

        fetty.allClearL(0.5);
        
        while (fetty.floorIR.getLightDetectedRaw() < LIGHT_THRESHOLD && opModeIsActive()) {
            waitOneFullHardwareCycle();
            fetty.move(0.2, 0.2);
        }

        fetty.move(0,0);
        fetty.dumpArm(1);
        fetty.button(0.45);
        sleep(1000);
        fetty.dumpArm(0.6);
        fetty.button(0);
        sleep(500);
        fetty.encoderMove(lastdist, lastdir);
    }
}
