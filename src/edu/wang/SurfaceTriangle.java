/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Path;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/5/16
 * @description
 * @parameter
 */
public class SurfaceTriangle extends Cell
{
    private String id;

    public SurfaceTriangle(LatLon top, LatLon left, LatLon right, String id)
    {
        super(top, left, right, id);
        this.id = id;
    }

    public boolean isUp()
    {
        if (!getId().equals(""))
        {
            return this.getGeocode().isUp();
        }
        else
        {
            double bottom1 = getRight().latitude.degrees;
            double bottom2 = getLeft().latitude.degrees;
            double top = getTop().latitude.degrees;
            if (top >= 0)
            {
                return top > bottom1 && top > bottom2;
            }
            else
            {
                return top < bottom1 && top < bottom2;
            }
        }
    }

    public LatLon getTop()
    {
        return getGeoVertices().get(0);
    }

    public LatLon getLeft()
    {
        return getGeoVertices().get(1);
    }

    public LatLon getRight()
    {
        return getGeoVertices().get(2);
    }

    public String getId()
    {
        return id;
    }

    public List<Double> edgeLengths()
    {
        List<Double> edges = new ArrayList<>();
        edges.add(LatLon.greatCircleDistance(getLeft(), getRight()).radians * Cons.RADIUS);// a
        edges.add(LatLon.greatCircleDistance(getTop(), getRight()).radians * Cons.RADIUS);// b
        edges.add(LatLon.greatCircleDistance(getLeft(), getTop()).radians * Cons.RADIUS);// c
        return edges;
    }

    public double maxEdge()
    {
        double a = edgeLengths().get(0);
        double b = edgeLengths().get(1);
        double c = edgeLengths().get(2);
        return Math.max(a, Math.max(b, c));
    }

    public double edgeStandardDeviation()
    {
        double edge1, edge2, edge3, sqrEdge1, sqrEdge2, sqrEdge3, avg, stdd;
        edge1 = edgeLengths().get(0);
        edge2 = edgeLengths().get(1);
        edge3 = edgeLengths().get(2);
        avg = (edge1 + edge2 + edge3) / 3.0;
        sqrEdge1 = Math.pow(edge1 - avg, 2);
        sqrEdge2 = Math.pow(edge2 - avg, 2);
        sqrEdge3 = Math.pow(edge3 - avg, 2);
        stdd = Math.sqrt((sqrEdge1 + sqrEdge2 + sqrEdge3) / 3.0);
        return stdd;
    }

//    public static Angle computeAngleA(LatLon latLon1, LatLon latLon2, LatLon latLon3)
//    {
//        // 返回顶角A
//        // 单位球面边长a、b、c
//        // 边的余弦公式 cosa = cosb*cosc+sinb*sinc*cosA
//        double a = LatLon.greatCircleDistance(latLon2, latLon3).radians;
//        double b = LatLon.greatCircleDistance(latLon1, latLon3).radians;
//        double c = LatLon.greatCircleDistance(latLon1, latLon2).radians;
//        return computeAngleA(a, b, c);
//    }

    public static Angle computeAngleA(LatLon A, LatLon B, LatLon C)
    {
        // 返回顶角A
        // 单位球面边长a、b、c
        // 边的余弦公式 cosa = cosb*cosc+sinb*sinc*cosA
        double a = LatLon.greatCircleDistance(B,C).radians;
        double b = LatLon.greatCircleDistance(A,C).radians;
        double c = LatLon.greatCircleDistance(B,A).radians;
        return computeAngleA(a,b,c);
    }
    public static Angle computeAngleA(double aEdgeRadian, double bEdgeRadian, double cEdgeRadian)
    {
        // 返回顶角A
        // 单位球面边长a、b、c
        // 边的余弦公式 cosa = cosb*cosc+sinb*sinc*cosA
        if (aEdgeRadian <= Cons.EPSILON || bEdgeRadian <= Cons.EPSILON || cEdgeRadian <= Cons.EPSILON)
        {
            return null;
        }
        double angle = Math.acos((Math.cos(aEdgeRadian) - Math.cos(bEdgeRadian) * Math.cos(cEdgeRadian)) / Math.sin(
            bEdgeRadian) / Math.sin(cEdgeRadian));
        return Angle.fromRadians(angle);
    }

