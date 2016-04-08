package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import com.qualcomm.robotcore.util.Range;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearTele extends LinearOpMode {

    Controller one = new Controller();
    Controller two = new Controller();
    double beaconPos = 0;

    @Override
    public void runOpMode() throws InterruptedException {
        RobotSetup  fetty   = new RobotSetup(hardwareMap, telemetry, this);
        waitForStart();
        fetty.initializeServos();



        while (opModeIsActive()) {


            //gamepads MUST be updated every loop with FTCLib.
            one.update(gamepad1);
            two.update(gamepad2);


            if      (two.right_trigger == 1) fetty.moveTape(-0.2);
            else if (two.right_bumper  == ButtonState.HELD) fetty.moveTape(0.2);
            else    fetty.moveTape(-gamepad2.left_stick_y * gamepad2.left_stick_y * gamepad2.left_stick_y);//TODO slowed this down need to test

            if      (two.left_trigger == 1) fetty.moveWinch(1);
            else if (two.left_bumper == ButtonState.HELD) fetty.moveWinch(-1);
            else    fetty.moveWinch(0);


            if (one.left_bumper  == ButtonState.RELEASED) fetty.trigL = !fetty.trigL;
            if (one.right_bumper == ButtonState.RELEASED) fetty.trigR = !fetty.trigR;

            if (fetty.trigL) {  fetty.climberL(fetty.CLIMBER_OUT);}
            else                fetty.climberL(fetty.CLIMBER_IN);

            if (fetty.trigR) {  fetty.climberR(fetty.CLIMBER_OUT);}
            else                fetty.climberR(fetty.CLIMBER_IN);


            if (two.dpad_down    == ButtonState.RELEASED) fetty.sweeper.setPower(-1);
            if (two.dpad_up      == ButtonState.RELEASED) fetty.sweeper.setPower(1);
            if (two.dpad_left    == ButtonState.RELEASED) fetty.sweeper.setPower(0);


            if (one.y == ButtonState.HELD) fetty.dumpArm(fetty.DUMP_UP); else fetty.dumpArm(fetty.DUMP_DOWN);


            fetty.allclear(two.right_stick_y/-2);


            //--------------------------------DIRECTION
            //This reverses our robot, so what was once our back is now our front.
            //call reverse() to switch directions, and check reverseVal for current state.
            if (one.x == ButtonState.RELEASED) fetty.reverse();
            fetty.redLED(fetty.isreversed());

            if (fetty.isreversed()) {fetty.move(-one.right_stick_y, -one.left_stick_y);
            } else {fetty.move (one.left_stick_y, one.right_stick_y);}

            if (gamepad1.dpad_up) {
                fetty.frontLeft.setPower(-0.5);
                fetty.frontRight.setPower(-0.5);
                fetty.backLeft.setPower(-0.5);
                fetty.backRight.setPower(-0.5);
            }



        }

    }
}
