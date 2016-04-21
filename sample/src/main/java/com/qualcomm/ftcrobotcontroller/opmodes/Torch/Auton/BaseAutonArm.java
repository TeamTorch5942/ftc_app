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
    static int MOVE_DEFENSE     = 20;
    static int TURN_DEFENSE     = 45;


    enum Wait {
        YES,NO
    }
    enum Alliance {
        RED,BLUE
    }
    enum Placement {
        CLOSE,FAR

    }
    enum Defense {
        YES, NO
    }

    abstract Wait      getWait();
    abstract Alliance  getAlliance();
    abstract Placement getPlacement();
    abstract Defense   getDefense();

    boolean wait = getWait() == Wait.YES;

    @Override
    public void runOpMode() throws InterruptedException {
        Alliance alliance = getAlliance();
        Placement placement = getPlacement();
        Defense defense = getDefense();
        RobotSetup fetty = new RobotSetup(hardwareMap,telemetry,this);


        waitForStart();
        //fetty.dumpArm(fetty.DUMP_DOWN);


        if (wait) {sleep(WAIT_TIME);}

        //if alliance.BLUE == true, then dist = 24, else dist = 73
        int firstdist = placement == placement.FAR ? FAR_MOVE_DIST : CLOSE_MOVE_DIST;
        int turnAngle      = placement == placement.FAR ? FAR_TURN_ANGLE: CLOSE_TURN_ANGLE;
        int Dturn   = defense == defense.YES ? TURN_DEFENSE : 0;
        int Dmove   = defense == defense.YES ? MOVE_DEFENSE : 0;

        if (alliance == alliance.RED  && placement == placement.FAR  ) turnDir = -1;
        if (alliance == alliance.RED  && placement == placement.CLOSE) turnDir =  1;
        if (alliance == alliance.BLUE && placement == placement.CLOSE) turnDir = -1;


        telemetry.addData("Alliance",alliance);
        telemetry.addData("Placement",placement);
        telemetry.addData("Turn Angle",turnDir);



        fetty.initializeServos();
        fetty.encoderMove(firstdist, 0.7);

        fetty.gTurn(turnAngle * turnDir, 0.7);
        while (fetty.floorIR.getLightDetectedRaw() < LIGHT_THRESHOLD && opModeIsActive()) {
            waitOneFullHardwareCycle();
            fetty.move(0.5, 0.5);
        }
        fetty.encoderMove(-3, -0.5);
        fetty.gTurn(75 * turnDir, 1);
        fetty.encoderMove(-3, -0.5);

        fetty.resetArm();
        while (fetty.armPos() > -3500) {
            if (fetty.isTouched()) fetty.resetArm();
            fetty.arm(-0.7);
            telemetry.addData("ARM",fetty.armPos());
            waitOneFullHardwareCycle();
        }
        fetty.arm(0);
        fetty.encoderMove(0.5, 0.5);
        sleep(100);
        while (fetty.armPos() < -20) {
            fetty.arm(0.7);
            telemetry.addData("ARM", fetty.armPos());
            waitOneFullHardwareCycle();
        }
        fetty.arm(0);

        fetty.gTurn(Dturn,.5);
        fetty.move(Dmove, 1);

        fetty.servoController.pwmDisable();
        fetty.move(0, 0);
    }
}