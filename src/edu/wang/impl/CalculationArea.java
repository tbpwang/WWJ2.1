/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.Geocode;
import edu.wang.io.*;
import edu.wang.model.MiddleArcSurfaceTriangle;
import gov.nasa.worldwind.geom.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/9/25
 * @description SQT法剖分并输出面积
 */
public class CalculationArea
{
    public static void main(String[] args)
    {
        int facet = 0;//A
        String ID = "A";
        Triangle triangle = OctahedronInscribed.getInstance().getFacetList().get(facet);
        LatLon p1 = IO.vec4ToLatLon(triangle.getA());
        LatLon p2 = IO.vec4ToLatLon(triangle.getB());
        LatLon p3 = IO.vec4ToLatLon(triangle.getC());

        MiddleArcSurfaceTriangle baseTriangle = new MiddleArcSurfaceTriangle(p1, p2, p3, new Geocode(ID));
        List<MiddleArcSurfaceTriangle> surfaceTriangles = new ArrayList<>();
        surfaceTriangles.add(baseTriangle);
        List<MiddleArcSurfaceTriangle> tempTriangles;
//        List<Double> surfaceAreas = new ArrayList<>();

        // 按照编码顺序排列
        //

        int level = 10;
        for (int i = 0; i < level; i++)
        {
            tempTriangles = new ArrayList<>();
            for (MiddleArcSurfaceTriangle tri :
                surfaceTriangles)
            {
                tempTriangles.addAll(Arrays.asList(tri.refine()));
            }
            if (tempTriangles.size() != 0)
            {
                String title = facet + "_" + i;
                for (MiddleArcSurfaceTriangle var :
                    surfaceTriangles)
                {
                    IO.write("OctahedronInscribed", title,
                        String.valueOf(var.getUnitArea() * Math.pow(Cons.RADIUS, 2)));
                }
                surfaceTriangles.clear();
                surfaceTriangles.addAll(tempTriangles);
            }
        }

        // 输出三角形
//        List<Vec4> list = OctahedronInscribed.getInstance().getPoints();
//        for (Vec4 var :
//            list)
//        {
//            System.out.print(var);
//            System.out.println(Constant.vec4ToLatLon(var));
//        }
//        System.out.println();
//        int i = 0;
//        Triangle[] triangles = OctahedronInscribed.getInstance().getFacets();
//        for (Triangle tri :
//            triangles)
//        {
//            System.out.println(i++);
//            System.out.print(tri.getA());
//            System.out.println(Constant.vec4ToLatLon(tri.getA()));
//            System.out.print(tri.getB());
//            System.out.println(Constant.vec4ToLatLon(tri.getB()));
//            System.out.print(tri.getC());
//            System.out.println(Constant.vec4ToLatLon(tri.getC()));
//            System.out.println();
//        }
    }
}
