/* Copyright (c) 2014, 2015 Qualcomm Technologies Inc

All rights reserved.

Redistribution and use in source and binary forms, with or without modification,
are permitted (subject to the limitations in the disclaimer below) provided that
the following conditions are met:

Redistributions of source code must retain the above copyright notice, this list
of conditions and the following disclaimer.

Redistributions in binary form must reproduce the above copyright notice, this
list of conditions and the following disclaimer in the documentation and/or
other materials provided with the distribution.

Neither the name of Qualcomm Technologies Inc nor the names of its contributors
may be used to endorse or promote products derived from this software without
specific prior written permission.

NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE. */

package com.qualcomm.ftcrobotcontroller.opmodes;

import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.BlueClose;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.BlueFar;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.RedClose;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.RedFar;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitBlueClose;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitBlueFar;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitRedClose;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitRedFar;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitBlueCloseDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitBlueFarDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitRedCloseDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.waitRedFarDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.BlueCloseDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.BlueFarDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.RedCloseDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.Auton.RedFarDefense;
import com.qualcomm.ftcrobotcontroller.opmodes.Torch.LinearTele;
import com.qualcomm.robotcore.eventloop.opmode.OpModeManager;
import com.qualcomm.robotcore.eventloop.opmode.OpModeRegister;

/**
 * Register Op Modes
 */
public class FtcOpModeRegister implements OpModeRegister {

    /**
     * The Op Mode Manager will call this method when it wants a list of all
     * available op modes. Add your op mode to the list to enable it.
     *
     * @param manager op mode manager
     */
    public void register(OpModeManager manager) {
        //Custom op modes
        manager.register("Teleop", LinearTele.class);
        manager.register("red close",RedClose.class);
        manager.register("red far",RedFar.class);
        manager.register("blue close",BlueClose.class);
        manager.register("blue far",BlueFar.class);
        manager.register("wait red close",waitRedClose.class);
        manager.register("wait red far",waitRedFar.class);
        manager.register("wait blue close",waitBlueClose.class);
        manager.register("wait blue far",waitBlueFar.class);
        manager.register("defense red close",RedCloseDefense.class);
        manager.register("defense red far",RedFarDefense.class);
        manager.register("defense blue close",BlueCloseDefense.class);
        manager.register("defense blue far",BlueFarDefense.class);
        manager.register("wait defense red close",waitRedCloseDefense.class);
        manager.register("wait defense red far",waitRedFarDefense.class);
        manager.register("wait defense blue close",waitBlueCloseDefense.class);
        manager.register("wait defense blue far",waitBlueFarDefense.class);
    }
}
