/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.Const;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/9/22
 * @description
 */
public class Perimeter
{
    public static List<Double> triangleEdges(Vec4 p1, Vec4 p2, Vec4 p3)
    {
        if (p1 == null || p2 == null || p3 == null)
        {
            String msg = Logging.getMessage("nullValue.三角形为空");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        List<Double> edgeList = new ArrayList<>(3);
        edgeList.add(p1.distanceTo3(p2));
        edgeList.add(p1.distanceTo3(p3));
        edgeList.add(p2.distanceTo3(p3));
        return edgeList;
    }

    public static double trianglePerimeter(Vec4 a, Vec4 b, Vec4 c)
    {
        List<Double> edges = triangleEdges(a, b, c);
        return edges.get(0) + edges.get(1) + edges.get(2);
    }

    public static double trianglePerimeter(Triangle triangle)
    {
        if (triangle == null)
        {
            String msg = Logging.getMessage("NullValue.三角形是空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        return trianglePerimeter(triangle.getA(), triangle.getB(), triangle.getC());
    }

    public static List<Double> sphericalTriangleEdge(LatLon a, LatLon b, LatLon c)
    {
        if (a == null || b == null || c == null)
        {
            String msg = Logging.getMessage("nullValue.顶点有空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        List<Double> edgeList = new ArrayList<>(3);
        edgeList.add(LatLon.greatCircleDistance(a, b).getRadians() * Const.RADIUS);
        edgeList.add(LatLon.greatCircleDistance(a, c).getRadians() * Const.RADIUS);
        edgeList.add(LatLon.greatCircleDistance(c, b).getRadians() * Const.RADIUS);
        return edgeList;
    }

    public static double sphericalTrianglePerimeter(LatLon a, LatLon b, LatLon c)
    {
        return sphericalTriangleEdge(a, b, c).get(0) + sphericalTriangleEdge(a, b, c).get(1) + sphericalTriangleEdge(a, b, c).get(2);
    }
}
