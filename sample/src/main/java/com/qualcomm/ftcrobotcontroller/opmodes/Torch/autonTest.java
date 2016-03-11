package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import android.graphics.Color;

import com.lasarobotics.library.util.Timers;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class autonTest extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        RobotSetup fetty  = new RobotSetup(hardwareMap,telemetry,this);

        fetty.startRobot();           //run our initialization function
        fetty.initializeServos();
        fetty.G.calibrate();          //calibrate our gyro

        //--------------------------------OPMODE START
        waitForStart();
        sleep(50);
        while (fetty.G.isCalibrating()) {
            fetty.blueLED(true);
            waitOneFullHardwareCycle();
        }
        fetty.redLED(true);
        fetty.blueLED(false);

        while (fetty.floorIR.getLightDetectedRaw() < 20 && opModeIsActive()) {
            telemetry.addData("Floor thangy", fetty.floorIR.getLightDetectedRaw());
            fetty.move(0.2, 0.2);
        }
        fetty.gTurn(38, 0.3);
        fetty.encoderMove(6, .3);
        sleep(500);
        fetty.gTurnAbsolute(125, 0.3);
        sleep(500);
        fetty.move(-0.2, -0.2);
        sleep(100);
        fetty.move(0, 0);
        fetty.allClearR(.5);
        fetty.allClearL(.5);
        sleep(100);
        fetty.dumpArm(1);
        sleep(1000);
        fetty.dumpArm(0);
        sleep(100);
        fetty.allClearR(0);
        fetty.allClearL(0);





        //"each their own to"
        //         --Travis Day 2k16

    }

}
