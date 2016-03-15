package com.qualcomm.ftcrobotcontroller.opmodes.Torch;

import android.graphics.Color;

import com.qualcomm.hardware.modernrobotics.ModernRoboticsI2cGyro;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DigitalChannelController;
import com.qualcomm.robotcore.hardware.LED;
import com.qualcomm.robotcore.hardware.OpticalDistanceSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.lasarobotics.library.options.OptionMenu;
import com.qualcomm.robotcore.hardware.ServoController;
import com.qualcomm.robotcore.hardware.VoltageSensor;
import com.qualcomm.robotcore.robocol.Telemetry;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.lasarobotics.library.drive.Tank;

public class RobotSetup {

    //We want these to be private because we never directly tell them what to do.
    //That is, until travis makes them public because he hates nice things.

    //for control, we use move(), moveWinch(), etc. and not (arm1 = 1.0)

    public   DcMotor frontLeft, frontRight, backLeft, backRight, arm1, arm2;
    private  Servo Servo1, Servo2, Servo3, Servo4, Servo5, Servo6;
    private  DeviceInterfaceModule cdim;
    private  ModernRoboticsI2cGyro G;
    public   OptionMenu allianceMenu;
    public   ColorSensor colorSensor;
    public   OpticalDistanceSensor IRsensor;
    public   OpticalDistanceSensor floorIR;
    public   ServoController servoController;
    private  VoltageSensor voltage;

    //declare Reverse Variable
    int reverseVal = 0;


    //declare motor control variables
    private int leftEncoderDistance;
    private int rightEncoderDistance;
    private int gyroDistance;
    private Telemetry telemetry;
    private LinearOpMode opControl;

    public RobotSetup(HardwareMap hardwareMap, Telemetry _telemetry, LinearOpMode op) throws
            InterruptedException {
        opControl = op;
        telemetry   = _telemetry; //No idea what this does, ask suitbots?

        //sensors
        G = (ModernRoboticsI2cGyro) hardwareMap.gyroSensor.get("gyro");
        cdim        = hardwareMap.deviceInterfaceModule.get("dim");
        floorIR     = hardwareMap.opticalDistanceSensor.get("ir");
        colorSensor = hardwareMap.colorSensor.get("color");


        //Motors
        frontLeft   = hardwareMap.dcMotor.get("1");
        frontRight  = hardwareMap.dcMotor.get("2");

        backLeft    = hardwareMap.dcMotor.get("3");
        backRight   = hardwareMap.dcMotor.get("4");

        arm1        = hardwareMap.dcMotor.get("5");
        arm2        = hardwareMap.dcMotor.get("6");

        //Servo Controller
        servoController  = hardwareMap.servoController.get("servo");
        Servo1          = hardwareMap.servo.get("s1");
        Servo2         = hardwareMap.servo.get("s2");
        Servo3        = hardwareMap.servo.get("s3");
        Servo4       = hardwareMap.servo.get("s4");
        Servo5      = hardwareMap.servo.get("s5");
        Servo6     = hardwareMap.servo.get("s6");
        
        frontLeft.setDirection(DcMotor.Direction.REVERSE);
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
        Tank.motor4(    frontLeft,
                        frontRight,
                        backLeft,
                        backRight,
                        l, r);}

    public void     reverse()   {reverseVal = 1-reverseVal;}
    public boolean  isreversed(){return reverseVal == 1;}


    public int lDistance() {return frontLeft.getCurrentPosition() - leftEncoderDistance;}
    public int rDistance() {return frontRight.getCurrentPosition() - rightEncoderDistance;}
    public int avgDistance(){return (lDistance()+rDistance())/2;}

    public void resetLEncoder(){leftEncoderDistance  = frontLeft.getCurrentPosition();}
    public void resetREncoder(){rightEncoderDistance = frontRight.getCurrentPosition();}
    public void resetEncoders(){
        resetLEncoder();
        resetREncoder();
    }
    public void encoderMove(double inches, double power) throws InterruptedException {
        resetLEncoder();
        resetREncoder();
        while (Math.abs(avgDistance() )<= Math.abs(inches*1600/12)) {
            opControl.waitOneFullHardwareCycle();
            move(power,power);
            telemetry.addData("distance",avgDistance()*12 / 1600);
        }
        move(0, 0);
    }


    public void moveWinch   (double power)    {arm1.setPower(power);}
    public void moveTape    (double power)    {arm2.setPower(power);}


    public void climberR    (double position) {Servo1.setPosition(1 - position);}
    public void climberL    (double position) {Servo2.setPosition(position);}
    public void dumpArm     (double position) {Servo3.setPosition(position);}
    public void allClearL   (double position) {Servo4.setPosition(1-position);}
    public void allClearR   (double position) {Servo5.setPosition(position);}
    public void button      (double position) {Servo6.setPosition(position);}

    public void punch       (double count) throws InterruptedException {

        for (int i = 0; i < count; i++) {
            button(BUTTON_OUT);
            opControl.sleep(100);
            button(BUTTON_SEARCH);
            opControl.sleep(100);
        }
    }

    //Servo positions, change these and they will be changed everywhere
    public static double DUMP_DOWN      = 0.4;
    public static double DUMP_UP        = 1;
    public static double CLIMBER_IN     = 0;
    public static double CLIMBER_OUT    = 1;
    public static double CLEAR_IN       = 0;
    public static double CLEAR_OUT      = 1;
    public static double CLEAR_WAY      = 0.5;
    public static double BUTTON_IN      = 0;
    public static double BUTTON_SEARCH  = 1;
    public static double BUTTON_OUT     = 0.45;

    //TODO make all initalize at 0
    public void initializeServos () {
        dumpArm   (DUMP_DOWN);
        climberL  (CLIMBER_IN);
        climberR  (CLIMBER_IN);
        allClearL (CLEAR_IN);
        allClearR (CLEAR_IN);
        button    (BUTTON_IN);
    }




    //----------------------------------------------------------------LIGHT FUNCTIONS
    public void blueLED (boolean state){cdim.setLED(0, state);} 
    public void redLED  (boolean state){cdim.setLED(1, state);}




    //----------------------------------------------------------------SENSOR FUNCTIONS

    //TODO new Gyro Reset functions
    public double vCheck() {return voltage.getVoltage();}
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
        telemetry.addData("Gyro", gyroDelta());
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

    public void floorTelemetry() {
        telemetry.addData("IR", floorIR.getLightDetectedRaw());
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
    public boolean isRed() {return (getAlliance().equals("Red"));}
    public boolean isBlue(){return (getAlliance().equals("Blue"));}

}