/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;

/**
 * @author Zheng WANG
 * @create 2019/8/31
 * @description
 */
public class IcosahedronInscribed
{
    private LatLon[] vertices;
    private Vec4[] points;
    private Triangle[] facets;
    private static IcosahedronInscribed instance;

    private IcosahedronInscribed()
    {
        points = new Vec4[12];
        points[0] =new Vec4(0, 0, -1);
        points[1] =new Vec4(-0.723606798, -0.525731109, -0.447213599);
        points[2] =new Vec4(-0.723606798, 0.525731109, -0.447213599);
        points[3] =new Vec4(-0.894427189, 0, 0.447213599);
        points[4] =new Vec4(-0.276393199, -0.850650808, 0.447213599);
        points[5] =new Vec4(-0.276393199, 0.850650808, 0.447213599);
        points[6] =new Vec4(0, 0, 1);
        points[7] =new Vec4(0.723606798, -0.525731109, 0.447213599);
        points[8] =new Vec4(0.723606798, 0.525731109, 0.447213599);
        points[9] =new Vec4(0.894427189, 0, -0.447213599);
        points[10] =new Vec4(0.276393199, -0.850650808, -0.447213599);
        points[11] =new Vec4(0.276393199, 0.850650808, -0.447213599);

        facets = new Triangle[20];
        facets[0] = new Triangle(points[0],points[1],points[2]);
        facets[1] = new Triangle(points[1],points[2],points[3]);
        facets[2] = new Triangle(points[1],points[3],points[4]);
        facets[3] = new Triangle(points[2],points[3],points[5]);
        facets[4] = new Triangle(points[3],points[5],points[6]);
        facets[5] = new Triangle(points[3],points[4],points[6]);
        facets[6] = new Triangle(points[4],points[7],points[10]);
        facets[7] = new Triangle(points[4],points[6],points[7]);
        facets[8] = new Triangle(points[5],points[6],points[8]);
        facets[9] = new Triangle(points[5],points[8],points[11]);
        facets[10] = new Triangle(points[6],points[7],points[8]);
        facets[11] = new Triangle(points[7],points[8],points[9]);
        facets[12] = new Triangle(points[7],points[9],points[10]);
        facets[13] = new Triangle(points[8],points[9],points[11]);
        facets[14] = new Triangle(points[0],points[9],points[11]);
        facets[15] = new Triangle(points[0],points[9],points[10]);
        facets[16] = new Triangle(points[1],points[4],points[10]);
        facets[17] = new Triangle(points[0],points[1],points[10]);
        facets[18] = new Triangle(points[0],points[2],points[11]);
        facets[19] = new Triangle(points[2],points[5],points[11]);
    }

//    private IcosahedronInscribed()
//    {
//        vertices = new LatLon[12];
//        vertices[0] = LatLon.fromDegrees(0.0000, 180.0000);
//        vertices[1] = LatLon.fromDegrees(-31.7175, -121.7175);
//        vertices[2] = LatLon.fromDegrees(31.7175, -121.7175);
//        vertices[3] = LatLon.fromDegrees(0.0000, -63.4349);
//        vertices[4] = LatLon.fromDegrees(-58.2825, -31.7175);
//        vertices[5] = LatLon.fromDegrees(58.2825, -31.7175);
//        vertices[6] = LatLon.fromDegrees(0.0000, 0.0000);
//        vertices[7] = LatLon.fromDegrees(-31.7175, 58.2825);
//        vertices[8] = LatLon.fromDegrees(31.7175, 58.2825);
//        vertices[9] = LatLon.fromDegrees(0.0000, 116.5651);
//        vertices[10] = LatLon.fromDegrees(-58.2825, 148.2825);
//        vertices[11] = LatLon.fromDegrees(58.2825, 148.2825);
//
//        facets = new Triangle[20];
//        facets[0] = new Triangle(Constant.latLonToVec4(vertices[0]),Constant.latLonToVec4(vertices[1]),Constant.latLonToVec4(vertices[2]));
//        facets[1] = new Triangle(Constant.latLonToVec4(vertices[1]),Constant.latLonToVec4(vertices[2]),Constant.latLonToVec4(vertices[3]));
//        facets[2] = new Triangle(Constant.latLonToVec4(vertices[1]),Constant.latLonToVec4(vertices[3]),Constant.latLonToVec4(vertices[4]));
//        facets[3] = new Triangle(Constant.latLonToVec4(vertices[2]),Constant.latLonToVec4(vertices[3]),Constant.latLonToVec4(vertices[5]));
//        facets[4] = new Triangle(Constant.latLonToVec4(vertices[3]),Constant.latLonToVec4(vertices[5]),Constant.latLonToVec4(vertices[6]));
//        facets[5] = new Triangle(Constant.latLonToVec4(vertices[3]),Constant.latLonToVec4(vertices[4]),Constant.latLonToVec4(vertices[6]));
//        facets[6] = new Triangle(Constant.latLonToVec4(vertices[4]),Constant.latLonToVec4(vertices[7]),Constant.latLonToVec4(vertices[10]));
//        facets[7] = new Triangle(Constant.latLonToVec4(vertices[4]),Constant.latLonToVec4(vertices[6]),Constant.latLonToVec4(vertices[7]));
//        facets[8] = new Triangle(Constant.latLonToVec4(vertices[5]),Constant.latLonToVec4(vertices[6]),Constant.latLonToVec4(vertices[8]));
//        facets[9] = new Triangle(Constant.latLonToVec4(vertices[5]),Constant.latLonToVec4(vertices[8]),Constant.latLonToVec4(vertices[11]));
//        facets[10] = new Triangle(Constant.latLonToVec4(vertices[6]),Constant.latLonToVec4(vertices[7]),Constant.latLonToVec4(vertices[8]));
//        facets[11] = new Triangle(Constant.latLonToVec4(vertices[7]),Constant.latLonToVec4(vertices[8]),Constant.latLonToVec4(vertices[9]));
//        facets[12] = new Triangle(Constant.latLonToVec4(vertices[7]),Constant.latLonToVec4(vertices[9]),Constant.latLonToVec4(vertices[10]));
//        facets[13] = new Triangle(Constant.latLonToVec4(vertices[8]),Constant.latLonToVec4(vertices[9]),Constant.latLonToVec4(vertices[11]));
//        facets[14] = new Triangle(Constant.latLonToVec4(vertices[0]),Constant.latLonToVec4(vertices[9]),Constant.latLonToVec4(vertices[11]));
//        facets[15] = new Triangle(Constant.latLonToVec4(vertices[0]),Constant.latLonToVec4(vertices[9]),Constant.latLonToVec4(vertices[10]));
//        facets[16] = new Triangle(Constant.latLonToVec4(vertices[1]),Constant.latLonToVec4(vertices[4]),Constant.latLonToVec4(vertices[10]));
//        facets[17] = new Triangle(Constant.latLonToVec4(vertices[0]),Constant.latLonToVec4(vertices[1]),Constant.latLonToVec4(vertices[10]));
//        facets[18] = new Triangle(Constant.latLonToVec4(vertices[0]),Constant.latLonToVec4(vertices[2]),Constant.latLonToVec4(vertices[11]));
//        facets[19] = new Triangle(Constant.latLonToVec4(vertices[2]),Constant.latLonToVec4(vertices[5]),Constant.latLonToVec4(vertices[11]));
//    }

