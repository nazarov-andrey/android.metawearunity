/*
 * Copyright 2014-2015 MbientLab Inc. All rights reserved.
 *
 * IMPORTANT: Your use of this Software is limited to those specific rights granted under the terms of a software
 * license agreement between the user who downloaded the software, his/her employer (which must be your
 * employer) and MbientLab Inc, (the "License").  You may not use this Software unless you agree to abide by the
 * terms of the License which can be found at www.mbientlab.com/terms.  The License limits your use, and you
 * acknowledge, that the Software may be modified, copied, and distributed when used in conjunction with an
 * MbientLab Inc, product.  Other than for the foregoing purpose, you may not use, reproduce, copy, prepare
 * derivative works of, modify, distribute, perform, display or sell this Software and/or its documentation for any
 * purpose.
 *
 * YOU FURTHER ACKNOWLEDGE AND AGREE THAT THE SOFTWARE AND DOCUMENTATION ARE PROVIDED "AS IS" WITHOUT WARRANTY
 * OF ANY KIND, EITHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION, ANY WARRANTY OF MERCHANTABILITY, TITLE,
 * NON-INFRINGEMENT AND FITNESS FOR A PARTICULAR PURPOSE. IN NO EVENT SHALL MBIENTLAB OR ITS LICENSORS BE LIABLE OR
 * OBLIGATED UNDER CONTRACT, NEGLIGENCE, STRICT LIABILITY, CONTRIBUTION, BREACH OF WARRANTY, OR OTHER LEGAL EQUITABLE
 * THEORY ANY DIRECT OR INDIRECT DAMAGES OR EXPENSES INCLUDING BUT NOT LIMITED TO ANY INCIDENTAL, SPECIAL, INDIRECT,
 * PUNITIVE OR CONSEQUENTIAL DAMAGES, LOST PROFITS OR LOST DATA, COST OF PROCUREMENT OF SUBSTITUTE GOODS, TECHNOLOGY,
 * SERVICES, OR ANY CLAIMS BY THIRD PARTIES (INCLUDING BUT NOT LIMITED TO ANY DEFENSE THEREOF), OR OTHER SIMILAR COSTS.
 *
 * Should you have any questions regarding your right to use this Software, contact MbientLab via email:
 * hello@mbientlab.com.
 */

package com.mbientlab.metawear;

import com.mbientlab.metawear.module.ColorTcs34725;
import com.mbientlab.metawear.module.ColorTcs34725.Gain;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

import java.util.ArrayList;
import java.util.Collection;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by etsai on 10/2/16.
 */
@RunWith(Parameterized.class)
public class TestColorTsc34725Config extends UnitTestBase {
    private final float[] INTEGRATION_TIMES= new float[] {4.8f, 612f};
    private final byte[] INTEGRATION_BITMASKS= new byte[] {(byte) 0xfe, 0x1};

    @Parameters(name = "gain: {0}")
    public static Collection<Object[]> data() {
        ArrayList<Object[]> parameters= new ArrayList<>();
        for(Gain g: Gain.values()) {
            for(int i= 0; i < 2; i++) {
                parameters.add(new Object[] { g, i, true });
                parameters.add(new Object[] { g, i, false });
            }
        }

        return parameters;
    }

    @Parameter
    public Gain gain;

    @Parameter(value = 1)
    public int integrationIdx;

    @Parameter(value = 2)
    public boolean illuminate;

    private ColorTcs34725 colorTcs34725;

    @Before
    public void setup() throws Exception {
        junitPlatform.boardInfo= new MetaWearBoardInfo(ColorTcs34725.class);
        connectToBoard();

        colorTcs34725= mwBoard.getModule(ColorTcs34725.class);
    }

    @Test
    public void configure() {
        byte[] expected= new byte[] {0x17, 0x02, INTEGRATION_BITMASKS[integrationIdx], (byte) gain.ordinal(), (byte) (illuminate ? 0x1 : 0x0)};

        ColorTcs34725.ConfigEditor editor = colorTcs34725.configure()
                .gain(gain)
                .integrationTime(INTEGRATION_TIMES[integrationIdx]);
        if (illuminate) {
            editor.enableIlluminatorLed();
        }
        editor.commit();

        assertArrayEquals(expected, junitPlatform.getLastCommand());
    }
}
