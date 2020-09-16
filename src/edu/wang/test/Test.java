/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.test;

import edu.wang.*;
import edu.wang.io.*;
import edu.wang.model.Area;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.util.List;

/**
 * @author Zheng WANG
 * @create 2019/12/23
 * @description
 */
public class Test
{
    public static void main(String[] args)
    {

        LatLon pp4 = LatLon.fromDegrees(45, 0);
//        LatLon pp4 = LatLon.ZERO;
        double pp4Lat = pp4.getLatitude().degrees;
        double pp4Lon = pp4.getLongitude().degrees;
        LatLon pp5 = LatLon.fromDegrees(90, 0);
        LatLon pp6 = LatLon.fromDegrees(46, 90);
        double pp6Lat = pp6.getLatitude().degrees;
        double pp6Lon = pp6.getLongitude().degrees;
        LatLon pp7 = LatLon.interpolateGreatCircle(0.5, pp5, pp6);
        System.out.println("GreatRhumbMid:");
        System.out.println("GreatCircle\t" + LatLon.interpolateGreatCircle(0.5, pp4, pp6));
        System.out.println("Rhumb\t" + LatLon.interpolateRhumb(0.5, pp4, pp6));
        System.out.println("Mid\t" + LatLon.fromDegrees((pp4Lat + pp6Lat) / 2.0, (pp4Lon + pp6Lon) / 2.0));
        System.out.println("===");
        System.out.println("pp7\t" + pp7);
        LatLon pp8 = LatLon.fromDegrees(45, -180);
        LatLon pp9 = LatLon.fromDegrees(45, -90);
        System.out.println("CrossDateLine\t=\t" + LatLon.locationsCrossDateline(pp8, pp9));
        double deltaPP89 = pp8.longitude.add(pp9.longitude).divide(2.0).degrees - 180;
        System.out.println("deltaPP89\t" + deltaPP89);
//        Vec4.
//        LatLon pp2 = LatLon.fromDegrees(90,0);
//        LatLon pp3 = LatLon.fromDegrees(0,90);
        Vec4 pp1 = new Vec4(0, 0, 1);
        Vec4 pp2 = new Vec4(0, 1, 0);
        Vec4 pp3 = new Vec4(1, 0, 0);
        System.out.println(pp1);
        System.out.println(pp2);
        System.out.println(pp3);

        System.out.println("0%2\t=\t" + 0 % 2);
        System.out.println("test = " + false);
        System.out.println("atan2(3,0) = " + Math.atan2(3, 0) / Math.PI);
        System.out.println("atan2(3,1) = " + Math.atan2(3, 1));
        LatLon top = LatLon.fromDegrees(90, 90);
        LatLon left = LatLon.fromDegrees(0, 0);
        LatLon right = LatLon.fromDegrees(0, 90);
        LatLon p1 = LatLon.fromDegrees(45, 0);
        LatLon p2 = LatLon.fromDegrees(0, 0);
        LatLon p3 = LatLon.fromDegrees(0, 45);
        LatLon p13 = LatLon.interpolateGreatCircle(0.5, p1, p3);
        System.out.println("Area_Null = " + Area.unitSphereSurfaceTriangleArea(p1, p13, p3));
        System.out.println("Area_Null = " + Area.unitSphereSurfaceTriangleArea(p1, p3, p13));
        LatLon p31 = LatLon.fromDegrees(p1.getLatitude().add(p3.latitude).divide(2.0).degrees,
            p1.getLongitude().add(p3.longitude).divide(2.0).degrees);
        LatLon p12 = LatLon.interpolateGreatCircle(0.5, p1, p2);
        LatLon p21 = LatLon.fromDegrees(p1.getLatitude().add(p2.latitude).divide(2.0).degrees,
            p1.getLongitude().add(p2.longitude).divide(2.0).degrees);
        System.out.println("TestComputeLong1 = " + QTMTriangle.calculateLongitude(p1, p3, 20));
        System.out.println("TestComputeLong2 = " + QTMTriangle.calculateLongitude(p1, p3, 0));
        double testComputeLong = QTMTriangle.calculateLongitude(p2, p1, 10);
        System.out.println("TestComputeLong = " + testComputeLong);
        System.out.println("TestComputeLong = " + QTMTriangle.calculateLongitude(p1, p2, 10));
        System.out.println("TestComputeLong3 = " + QTMTriangle.calculateLongitude(p1, p2, 0));
        System.out.println("TestComputeLong4 = " + QTMTriangle.calculateLongitude(p1, p2, 20));
        System.out.println("1E-6\tRadian=\t" + IO.check(Math.toDegrees(1e-6)) + "\tdegree");
        System.out.println("1E-6\tRadian=\t" + (Math.toDegrees(1e-6)) + "\tdegree");
        System.out.println("1E-6*Radius\t=\t" + Const.RADIUS * 1E-6 + "\tm");
        System.out.println("TestComputeLong5 = " + QTMTriangle.calculateLongitude(LatLon.fromDegrees(30, -90),
            LatLon.fromDegrees(34, -100), 32));
        System.out.println("TestComputeLong5 = " + QTMTriangle.calculateLongitude(LatLon.fromDegrees(34, -100),
            LatLon.fromDegrees(30, -90), 32));
        LatLon testLong5 = LatLon.interpolateRhumb(0.5, LatLon.fromDegrees(50, -100), LatLon.fromDegrees(30, -90));
        System.out.println("testLong5\t" + testLong5);

        double testComputeLong5 = QTMTriangle.calculateLongitude(LatLon.fromDegrees(50, -100),
            LatLon.fromDegrees(30, -100), 40);

        System.out.println("1E-4\tdegree = " + Math.toRadians(1e-4) * Const.RADIUS + "\tm");
        System.out.println("1E-4\tdegree = " + Math.toRadians(1e-4) + "\tradian");
        System.out.println("1E-4\tdegree = " + IO.check(Math.toRadians(1e-4)) + "\tradian");
        System.out.println("TestComputeLong5 = " + testComputeLong5);
        System.out.println(p12);
        System.out.println(p13);
        double distance1 = LatLon.greatCircleDistance(p13, p31).radians;
        double distance2 = LatLon.greatCircleDistance(p21, p12).radians;
        System.out.println("p13->p31 = " + (distance1 <= Const.EPSILON ? 0.0 : IO.check(distance1 * Const.RADIUS)));
        System.out.println(IO.check(180 * distance1 / Math.PI));
        System.out.println("deltaArea = " + Area.unitSphereSurfaceTriangleArea(p1, p31, p3));
        System.out.println(
            "deltaArea = " + Area.unitSphereSurfaceTriangleArea(p1, p31, p3) * Const.RADIUS * Const.RADIUS);
        System.out.println("delta1000 = " + IO.check(1000 / Const.RADIUS));
        System.out.println(
            "deltaRate = " + Area.unitSphereSurfaceTriangleArea(p1, p31, p3) / Area.unitSphereSurfaceTriangleArea(p1,
                p2, p3));
        System.out.println(IO.check(1000 / Const.RADIUS * 180 / Math.PI));
        System.out.println("p12->p21 = " + (distance2 <= Const.EPSILON ? 0.0 : IO.check(distance2 * Const.RADIUS)));
        System.out.println(IO.check(distance2 * Const.RADIUS));

        QTMTriangle QTMTriangle = new QTMTriangle(top, left, right, new Geocode("A"));
        edu.wang.model.QTMTriangle[] subQTMTriangle = QTMTriangle.refine();
        System.out.println("QTMArea0 = " + subQTMTriangle[0].computeArea());
        System.out.println("QTMArea1 = " + subQTMTriangle[1].computeArea());
        System.out.println("QTMArea2 = " + subQTMTriangle[2].computeArea());
        double QtmArea3 = subQTMTriangle[3].computeArea();
        System.out.println("QTMArea3 = " + subQTMTriangle[3].computeArea());
        System.out.println(subQTMTriangle[3]);
        double sum = (subQTMTriangle[0].computeArea() + subQTMTriangle[1].computeArea()
            + subQTMTriangle[2].computeArea() + subQTMTriangle[3].computeArea());
        System.out.println("QTMArea3 = " + (QTMTriangle.computeArea() - (subQTMTriangle[0].computeArea()
            + subQTMTriangle[1].computeArea()
            + subQTMTriangle[2].computeArea())));
        System.out.println("QTMAreaAll = " + QTMTriangle.computeArea());
        System.out.println("Sum = " + sum);
        System.out.println("sum/All = " + sum / QTMTriangle.computeArea());

        MiddleArcSurfaceTriangle surfaceTriangle = new MiddleArcSurfaceTriangle(top, left, right, new Geocode("A"));
        MiddleArcSurfaceTriangle[] subSurfaceTriangle = surfaceTriangle.refine();
        System.out.println("sArea0 = " + subSurfaceTriangle[0].computeArea());
        System.out.println("sArea1 = " + subSurfaceTriangle[1].computeArea());
        System.out.println("sArea2 = " + subSurfaceTriangle[2].computeArea());
        System.out.println("sArea3 = " + subSurfaceTriangle[3].computeArea());
        System.out.println("sAreaAll = " + surfaceTriangle.computeArea());
        double sum1 = (subSurfaceTriangle[0].computeArea() + subSurfaceTriangle[1].computeArea()
            + subSurfaceTriangle[2].computeArea() + subSurfaceTriangle[3].computeArea());
        System.out.println("Sum = " + sum1);
        System.out.println("sum/all = " + sum1 / surfaceTriangle.computeArea());

        System.out.println("==");
        SmallCircle circle1 = new SmallCircle(right, top, left);
        double cola = circle1.getCoLatitude().degrees;
        System.out.println(cola);
        LatLon latLon1 = LatLon.fromDegrees(30, 60);
        LatLon latLon2 = LatLon.fromDegrees(30, 61);
        LatLon rPole = LatLon.fromDegrees(90, 0);
        SmallCircle circle2 = new SmallCircle(latLon1, latLon2, rPole);
        double linarDis = LatLon.linearDistance(latLon1, latLon2).radians;
        System.out.println("linarDis = " + linarDis);
        Vec4 v1 = IO.latLonToVec4(latLon1);
        Vec4 v2 = IO.latLonToVec4(latLon2);
        double linarDis2 = v1.distanceTo3(v2);
        System.out.println("linarDis2 = " + linarDis2);
        System.out.println("GreatCircle = " + LatLon.greatCircleDistance(latLon1, latLon2).radians);
        double dis1 = circle2.getLength();
        System.out.println("smallcircle = " + dis1);
        SurfaceTriangle surfaceTriangle1 = new SurfaceTriangle(rPole, latLon1, latLon2, "");
        System.out.println("surfaceTriangle1 = " + surfaceTriangle1.getUnitArea());
        System.out.println("surfaceTriangle2 = " + Area.unitSphereSurfaceTriangleArea(rPole, latLon1, latLon2));

        System.out.println("测试小圆弧三角形（SongTriangle）");
        SmallCircle smallCircle1 = new SmallCircle(LatLon.fromDegrees(0, 45), LatLon.fromDegrees(45, 0),
            LatLon.fromDegrees(0, 0));
        SmallCircle smallCircle2 = new SmallCircle(LatLon.fromDegrees(45, 90), LatLon.fromDegrees(0, 45),
            LatLon.fromDegrees(0, 90));
        SmallCircle smallCircle3 = new SmallCircle(LatLon.fromDegrees(45, 0), LatLon.fromDegrees(45, 90),
            LatLon.fromDegrees(90, 0));

        LatLon pB = smallCircle1.getFirst().latLon;
        LatLon pA = smallCircle1.getLast().latLon;
        Vec4 pO = smallCircle1.getCircleCenter();
        LatLon middle = smallCircle1.getMiddlePoint();
        distance1 = LatLon.greatCircleDistance(IO.vec4ToLatLon(pO), (pA)).radians;
        distance2 = LatLon.greatCircleDistance(IO.vec4ToLatLon(pO), (pB)).radians;
        double distance3 = LatLon.greatCircleDistance(IO.vec4ToLatLon(pO), middle).radians;
        System.out.println("distance: " + IO.check(distance1));
        System.out.println("distance: " + IO.check(distance2));
        System.out.println("distance: " + IO.check(distance3));

        SongTriangle songTriangle = new SongTriangle(smallCircle3, smallCircle2, smallCircle1, "a");
//        SongTriangle[] triangles = songTriangle.refine();
//        for (int i = 0; i < 4; i++)
//        {
//            System.out.println("" + i + ": " + triangles[i].getUnitArea());
//        }

        latLon1 = LatLon.fromDegrees(30.1234567, 40.234567);
        latLon2 = LatLon.fromDegrees(40.4567811, 30.1235555);
        LatLon latLon3 = LatLon.fromDegrees(50.5555555, 80.888888123);
        SurfaceTriangle surfaceTriangle2 = new SurfaceTriangle(latLon1, latLon2, latLon3, "");
        List<LatLon> vertices = surfaceTriangle2.getGeoVertices();
        System.out.println("Vertices: " + vertices);

        LatLon latLon = LatLon.fromDegrees(0, 0);
        Vec4 vec4 = IO.latLonToVec4(latLon);
        PolarPoint polarPoint = PolarPoint.fromCartesian(vec4);

        System.out.println("polarPoint : " + polarPoint.toCartesian());
        PolarPoint pole1 = PolarPoint.fromDegrees(0, 0, 1);
        PolarPoint pole2 = PolarPoint.fromDegrees(90, 90, 1);
        PolarPoint pole3 = PolarPoint.fromDegrees(0, 90, 1);
        PolarPoint pole4 = PolarPoint.fromCartesian(1, 2, 3);
//        System.out.println(pole.toString());
        Vec4 poleVec1 = IO.check(pole1.toCartesian());
        Vec4 poleVec2 = IO.check(pole2.toCartesian());
        Vec4 poleVec3 = IO.check(pole3.toCartesian());
        Vec4 poleVec4 = IO.check(pole4.toCartesian());

        System.out.println("latLon " + IO.latLonToVec4(latLon));
        System.out.println("poleVec1 " + poleVec1);
        System.out.println("poleVec2 " + poleVec2);
        System.out.println("poleVec3 " + poleVec3);
        System.out.println("poleVec4 " + poleVec4);

//        List<Double> values = new ArrayList<>();
        double i = Math.PI / 180;
        String title = String.valueOf(System.currentTimeMillis());
        while (i <= Math.PI / 2)
        {
//            values.add(calculateFunction(i));
            IO.write("songFunctionValue", "songFunctionValues" + title,
                IO.formatDouble(i * 180 / Math.PI) + ":\tAreaDelta\t=\t" + IO.formatDouble(
                    calculateFunction(i, Math.PI / 6, Math.PI / 20)));
            i += Math.PI / 180;
        }
    }

