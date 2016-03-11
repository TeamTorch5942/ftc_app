package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Travis on 2/12/16.
 */
public class AutonBlueWait extends LinearOpMode {
    public void runOpMode()throws InterruptedException {
        RobotSetup fetty = new RobotSetup(hardwareMap,telemetry,this);
        fetty.startRobot();
        fetty.initializeServos();
        waitForStart();
        while (fetty.G.isCalibrating() && opModeIsActive()){
            fetty.blueLED(true);
            waitOneFullHardwareCycle();
        }
        sleep(10000);
        fetty.blueLED(false);
        fetty.encoderMove(24, 0.3);
        fetty.encoderMove(124, 0.7);
        fetty.gTurn(130, 0.2);
        fetty.allClearL(0.5);
        //sleep(500);
        while (fetty.floorIR.getLightDetectedRaw() < 20 && opModeIsActive()) {
            waitOneFullHardwareCycle();
            telemetry.addData("line", fetty.floorIR.getLightDetectedRaw());
            fetty.move(0.2, 0.2);
        }
        waitOneFullHardwareCycle();
        fetty.move(0, 0);
        telemetry.addData("line", "SPOTTED");
        fetty.resetLEncoder();
        fetty.resetEncoders();
        //sleep(1000);
        telemetry.addData("encoders", fetty.avgDistance());
        telemetry.addData("bot", "Moving");
        fetty.encoderMove(5, 0.2);
        telemetry.addData("bot", "stopped");
        //sleep(100);
        telemetry.addData("bot", "dumpling");
        fetty.dumpArm(1);
        fetty.move(0, 0);
        sleep(1000);
        fetty.dumpArm(.6);
        sleep(500);
        fetty.allClearL(0);
        //sleep(1000);
        fetty.encoderMove(26, 0.5);
        waitOneFullHardwareCycle();
        fetty.move(0,0);


    }
}
