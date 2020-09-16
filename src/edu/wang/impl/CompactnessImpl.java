/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.LatLon;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/9/22
 * @description plane compactness, sphere compactness, shape distortion = planeCompactness/sphereCompactness
 */
public class CompactnessImpl
{
    public static void main(String[] args)
    {
        IcosahedronInscribed instance = IcosahedronInscribed.getInstance();
        int facesNumber = 20; // 8
//        OctahedronInscribed instance = OctahedronInscribed.getInstance();
//        int facesNumber = 8; // 20
        LatLon a, b, c;
        double baseSphericalFacetArea = 4 * Math.PI * Math.pow(Const.RADIUS, 2) / facesNumber;
        // perimeter: planar and spherical perimeter
        List<List<Double>> perimeter = new ArrayList<>();
        List<Double> perimeterPlSp;
        // area: planar and spherical area
        List<List<Double>> area = new ArrayList<>();
        List<Double> areaPlSp;
        // compactness Or Shape Distortion
        List<List<Double>> distortion = new ArrayList<>();
        List<Double> distortionPlSp;
//        List<Double> shapeDistortion;
        double epsilon = 1e10 * facesNumber;
        double delta;
        int facet = 0;
        List<Double> show = new ArrayList<>();
        for (int i = 0; i < facesNumber; i++)
        {
            a = IO.vec4ToLatLon(instance.getFacets()[i].getA());
            b = IO.vec4ToLatLon(instance.getFacets()[i].getB());
            c = IO.vec4ToLatLon(instance.getFacets()[i].getC());
            perimeterPlSp = new ArrayList<>(2);
            perimeterPlSp.add(Perimeter.trianglePerimeter(instance.getFacets()[i]));
            perimeterPlSp.add(Perimeter.sphericalTrianglePerimeter(a, b, c));
            perimeter.add(perimeterPlSp);
            areaPlSp = new ArrayList<>(2);
            areaPlSp.add(Area.planeTriangleArea(instance.getFacets()[i]));
            areaPlSp.add(Area.unitSphereSurfaceTriangleArea(a, b, c)*Math.pow(Const.RADIUS,2));
            area.add(areaPlSp);

            delta = Math.abs(areaPlSp.get(1) - baseSphericalFacetArea);
            show.add(delta);
            if (delta < epsilon)
            {
                epsilon = delta;
                facet = i;
            }

            // distortion
            distortionPlSp = new ArrayList<>(3);
            double disPl = 4 * Math.PI * areaPlSp.get(0) / Math.pow(perimeterPlSp.get(0), 2);
            distortionPlSp.add(disPl);
            double aSp = areaPlSp.get(1);
            double pSp2 = Math.pow(perimeterPlSp.get(1), 2);
            double r2 = Math.pow(Const.RADIUS, 2);
            double disSp = (4 * Math.PI * r2 * aSp - Math.pow(aSp, 2)) / (r2 * pSp2);
            distortionPlSp.add(disSp);
            distortionPlSp.add(disPl / disSp);
            distortion.add(distortionPlSp);
        }
        System.out.println("AverageSphereArea = " + baseSphericalFacetArea);
//        System.out.println("Perimeter = " + 3 * Math.sqrt(2) * Constant.radius);
        System.out.print(facet);
        System.out.println(", spAreaDelta = " + epsilon);
        System.out.println("Area        " + area.get(facet));
        System.out.println("Perimeter   " + perimeter.get(facet));
        System.out.println("++++++++++++++++++++++++++");

//        System.out.println("Area: " + area);
//        System.out.println("Peri: " + perimeter);
//        System.out.println("Dist: " + distortion);
        for (int i = 0; i < facesNumber; i++)
        {
            System.out.println("AreaDelta = " + show.get(i));
//            System.out.println("A: " + area.get(i));
//            System.out.println("P: " + perimeter.get(i));
//            System.out.println("D: " + distortion.get(i));
        }
        for (int i = 0; i < facesNumber; i++)
        {
//            System.out.println("AreaDelta = " + show);
            System.out.println("A: " + area.get(i));
//            System.out.println("P: " + perimeter.get(i));
//            System.out.println("D: " + distortion.get(i));
        }
        for (int i = 0; i < facesNumber; i++)
        {
//            System.out.println("AreaDelta = " + show);
//            System.out.println("A: " + area.get(i));
            System.out.println("P: " + perimeter.get(i));
//            System.out.println("D: " + distortion.get(i));
        }
        for (int i = 0; i < facesNumber; i++)
        {
//            System.out.println("AreaDelta = " + show);
//            System.out.println("A: " + area.get(i));
//            System.out.println("P: " + perimeter.get(i));
            System.out.println("D: " + distortion.get(i));
        }
    }
}
