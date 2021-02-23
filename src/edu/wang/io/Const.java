/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.io;

import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.globes.*;
import gov.nasa.worldwind.render.*;

import java.awt.*;

/**
 * @author Zheng WANG
 * @create 2019/4/28 21:58
 * @description superclass class
 * @parameter return Sphere(Geometry) and Globe(Globe)
 */
public final class Const
{
    private static Vec4 ORIGIN = Vec4.ZERO;
    // 长半轴a＝6378137(m), Earth.WGS84_EQUATORIAL_RADIUS
    // 与CGCS2000或WGS84相同表面积的球半径近似为：R2= 6371007.2(m)
    public final static double RADIUS = 6371007.2;//6371.007km
//    public final static double radius = 6371.0072;

    public final static int PRECISION = 12;
    public final static int HALF_PRECISION = 6;
    public final static double EPSILON = Math.pow(10, -HALF_PRECISION);

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

    public static ShapeAttributes defaultPolygonAttribute(Color color)
    {
        return defaultPolygonAttribute(0.5, color);
    }

    public static ShapeAttributes defaultPolygonAttribute(double opacity, Color color)
    {
        ShapeAttributes attributes = new BasicShapeAttributes();

        // for Path
//        attributes.setOutlineMaterial(new Material(new Color(20, 120, 20)));
        attributes.setOutlineMaterial(new Material(color));
        attributes.setDrawOutline(false);
////        attributes.setEnableLighting(false);
//        attributes.setOutlineOpacity(0.0001);

        // for Polygon
        attributes.setInteriorMaterial(new Material(color));
        attributes.setInteriorOpacity(opacity);
//        attributes.setInteriorOpacity(0.008);

        return attributes;
    }

    public static ShapeAttributes defaultPolygonAttribute(double opacity)
    {
        return defaultPolygonAttribute(opacity, new Color(237, 177, 32));
    }

    public static ShapeAttributes defaultPolygonAttribute()
    {
        return defaultPolygonAttribute(0.3);
    }

    public static ShapeAttributes defaultPathAttribute()
    {
        ShapeAttributes attributes = new BasicShapeAttributes();
        // for Path
        attributes.setOutlineMaterial(new Material(new Color(255, 255, 255)));
//        attributes.setOutlineMaterial(new Material(new Color(250, 250, 0)));
//        attributes.setOutlineMaterial(new Material(new Color(0, 0, 100)));
//        attributes.setOutlineMaterial(new Material(new Color(200, 200, 200)));
        attributes.setOutlineWidth(4.0);

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
