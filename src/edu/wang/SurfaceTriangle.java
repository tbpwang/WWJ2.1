/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import edu.wang.io.*;
import edu.wang.model.Length;
import gov.nasa.worldwind.avlist.AVKey;
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
    public SurfaceTriangle(LatLon top, LatLon left, LatLon right, String id)
    {
        this(top, left, right, new Geocode(id));
    }

    public SurfaceTriangle(LatLon top, LatLon left, LatLon right, Geocode geocode)
    {
        super(top, left, right, geocode);
    }

    public boolean isUp()
    {
        String id = getGeocode().getID();
        if (!id.equals(""))
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

    public List<Double> edgeLengths()
    {
        List<Double> list = new ArrayList<>();
        for (int i = 0; i < edgeLengthsInUnit().size(); i++)
        {
            list.add(edgeLengthsInUnit().get(i) * Const.RADIUS);
        }
        return list;
    }

    public List<Double> edgeLengthsInUnit()
    {
        List<Double> edges = new ArrayList<>();

        edges.add(Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, getLeft(), getRight()));// a
        edges.add(Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, getTop(), getRight()));// b
        edges.add(Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, getLeft(), getTop()));// c
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
        double a = Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, B, C);
        double b = Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, A, C);
        double c = Length.calculateArcLengthInUnit(AVKey.GREAT_CIRCLE, B, A);
        return computeAngleA(a, b, c);
    }

    private static Angle computeAngleA(double aEdgeRadian, double bEdgeRadian, double cEdgeRadian)
    {
        // 返回顶角A
        // 单位球面边长a、b、c
        // 边的余弦公式 cosa = cosb*cosc+sinb*sinc*cosA
        if (aEdgeRadian <= Const.EPSILON || bEdgeRadian <= Const.EPSILON || cEdgeRadian <= Const.EPSILON)
        {
            return Angle.ZERO;
        }

        double angle = Math.acos((Math.cos(aEdgeRadian) - Math.cos(bEdgeRadian) * Math.cos(cEdgeRadian)) / Math.sin(
            bEdgeRadian) / Math.sin(cEdgeRadian));
        if (angle > Math.PI / 2)
            angle = Math.PI - angle;
        return Angle.fromRadians(angle);
    }

    public List<Angle> innerAngle()
    {
        List<Double> edges = edgeLengthsInUnit();
        List<Angle> angles = new ArrayList<>();

        double a, b, c;
        Angle A, B, C;

        a = edges.get(0);
        b = edges.get(1);
        c = edges.get(2);

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
        A = innerAngle().get(0).degrees;
        B = innerAngle().get(1).degrees;
        C = innerAngle().get(2).degrees;

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
        return unitArea * Const.RADIUS * Const.RADIUS;
    }

    public double getUnitArea()
    {
        List<Angle> angles = innerAngle();
        return (angles.get(0).radians + angles.get(1).radians + angles.get(2).radians - Math.PI);

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

//    public LatLon getMidpoint(LatLon p1, LatLon p2, String aVKeyType)
//    {
//        LatLon mid;
//        switch (aVKeyType)
//        {
//            case AVKey.GREAT_CIRCLE:
//                mid = LatLon.interpolateGreatCircle(0.5, p1, p2);
//                break;
//            case AVKey.RHUMB_LINE:
//            case AVKey.LOXODROME:
//                mid = LatLon.interpolateRhumb(0.5, p1, p2);
//                break;
//            case AVKey.LINEAR:
//                mid = LatLon.interpolate(AVKey.LINEAR, 0.5, p1, p2);
//                break;
//            default:
//                mid = null;
//        }
//        return mid;
//    }

    public SurfaceTriangle[] refine(String aVKeyTypeBottom, String aVKeyTypeLeft, String aVKeyTypeRight)
    {
        LatLon A = getTop();
        LatLon B = getLeft();
        LatLon C = getRight();

//        LatLon a = LatLon.interpolateGreatCircle(0.5, B, C);
        LatLon a = getMidpoint(B, C, aVKeyTypeBottom);
        LatLon b = getMidpoint(A, C, aVKeyTypeRight);
        LatLon c = getMidpoint(A, B, aVKeyTypeLeft);

//        String ID = getID();
        String ID = getGeocode().getID();
        SurfaceTriangle[] triangles = new SurfaceTriangle[4];

        triangles[0] = new SurfaceTriangle(a, c, b, ID + "0");
        triangles[1] = new SurfaceTriangle(A, c, b, ID + "1");
        triangles[2] = new SurfaceTriangle(c, B, a, ID + "2");
        triangles[3] = new SurfaceTriangle(b, a, C, ID + "3");

        return triangles;
    }

    @Override
    public SurfaceTriangle[] refine()
    {
        return refine(AVKey.GREAT_CIRCLE, AVKey.GREAT_CIRCLE, AVKey.GREAT_CIRCLE);
    }

    @Override
    public Path[] renderPath()
    {
        // TODO
        return null;
    }
}