    public List<Angle> interiorAngle()
    {
        List<Double> edges = edgeLengths();
        List<Angle> angles = new ArrayList<>();
        double a, b, c;
        Angle A, B, C;
        a = edges.get(0) / Cons.RADIUS;
        b = edges.get(1) / Cons.RADIUS;
        c = edges.get(2) / Cons.RADIUS;
        // 边的余弦公式 cosa = cosb*cosc+sinb*sinc*cosA
//        if (a <= Cons.EPSILON || b <= Cons.EPSILON || c <= Cons.EPSILON)
//        {
//            return null;
//        }
//        A = Math.acos((Math.cos(a) - Math.cos(b) * Math.cos(c)) / (Math.sin(b) * Math.sin(c)));
//        B = Math.acos((Math.cos(b) - Math.cos(a) * Math.cos(c)) / (Math.sin(a) * Math.sin(c)));
//        C = Math.acos((Math.cos(c) - Math.cos(b) * Math.cos(a)) / (Math.sin(b) * Math.sin(a)));
        A = computeAngleA(a, b, c);
        B = computeAngleA(b, a, c);
        C = computeAngleA(c, a, b);

        angles.add(A);
        angles.add(B);
        angles.add(C);
        return angles;
    }

    public double angleStandardDeviation()
    {
        double A, B, C, avg, AA, BB, CC, stdd;
        A = interiorAngle().get(0).degrees;
        B = interiorAngle().get(1).degrees;
        C = interiorAngle().get(2).degrees;

        avg = (A + B + C) / 3.0;
        AA = Math.pow(A - avg, 2);
        BB = Math.pow(B - avg, 2);
        CC = Math.pow(C - avg, 2);
        stdd = Math.sqrt((AA + BB + CC) / 3.0);
        return IO.check(stdd);
    }

    public double computeArea()
    {
        double unitArea = getUnitArea();
        return unitArea * Cons.RADIUS * Cons.RADIUS;
    }

    public double getUnitArea()
    {
        List<Angle> interiors = interiorAngle();
        return (interiors.get(0).radians + interiors.get(1).radians + interiors.get(2).radians - Math.PI);

//        int length = getGeoVertices().size();
//        double excess = -Math.PI * (length - 1 - 2);
//        LatLon startPoint, midPoint, endPoint;
//        // 临时三角形的周长p、以及角度ABC对应的边abc
//        double p, a, b, c;
//        a= LatLon.greatCircleDistance(getLeft(),getRight()).radians;
//        c= LatLon.greatCircleDistance(getLeft(),getTop()).radians;
//        b= LatLon.greatCircleDistance(getTop(),getRight()).radians;
//        for (int i = 0; i < length - 1; i++)
//        {
//            if (i == 0)
//            {
//                startPoint = getGeoVertices().get(length - 2);
//                midPoint = getGeoVertices().get(0);
//                endPoint = getGeoVertices().get(1);
//            }
//            else
//            {
//                startPoint = getGeoVertices().get(i - 1);
//                midPoint = getGeoVertices().get(i);
//                endPoint = getGeoVertices().get(i + 1);
//            }
//            c = LatLon.greatCircleDistance(startPoint, midPoint).radians;
//            b = LatLon.greatCircleDistance(endPoint, midPoint).radians;
//            a = LatLon.greatCircleDistance(startPoint, endPoint).radians;
//            p = (a + b + c) / 2;
//            excess += 2 * Math.asin(Math.sqrt(Math.sin(p - c) * Math.sin(p - b) / Math.sin(c) / Math.sin(b)));
//        }
//        return excess;
    }

    @Override
    public Cell[] refine()
    {
        return null;
    }

    @Override
    public Path[] renderPath()
    {
        return null;
    }
}
