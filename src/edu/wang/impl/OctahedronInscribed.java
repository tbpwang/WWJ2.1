/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/9/1
 * @description
 */
public class OctahedronInscribed
{
    private static List<Vec4> points;
    private static List<Triangle> facetList;
    private static Triangle[] facets;
    private static OctahedronInscribed instance;

    private OctahedronInscribed()
    {
        points = new ArrayList<>();
        points.add(new Vec4(1, 0, 0));
        points.add(new Vec4(-1, 0, 0));
        points.add(new Vec4(0, 1, 0));
        points.add(new Vec4(0, -1, 0));
        points.add(new Vec4(0, 0, 1));
        points.add(new Vec4(0, 0, -1));

//        facets = new ArrayList<>();
        facets = new Triangle[8];
        facets[0] = new Triangle(points.get(4), points.get(0), points.get(2));
        facets[1] = new Triangle(points.get(4), points.get(2), points.get(1));
        facets[2] = new Triangle(points.get(4), points.get(1), points.get(3));
        facets[3] = new Triangle(points.get(4), points.get(3), points.get(0));
        facets[4] = new Triangle(points.get(5), points.get(0), points.get(2));
        facets[5] = new Triangle(points.get(5), points.get(2), points.get(1));
        facets[6] = new Triangle(points.get(5), points.get(1), points.get(3));
        facets[7] = new Triangle(points.get(5), points.get(3), points.get(0));
    }

    public static OctahedronInscribed getInstance()
    {
        return instance == null ? new OctahedronInscribed() : instance;
    }

    public List<Vec4> getPoints()
    {
        return points;
    }

    public Triangle[] getFacets()
    {
        return facets;
    }

    public List<Triangle> getFacetList()
    {
        return Arrays.asList(facets);
    }

    public static void main(String[] args)
    {
        // dispersion
        OctahedronInscribed octahedron = OctahedronInscribed.getInstance();
        double xSum = 0.0;
        double ySum = 0.0;
        double zSum = 0.0;
        double sumSquare;
//        for (Vec4 v4 : vec4List)
        for (Vec4 v4 : octahedron.getPoints())
        {
            xSum += v4.x;
            ySum += v4.y;
            zSum += v4.z;
            System.out.println(IO.vec4ToLatLon(v4));
        }
        pointVector(xSum, ySum, zSum, 6);
    }

    private static void pointVector(double x, double y, double z, int i)
    {
        double sumSquare;
        sumSquare = x * x + y * y + z * z;
        double resultantLength = Math.sqrt(sumSquare);
        System.out.println("Sum = " + sumSquare);
        System.out.println("Resultant " + resultantLength);
        Vec4 meanLocation = new Vec4(x / resultantLength, y / resultantLength, z / resultantLength);
        System.out.println("MeanLocation" + meanLocation);
        double dispersion = 1.0 - resultantLength / i;
        System.out.println("dispersionUnit = " + dispersion);
        double rs = Cons.getGlobe().getRadius();
        System.out.println("dispersion = " + dispersion * rs);
    }
}
