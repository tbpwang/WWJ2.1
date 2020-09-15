/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/11/19
 * @description
 */
public class FibonacciLattice
{
//    private int number;
//    private List<LatLon> fibonacci;
//    public FibonacciLattice instance = new FibonacciLattice(number);
    public static List<Position> getFibonacci(int number)
    {
        if (number< 0)
        {
            String msg = Logging.getMessage("valueError.负值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        List<Position> fibonacci = new ArrayList<>();
        double goldenRatio = 2.0/(1+Math.sqrt(5));
        double delt = 180.0/Math.PI;
        double lat,lon;
        for (int i = -number; i <= number; i++)
        {
            lat = Math.asin(2.0*i/(2*number+1))*delt;
            lon = i%delt*360/goldenRatio;
            if (lon<-180)
            {
                lon = lon + 360;
            }
            if (lon>180)
            {
                lon = lon - 360;
            }
            fibonacci.add(Position.fromDegrees(lat,lon));
        }
        return fibonacci;
    }
}
