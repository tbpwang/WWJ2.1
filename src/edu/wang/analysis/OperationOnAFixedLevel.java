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
 * @Time: 2021/2/26 13:46
 * @Param: read TXT and calculate a fixed level, some like ReadingTXTVec4WithID
 * TXT: ID v1.x v1.y v1.z v2.x v2.y	v2.z v3.x v3.y v3.z
 */
public class OperationOnAFixedLevel
{
    public static void main(String[] args)
    {
        int level = 8;
        String readFilePath = "D:\\outData\\QTMlevel8\\vec4Vertices.txt";
        operate(readFilePath,level);
    }

    private static void operate(String readFilePath, int level)
    {
        int maxLine = (int) Math.pow(4, level);

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String suffix = dateFormat.format(date);
        String outFilePath = "QTM" + "Level" + level + "_" + suffix;
        String outMetaData = "ID" + "\t\t" + "SurfaceArea" + "\t\t"
            + "PlaneArea" + "\t\t"
            + "cellEdgeLength" + "\t\t" + "PlaneEdgeLength" + "\t\t"
            + "similarity[3]";
        IO.write(outFilePath, "metaData", outMetaData);

        StringBuilder outData;
        QTMTriangle triangle;
        Vec4 top, left, right;
        for (int i = 0; i < maxLine; i++)
        {
            String[] temp = IO.readVec4TxtWithIDLineByNo(readFilePath, i);
//            Vec4 top, left, right;
            top = new Vec4(IO.check(Double.valueOf(temp[1]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[2]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[3]), Const.HALF_PRECISION));
            left = new Vec4(IO.check(Double.valueOf(temp[4]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[5]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[6]), Const.HALF_PRECISION));
            right = new Vec4(IO.check(Double.valueOf(temp[7]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[8]), Const.HALF_PRECISION),
                IO.check(Double.valueOf(temp[9]), Const.HALF_PRECISION));

            triangle = new QTMTriangle(IO.vec4ToLatLon(top), IO.vec4ToLatLon(left), IO.vec4ToLatLon(right), temp[0]);

            outData = new StringBuilder();
            outData.append(temp[0]).append("\t");

            outData.append(IO.check(triangle.calculateCellArea(true), Const.PRECISION)).append("\t");

            outData.append(IO.check(Area.planeTriangleArea(triangle.getTop(), triangle.getLeft(), triangle.getRight()),
                Const.PRECISION)).append("\t");

            outData.append(
                IO.check(Length.calculateArcLength(AVKey.RHUMB_LINE, triangle.getLeft(), triangle.getRight()),
                    Const.HALF_PRECISION)).append("\t");
            outData.append(
                IO.check(Length.calculateArcLength(AVKey.GREAT_CIRCLE, triangle.getRight(), triangle.getTop()),
                    Const.HALF_PRECISION)).append("\t");
            outData.append(
                IO.check(Length.calculateArcLength(AVKey.GREAT_CIRCLE, triangle.getTop(), triangle.getLeft()),
                    Const.HALF_PRECISION)).append("\t");

            outData.append(IO.check(Length.calculateLineLength(triangle.getLeft(), triangle.getRight()),
                Const.HALF_PRECISION)).append("\t");
            outData.append(IO.check(Length.calculateLineLength(triangle.getTop(), triangle.getRight()),
                Const.HALF_PRECISION)).append("\t");
            outData.append(
                IO.check(Length.calculateLineLength(triangle.getLeft(), triangle.getTop()),
                    Const.HALF_PRECISION)).append("\t");
            // Similarity
            LatLon center = triangle.getCenter();
            outData.append(IO.check(Length.calculateLineLength(center, triangle.getTop()),
                Const.HALF_PRECISION)).append("\t");
            outData.append(IO.check(Length.calculateLineLength(center, triangle.getLeft()),
                Const.HALF_PRECISION)).append("\t");
            outData.append(
                IO.check(Length.calculateLineLength(center, triangle.getRight()), Const.HALF_PRECISION));

            IO.write(outFilePath, "Similarity", outData.toString());

        }
        System.gc();
    }
}
