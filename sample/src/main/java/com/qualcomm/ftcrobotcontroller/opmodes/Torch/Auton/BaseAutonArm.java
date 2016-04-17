package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by taand on 3/10/2016.
 */
public abstract class BaseAutonArm extends LinearOpMode {

    //static means these can't be modified by the program.
    static int FAR_TURN_ANGLE   = 127;
    static int CLOSE_TURN_ANGLE = 40;
    static int FAR_MOVE_DIST    = 124;
    static int CLOSE_MOVE_DIST  = 75;
    static int LIGHT_THRESHOLD  = 100;
    static int WAIT_TIME        = 10 * 1000; //ten seconds
    static int turnDir          = 1;

    enum Wait {
        YES,NO
    }
    enum Alliance {
        RED,BLUE
    }
    enum Placement {
        CLOSE,FAR
    }
    abstract Wait     getWait();
    abstract Alliance getAlliance();
    abstract Placement getPlacement();

    boolean wait = getWait() == Wait.YES;

    @Override
    public void runOpMode() throws InterruptedException {
        Alliance alliance = getAlliance();
        Placement placement = getPlacement();
        RobotSetup fetty = new RobotSetup(hardwareMap,telemetry,this);


        waitForStart();
        //fetty.dumpArm(fetty.DUMP_DOWN);


        if (wait) {sleep(WAIT_TIME);}

        //if alliance.BLUE == true, then dist = 24, else dist = 73
        int firstdist = placement == placement.FAR ? FAR_MOVE_DIST : CLOSE_MOVE_DIST;
        int turn      = placement == placement.FAR ? FAR_TURN_ANGLE: CLOSE_TURN_ANGLE;

        if (alliance == alliance.RED && placement == placement.FAR) turnDir = -1;
        if (alliance == alliance.BLUE && placement == placement.CLOSE) turnDir = -1;


        fetty.initializeServos();
        fetty.encoderMove(firstdist, 0.7);

        fetty.gTurn(turn * turnDir, 0.7);
        while (fetty.floorIR.getLightDetectedRaw() < LIGHT_THRESHOLD && opModeIsActive()) {
            waitOneFullHardwareCycle();
            fetty.move(0.5, 0.5);
        }
        fetty.encoderMove(-3, -0.5);
        fetty.gTurn(80 * turnDir, 0.7);
        fetty.encoderMove(-3, -0.5);
        fetty.encoderMove(1, 0.5);

        fetty.resetArm();
        while (fetty.armPos() > -3500) {
            fetty.allclear.setPower(-0.7);
            telemetry.addData("ARM",fetty.allclear.getCurrentPosition());
            waitOneFullHardwareCycle();
        }
        fetty.allclear.setPower(0);
        sleep(100);
        while (fetty.armPos() < -20) {
            fetty.allclear.setPower(0.7);
            telemetry.addData("ARM", fetty.allclear.getCurrentPosition());
            waitOneFullHardwareCycle();
        }
        fetty.allclear.setPower(0);

        fetty.servoController.pwmDisable();
        fetty.move(0, 0);
    }
}