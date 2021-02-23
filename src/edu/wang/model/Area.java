/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

/**
 * @author Zheng WANG
 * @create 2019/9/22
 * @description 计算面积，平面面积，单位球面上的面积
 */
public class Area
{
    public static double normalizePlaneTriangleArea(Vec4 a, Vec4 b, Vec4 c)
    {
        if (a == null || b == null || c == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        Vec4 p1 = a.normalize3();
        Vec4 p2 = b.normalize3();
        Vec4 p3 = c.normalize3();

        double lab = p1.distanceTo3(p2);
        double lac = p1.distanceTo3(p3);
        double lbc = p2.distanceTo3(p3);
//        double half = (lab + lac + lbc) / 2.0;

        //return Math.sqrt(half * (half - lab) * (half - lac) * (half - lbc)) * Math.pow(Const.RADIUS, 2);
        return calculateTriangleAreaBySides(AVKey.LINEAR, lab, lac, lbc);
    }

    public static double planeTriangleArea(LatLon pa, LatLon pb, LatLon pc)
    {
        double a = Length.calculateLineLength(pb, pc);
        double b = Length.calculateLineLength(pa, pc);
        double c = Length.calculateLineLength(pa, pb);
        if (a <= 0 | b <= 0 | c <= 0)
            return 0.0;
        double area = calculateTriangleAreaBySides(AVKey.LINEAR, a, b, c);
        return area > 0.0 ? area : 0.0;
    }

    public static double planeTriangleArea(Vec4 a, Vec4 b, Vec4 c)
    {
        if (a == null || b == null || c == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        double lab = Length.calculateLineLength(a, b);
        double lac = Length.calculateLineLength(a, c);
        double lbc = Length.calculateLineLength(b, c);
        if (lab <= 0 | lac <= 0 | lbc <= 0)
            return 0.0;
        double area = calculateTriangleAreaBySides(AVKey.LINEAR, lab, lac, lbc);
        return area > 0.0 ? area : 0.0;
    }

    private static double calculateTriangleAreaBySides(String sideType, double aSide, double bSide, double cSide)
    {
        //sideType = AVKey.LINEAR  OR  AVKey.GREAT_CIRCLE
        double p = (aSide + bSide + cSide) / 2.0;
        double s = -1.0;
        if (sideType.equals(AVKey.LINEAR))
            s = Math.sqrt(p * (p - aSide) * (p - bSide) * (p - cSide));
        if (sideType.equals(AVKey.GREAT_CIRCLE))
        {
            double r = Const.RADIUS;
            if (p > 1.5 * Math.PI)
            {
                p = p / r;
                aSide = aSide / r;
                bSide = bSide / r;
                cSide = cSide / r;
            }
            double qtE = Math.atan(Math.sqrt(
                Math.tan(p / 2) * Math.tan((p - aSide) / 2) * Math.tan((p - bSide) / 2) * Math.tan((p - cSide) / 2)));
            if (qtE < 0)
                return 0.0;
            s = Math.pow(r, 2) * 4 * qtE;
        }
        return s;
    }

    public static double planeTriangleArea(Triangle triangle)
    {
        if (triangle == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
//        Vec4 a, b, c;
//        a = triangle.getA();
//        b = triangle.getB();
//        c = triangle.getC();
        return planeTriangleArea(triangle.getA(), triangle.getB(), triangle.getC());
//        return normalizePlaneTriangleArea(triangle.getA(), triangle.getB(), triangle.getC());
    }

    public static double calculateCrownArea(LatLon p1, LatLon p2)
    {
        if (p1.longitude.compareTo(p2.longitude) == 0)
            return 0.0;

        LatLon left = p1.longitude.compareTo(p2.longitude) < 0 ? p1 : p2;
        LatLon right = p1.longitude.compareTo(p2.longitude) > 0 ? p1 : p2;

        double deltaDegree = right.getLongitude().subtract(left.getLongitude()).degrees;
        double ratio = deltaDegree / 360.0;
        //System.out.println("ratio\t" + ratio);
        // latitude范围[0,90]
        Angle bottom = left.latitude.add(right.latitude).divide(2.0);
        // 2*PI*R^2(1-sin)
        double radius = Const.RADIUS;
        return 2 * Math.PI * Math.pow(radius, 2) * (1.0 - Math.abs(bottom.sin())) * ratio;
    }

    public static double unitSphereTriangleArea(LatLon top, LatLon left, LatLon right)
    {
        return sphericalTriangleArea(top, left, right) / Math.pow(Const.RADIUS, 2);
    }

    public static double sphericalTriangleArea(LatLon top, LatLon left, LatLon right)
    {
        if (top == null || left == null || right == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        double p, la, lb, lc;
//        Length.calculateArcLength(AVKey.GREAT_CIRCLE,)
//        la = LatLon.greatCircleDistance(left, right).radians * Const.RADIUS;
//        lb = LatLon.greatCircleDistance(top, right).radians * Const.RADIUS;
//        lc = LatLon.greatCircleDistance(top, left).radians * Const.RADIUS;
        la = Length.calculateArcLength(AVKey.GREAT_CIRCLE, left, right);
        lb = Length.calculateArcLength(AVKey.GREAT_CIRCLE, top, right);
        lc = Length.calculateArcLength(AVKey.GREAT_CIRCLE, top, left);
        if (la < 0 | lb < 0 | lc < 0)
            return 0.0;
        p = (la + lb + lc) / 2.0;
//        double A = 2 * Math.asin(Math.sqrt(Math.sin(p - lc) * Math.sin(p - lb) / Math.sin(lc) / Math.sin(lb)));
//        double B = 2 * Math.asin(Math.sqrt(Math.sin(p - lc) * Math.sin(p - la) / Math.sin(lc) / Math.sin(la)));
//        double C = 2 * Math.asin(Math.sqrt(Math.sin(p - la) * Math.sin(p - lb) / Math.sin(la) / Math.sin(lb)));
//        double radius = 1.0;
//        return (A + B + C - Math.PI) * radius;
        double area = calculateTriangleAreaBySides(AVKey.GREAT_CIRCLE, la, lb, lc);
        return area > 0.0 ? area : 0.0;
    }

    public static double sphericalCapArea(double latRadian)
    {
        double halfPI = Math.PI / 2;
        double radius = Const.RADIUS;
        double area;
        if (latRadian >= 0 && latRadian <= halfPI)
        {
            if (halfPI - latRadian <= Const.EPSILON)
            {
                area = 0;
            }
            else
            {
                // S = 2πR*R(1 - sinθ)
                area = 2 * Math.PI * Math.pow(radius, 2) * (1 - Math.sin(latRadian));
            }
        }
        else if (latRadian <= 0 && latRadian >= -1 * halfPI)
        {
            if (halfPI + latRadian <= Const.EPSILON)
            {
                area = 0;
            }
            else
            {
                // S = 2π*R*R(1 - sinθ)
                area = 2 * Math.PI * Math.pow(radius, 2) * (1 - Math.sin(-1 * latRadian));
            }
        }
        else
        {
            area = 0;
        }
        return area;
    }
}
