package com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton;

/**
 * Created by taand on 3/10/2016.
 */
public class waitRedCloseDefense extends BaseAutonArm {
    Alliance getAlliance()  {return Alliance.RED;}
    Wait     getWait()      {return Wait.YES;}
    Placement getPlacement(){return Placement.CLOSE;}
    Defense   getDefense()  {return Defense.YES;}

}
