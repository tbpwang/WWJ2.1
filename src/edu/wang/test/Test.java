/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.test;

import edu.wang.*;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Polygon;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/12/23
 * @description
 */
public class Test
{
    public static void main(String[] args)
    {
        LatLon p0 = LatLon.fromDegrees(90, 0);
        LatLon p1 = LatLon.fromDegrees(20, 0);
        LatLon p2 = LatLon.fromDegrees(20, 90);

        List<Position> boundary = new ArrayList<>();
        Position pos0, pos1, pos2;
        pos0 = new Position(p0, 0);
        pos1 = new Position(p1, 0);
        pos2 = new Position(p2, 0);
        SurfaceTriangle triangle = new SurfaceTriangle(p0,p1,p2,"abc");
        //IO.write("test","abc",triangle.coordinatesVec4WithID());
        System.out.println("Center\t" + IO.latLonToVec4(triangle.getCenter()));
        System.out.println("Center\t" + IO.latLonToVec4(triangle.getCenter()).normalize3());
        Vec4 v1,v2,v3;
        v1 = IO.latLonToVec4(p0);
        v2 = IO.latLonToVec4(p1);
        v3 = IO.latLonToVec4(p2);
        Vec4 v123 = new Vec4((v1.getX()+v2.getX()+v3.getX())/3,(v1.getY()+v2.getY()+v3.getY())/3,(v1.getZ()+v2.getZ()+v3.getZ())/3);
        System.out.println("V123\t" + v123.normalize3().multiply3(Const.RADIUS));

//        String filePath = "D:\\outData\\QTMlevel8\\vec4Vertices.txt";
//        for (int i = 0; i < 4; i++)
//        {
//            String[] temp = IO.readVec4TxtWithIDLineByNo(filePath,i);
//            System.out.println("" + temp[0]+"\t");
//            for (int j = 1; j < temp.length; j=j+3)
//            {
//                System.out.println(""+temp[j]+"\t"+temp[j+1]+"\t"+temp[j+2]);
//            }
//        }

//        Angle bottom = p1.latitude;
//        double detaLon = p2.longitude.subtract(p1.longitude).radians;
//        double ratio = detaLon / 2 / Math.PI;
//        double gcdDegree = LatLon.greatCircleDistance(p1, p2).radians;
//        double ldDegree = LatLon.linearDistance(p1, p2).radians;
//        Vec4 v10 = IO.latLonToVec4(p1);
//        Vec4 v11 = IO.latLonToVec4(p1).normalize3();
//        Vec4 v1 = IO.latLonToVec4(p1).multiply3(Const.RADIUS);
//        Vec4 v2 = IO.latLonToVec4(p2).multiply3(Const.RADIUS);
//        System.out.println("gcdDegree = " + gcdDegree * Const.RADIUS);
//        System.out.println("ldDegree = " + ldDegree * Const.RADIUS);
//        System.out.println("V2V = " + v1.distanceTo3(v2));
//        System.out.println("v10 = " + v10);
//        System.out.println("v11 = " + v11);
//        System.out.println("v1 = " + v1);
//        System.out.println("GREAT_CIRCLE\t=\t" + Length.calculateArcLength(AVKey.GREAT_CIRCLE, p1, p2));
//        System.out.println("Small_CIRCLE\t=\t" + Length.calculateArcLength(AVKey.RHUMB_LINE, p1, p2));
//        System.out.println("Small_CIRCLE2\t=\t" + Const.RADIUS * bottom.cos() * detaLon);
//        QTMTriangle triangle = new QTMTriangle(p0, p1, p2, "");
//        double area = triangle.calculateCellArea();
//        double areaPole = triangle.calculateCellArea(true);
//        System.out.println("area\t=\t" + area);
//        System.out.println("areaPole\t=\t" + areaPole);
//        System.out.println("Sphere/8\t=\t" + 4 * Math.PI * Math.pow(Const.RADIUS, 2) / 8);
//        System.out.println("==============");
//        double length = Length.calculateArcLength(AVKey.GREAT_CIRCLE, p1, p2);
//        double length1 = LatLon.greatCircleDistance(p1, p2).radians * Const.RADIUS;
//        System.out.println("length\t=\t" + length);
//        System.out.println("length1\t=\t" + length1);
//        LatLon p3 = LatLon.interpolateRhumb(1.0 / 1000, p1, p2);
//        double a = LatLon.greatCircleDistance(p1, p3).radians;
//        double b = LatLon.greatCircleDistance(p0, p3).radians;
//        double c = LatLon.greatCircleDistance(p0, p1).radians;
//        double p = (a + b + c) / 2;
//        double E = 4 * Math.atan(
//            Math.sqrt(Math.tan(p / 2) * Math.tan((p - a) / 2) * Math.tan((p - b) / 2) * Math.tan((p - c) / 2)));
//        System.out.println("area\t=\t" + E * Math.pow(Const.RADIUS, 2));
    }
}
