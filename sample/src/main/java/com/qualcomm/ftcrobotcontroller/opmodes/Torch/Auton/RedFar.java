package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

/**
 * Created by taand on 3/10/2016.
 */
public class RedFar extends BaseAutonArm {
    Alliance getAlliance()  {return Alliance.RED;}
    Wait     getWait()      {return Wait.NO;}
    Placement getPlacement(){return Placement.FAR;}
    Defense   getDefense()  {return Defense.NO;}
}
