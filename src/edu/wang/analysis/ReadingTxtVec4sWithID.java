/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/22 22:20
 * @Param: QTMTriangleReadingTxtVec4sWithID
 */
public class ReadingTxtVec4sWithID
{
    public static void main(String[] args)
    {
        StringBuilder outMetaData = new StringBuilder();
        outMetaData.append("ID").append("\t\t").append("SurfaceArea").append("\t\t").append(
            "PlaneArea").append("\t\t");
        outMetaData.append("cellEdgeLength").append("\t\t").append("PlaneEdgeLength").append("\t\t");
        outMetaData.append("similarity[3]");

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String suffix = dateFormat.format(date);

        String prefixPath = "D:\\outData\\QTMlevel8";
        String filePath = prefixPath + "\\vec4Vertices.txt";
        int from = 8;
        int maxLine = (int) Math.pow(4, from);
        int subLevel = 4;

        StringBuilder outData;
        for (int i = 0; i < maxLine; i++)
        {
//            outData = new StringBuilder();
            System.gc();
            String[] temp = IO.readVec4TxtWithIDLineByNo(filePath, i);
            QTMTriangle triangle = init(temp);
//            String outFilePath;

            for (int j = 3; j < subLevel; j++)
            {
                int outLevel = j + 1;
                String outFilePath = "QTM" + "Level" + (from + outLevel) + "_" + suffix;
                if (i == 0)
                {
                    IO.write(outFilePath, "metaData", outMetaData.toString());
                }

                List<QTMTriangle> subTriangle = SavingAsVec4sWithID.refineQTMTriangels(outLevel,
                    triangle);

                outData = new StringBuilder();
                for (QTMTriangle tri :
                    subTriangle)
                {
                    if (!outData.toString().isEmpty())
                        outData.append(System.lineSeparator());

                    outData.append(tri.getGeocode().getID()).append("\t");

                    outData.append(IO.check(tri.calculateCellArea(true), Const.PRECISION)).append("\t");

                    outData.append(IO.check(Area.planeTriangleArea(tri.getTop(), tri.getLeft(), tri.getRight()),
                        Const.PRECISION)).append("\t");

                    outData.append(IO.check(Length.calculateArcLength(AVKey.RHUMB_LINE, tri.getLeft(), tri.getRight()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(IO.check(Length.calculateArcLength(AVKey.GREAT_CIRCLE, tri.getRight(), tri.getTop()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(IO.check(Length.calculateArcLength(AVKey.GREAT_CIRCLE, tri.getTop(), tri.getLeft()),
                        Const.HALF_PRECISION)).append("\t");

                    outData.append(IO.check(Length.calculateLineLength(tri.getLeft(), tri.getRight()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(IO.check(Length.calculateLineLength(tri.getTop(), tri.getRight()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(
                        IO.check(Length.calculateLineLength(tri.getLeft(), tri.getTop()), Const.HALF_PRECISION)).append(
                        "\t");
                    // shape
                    LatLon center = tri.getCenter();
                    outData.append(IO.check(Length.calculateLineLength(center, tri.getTop()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(IO.check(Length.calculateLineLength(center, tri.getLeft()),
                        Const.HALF_PRECISION)).append("\t");
                    outData.append(
                        IO.check(Length.calculateLineLength(center, tri.getRight()), Const.HALF_PRECISION));
                }
//                IO.write(outFilePath, "subLevel" + outLevel, outData.toString());
                IO.write(outFilePath, temp[0] + "subLevel" + outLevel, outData.toString());
//                    outData.deleteCharAt(outData.length() - 2).toString());
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
            System.out.println("class: ReadingTxtVec4sWithID.Error");

        LatLon top = IO.vec4ToLatLon(vertices.get(0));
        LatLon left = IO.vec4ToLatLon(vertices.get(1));
        LatLon right = IO.vec4ToLatLon(vertices.get(2));

        return new QTMTriangle(top, left, right, ID);
    }
}
