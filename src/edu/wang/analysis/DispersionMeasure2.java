/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.impl.*;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/9/4
 * @description Triangles in the Plane
 */
public class DispersionMeasure2
{
    public static void main(String[] args)
    {
        // 比较每个三角形的dispersion
//        IcosahedronInscribed icosa = IcosahedronInscribed.getInstance();
//        Triangle triangle = icosa.getFacets()[5];
//        String folder = "icosa";
        OctahedronInscribed octa = OctahedronInscribed.getInstance();
        Triangle triangle = octa.getFacetList().get(3);
        String folder = "octa";

        List<Double> dispersion;
        List<Vec4> vec4List;
        SphereStatisticsPoint statistics;

//        i --> level
        for (int i = 10; i <= 10; i++)
        {
            QuadTriangle quadTriangle = new QuadTriangle(triangle, i);
            dispersion = new ArrayList<>();
            for (Triangle t :
                quadTriangle.getFacets())
            {
                vec4List = new ArrayList<>();
                vec4List.add(t.getA());
                vec4List.add(t.getB());
                vec4List.add(t.getC());
//                List<LatLon> temp = new ArrayList<>();
//                temp.add(Constant.vec4ToLatLon(t.getA()));
//                temp.add(Constant.vec4ToLatLon(t.getB()));
//                temp.add(Constant.vec4ToLatLon(t.getC()));
                statistics = new SphereStatisticsPoint(vec4List);
                dispersion.add(statistics.getDispersionInUnit());
//                System.out.println("MassCenter"+ Constant.vec4ToLatLon(statistics.getMassCenter()));
//                System.out.println("Center "+ LatLon.getCenter(temp));
            }

//            IO.write(folder, folder + "Dispersion", "Level = " + i);
            for (Double d : dispersion)
            {
                IO.write(folder, folder + "Dispersion" + i, d.toString());
            }
        }
    }
}
