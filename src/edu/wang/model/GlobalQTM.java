/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import gov.nasa.worldwind.geom.LatLon;

import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2020/11/17 16:29
 * @Param:
 */
public class GlobalQTM
{
    private static final List<QTMTriangle> globalQTMs = new ArrayList<>();

    private static void setGlobalQTMs()
    {
        // up
        // QTM1
        LatLon A = LatLon.fromDegrees(90.0, 0.0);
        LatLon B = LatLon.fromDegrees(0.0, 0.0);
        LatLon C = LatLon.fromDegrees(0.0, 90.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM2
        A = LatLon.fromDegrees(90.0, 0.0);
        B = LatLon.fromDegrees(0.0, 90.0);
        C = LatLon.fromDegrees(0.0, 180.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM3
        A = LatLon.fromDegrees(90.0, 0.0);
        B = LatLon.fromDegrees(0.0, -180.0);
        C = LatLon.fromDegrees(0.0, -90.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM4
        A = LatLon.fromDegrees(90.0, 0.0);
        B = LatLon.fromDegrees(0.0, -90.0);
        C = LatLon.fromDegrees(0.0, 0.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        //down
        // QTM5
        A = LatLon.fromDegrees(-90.0, 0.0);
        B = LatLon.fromDegrees(0.0, 0.0);
        C = LatLon.fromDegrees(0.0, 90.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM6
        A = LatLon.fromDegrees(-90.0, 0.0);
        B = LatLon.fromDegrees(0.0, 90.0);
        C = LatLon.fromDegrees(0.0, 180.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM7
        A = LatLon.fromDegrees(-90.0, 0.0);
        B = LatLon.fromDegrees(0.0, -180.0);
        C = LatLon.fromDegrees(0.0, -90.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));

        // QTM8
        A = LatLon.fromDegrees(-90.0, 0.0);
        B = LatLon.fromDegrees(0.0, -90.0);
        C = LatLon.fromDegrees(0.0, 0.0);
        globalQTMs.add(new QTMTriangle(A,B,C,""));
    }

    public static List<QTMTriangle> getGlobalQTMs()
    {
        if (globalQTMs.size() == 0)
        {
            setGlobalQTMs();
        }
        return globalQTMs;
    }
}
