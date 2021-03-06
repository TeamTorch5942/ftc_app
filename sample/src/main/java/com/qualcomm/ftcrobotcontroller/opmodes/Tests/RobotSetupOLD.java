package com.qualcomm.ftcrobotcontroller.opmodes.Tests;

import com.lasarobotics.library.drive.Tank;
import com.lasarobotics.library.options.OptionMenu;
import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.robocol.Telemetry;

public class RobotSetupOLD {

    //We want these to be private because we never directly tell them what to do.
    //for control, we use move(), moveWinch(), etc. and not (arm1 = 1.0)
    //Gyro is public because sometimes I call bot.G.calibrate().
    //Could be made private once we have functions for every gyro use.
    //similarly, menu is public because we call bot.menu.show()
    private DcMotor frontLeft, frontRight, backLeft, backRight, midLeft, midRight, arm1, arm2;
    private Servo Servo2, Servo1, Servo3, Servo4, Servo5, Servo6;
    private DeviceInterfaceModule cdim;
    public  ModernRoboticsI2cGyro G;
    public  OptionMenu allianceMenu;
    public  ColorSensor colorSensor;
    public  OpticalDistanceSensor IRsensor;
    public  OpticalDistanceSensor floorIR;
    public  DigitalChannel echo;
    public  DigitalChannel trig;

    //declare Reverse Variable
    boolean reverseVal = false;

    private double bumperPos = 1;

    //declare motor control variables
    private int leftEncoderDistance;
    private int rightEncoderDistance;
    private int gyroDistance;
    private Telemetry telemetry;
    private LinearOpMode opControl;

    RobotSetupOLD(HardwareMap hardwareMap, Telemetry _telemetry, LinearOpMode op) throws
            InterruptedException {
        opControl = op;
        telemetry   = _telemetry; //No idea what this does, ask suitbots?

        //sensors
        G = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        cdim        = hardwareMap.deviceInterfaceModule.get("dim");
        IRsensor    = hardwareMap.opticalDistanceSensor.get("ir");
        colorSensor = hardwareMap.colorSensor.get("color");
        floorIR     = hardwareMap.opticalDistanceSensor.get("ir2");
        echo        = hardwareMap.digitalChannel.get("echo");
        trig        = hardwareMap.digitalChannel.get("trig");
        echo.setMode(DigitalChannelController.Mode.INPUT);


        //Motors
        frontLeft   = hardwareMap.dcMotor.get("1");
        frontRight  = hardwareMap.dcMotor.get("2");

        midRight    = hardwareMap.dcMotor.get("5");
        midLeft     = hardwareMap.dcMotor.get("6");

        backRight   = hardwareMap.dcMotor.get("7");
        backLeft    = hardwareMap.dcMotor.get("8");

        arm1        = hardwareMap.dcMotor.get("3");
        arm2        = hardwareMap.dcMotor.get("4");

        //Servo Controller
        Servo1      = hardwareMap.servo.get("s1");
        Servo2      = hardwareMap.servo.get("s2");
        Servo3      = hardwareMap.servo.get("s3");
        Servo4      = hardwareMap.servo.get("s4");
        Servo5      = hardwareMap.servo.get("s5");
        Servo6      = hardwareMap.servo.get("s6");

        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
        midLeft.setDirection(DcMotor.Direction.REVERSE);
        backLeft.setDirection(DcMotor.Direction.REVERSE);


        /*Our Alliance Selection Menu Setup
        OptionMenu.Builder builder    = new OptionMenu.Builder(hardwareMap.appContext);
        SingleSelectCategory alliance = new SingleSelectCategory("alliance");

        alliance.addOption("Red");
        alliance.addOption("Blue");

        builder.addCategory(alliance);
        allianceMenu = builder.create();
        */
    }




    //----------------------------------------------------------------MOVEMENT FUNCTIONS
    //TODO only moved 4 motors sometimes. need to figure out why.
    public void move(double l, double r) throws InterruptedException {
        opControl.waitOneFullHardwareCycle();
        Tank.motor6(frontLeft,
                        frontRight,
                        midLeft,
                        midRight,
                        backLeft,
                        backRight,
                        l, r);}

    public void     reverse()   {reverseVal = !reverseVal;}
    public boolean  isreversed(){return reverseVal;}


    public int lDistance() {return midLeft.getCurrentPosition() - leftEncoderDistance;}
    public int rDistance() {return midRight.getCurrentPosition() - rightEncoderDistance;}
    public int avgDistance(){return (lDistance()+rDistance())/2;}

