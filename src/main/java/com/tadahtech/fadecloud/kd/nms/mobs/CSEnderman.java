package com.tadahtech.fadecloud.kd.nms.mobs;

import com.tadahtech.fadecloud.kd.nms.NMS;
import net.minecraft.server.v1_8_R3.EntityEnderman;
import net.minecraft.server.v1_8_R3.World;

/**
 * Created by Timothy Andis (TadahTech) on 7/27/2015.
 */
public class CSEnderman extends EntityEnderman {

    public CSEnderman(World world) {
        super(world);
        NMS.clearGoals(targetSelector, goalSelector);
    }

    @Override
    public void g(double d, double d2, double d1){

    }
}