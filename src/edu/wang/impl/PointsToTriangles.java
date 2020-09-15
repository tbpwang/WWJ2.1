/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.io.IO;
import gov.nasa.worldwind.geom.Vec4;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/10/25
 * @description
 */
public class PointsToTriangles
{
    private List<List<Vec4>> rowColumnList;
    private int level;

    public PointsToTriangles(List<Vec4> vecPoints, int level)
    {
        if (vecPoints == null)
        {
            String msg = Logging.getMessage("nullValue.点列为空");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        int length = vecPoints.size();
        int maxRow = (int) (Math.pow(2, level) + 1);
        if (length != maxRow * (maxRow + 1) / 2)
        {
            String msg = Logging.getMessage("missValue.点数与层数不对应");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        this.level = level;
        rowColumnList = new ArrayList<>(maxRow);

        for (int i = 0; i < maxRow; i++)
        {
            int maxCol = maxRow - i;
            List<Vec4> columns = new ArrayList<>(maxCol);
            int from = i * maxRow - i * (i - 1) / 2;
            int to = from + maxCol;
            columns.addAll(vecPoints.subList(from, to));
            rowColumnList.add(columns);
        }
    }

    public List<List<Vec4>> getRowColumnList()
    {
        return rowColumnList;
    }

    public String pointToPointDistance()
    {
        // double distance
        // 格式：数值"\t"
        List<List<Vec4>> rowColList = getRowColumnList();
        StringBuilder distances = new StringBuilder();

        for (int i = 0; i < rowColList.size(); i++)
        {
            List<Vec4> row = rowColList.get(i);
            for (int j = 0; j < row.size(); j++)
            {
                // thisRow = i;
                int nextRow = i + 1;
                // thisCol = j;
                int lastCol = j - 1;
                int nextCol = j + 1;
                if (cover(i, nextCol))
                {
                    distances.append(IO.formatDouble(row.get(j).distanceTo3(row.get(nextCol)), 6)).append(
                        System.lineSeparator());
                }
                if (cover(nextRow, lastCol))
                {
                    distances.append(
                        IO.formatDouble(row.get(j).distanceTo3(rowColList.get(nextRow).get(lastCol)), 6)).append(
                        System.lineSeparator());
                }
                if (cover(nextRow, j))
                {
                    distances.append(IO.formatDouble(row.get(j).distanceTo3(rowColList.get(nextRow).get(j)), 6)).append(
                        System.lineSeparator());
                }
            }
        }

        return distances.toString();
    }

    private boolean cover(int i, int j)
    {
        int maxRow = (int) Math.pow(2, level) + 1;
        int maxCol = maxRow - i;
        if (i < 0 || i >= maxRow)
        {
            return false;
        }
        else
        {
            return j >= 0 && j < maxCol;
        }
    }

    public static void main(String[] args)
    {
        String folder = "D:\\outData\\SQT\\";
        String fileName = "spPoint_";//spPoint_1.txt
        // List<List<Vec4>> rowColList;
        for (int i = 1; i <= 1; i++)
        {
            //fileName = fileName + i + ".txt";
            List<Vec4> vertices = IO.parsePointsToVec(folder + fileName + i + ".txt");
            PointsToTriangles pointsToTriangles = new PointsToTriangles(vertices, i);
            // rowColList = pointsToTriangles.getRowColumnList();

            String distances = pointsToTriangles.pointToPointDistance();

            // IO.write("SQT", "distance_" + i, distances);
        }
    }
}