    private IcosahedronInscribed(String source)
    {
        // 根据袁文，等,2009
        vertices = new LatLon[12];
        vertices[0] = LatLon.fromDegrees(90.0,0.0);
        vertices[1] = LatLon.fromDegrees(26.565,0.0);
        vertices[2] = LatLon.fromDegrees(26.565,72.0);
        vertices[3] = LatLon.fromDegrees(26.565,144.0);
        vertices[4] = LatLon.fromDegrees(26.565,-144.0);
        vertices[5] = LatLon.fromDegrees(26.565,-72.0);
        vertices[6] = LatLon.fromDegrees(-26.565,36.0);
        vertices[7] = LatLon.fromDegrees(-26.565,108.0);
        vertices[8] = LatLon.fromDegrees(-26.565,180.0);
        vertices[9] = LatLon.fromDegrees(-26.565,-108);
        vertices[10] = LatLon.fromDegrees(-26.565,-36.0);
        vertices[11] = LatLon.fromDegrees(-90.0,0.0);
        facets = new Triangle[20];
        facets[0] = new Triangle(IO.latLonToVec4(vertices[0]), IO.latLonToVec4(vertices[1]),
            IO.latLonToVec4(vertices[2]));
        facets[1] = new Triangle(IO.latLonToVec4(vertices[0]), IO.latLonToVec4(vertices[2]),
            IO.latLonToVec4(vertices[3]));
        facets[2] = new Triangle(IO.latLonToVec4(vertices[0]), IO.latLonToVec4(vertices[3]),
            IO.latLonToVec4(vertices[4]));
        facets[3] = new Triangle(IO.latLonToVec4(vertices[0]), IO.latLonToVec4(vertices[4]),
            IO.latLonToVec4(vertices[5]));
        facets[4] = new Triangle(IO.latLonToVec4(vertices[0]), IO.latLonToVec4(vertices[5]),
            IO.latLonToVec4(vertices[1]));
        facets[5] = new Triangle(IO.latLonToVec4(vertices[6]), IO.latLonToVec4(vertices[1]),
            IO.latLonToVec4(vertices[2]));
        facets[6] = new Triangle(IO.latLonToVec4(vertices[7]), IO.latLonToVec4(vertices[2]),
            IO.latLonToVec4(vertices[3]));
        facets[7] = new Triangle(IO.latLonToVec4(vertices[8]), IO.latLonToVec4(vertices[3]),
            IO.latLonToVec4(vertices[4]));
        facets[8] = new Triangle(IO.latLonToVec4(vertices[9]), IO.latLonToVec4(vertices[4]),
            IO.latLonToVec4(vertices[5]));
        facets[9] = new Triangle(IO.latLonToVec4(vertices[10]), IO.latLonToVec4(vertices[5]),
            IO.latLonToVec4(vertices[1]));
        facets[10] = new Triangle(IO.latLonToVec4(vertices[2]), IO.latLonToVec4(vertices[6]),
            IO.latLonToVec4(vertices[7]));
        facets[11] = new Triangle(IO.latLonToVec4(vertices[3]), IO.latLonToVec4(vertices[7]),
            IO.latLonToVec4(vertices[8]));
        facets[12] = new Triangle(IO.latLonToVec4(vertices[4]), IO.latLonToVec4(vertices[8]),
            IO.latLonToVec4(vertices[9]));
        facets[13] = new Triangle(IO.latLonToVec4(vertices[5]), IO.latLonToVec4(vertices[9]),
            IO.latLonToVec4(vertices[10]));
        facets[14] = new Triangle(IO.latLonToVec4(vertices[1]), IO.latLonToVec4(vertices[10]),
            IO.latLonToVec4(vertices[6]));
        facets[15] = new Triangle(IO.latLonToVec4(vertices[11]), IO.latLonToVec4(vertices[6]),
            IO.latLonToVec4(vertices[7]));
        facets[16] = new Triangle(IO.latLonToVec4(vertices[11]), IO.latLonToVec4(vertices[7]),
            IO.latLonToVec4(vertices[8]));
        facets[17] = new Triangle(IO.latLonToVec4(vertices[11]), IO.latLonToVec4(vertices[8]),
            IO.latLonToVec4(vertices[9]));
        facets[18] = new Triangle(IO.latLonToVec4(vertices[11]), IO.latLonToVec4(vertices[9]),
            IO.latLonToVec4(vertices[10]));
        facets[19] = new Triangle(IO.latLonToVec4(vertices[11]), IO.latLonToVec4(vertices[10]),
            IO.latLonToVec4(vertices[6]));
    }

    public static IcosahedronInscribed getInstance()
    {
        return instance==null?new IcosahedronInscribed(""):instance;
    }

    public Vec4[] getPoints()
    {
        return points;
    }

    public LatLon[] getVertices()
    {
        return vertices;
    }

    public Triangle[] getFacets()
    {
        return facets;
    }
}
