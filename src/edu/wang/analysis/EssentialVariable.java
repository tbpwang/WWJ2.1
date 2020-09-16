/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.*;
import edu.wang.impl.VertexWithNeighbors;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/12/27
 * @description gggad 面积、角度、边长、形状...,格点内蕴距离（大圆弧距离）、格元紧度、格元分形分维数、格元离散度
 */
public class EssentialVariable
{
    public static void main(String[] args)
    {
        int maxLevel = 11;

        // basic data
        int basicShape = 3;
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        String prefix = "SQT";
        String folder = prefix + dateFormat.format(date);
        String pointName = "point";

        // metadata
        String metadata = "\t" + prefix + ".txt" + "\t" + "basicShape = " + basicShape + System.lineSeparator()
            + "Column1ID" + "\t" + "triangle.getGeocode().getID())" + System.lineSeparator()
            + "Column2面积" + "\t" + "triangle.getUnitArea() * Math.pow(Const.radius, 2)"
            + System.lineSeparator()
            + "Column3角度" + "\t" + "triangle.innerAngle().get(0).degrees" + System.lineSeparator()
            + "Column4边长" + "\t" + "triangle.distance().get(i).radians * Const.radius;"
            + System.lineSeparator()
//        metadata.append("(4)边长b").append("\t");
//        metadata.append("(5)边长c").append("\t");
            + "Column5紧度" + "\t"
            + "4 * Math.PI * Math.pow(Const.radius, 2.0) * area - Math.pow(area, 2.0)) / Math.pow(Const.radius * perimeter, 2.0)"
            + System.lineSeparator()
            + "Column6形状指数" + "\t" + "basicShape * Math.sqrt(area)/ perimeter;"
            + System.lineSeparator()
            + "Column7分维数" + "\t" + "2 * Math.log(perimeter / 4.0) / Math.log(area)"
            + System.lineSeparator()
            + "Column8离散度" + "\t" + System.lineSeparator()
//        metadata.append("(10)面积×离散度2").append("\t");
            + "Column9twoCenter距离" + "\t" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()
            // Neighbor cell的邻接距离
            + "Column10边邻接距离" + "\t" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()
            + "Column11角邻接距离" + "\t" + "LatLon.greatCircleDistance(p1,p2).radians * Const.radius"
            + System.lineSeparator()

            // pointName
            + "\t" + pointName + ".txt" + System.lineSeparator()
            + "Column1坐标" + System.lineSeparator()
            + "Column2邻近点方位角" + "\t" + "angle.degrees" + System.lineSeparator()
            + "Column3到邻近点距离" + "\t" + "angle.radians * Const.radius" + System.lineSeparator();
        IO.write(folder, "metadata", metadata);

        // calculation by metadata
        String fileName = "";
        for (int level = 1; level < maxLevel; level++)
        {
            MidArcTriangleMesh triangleMesh = new MidArcTriangleMesh(level);
            MidArcTriangleMesh mesh = triangleMesh.cutMesh();
            fileName = prefix + "_" + ((SurfaceTriangle) mesh.getCellNodes().get(0).getCell()).getGeocode().getBaseID()
                + "_" + level;

            StringBuilder cellContents = new StringBuilder();

            // triangle
            // shape = 3
            List<Mesh.CellNode> cellNodeList = mesh.getCellNodes();
            for (Mesh.CellNode nodeCell : cellNodeList)
            {
                MiddleArcSurfaceTriangle triangle = (MiddleArcSurfaceTriangle) nodeCell.getCell();

                double area;
                double edge1, edge2, edge3, perimeter;
                double compactness;//(4*PI*R*R*A-A*A)/(R*R*p*p)
                double shapeIndex;//perimeter/4/sqrt(area)
                double dimension;//2*ln(p/4)/lnA
                double dispersion;//
//                double areaMulDis2;//area*dispersion
//                cellContents.append(triangle.getGeocode().getID()).append(System.lineSeparator());
                cellContents.append(triangle.getGeocode().getID()).append("\t\t");
                area = triangle.getUnitArea() * Math.pow(Const.RADIUS, 2);
//                cellContents.append("L2\t").append(area).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(area, 14)).append("\t\t");
                // 内角
                cellContents.append(IO.formatDouble(triangle.innerAngle().get(0).degrees, 14)).append("\t");
                cellContents.append(IO.formatDouble(triangle.innerAngle().get(1).degrees, 14)).append("\t");
                cellContents.append(IO.formatDouble(triangle.innerAngle().get(2).degrees, 14)).append("\t\t");

                // 根据不同的剖分方法，使用不同的计算方法计算边长
                edge1 = triangle.edgeLengths().get(0);
                edge2 = triangle.edgeLengths().get(1);
                edge3 = triangle.edgeLengths().get(2);
                perimeter = edge1 + edge2 + edge3;
//                cellContents.append("L3\t").append(edge1).append("\t");
                cellContents.append(IO.formatDouble(edge1)).append("\t");
                cellContents.append(IO.formatDouble(edge2)).append("\t");
//                cellContents.append(edge3).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(edge3)).append("\t\t");
                //cellContents.append(IO.formatDouble(perimeter)).append("\t");
                compactness = (4 * Math.PI * Math.pow(Const.RADIUS, 2.0) * area - Math.pow(area, 2.0)) / Math.pow(
                    Const.RADIUS * perimeter, 2.0);
//                cellContents.append("L4\t").append(IO.formatDouble(compactness)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(compactness)).append("\t\t");
//                shapeIndex = perimeter / shape / Math.sqrt(area);
                shapeIndex = basicShape * Math.sqrt(area) / perimeter;
//                cellContents.append("L5\t").append(IO.formatDouble(shapeIndex)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(shapeIndex)).append("\t\t");
                dimension = 2 * Math.log(perimeter / 4.0) / Math.log(area);
//                cellContents.append("L6\t").append(IO.formatDouble(dimension)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(dimension)).append("\t\t");

                List<Vec4> vertices = new ArrayList<>();
                vertices.add(IO.check(IO.latLonToVec4(triangle.getTop())));
                vertices.add(IO.check(IO.latLonToVec4(triangle.getLeft())));
                vertices.add(IO.check(IO.latLonToVec4(triangle.getRight())));
                SphereStatisticsPoint sPoints = new SphereStatisticsPoint(vertices);
                dispersion = sPoints.getDispersionUnit();
//                cellContents.append("L7\t").append(IO.formatDouble(dispersion, 6)).append(System.lineSeparator());
                cellContents.append(IO.formatDouble(dispersion)).append("\t\t");
//                areaMulDis2 = area * Math.pow(dimension, 2.0);
//                cellContents.append(IO.formatDouble(areaMulDis2, 6)).append("\t");

                // 邻近cell的质心距离
                LatLon triangleCenter = triangle.getCenter();
                List<Mesh.Neighbor> neighborList = nodeCell.getNeighborList();
                List<Double> nearAngleDistances = new ArrayList<>();
                List<Double> nearEdgeDistances = new ArrayList<>();
                List<Double> nearDistances = new ArrayList<>();
                List<Double> twoLineCenterDistances = new ArrayList<>();
                List<LatLon> edgeCenters = new ArrayList<>();

                edgeCenters.add(LatLon.interpolateGreatCircle(0.5, triangle.getTop(), triangle.getLeft()));
                edgeCenters.add(LatLon.interpolateGreatCircle(0.5, triangle.getTop(), triangle.getRight()));
                edgeCenters.add(LatLon.interpolateGreatCircle(0.5, triangle.getLeft(), triangle.getRight()));

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
                        double distance1 = LatLon.greatCircleDistance(edgeCenters.get(1), cellsCenter).degrees;
                        double distance2 = LatLon.greatCircleDistance(edgeCenters.get(2), cellsCenter).degrees;
                        twoLineCenterDistances.add(Math.min(Math.min(distance0, distance1), distance2));
                    }
                    else if (aNber.getType() == Const.NEIGHBOR_TYPE_VERTEX)
                    {
                        nearAngleDistances.add(
                            LatLon.greatCircleDistance(triangleCenter, nearCenter).radians * Const.RADIUS);
                    }
                    else
                    {
                        nearDistances.add(LatLon.greatCircleDistance(triangleCenter, nearCenter).radians * Const.RADIUS);
                    }
                }
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

            // pointName
            StringBuilder nodeContents = new StringBuilder();
            /**
             nodeContents.append("(1)坐标").append("\t");
             //            nodeContents.append("(2)邻近夹角t1/度").append("\t");
             nodeContents.append("(2)方位角t1/度").append("\t");
             nodeContents.append("(3)方位角t2/度").append("\t");
             nodeContents.append("(4)方位角t3/度").append("\t");
             nodeContents.append("(5)方位角t4/度").append("\t");
             nodeContents.append("(6)方位角t5/度").append("\t");
             nodeContents.append("(7)方位角t6/度").append("\t");
             nodeContents.append("(8)邻近距离d1/m").append("\t");
             nodeContents.append("(9)邻近距离d2/m").append("\t");
             nodeContents.append("(10)邻近距离d3/m").append("\t");
             nodeContents.append("(11)邻近距离d4/m").append("\t");
             nodeContents.append("(12)邻近距离d5/m").append("\t");
             nodeContents.append("(13)邻近距离d6/m").append("\t").append(System.lineSeparator());
             **/

            // VertexWithNeighbors vertex = new VertexWithNeighbors(mesh)
            VertexWithNeighbors vertexes = new VertexWithNeighbors(mesh);
            List<List<LatLon>> vertexList = vertexes.getLatLonNodes();
//            List<List<Angle>> surroundedAngles = new ArrayList<>();
//            List<List<Angle>> surroundedDistances = new ArrayList<>();
//            List<Double> distances = new ArrayList<>();
            for (List<LatLon> nodes : vertexList)
            {
                int length = nodes.size();
                // 坐标
//                nodeContents.append(nodes.get(0)).append("\t");
//                nodeContents.append(nodes.get(0)).append(System.lineSeparator());
                nodeContents.append(nodes.get(0)).append("\t\t");
                // 邻近夹角（度），邻近方位角
                List<Angle> angles = vertexes.surroundedAzimuth(nodes.get(0), nodes.subList(1, length));
//                nodeContents.append("L2\t");
                for (Angle angle : angles)
                {
                    nodeContents.append(IO.formatDouble(angle.normalize().degrees, 8)).append("\t");
                }
//                nodeContents.append(System.lineSeparator());
                nodeContents.append("\t");
                // 邻近距离（m）
                List<Angle> distances = vertexes.surroundedDistances(nodes.get(0), nodes.subList(1, length));
//                nodeContents.append("L3\t");
                for (Angle distance :
                    distances)
                {
                    nodeContents.append(IO.formatDouble(distance.radians * Const.RADIUS)).append("\t");
                }
                nodeContents.append(System.lineSeparator());
            }

            IO.write(folder, pointName + level, nodeContents.toString());
        }
    }
}
