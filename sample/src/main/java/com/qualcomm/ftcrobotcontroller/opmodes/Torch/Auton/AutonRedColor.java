package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.RobotSetup;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

/**
 * Created by Travis on 2/12/16.
 */
public class AutonRedColor extends LinearOpMode {
    public void runOpMode() throws InterruptedException {
        RobotSetup fetty = new RobotSetup(hardwareMap, telemetry, this);
        waitForStart();
        fetty.initializeServos();
        fetty.resetLEncoder();
        fetty.resetREncoder();

        fetty.blueLED(false);
        fetty.encoderMove(24, 0.3);
        fetty.encoderMove(73, 0.7);
        fetty.gTurn(45, 0.3);
        fetty.encoderMove(4, 0.3);
        fetty.allClearL(0.5);
        while (fetty.floorIR.getLightDetectedRaw() < 15 && opModeIsActive()) {
            waitOneFullHardwareCycle();
            telemetry.addData("line", fetty.floorIR.getLightDetectedRaw());
            fetty.move(0.2, 0.2);
        }
        fetty.dumpArm(1);
        fetty.move(0, 0);
        fetty.button(0.45);
        sleep(500);
        fetty.dumpArm(.6);
        sleep(500);
        fetty.allClearL(0);
        fetty.button(0);
        /*if (fetty.colorSensor.red() < fetty.colorSensor.blue()) {
            telemetry.addData("Blue ", fetty.colorSensor.blue());
        } else if (fetty.colorSensor.blue() < fetty.colorSensor.red()) {
            telemetry.addData("Red ", fetty.colorSensor.red());
        }*/
        sleep(1000);
        fetty.encoderMove(13, -0.5);
        waitOneFullHardwareCycle();
        fetty.move(0, 0);


    }
}
