/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.Const;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

/**
 * @author Zheng WANG
 * @create 2019/9/22
 * @description 计算面积，平面面积，单位球面上的面积
 */
public class Area
{
    public static double planeTriangleArea(Vec4 a, Vec4 b, Vec4 c)
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
        double half = (lab + lac + lbc) / 2.0;

        return Math.sqrt(half * (half - lab) * (half - lac) * (half - lbc)) * Math.pow(Const.RADIUS, 2);
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
    }

    public static double unitSphereSurfaceTriangleArea(LatLon top, LatLon left, LatLon right)
    {
        if (top == null || left == null || right == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        double p, la, lb, lc;
        la = LatLon.greatCircleDistance(left, right).radians;
        lc = LatLon.greatCircleDistance(top, left).radians;
        lb = LatLon.greatCircleDistance(top, right).radians;
        p = (la + lb + lc) / 2.0;
        double A = 2 * Math.asin(Math.sqrt(Math.sin(p - lc) * Math.sin(p - lb) / Math.sin(lc) / Math.sin(lb)));
        double B = 2 * Math.asin(Math.sqrt(Math.sin(p - lc) * Math.sin(p - la) / Math.sin(lc) / Math.sin(la)));
        double C = 2 * Math.asin(Math.sqrt(Math.sin(p - la) * Math.sin(p - lb) / Math.sin(la) / Math.sin(lb)));
        double radius = 1.0;
        return (A + B + C - Math.PI) * radius;
    }
}