    private static double calculateFunctionDerivative(double x, double alpha, double semiLuneArea)
    {
        double a = Math.cos(alpha) - 1;
        double b = Math.cos(alpha) + 1;
        double c = 1 - Math.pow(Math.cos(x), 2);
        double d = a / c;
        double f_x_d = 4 * a / Math.pow(c, 2) * Math.cos(x) * Math.sin(x) / Math.sqrt(b * b - Math.pow(2 + d, 2))
            + Math.acos(1 + d) * Math.sin(x)
            - 2 * a / Math.pow(c, 2) * Math.sin(x) * Math.pow(Math.cos(x), 2) / Math.sqrt(1 - Math.pow(1 + d, 2));
        return f_x_d;
    }

    private static double calculateFunction(double x, double alpha, double semiLuneArea)
    {
        // x = [0,PI/2]

//        if (initCoLat <= Const.EPSILON)
//        {
//            System.out.println("semiLuneArea - Math.PI = ");
//            System.out.println(semiLuneArea - Math.PI);
////            return 0.0;
//            return semiLuneArea - Math.PI;
//        }
//        double top = Math.acos(1 + (bottomCos - 1) / (1 - Math.pow(Math.cos(initCoLat), 2)));
//        double btm = Math.asin(Math.sqrt((1 + Math.cos(top)) / (1 + bottomCos)));
//        return semiLuneArea - (Math.PI - 2 * btm - top * Math.cos(initCoLat));

        double a = Math.cos(alpha) - 1;
        double b = Math.cos(alpha) + 1;
        double c = a / (1 - Math.pow(Math.cos(x), 2));
        double f_x = Math.PI - 2 * Math.asin((2 + c) / b) - Math.acos(1 + c) * Math.cos(x) - semiLuneArea;
        return f_x;
    }
}
