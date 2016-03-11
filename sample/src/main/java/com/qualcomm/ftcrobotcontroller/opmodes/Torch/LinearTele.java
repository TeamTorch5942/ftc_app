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
            else    fetty.moveTape(-gamepad2.left_stick_y*gamepad2.left_stick_y*gamepad2.left_stick_y);//TODO slowed this down need to test

            if      (two.left_trigger == 1) fetty.moveWinch(1);
            else if (two.left_bumper == ButtonState.HELD) fetty.moveWinch(-1);
            else    fetty.moveWinch(0);

            //left  down = 1.
            //right down = 0.
            if (one.left_bumper  == ButtonState.HELD) fetty.climberL(1);   else fetty.climberL(0);
            if (one.right_bumper == ButtonState.HELD) fetty.climberR(1);   else fetty.climberR(0);
            if (one.y            == ButtonState.HELD) fetty.dumpArm(1);    else fetty.dumpArm(0.6);
            if (one.right_trigger== 1 || one.left_trigger == 1) {
                fetty.allClearR(1);
                fetty.allClearL(1);
            }
            else {
                fetty.allClearR(0);
                fetty.allClearL(0);
            }
            if (one.dpad_right == ButtonState.HELD && beaconPos <= 1) {
                beaconPos += 0.05;
            }
            else if (one.dpad_left ==ButtonState.HELD && beaconPos >= 0) {
                beaconPos -= 0.05;
            }
            beaconPos = Range.clip(beaconPos, 0, 1);
            fetty.button(beaconPos);
            telemetry.addData("beaconPos",beaconPos);

            //--------------------------------DIRECTION
            //This reverses our robot, so what was once our back is now our front.
            //call reverse() to switch directions, and check reverseVal for current state.
            if (one.x == ButtonState.RELEASED) fetty.reverse();
            fetty.redLED(fetty.isreversed());

            if (fetty.isreversed()) {fetty.move(-one.right_stick_y, -one.left_stick_y);}
            else                    {fetty.move (one.left_stick_y, one.right_stick_y);}

            if (gamepad1.dpad_up) {
                fetty.frontRight.setPower(-0.5);
                fetty.backRight.setPower(-0.5);
                fetty.frontLeft.setPower(-0.5);
                fetty.backLeft.setPower(-0.5);
            }



        }

    }
}
