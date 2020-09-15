/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.impl.VertexWithNeighbors;
import edu.wang.io.IO;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.LatLon;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/12/19
 * @description 输出格点分布，格点的邻接方位角
 */
public class LatticeAzimuth
{

    public static void main(String[] args)
    {
        // construction Mesh
        int maxLevel = 11;
        String folder = "SQT";
        String fileName = "Azimuth";
        for (int level = 10; level < maxLevel; level++)
        {
            MidArcTriangleMesh triangleMesh = new MidArcTriangleMesh(level);
            MidArcTriangleMesh mesh = triangleMesh.cutMesh();
            VertexWithNeighbors vertex = new VertexWithNeighbors(mesh);

//            // test
//            for (Mesh.CellNode cellNode:
//            mesh.getCellNodes())
//            {
//                System.out.println((MiddleArcSurfaceTriangle)cellNode.getCell());
//            }

            List<List<LatLon>> lattices = vertex.getLatLonNodes();
//            List<List<LatLon>> lattices = new ArrayList<>();
//            List<LatLon> adjPoints;
//            HashSet<LatLon> adjPointSet;
//            for (Mesh.CellNode cellNode :
//                mesh.getCellNodes())
//            {
//                MiddleArcSurfaceTriangle thisTriangle = (MiddleArcSurfaceTriangle) cellNode.getCell();
//
//                // getGeoVertices的size是否有问题？？
//                List<LatLon> thisVertices = thisTriangle.getGeoVertices();
//                for (LatLon p1 : thisVertices)
//                {
//                    adjPoints = new ArrayList<>();
//                    adjPointSet = new HashSet<>();
//                    adjPoints.add(p1);
//                    adjPointSet.add(p1);
//                    if (adjPointSet.add(thisVertices.get(0)))
//                    {
//                        adjPoints.add(thisVertices.get(0));
//                    }
//                    if (adjPointSet.add(thisVertices.get(1)))
//                    {
//                        adjPoints.add(thisVertices.get(1));
//                    }
//                    if (adjPointSet.add(thisVertices.get(2)))
//                    {
//                        adjPoints.add(thisVertices.get(2));
//                    }
//                    for (Mesh.Neighbor nb : cellNode.getNeighborList())
//                    {
//                        MiddleArcSurfaceTriangle otherTriangle = (MiddleArcSurfaceTriangle) nb.getCellNode().getCell();
//                        List<LatLon> otherVertices = otherTriangle.getGeoVertices();
//                        for (LatLon p2 : otherVertices)
//                        {
//                            if (p2.equals(p1))
//                            {
//                                ///
//                                // 判断两个点是否相等（p2、p1），若是则给出邻近点
//                                if (adjPointSet.add(otherVertices.get(0)))
//                                {
//                                    adjPoints.add(otherVertices.get(0));
//                                }
//                                if (adjPointSet.add(otherVertices.get(1)))
//                                {
//                                    adjPoints.add(otherVertices.get(1));
//                                }
//                                if (adjPointSet.add(otherVertices.get(2)))
//                                {
//                                    adjPoints.add(otherVertices.get(2));
//                                }
//                                break;
//                            }
//                        }
//                    }
//                    if (!adjPoints.isEmpty())
//                        lattices.add(adjPoints);
//                }
//            }
            // output
            StringBuilder strOut;
            StringBuilder azimuthOut;
            for (List<LatLon> ltc : lattices)
            {
                azimuthOut = new StringBuilder();
                azimuthOut.append(ltc.get(0)).append("\t");
                strOut = new StringBuilder();
                strOut.append(ltc.size() - 1).append(",\t");
                for (int i = 1; i < ltc.size(); i++)
                {
                    azimuthOut.append(ltc.get(i)).append("\t");
                    double azimuth = LatLon.greatCircleAzimuth(ltc.get(0), ltc.get(i)).degrees;
                    strOut.append(azimuth).append("\t");
                }
                IO.write(folder, fileName + level, strOut.toString());
                IO.write(folder,fileName+"Degree"+level,azimuthOut.toString());
            }
        }
    }
//    void test()
//    {
//        latticeNode
//        // 记录唯一格点及其邻接点
//        List<List<LatLon>> latticeNode = new ArrayList<>();
//        Set<LatLon> latticeSet = new HashSet<>();
//        List<LatLon> adjPoints;
//        Set<LatLon> adjSet;
//        for (Mesh.CellNode thisCell : mesh.getCellNodes())
//        {
//            List<LatLon> thisCellVt = ((MiddleArcSurfaceTriangle) thisCell.getCell()).getGeoVertices();
//            adjPoints = new ArrayList<>();
//            for (LatLon p1 : thisCellVt)
//            {
//                adjSet = new HashSet<>();
//                if (latticeSet.add(p1))
//                {
//                    adjSet.add(p1);
//                    adjPoints.add(p1);
//                    if (adjSet.add(thisCellVt.get(0)))
//                    {
//                        adjPoints.add(thisCellVt.get(0));
//                    }
//                    if (adjSet.add(thisCellVt.get(1)))
//                    {
//                        adjPoints.add(thisCellVt.get(1));
//                    }
//                    if (adjSet.add(thisCellVt.get(2)))
//                    {
//                        adjPoints.add(thisCellVt.get(2));
//                    }
//                    // neighbor
//                    for (Mesh.Neighbor nb : thisCell.getNeighborList())
//                    {
//                        List<LatLon> nbCellVt
//                            = ((MiddleArcSurfaceTriangle) nb.getCellNode().getCell()).getGeoVertices();
//                        for (LatLon p2 : nbCellVt)
//                        {
//                            if (p2.equals(p1))
//                            {
//                                if (adjSet.add(nbCellVt.get(0)))
//                                {
//                                    adjPoints.add(nbCellVt.get(0));
//                                }
//                                if (adjSet.add(nbCellVt.get(1)))
//                                {
//                                    adjPoints.add(nbCellVt.get(1));
//                                }
//                                if (adjSet.add(nbCellVt.get(2)))
//                                {
//                                    adjPoints.add(nbCellVt.get(2));
//                                }
//                            }
//                        }
//                    }
//                }
//                if (adjPoints.size()!=0)
//                {
//                    latticeNode.add(adjPoints);
//                }
//            }
//        }
//        // 输出 latticeNode
//        List<List<Angle>> latticeAngle = new ArrayList<>();
//        List<Angle> adjAngle;
//        for (List<LatLon> l1 : latticeNode)
//        {
//            adjAngle = new ArrayList<>();
//            for (int i = 1; i < l1.size(); i++)
//            {
//                adjAngle.add(LatLon.greatCircleAzimuth(l1.get(0), l1.get(i)));
//            }
//            if (adjAngle.size()!=0)
//            {
//                latticeAngle.add(adjAngle);
//            }
//        }
//        String title = "adjAzimuth" + counter;
//        StringBuilder angleOut;
//        for (List<Angle> l1 : latticeAngle)
//        {
//            angleOut = new StringBuilder();
//            angleOut.append(l1.size()).append(",\t");
//            for (int i = 0; i < l1.size(); i++)
//            {
//                angleOut.append(IO.formatDouble(l1.get(i).getDegrees(),6)).append(",\t");
//            }
//            angleOut.append(System.lineSeparator());
//            IO.write(fileFolder, title, angleOut.toString());
//        }
//    }
}
