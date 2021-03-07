/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.io.IO;
import edu.wang.model.QTMTriangle;
import gov.nasa.worldwind.geom.LatLon;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/22 20:17
 * @Param: 把第8层等剖分层单元格写到文本，待以后每次读取重新剖分
 * QTMTriangleSubdivisionAndSavingAsVec4s
 */
public class SavingAsVec4sWithID
{
    private static QTMTriangle initQTMTriangle()
    {
        String ID = "A";
        LatLon top = LatLon.fromDegrees(90, 0);
        LatLon left = LatLon.fromDegrees(0, 0);
        LatLon right = LatLon.fromDegrees(0, 90);
        return initQTMTriangle(top, left, right, ID);
    }

    private static QTMTriangle initQTMTriangle(LatLon top, LatLon left, LatLon right, String ID)
    {
        return new QTMTriangle(top, left, right, ID);
    }

    public static void main(String[] args)
    {
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        int level = 8;
        String prefix = "QTM";
        String folder = prefix + "level" + level + "_" + dateFormat.format(date);
        String pointName = "vec4Vertices";
        QTMTriangle triangle = initQTMTriangle();
        List<QTMTriangle> triangleList = refineQTMTriangels(level, triangle);
        saveAS(triangleList, folder, pointName);
    }

    public static List<QTMTriangle> refineQTMTriangels(int level, QTMTriangle triangle)
    {
        List<QTMTriangle> triangleList = new ArrayList<>();
        triangleList.add(triangle);
        List<QTMTriangle> tempTriangles;
        for (int i = 0; i < level; i++)
        {
            tempTriangles = new ArrayList<>();
            for (QTMTriangle qtm :
                triangleList)
            {
                tempTriangles.addAll(Arrays.asList(qtm.refine()));
            }
            triangleList.clear();
            triangleList.addAll(tempTriangles);
        }
        return triangleList;
    }

    public static void saveAS(List<QTMTriangle> triangleList, String pathName, String fileName)
    {
        for (QTMTriangle qtm :
            triangleList)
        {
            IO.write(pathName, fileName, qtm.coordinatesVec4WithID());
        }
    }
}
