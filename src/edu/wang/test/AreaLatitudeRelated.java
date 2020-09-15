/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.test;

import edu.wang.io.Cons;

/**
 * @author Zheng WANG
 * @create 2019/7/16
 * @description
 */
public class AreaLatitudeRelated
{
    // chi = pi/2 - latitude
    private double coefficient_kappa(int level)
    {
        // level > 4
        double R = Cons.getEarth().getRadius();
        return Math.pow(Math.PI, 3) * Math.pow(R, 2) / Math.pow(2, 2 * level + 4);
    }

    // chi = pi/2 - latitude
    private double sinc(double chi)
    {
        return chi == 0 ? 0.0 : Math.sin(chi) / chi;
    }
}
