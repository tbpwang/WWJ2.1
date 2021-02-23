/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.IO;
import gov.nasa.worldwind.geom.*;

public class CellVertex
{
    // cell vertex
    public LatLon latLon;
    public Vec4 vec4;
    private double height;
//    private String someValue;

    public CellVertex(LatLon latLon)
    {
        this.latLon = latLon;
        vec4 = IO.latLonToVec4(latLon);
        height = 0.0;
    }

    public CellVertex(Vec4 vec4)
    {
        this.vec4 = vec4;
        latLon = IO.vec4ToLatLon(vec4);
        height = 0.0;
    }

//    public LatLon getLatLon()
//    {
//        return latLon;
//    }
//
//    public Vec4 getVec4()
//    {
//        return vec4;
//    }

    public void setHeight(double height)
    {
        this.height = height;
    }

    public double getHeight()
    {
        return height;
    }
}
