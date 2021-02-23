/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.io.IO;
import edu.wang.model.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/22 22:20
 * @Param:
 */
public class QTMTriangleReadingTxtVec4sAndSubdivision
{
    public static void main(String[] args)
    {
        StringBuilder outMetaData = new StringBuilder();
        outMetaData.append("ID").append(System.lineSeparator()).append("SurfaceArea").append("\t\t").append(
            "PlaneArea").append(System.lineSeparator());
        outMetaData.append("cellEdgeLength").append("\t\t").append("PlaneEdgeLength").append(System.lineSeparator());

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");


        String prefixPath = "D:\\outData\\QTMlevel8_202102222251";
        String filePath = prefixPath + "\\vec4Vertices.txt";
        int from = 8;
        int maxLine = (int) Math.pow(4, from);
        int subLevel = 3;

        StringBuilder outData;
        for (int i = 0; i < maxLine; i++)
        {
            outData = new StringBuilder();
            System.gc();
            String[] temp = IO.readVec4TxtLineWithID(filePath, i);
            QTMTriangle triangle = init(temp);

            String outFilePath = "QTM" + temp[0] + "_" + dateFormat.format(date);
            IO.write(outFilePath, "metaData", outMetaData.toString());
            for (int j = 0; j < subLevel; j++)
            {
                int outLevel = from + j + 1;
                List<QTMTriangle> subTriangle = QTMTriangleSubdivisionAndSavingAsVec4s.getQTMTriangels(subLevel,
                    triangle);

                for (QTMTriangle tri :
                    subTriangle)
                {
                    outData.append(tri.getGeocode().getID()).append("\t\t");
                    outData.append(tri.calculateCellArea(true)).append("\t\t");
                    outData.append(Area.planeTriangleArea(tri.getTop(), tri.getLeft(), tri.getRight())).append("\t\t");
                    outData.append(Length.calculateArcLength(AVKey.RHUMB_LINE, tri.getLeft(), tri.getRight())).append(
                        "\t");
                    outData.append(Length.calculateArcLength(AVKey.GREAT_CIRCLE, tri.getRight(), tri.getTop())).append(
                        "\t");
                    outData.append(Length.calculateArcLength(AVKey.GREAT_CIRCLE, tri.getTop(), tri.getLeft())).append(
                        "\t\t");
                    outData.append(Length.calculateLineLength(tri.getLeft(), tri.getRight())).append("\t");
                    outData.append(Length.calculateLineLength(tri.getTop(), tri.getRight())).append("\t");
                    outData.append(Length.calculateLineLength(tri.getLeft(), tri.getTop())).append(
                        System.lineSeparator());
                }
                IO.write(outFilePath, temp[0] + "_" + outLevel, outData.toString());
            }
        }
    }

    private static QTMTriangle init(String[] vec4WithID)
    {
        String ID = vec4WithID[0];
        List<Vec4> vertices = new ArrayList<>();
        for (int j = 1; j < vec4WithID.length; j = j + 3)
        {
            double x, y, z;
            x = Double.parseDouble(vec4WithID[j]);
            y = Double.parseDouble(vec4WithID[j + 1]);
            z = Double.parseDouble(vec4WithID[j + 2]);
            vertices.add(new Vec4(x, y, z));
        }
        if (vertices.size() != 3)
            System.out.println("class: QTMTriangleReadingTxtVec4sAndSubdivision.Error");

        LatLon top = IO.vec4ToLatLon(vertices.get(0));
        LatLon left = IO.vec4ToLatLon(vertices.get(1));
        LatLon right = IO.vec4ToLatLon(vertices.get(2));

        return new QTMTriangle(top, left, right, ID);
    }
}
