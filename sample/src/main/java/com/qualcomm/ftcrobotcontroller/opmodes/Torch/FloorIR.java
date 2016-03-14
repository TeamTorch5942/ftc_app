package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import android.graphics.Color;

import com.lasarobotics.library.util.Timers;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class FloorIR extends LinearOpMode{
    @Override
    public void runOpMode() throws InterruptedException {
        RobotSetup fetty  = new RobotSetup(hardwareMap,telemetry,this);

        fetty.initializeServos();
        fetty.allClearR(0.5);
        fetty.allClearL(0.5);
        waitForStart();
        while(opModeIsActive()){
            fetty.colorTelemetry();
            fetty.floorTelemetry();
            fetty.dumpArm((gamepad1.left_stick_y+1)/2);
        }


        //"each their own to"
        //         --Travis Day 2k16

    }

}

