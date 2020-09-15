/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.*;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/5/27
 * @description 用于验证TriangleMesh的功能，建立全部格网后，
 * 对每个格网的node填充邻居信息（注：遍历较慢，后继需改进算法）
 * 按行列号输出三角形及其邻域三角形
 * 记录离散度
 */
public class NeighborhoodFinder
{
    public static void main(String[] args)
    {
        // constructor
        int level = 10;
        for (int counter = 1; counter <= level; counter++)
        {
            SurfaceTriangleMesh tempMesh = new SurfaceTriangleMesh(counter);
            SurfaceTriangleMesh mesh = new SurfaceTriangleMesh(counter);

            for (SphericalTriangleOctahedron polygon : SphericalTriangleOctahedron.values())
            {
                List<MiddleArcSurfaceTriangle> triangleList = new ArrayList<>();
                triangleList.add(polygon.baseTriangle());

                List<MiddleArcSurfaceTriangle> temp;

                for (int i = 0; i < counter; i++)
                {
                    temp = new ArrayList<>();
                    for (MiddleArcSurfaceTriangle middleArcTriangle : triangleList)
                    {
                        temp.addAll(Arrays.asList(middleArcTriangle.refine()));
                    }
                    if (!temp.isEmpty())
                    {
                        triangleList.clear();
                        triangleList.addAll(temp);
                    }
                }

                for (MiddleArcSurfaceTriangle middleArcTriangle : triangleList)
                {
                    tempMesh.addNode(middleArcTriangle);
                }
            }

            int meshSize = tempMesh.getCellNodes().size();
            // 只算一个八分体的面
            int listSize = meshSize / 8;
            List<Mesh.CellNode> cellNodeList = new ArrayList<>(tempMesh.getCellNodes().subList(0, listSize));
//        int count = 0;
            for (Mesh.CellNode cellNode : cellNodeList)
            {
                tempMesh.fillNeighbors(cellNode);
            }

            // construction
            mesh.setCellNodes(cellNodeList);

            String fileFolder = "mesh";
            String fileName = "mesh" + counter;
//            StringBuilder contents = new StringBuilder();

            for (Mesh.CellNode cellNode : mesh.getCellNodes())
            {
                StringBuilder contents = new StringBuilder();
                contents.append("ID\t").append("延伸度OA\t").append("延伸度OB\t").append("延伸度OC\t").append(
                    "两线中点距离1\t").append("两线中点距离2\t").append("两线中点距离3\t").append("两格元心距离1——12\t");
                MiddleArcSurfaceTriangle mine = (MiddleArcSurfaceTriangle) cellNode.getCell();
                contents.append(mine.getGeocode().getID()).append("\t");
//                StringBuilder vertices = new StringBuilder();
//                vertices.append(IO.formatDouble(Cons.latLonToVec4(mine.getTop())).);
                LatLon myCenter = mine.getCenter();
                // 计算延伸度
                double lengthA = LatLon.greatCircleDistance(myCenter, mine.getTop()).getRadians() * Cons.RADIUS;
                double lengthB = LatLon.greatCircleDistance(myCenter, mine.getLeft()).getRadians() * Cons.RADIUS;
                double lengthC = LatLon.greatCircleDistance(myCenter, mine.getRight()).getRadians() * Cons.RADIUS;
                contents.append(IO.formatDouble(lengthA, 6)).append("\t");
                contents.append(IO.formatDouble(lengthB, 6)).append("\t");
                contents.append(IO.formatDouble(lengthC, 6)).append("\t");

                LatLon middleA = LatLon.interpolateGreatCircle(0.5, mine.getLeft(), mine.getRight());
                LatLon middleB = LatLon.interpolateGreatCircle(0.5, mine.getTop(), mine.getRight());
                LatLon MiddleC = LatLon.interpolateGreatCircle(0.5, mine.getLeft(), mine.getTop());
                List<Mesh.Neighbor> neighborList = cellNode.getNeighborList();
                // 两边中点距离，getType() == 1
                // 两元中心点距离 tempContents
                StringBuilder tempContents = new StringBuilder();
                for (Mesh.Neighbor n : neighborList)
                {
                    MiddleArcSurfaceTriangle other = (MiddleArcSurfaceTriangle) n.getCellNode().getCell();
                    LatLon otherCenter = other.getCenter();
                    tempContents.append(
                        IO.formatDouble(LatLon.greatCircleDistance(myCenter, otherCenter).getRadians() * Cons.RADIUS,
                            6)).append("\t");

                    if (n.getType() == 1)
                    {
                        LatLon centersMiddle = LatLon.interpolateGreatCircle(0.5, myCenter, otherCenter);
                        contents.append(
                            IO.formatDouble(nearDistance(centersMiddle, middleA, middleB, MiddleC), 6)).append("\t");
                    }
                }
//            System.out.println(neighborList.size());
                for (int i = 0; i < 12 - neighborList.size(); i++)
                {
                    tempContents.append("0").append("\t");
                }
                // contents.append(tempContents.toString()).append(System.lineSeparator());
                contents.append(tempContents.toString());
                IO.write(fileFolder, fileName, contents.toString());
            }
            IO.write(fileFolder, fileName, System.lineSeparator());

        }
    }

    private static double nearDistance(LatLon p0, LatLon p1, LatLon p2, LatLon p3)
    {
        double dis1 = LatLon.greatCircleDistance(p0, p1).getRadians();
        double dis2 = LatLon.greatCircleDistance(p0, p2).getRadians();
        double dis3 = LatLon.greatCircleDistance(p0, p3).getRadians();
        return Math.min(dis1, Math.min(dis2, dis3)) * Cons.RADIUS;
    }
}