    public void resetLEncoder(){leftEncoderDistance  = midLeft.getCurrentPosition();}
    public void resetREncoder(){rightEncoderDistance = midRight.getCurrentPosition();}
    public void resetEncoders(){
        resetLEncoder();
        resetREncoder();
    }
    public void encoderMove(double inches, double power) throws InterruptedException {
        while (Math.abs(avgDistance() )<= Math.abs(inches*1600/12)) {
            opControl.waitOneFullHardwareCycle();
            move(power,power);
            telemetry.addData("distance",avgDistance()*12/1600);
        }
        move(0, 0);
    }


    public void moveWinch   (double power)    {arm1.setPower(power);}
    public void moveTape    (double power)    {arm2.setPower(power);}


    public void climberR    (double position) {Servo1.setPosition(1-position);}
    public void climberL    (double position) {Servo2.setPosition(position);}
    public void dumpArm     (double position) {Servo3.setPosition(position);}
    public void allClearL   (double position) {Servo4.setPosition(1-position);}
    public void allClearR   (double position) {Servo5.setPosition(position);}
    public void bumperToggle()                {bumperPos = 1-bumperPos; Servo6.setPosition(bumperPos);}

    //TODO make all initalize at 0
    public void initializeServos () {
        climberL(0);
        climberR(0);
        dumpArm(0.6);
        allClearL(0);
        allClearR(0);
        bumperToggle();

    }




    //----------------------------------------------------------------LIGHT FUNCTIONS
    public void blueLED (boolean state){cdim.setLED(0, state);} 
    public void redLED  (boolean state){cdim.setLED(1, state);}
    public void enableTrigger  (boolean state){cdim.setDigitalChannelState(1, state);}





    //----------------------------------------------------------------SENSOR FUNCTIONS

    //TODO new Gyro Reset functions
    public int gyroDelta() {return gyroDistance - G.getIntegratedZValue();}
    public void resetDelta(){gyroDistance  = G.getIntegratedZValue();}

    public void gTurn(int degrees, double power) throws InterruptedException {
        resetDelta();               //Reset Gyro
        float direction = Math.signum(degrees); //get +/- sign of target
        opControl.waitOneFullHardwareCycle();
        move(-direction * power, direction * power);
        //move in the right direction
        //we start moving BEFORE the while loop.
        while ( Math.abs(gyroDelta()) < Math.abs(degrees)){
            opControl.waitOneFullHardwareCycle();
        }
        move(0, 0);
        resetEncoders();
    }

    public void gTurnAbsolute(int degrees, double power) throws InterruptedException {
        float direction = Math.signum(degrees); //get +/- sign of target
        opControl.waitOneFullHardwareCycle();
        move(-direction * power, direction * power);
        //move in the right direction
        //we start moving BEFORE the while loop.
        while ( Math.abs(G.getIntegratedZValue()) < Math.abs(degrees)){
            opControl.waitOneFullHardwareCycle();
        }
        move(0, 0);
        resetEncoders();
    }

    public void colorTelemetry(){
        telemetry.addData("Clear", colorSensor.alpha());
        telemetry.addData("Red  ", colorSensor.red());
        telemetry.addData("Green", colorSensor.green());
        telemetry.addData("Blue ", colorSensor.blue());
    }

    //----------------------------------------------------------------TELEMETRY FUNCTIONS
    public void defaultTelemetry(){
        telemetry.addData("Gyro", gyroDelta());
        telemetry.addData("reversed", isreversed());
        telemetry.addData("Encoder L", lDistance());
        telemetry.addData("Encoder R", rDistance());
    }





    //--------------------------------MISC FUNCTIONS
    public String getAlliance(){
        //catch{} prevents app from crashing when alliance is unselected.
        try {
            allianceMenu.selectedOption("alliance");
        } catch (IllegalArgumentException e){
            return "None";
        }
        return allianceMenu.selectedOption("alliance");
    }
    public boolean isRed(){
        return (getAlliance().equals("Red"));
    }
    public boolean isBlue(){
        return (getAlliance().equals("Blue"));
    }


    public void startRobot() throws InterruptedException {
        blueLED(false);
        redLED(false);
        move(0,0);
        moveTape(0);
        moveWinch(0);
        resetEncoders();
        reverseVal = false;
    }
}