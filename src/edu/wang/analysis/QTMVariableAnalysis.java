/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.*;
import edu.wang.io.*;
import edu.wang.model.Area;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2020编辑 20210311
 * @description 生成QTM计算格网，用于相关性等数据分析
 */

public class QTMVariableAnalysis
{
    public static void main(String[] args)
    {
        String version = "QTMVariableAnalysis";

        int maxLevel = 9;

        // basic data
        int basicShape = 3;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
//        String prefix = "QTM";
        String prefix = "SQT";
        String folder = prefix + dateFormat.format(date);
        String pointName = "point";



        // metadata
        String metadata = version + "\t\t" + prefix + ".txt\t" + "basicShape = " + basicShape + System.lineSeparator()
            + "Column1ID\t" + "triangle.getGeocode().getID())" + System.lineSeparator()

            + "Column2Sphere面积" + "\t" + "triangle.calculateCellArea(true)" + System.lineSeparator()
            + "Column3Plane面积" + "\t" + "Area.planeTriangleArea()" + System.lineSeparator()

            + "Column4角度(3)\t" + "triangle.innerAngle().get(i).degrees" + System.lineSeparator()

            + "Column5边长(3)" + "\t" + "triangle.edgeLengths().get(i)" + System.lineSeparator()
            + "Column6边长(3)" + "\t" + "Length.calculateLineLength()" + System.lineSeparator()

            + "Column7紧度\t" + "sqrt()\t"
            + "math.sqrt(4 * Math.PI * Math.pow(Const.radius, 2.0) * area - Math.pow(area, 2.0)) / (Const.radius * perimeter)"
            + System.lineSeparator()
            + "Column8形状指数\t" + "basicShape(4.559) * Math.sqrt(area)/ perimeter;"
            + System.lineSeparator()
            + "Column9分维数\t" + "2 * Math.log(perimeter) / Math.log(area)"
            + System.lineSeparator()
            + "Column10离散度\t" + System.lineSeparator()
//        metadata.append("(10)面积×离散度2").append("\t");
            + "Column11twoCenter距离(3)\t" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()
            // Neighbor cell的邻接距离
            + "Column12边邻接距离\t(<=9)" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()
            + "Column13角邻接距离\t(3)" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()

            // column 1 id; column 2 qtmArea;column 3 plArea;
            // column 4 innerAngle(3);column 5 edge(3);column 6 lineLength;
            // column7 compactness; column 8 shapeIndex;column 9 dimension;
            // column 10 dispersion;column 11 twoLineCenterDistances(3); column 12+13 nberDistances(<=12)

            // pointName
            + "\t" + pointName + ".txt" + System.lineSeparator()
            + "Column1坐标" + System.lineSeparator()
            + "Column2邻近点方位角" + "\t" + "angle.degrees" + System.lineSeparator()
            + "Column3到邻近点距离" + "\t" + "angle.radians * Const.radius" + System.lineSeparator();
        IO.write(folder, "metadata", metadata);

        // calculation by metadata
        String fileName;
        for (int level = 1; level < maxLevel; level++)
        {
//            System.out.println("ForiLevel\t=\t" + level);
            System.gc();
            MidArcTriangleMesh triangleMesh = new MidArcTriangleMesh(level);
//            QTMTriangleMesh triangleMesh = new QTMTriangleMesh(level);

            MidArcTriangleMesh mesh = triangleMesh.cutMesh();
//            QTMTriangleMesh mesh = triangleMesh.cutMesh();
//            System.out.println(level);
            fileName = prefix + "_" + ((SurfaceTriangle) mesh.getCellNodes().get(0).getCell()).getGeocode().getBaseID()
                + "_" + level;

            StringBuilder cellContents = new StringBuilder();

            // triangle
            // shape = 3
            List<Mesh.CellNode> cellNodeList = mesh.getCellNodes();
            for (Mesh.CellNode nodeCell : cellNodeList)
            {
                MiddleArcSurfaceTriangle nodeTriangle = (MiddleArcSurfaceTriangle) nodeCell.getCell();
//                QTMTriangle nodeTriangle = (QTMTriangle) nodeCell.getCell();

                double area, plArea;
                double edge1, edge2, edge3, perimeter;
                double pla, plb, plc;
                double compactness;//(4*PI*R*R*A-A*A)/(R*R*p*p)
                double shapeIndex;//perimeter/4/sqrt(area)
                double dimension;//2*ln(p/4)/lnA
                double dispersion;//
//                double areaMulDis2;//area*dispersion
//                cellContents.append(triangle.getGeocode().getID()).append(System.lineSeparator());
                // column 1 id
                cellContents.append(nodeTriangle.getGeocode().getID()).append("\t\t");
                // column 2 qtmArea
//                area = nodeTriangle.calculateCellArea(true);
                area = Area.sphericalTriangleArea(nodeTriangle.getTop(),nodeTriangle.getLeft(),nodeTriangle.getRight());

//                cellContents.append("L2\t").append(area).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(area, Const.PRECISION)).append("\t\t");

                // column 3 plArea
                plArea = Area.planeTriangleArea(nodeTriangle.getTop(), nodeTriangle.getLeft(), nodeTriangle.getRight());
                cellContents.append(IO.formatDouble(plArea, Const.PRECISION)).append("\t\t");

                // 内角 column 4 innerAngle
                cellContents.append(IO.formatDouble(nodeTriangle.innerAngle().get(0).degrees, Const.PRECISION)).append(
                    "\t");
                cellContents.append(IO.formatDouble(nodeTriangle.innerAngle().get(1).degrees, Const.PRECISION)).append(
                    "\t");
                cellContents.append(IO.formatDouble(nodeTriangle.innerAngle().get(2).degrees, Const.PRECISION)).append(
                    "\t\t");

                // column 5 edge
                //
                // 根据不同的剖分方法，使用不同的计算方法
                edge1 = nodeTriangle.edgeLengths().get(0);
                edge2 = nodeTriangle.edgeLengths().get(1);
                edge3 = nodeTriangle.edgeLengths().get(2);
                perimeter = edge1 + edge2 + edge3;
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

                // column7 compactness

                // compactness = sqrt(4*PI*A-(A/r)^2)/P;
                compactness = Math.sqrt(4 * Math.PI * area - Math.pow(area / Const.RADIUS, 2)) / perimeter;
//                compactness = Math.sqrt(4 * Math.PI * Math.pow(Const.RADIUS, 2.0) * area - Math.pow(area, 2.0)) / Math.pow(
//                    Const.RADIUS * perimeter, 2.0);
////                cellContents.append("L4\t").append(IO.formatDouble(compactness)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(compactness)).append("\t\t");
//                column 8 shapeIndex = perimeter / shape / Math.sqrt(area);

                shapeIndex = 4.559 * Math.sqrt(area) / perimeter;
//                cellContents.append("L5\t").append(IO.formatDouble(shapeIndex)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(shapeIndex)).append("\t\t");
//                column 9 dimension = 2 * Math.log(perimeter / 4.0) / Math.log(area);

                dimension = 2 * Math.log(perimeter) / Math.log(area);
//                cellContents.append("L6\t").append(IO.formatDouble(dimension)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(dimension)).append("\t\t");

                List<Vec4> vertices = new ArrayList<>();
                vertices.add(IO.check(IO.latLonToVec4(nodeTriangle.getTop())));
                vertices.add(IO.check(IO.latLonToVec4(nodeTriangle.getLeft())));
                vertices.add(IO.check(IO.latLonToVec4(nodeTriangle.getRight())));
                SphereStatisticsPoint sPoints = new SphereStatisticsPoint(vertices);

//                column 10 dispersion = sPoints.getDispersionInUnit();

                dispersion = sPoints.getDispersion();
//                cellContents.append("L7\t").append(IO.formatDouble(dispersion, 6)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(dispersion)).append("\t\t");
//                areaMulDis2 = area * Math.pow(dimension, 2.0);
//                cellContents.append(IO.formatDouble(areaMulDis2, 6)).append("\t");

                // 邻近cell的质心距离
                LatLon triangleCenter = nodeTriangle.getCenter();
                List<Mesh.Neighbor> neighborList = nodeCell.getNeighborList();
                List<Double> nearAngleDistances = new ArrayList<>();
                List<Double> nearEdgeDistances = new ArrayList<>();
                List<Double> nearDistances = new ArrayList<>();
                List<Double> twoLineCenterDistances = new ArrayList<>();
                List<LatLon> edgeCenters = new ArrayList<>();

                edgeCenters.add(LatLon.interpolateGreatCircle(0.5, nodeTriangle.getTop(), nodeTriangle.getLeft()));
                edgeCenters.add(LatLon.interpolateGreatCircle(0.5, nodeTriangle.getTop(), nodeTriangle.getRight()));
                // edgeCenters.add(LatLon.interpolateGreatCircle(0.5, nodeTriangle.getLeft(), nodeTriangle.getRight()));
                edgeCenters.add(LatLon.interpolateRhumb(0.5, nodeTriangle.getLeft(), nodeTriangle.getRight()));

                for (Mesh.Neighbor aNber :
                    neighborList)
                {
                    SurfaceTriangle nberTri = (SurfaceTriangle) aNber.getCellNode().getCell();
                    LatLon nearCenter = nberTri.getCenter();
                    if (aNber.getType() == Const.NEIGHBOR_TYPE_EDGE)
                    {
                        nearEdgeDistances.add(
                            LatLon.greatCircleDistance(triangleCenter, nearCenter).radians * Const.RADIUS);
                        LatLon cellsCenter = LatLon.interpolateGreatCircle(0.5, triangleCenter, nearCenter);
                        double distance0 = LatLon.greatCircleDistance(edgeCenters.get(0), cellsCenter).radians
                            * Const.RADIUS;
                        double distance1 = LatLon.greatCircleDistance(edgeCenters.get(1), cellsCenter).degrees
                            * Const.RADIUS;
                        double distance2 = LatLon.greatCircleDistance(edgeCenters.get(2), cellsCenter).degrees
                            * Const.RADIUS;
                        twoLineCenterDistances.add(Math.min(Math.min(distance0, distance1), distance2));
                    }
                    else if (aNber.getType() == Const.NEIGHBOR_TYPE_VERTEX)
                    {
                        nearAngleDistances.add(
                            LatLon.greatCircleDistance(triangleCenter, nearCenter).radians * Const.RADIUS);
                    }
                    else
                    {
                        nearDistances.add(
                            LatLon.greatCircleDistance(triangleCenter, nearCenter).radians * Const.RADIUS);
                    }
                }
                // column 11 twoLineCenterDistances

//                cellContents.append(IO.check(twoLineCenterDistances.get(0),Const.PRECISION));
//                cellContents.append(IO.check(twoLineCenterDistances.get(1),Const.PRECISION));
//                cellContents.append(IO.check(twoLineCenterDistances.get(2),Const.PRECISION));
                /** 12邻近
                 int length = nberDistances.size();
                 for (int i = 0; i < 12 - length; i++)
                 {
                 nberDistances.add(0.0);
                 }
                 for (int i = 0; i < 12; i++)
                 {
                 cellContents.append(IO.formatDouble(nberDistances.get(i))).append("\t");
                 }
                 **/
                // column 11, column 12 nberDistances

                if (twoLineCenterDistances.size() != 0)
                {
//                    cellContents.append("L10\t");
                    for (Double twoLineCenterDistance : twoLineCenterDistances)
                    {
                        cellContents.append(IO.formatDouble(twoLineCenterDistance)).append("\t");
                    }
                    cellContents.append("\t");
                }
                if (nearEdgeDistances.size() != 0)
                {
//                    cellContents.append("L9\t");
                    for (Double nearEdgeDistance : nearEdgeDistances)
                    {
                        cellContents.append(IO.formatDouble(nearEdgeDistance)).append("\t");
                    }
                    cellContents.append("\t");
                }
                if (nearDistances.size() != 0)
                {
                    cellContents.append("-1:").append("\t");

                    for (Double nearDistance : nearDistances)
                    {
                        cellContents.append(IO.formatDouble(nearDistance)).append("\t");
                    }
                    cellContents.append("\t");
                }

                if (nearAngleDistances.size() != 0)
                {
//                    cellContents.append("L8\t");
                    for (Double nearAngleDistance : nearAngleDistances)
                    {
                        cellContents.append(IO.formatDouble(nearAngleDistance)).append("\t");
                    }
                    cellContents.append(System.lineSeparator());
                }
            }
            // 输出cell
            // 根据时间输出文件夹

//            folder += dateFormat.format(date);
            IO.write(folder, fileName, cellContents.toString());

//            // pointName
//            StringBuilder nodeContents = new StringBuilder();
//            /**
//             nodeContents.append("(1)坐标").append("\t");
//             //            nodeContents.append("(2)邻近夹角t1/度").append("\t");
//             nodeContents.append("(2)方位角t1/度").append("\t");
//             nodeContents.append("(3)方位角t2/度").append("\t");
//             nodeContents.append("(4)方位角t3/度").append("\t");
//             nodeContents.append("(5)方位角t4/度").append("\t");
//             nodeContents.append("(6)方位角t5/度").append("\t");
//             nodeContents.append("(7)方位角t6/度").append("\t");
//             nodeContents.append("(8)邻近距离d1/m").append("\t");
//             nodeContents.append("(9)邻近距离d2/m").append("\t");
//             nodeContents.append("(10)邻近距离d3/m").append("\t");
//             nodeContents.append("(11)邻近距离d4/m").append("\t");
//             nodeContents.append("(12)邻近距离d5/m").append("\t");
//             nodeContents.append("(13)邻近距离d6/m").append("\t").append(System.lineSeparator());
//             **/
//
//            // VertexWithNeighbors vertex = new VertexWithNeighbors(mesh)
//            VertexWithNeighbors vertexes = new VertexWithNeighbors(mesh);
//            List<List<LatLon>> vertexList = vertexes.getLatLonNodes();
////            List<List<Angle>> surroundedAngles = new ArrayList<>();
////            List<List<Angle>> surroundedDistances = new ArrayList<>();
////            List<Double> distances = new ArrayList<>();
//            for (List<LatLon> nodes : vertexList)
//            {
//                int length = nodes.size();
//                // 坐标
////                nodeContents.append(nodes.get(0)).append("\t");
////                nodeContents.append(nodes.get(0)).append(System.lineSeparator());
//                nodeContents.append(nodes.get(0)).append("\t\t");
//                // 邻近夹角（度），邻近方位角
//                List<Angle> angles = vertexes.surroundedAzimuth(nodes.get(0), nodes.subList(1, length));
////                nodeContents.append("L2\t");
//                for (Angle angle : angles)
//                {
//                    nodeContents.append(IO.formatDouble(angle.normalize().degrees, 8)).append("\t");
//                }
////                nodeContents.append(System.lineSeparator());
//                nodeContents.append("\t");
//                // 邻近距离（m）
//                List<Angle> distances = vertexes.surroundedDistances(nodes.get(0), nodes.subList(1, length));
////                nodeContents.append("L3\t");
//                for (Angle distance :
//                    distances)
//                {
//                    nodeContents.append(IO.formatDouble(distance.radians * Const.RADIUS)).append("\t");
//                }
//                nodeContents.append(System.lineSeparator());
//            }
//
//            IO.write(folder, pointName + level, nodeContents.toString());
        }
    }
}
