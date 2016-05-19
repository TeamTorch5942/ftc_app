package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

/**
 * Created by taand on 3/10/2016.
 */
public class waitBlueFar extends BaseAutonArm {
    Alliance  getAlliance() {return Alliance.BLUE;}
    Wait      getWait()     {return Wait.YES;}
    Placement getPlacement(){return Placement.FAR;}
    Defense   getDefense()  {return Defense.NO;}
}
