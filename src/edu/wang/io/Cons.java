/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.io;

import edu.wang.Geocode;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.render.*;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwindx.examples.ParallelPaths;

import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * @author Zheng WANG
 * @create 2019/4/28 21:58
 * @description superclass class
 * @parameter return Sphere(Geometry) and Globe(Globe)
 */
public final class Cons
{
    public final static double EPSILON = 1.0E-6;
    private static Vec4 ORIGIN = Vec4.ZERO;
    // 长半轴a＝6378137(m), Earth.WGS84_EQUATORIAL_RADIUS
    // 与CGCS2000或WGS84相同表面积的球半径近似为：R2= 6371007.2(m)
    public final static double RADIUS = 6371007.2;//6371.007km
//    public final static double radius = 6371.0072;

    public final static int PRECISION = 6;

    public static int NEIGHBOR_TYPE_EDGE = 1;

    public static int NEIGHBOR_TYPE_VERTEX = 0;

    public static Sphere getSphere()
    {
        return new Sphere(ORIGIN, RADIUS);
    }

    public static Sphere getUnitSphere()
    {
        return Sphere.UNIT_SPHERE;
    }

    public static Globe getGlobe()
    {
        return new EllipsoidalGlobe(RADIUS, RADIUS, 0.0, null, ORIGIN);
    }

    public static Globe getUnitGlobe()
    {
        return new EllipsoidalGlobe(1.0, 1.0, 0.0, null, ORIGIN);
    }

    public static Globe getEarth()
    {
        return new Earth();
    }

    public static Globe getEarthFlat()
    {
        return new EarthFlat();
    }

    public static ShapeAttributes defaultPolygonAttribute()
    {
        ShapeAttributes attributes = new BasicShapeAttributes();

        // for Path
        attributes.setOutlineMaterial(new Material(new Color(20, 120, 20)));

        // for Polygon
        attributes.setInteriorMaterial(new Material(new Color(20, 100, 20)));
        attributes.setInteriorOpacity(0.008);

        return attributes;
    }

    public static ShapeAttributes defaultPathAttribute()
    {
        ShapeAttributes attributes = new BasicShapeAttributes();
        // for Path
        attributes.setOutlineMaterial(new Material(new Color(20, 150, 20)));
        attributes.setOutlineWidth(2.0);

        return attributes;
    }

    public static ShapeAttributes defaultPathAttribute(Color color)
    {
        ShapeAttributes attributes = new BasicShapeAttributes();
        // for Path
        attributes.setOutlineMaterial(new Material(color));
        attributes.setOutlineWidth(2.0);

        return attributes;
    }

    private static void test()
    {
    }
}
