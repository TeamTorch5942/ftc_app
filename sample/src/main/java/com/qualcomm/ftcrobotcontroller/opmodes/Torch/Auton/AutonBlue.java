package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;


/**
 * Created by Travis on 2/12/16.
 */
public class AutonBlue extends LinearOpMode {
    public void runOpMode()throws InterruptedException {
        RobotSetup fetty = new RobotSetup(hardwareMap,telemetry,this);
        waitForStart();
        fetty.initializeServos();
        fetty.resetLEncoder();
        fetty.resetREncoder();
        fetty.blueLED(false);
        fetty.encoderMove(24, 0.3);
        fetty.encoderMove(124, 0.7);
        fetty.gTurn(130, 0.3);
        fetty.allClearL(0.5);
        //sleep(500);
        while (fetty.floorIR.getLightDetectedRaw() < 20 && opModeIsActive()) {
            waitOneFullHardwareCycle();
            telemetry.addData("line", fetty.floorIR.getLightDetectedRaw());
            fetty.move(0.2, 0.2);
        }

        fetty.dumpArm(1);
        fetty.move(0, 0);
        sleep(1000);
        fetty.dumpArm(.6);
        sleep(500);
        fetty.allClearL(0);
        //sleep(1000);
        fetty.encoderMove(35, 0.5);
        waitOneFullHardwareCycle();
        fetty.move(0,0);


    }
}
