package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import com.lasarobotics.library.util.RollingAverage;
import com.qualcomm.robotcore.util.Range;

import com.lasarobotics.library.controller.ButtonState;
import com.lasarobotics.library.controller.Controller;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public class LinearTele extends LinearOpMode {

    Controller one = new Controller();
    Controller two = new Controller();
    RollingAverage avg = new RollingAverage(20);

    @Override
    public void runOpMode() throws InterruptedException {
        RobotSetup  fetty   = new RobotSetup(hardwareMap, telemetry, this);
        waitForStart();
        fetty.initializeServos();



        while (opModeIsActive()) {


            //gamepads MUST be updated every loop with FTCLib.
            one.update(gamepad1);
            two.update(gamepad2);


//--------------------------------GAMEPAD 1
            if (one.left_bumper  == ButtonState.RELEASED) fetty.trigL = !fetty.trigL;
            if (one.right_bumper == ButtonState.RELEASED) fetty.trigR = !fetty.trigR;
            //if (one.y == ButtonState.HELD) fetty.dumpArm(fetty.DUMP_UP); else fetty.dumpArm(fetty.DUMP_DOWN);

            if (fetty.trigL) {  fetty.climberL(fetty.CLIMBER_OUT);}
            else                fetty.climberL(fetty.CLIMBER_IN);
            if (fetty.trigR) {  fetty.climberR(fetty.CLIMBER_OUT);}
            else                fetty.climberR(fetty.CLIMBER_IN);

            //DIRECTION
            //This reverses our robot, so what was once our back is now our front.
            //call reverse() to switch directions, and check reverseVal for current state.
            if (one.x == ButtonState.RELEASED) fetty.reverse();
            fetty.redLED(fetty.isreversed());

            if (fetty.isreversed()) {fetty.move(-one.right_stick_y, -one.left_stick_y);
            } else {fetty.move (one.left_stick_y, one.right_stick_y);}

            if (gamepad1.dpad_up) {
                fetty.move(-0.5,-0.5);
            }

//--------------------------------GAMEPAD 2
            fetty.moveTape(gamepad2.left_stick_y * gamepad2.left_stick_y * gamepad2.left_stick_y);
            if      (two.left_trigger == 1) fetty.moveWinch(1);
            else if (two.left_bumper == ButtonState.HELD) fetty.moveWinch(-1);
            else    fetty.moveWinch(0);


            if (two.right_trigger == 1) fetty.sweeper.setPower(1);
            else if (two.right_bumper == ButtonState.HELD) fetty.sweeper.setPower(-1);
            else fetty.sweeper.setPower(0);



            if (two.x  == ButtonState.HELD) {
                fetty.doorL(fetty.DOOR_OUT);
                fetty.doorR(fetty.DOOR_IN);
                fetty.conveyor(1);
            }
            else if (two.b == ButtonState.HELD) {
                fetty.doorR(fetty.DOOR_OUT);
                fetty.doorL(fetty.DOOR_IN);
                fetty.conveyor(0);
            }
            else {
                fetty.doorL(fetty.DOOR_IN);
                fetty.doorR(fetty.DOOR_IN);
            }
            if (two.a == ButtonState.RELEASED) fetty.conveyor(0.5);


            if (fetty.T.isPressed()){
                fetty.arm(Math.min(two.right_stick_y,0));
            }
            else {
                fetty.arm(two.right_stick_y);
            }

            /*if (fetty.tilt() > 30) {
                fetty.arm(two.right_stick_y);
            }*/

            avg.addValue(fetty.tiltY());
            telemetry.addData("Touch", fetty.T);
            telemetry.addData("avg", avg.getAverage());
            telemetry.addData("Y", fetty.tiltY());




        }

    }
}
