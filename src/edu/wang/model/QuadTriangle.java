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

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/8/30
 * @description
 */
public class QuadTriangle
{
    //    private ChainPoint middlePoint;
    private List<Vec4> points;
    private List<Triangle> facets;
    private int level;
    private boolean isSubdivision;

    public QuadTriangle(Triangle triangle, int level)
    {
        if (triangle == null)
        {
            String message = Logging.getMessage("NullError.初始三角形不能为空值");
            Logging.logger().severe(message);

        }
        facets = new ArrayList<>(Collections.singletonList(triangle));
        points = new ArrayList<>();
        points.add(triangle.getA());
        points.add(triangle.getB());
        points.add(triangle.getC());
        this.level = Math.max(level, 0);
        isSubdivision = false;
    }

    public List<Vec4> getPoints()
    {
        if (!isSubdivision)
        {
            subdivide();
        }
        return points;
    }

    public List<Triangle> getFacets()
    {
        if (!isSubdivision)
        {
            subdivide();
        }
        return facets;
    }

    private void subdivide()
    {
        if (isSubdivision)
        {
            return;
        }
        String pathType = AVKey.GREAT_CIRCLE;
//        List<Triangle> triangleList = new ArrayList<>();
        List<Triangle> temp = new ArrayList<>();
//        List<Vec4> vec4List = new ArrayList<>();

//        triangleList.add(triangle);

//        Vec4 vA = triangleList.get(0).getA(), vB = triangleList.get(0).getB(), vC = triangleList.get(0).getC();
        Vec4 vA, vB, vC;
//        vec4List.add(vA);
//        vec4List.add(vB);
//        vec4List.add(vC);
        LatLon lA, lB, lC;
        Vec4 vASub, vBSub, vCSub;
        LatLon lASub, lBSub, lCSub;
        Set set = new HashSet();
        set.add(points.get(0));
        set.add(points.get(1));
        set.add(points.get(2));
        for (int i = 0; i < level; i++)
        {
            for (Triangle t :
                facets)
            {
                vA = t.getA();
                vB = t.getB();
                vC = t.getC();
                lA = IO.vec4ToLatLon(vA);
                lB = IO.vec4ToLatLon(vB);
                lC = IO.vec4ToLatLon(vC);
                lASub = LatLon.interpolate(pathType, 0.5, lB, lC);
                lBSub = LatLon.interpolate(pathType, 0.5, lA, lC);
                lCSub = LatLon.interpolate(pathType, 0.5, lA, lB);
                vASub = IO.check(IO.latLonToVec4(lASub));
                vBSub = IO.check(IO.latLonToVec4(lBSub));
                vCSub = IO.check(IO.latLonToVec4(lCSub));
                if (set.add(vASub))
                {
                    points.add(vASub);
                }
                if (set.add(vBSub))
                {
                    points.add(vBSub);
                }
                if (set.add(vCSub))
                {
                    points.add(vCSub);
                }

                temp.add(new Triangle(vASub, vBSub, vCSub));
                temp.add(new Triangle(vA, vCSub, vBSub));
                temp.add(new Triangle(vB, vCSub, vASub));
                temp.add(new Triangle(vC, vBSub, vASub));
            }
            facets.clear();
            facets.addAll(temp);
            temp.clear();
        }
        isSubdivision = true;
    }
}
