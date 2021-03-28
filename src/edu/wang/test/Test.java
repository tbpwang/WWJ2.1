/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.test;

import edu.wang.*;
import edu.wang.io.*;
import edu.wang.model.*;
import edu.wang.model.Area;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.render.Polygon;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/12/23
 * @description
 */
public class Test
{
    public static void main(String[] args)
    {

        String version = "QTMVariableAnalysis";

        int maxLevel = 10;

        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
//        String prefix = "QTM";
        String prefix = "SQT";
        String folder = prefix + dateFormat.format(date);
        String pointName = "point";

        // metadata
        String metadata = version + "\t\t" + prefix + ".txt\t" + System.lineSeparator()
            + "Column1ID\t" + "triangle.getGeocode().getID())" + System.lineSeparator()

            + "Column2Sphere面积" + "\t" + "triangle.calculateCellArea(true)" + System.lineSeparator()
            + "Column3Plane面积" + "\t" + "Area.planeTriangleArea()" + System.lineSeparator()

            + "Column5边长(3)" + "\t" + "triangle.edgeLengths().get(i)" + System.lineSeparator()
            + "Column6边长(3)" + "\t" + "Length.calculateLineLength()" + System.lineSeparator();

        IO.write(folder, "metadata", metadata);

        // calculation by metadata
        String fileName;
        for (int level = 9; level < maxLevel; level++)
        {
            System.gc();
            MidArcTriangleMesh triangleMesh = new MidArcTriangleMesh(level);

            MidArcTriangleMesh mesh = triangleMesh.cutMesh();
            fileName = prefix + "_" + ((SurfaceTriangle) mesh.getCellNodes().get(0).getCell()).getGeocode().getBaseID()
                + "_" + level;

            StringBuilder cellContents = new StringBuilder();

            List<Mesh.CellNode> cellNodeList = mesh.getCellNodes();
            for (Mesh.CellNode nodeCell : cellNodeList)
            {
                MiddleArcSurfaceTriangle nodeTriangle = (MiddleArcSurfaceTriangle) nodeCell.getCell();

                double area, plArea;
                double edge1, edge2, edge3;
                double pla, plb, plc;
                // column 1 id
                cellContents.append(nodeTriangle.getGeocode().getID()).append("\t\t");
                // column 2 qtmArea
//                area = nodeTriangle.calculateCellArea(true);
                area = Area.sphericalTriangleArea(nodeTriangle.getTop(), nodeTriangle.getLeft(),
                    nodeTriangle.getRight());

//                cellContents.append("L2\t").append(area).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(area, Const.PRECISION)).append("\t\t");

                // column 3 plArea
                plArea = Area.planeTriangleArea(nodeTriangle.getTop(), nodeTriangle.getLeft(), nodeTriangle.getRight());
                cellContents.append(IO.formatDouble(plArea, Const.PRECISION)).append("\t\t");
                // column 5 edge
                //
                // 根据不同的剖分方法，使用不同的计算方法
                edge1 = nodeTriangle.edgeLengths().get(0);
                edge2 = nodeTriangle.edgeLengths().get(1);
                edge3 = nodeTriangle.edgeLengths().get(2);
                //perimeter = edge1 + edge2 + edge3;
//                cellContents.append("L3\t").append(edge1).append("\t");
                cellContents.append(IO.formatDouble(edge1)).append("\t");
                cellContents.append(IO.formatDouble(edge2)).append("\t");
//                cellContents.append(edge3).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(edge3)).append("\t\t");
                //cellContents.append(IO.formatDouble(perimeter)).append("\t");

                // column 6 lineLength(plEdge)

                pla = Length.calculateLineLength(nodeTriangle.getLeft(), nodeTriangle.getRight());
                plb = Length.calculateLineLength(nodeTriangle.getTop(), nodeTriangle.getRight());
                plc = Length.calculateLineLength(nodeTriangle.getLeft(), nodeTriangle.getTop());
                cellContents.append(IO.formatDouble(pla)).append("\t");
                cellContents.append(IO.formatDouble(plb)).append("\t");
                cellContents.append(IO.formatDouble(plc)).append("\t\t");
                cellContents.append(System.lineSeparator());
            }
            // 输出cell
            // 根据时间输出文件夹
            IO.write(folder, fileName, cellContents.toString());
        }
    }
}
