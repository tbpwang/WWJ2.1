/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.*;
import gov.nasa.worldwind.geom.LatLon;

/**
 * @author Zheng WANG
 * @create 2019/5/7 14:41
 * @description 包括正八面体球的所有面——八个球面三角形即球面八分体
 * @parameter
 */
public enum SphericalTriangleOctahedron
{
    // MiddleArcTriangle
//    first(new SurfaceTriangle(LatLon.fromDegrees(90.0, 0.0),LatLon.fromDegrees(0.0, 0.0),LatLon.fromDegrees(0.0, 90.0),new Geocode("A").getID())),
//    second(new SurfaceTriangle(LatLon.fromDegrees(90.0, 90.0),LatLon.fromDegrees(0.0, 90.0),LatLon.fromDegrees(0.0, 180.0),new Geocode("B").getID())),
//    third(new SurfaceTriangle(LatLon.fromDegrees(90.0, 180.0),LatLon.fromDegrees(0.0, 180.0),LatLon.fromDegrees(0.0, -90.0),new Geocode("C").getID())),
//    fourth(new SurfaceTriangle(LatLon.fromDegrees(90.0, -90.0),LatLon.fromDegrees(0.0, -90.0),LatLon.fromDegrees(0.0, 0.0),new Geocode("D").getID())),
//    fifth(new SurfaceTriangle(LatLon.fromDegrees(-90.0, 0.0),LatLon.fromDegrees(0.0, 0.0),LatLon.fromDegrees(0.0, 90.0),new Geocode("E").getID())),
//    sixth(new SurfaceTriangle(LatLon.fromDegrees(-90.0, 90.0),LatLon.fromDegrees(0.0, 90.0),LatLon.fromDegrees(0.0, 180.0),new Geocode("F").getID())),
//    seventh(new SurfaceTriangle(LatLon.fromDegrees(-90.0, 180.0),LatLon.fromDegrees(0.0, 180.0),LatLon.fromDegrees(0.0, -90.0),new Geocode("G").getID())),
//    eighth(new SurfaceTriangle(LatLon.fromDegrees(-90.0, -90.0),LatLon.fromDegrees(0.0, -90.0),LatLon.fromDegrees(0.0, 0.0),new Geocode("H").getID()));
//    private SurfaceTriangle cell;
    first(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(90.0, 0.0),LatLon.fromDegrees(0.0, 0.0),LatLon.fromDegrees(0.0, 90.0),new Geocode("A"))),
    second(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(90.0, 90.0),LatLon.fromDegrees(0.0, 90.0),LatLon.fromDegrees(0.0, 180.0),new Geocode("B"))),
    third(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(90.0, 180.0),LatLon.fromDegrees(0.0, 180.0),LatLon.fromDegrees(0.0, -90.0),new Geocode("C"))),
    fourth(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(90.0, -90.0),LatLon.fromDegrees(0.0, -90.0),LatLon.fromDegrees(0.0, 0.0),new Geocode("D"))),
    fifth(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(-90.0, 0.0),LatLon.fromDegrees(0.0, 0.0),LatLon.fromDegrees(0.0, 90.0),new Geocode("E"))),
    sixth(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(-90.0, 90.0),LatLon.fromDegrees(0.0, 90.0),LatLon.fromDegrees(0.0, 180.0),new Geocode("F"))),
    seventh(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(-90.0, 180.0),LatLon.fromDegrees(0.0, 180.0),LatLon.fromDegrees(0.0, -90.0),new Geocode("G"))),
    eighth(new MiddleArcSurfaceTriangle(LatLon.fromDegrees(-90.0, -90.0),LatLon.fromDegrees(0.0, -90.0),LatLon.fromDegrees(0.0, 0.0),new Geocode("H")));

    private MiddleArcSurfaceTriangle cell;
    SphericalTriangleOctahedron(MiddleArcSurfaceTriangle cell)
    {
        this.cell = cell;
    }
    public MiddleArcSurfaceTriangle baseTriangle()
    {
        return cell;
    }

    private void testValue()
    {

    }

}
