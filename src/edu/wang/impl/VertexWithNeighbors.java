/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/12/26
 * @description 在三角形网中，独立出每个顶点及其邻接点， 因此首先得有三角形网，然后计算与顶点相关的指标
 */
public class VertexWithNeighbors
{
    //
//    MidArcTriangleMesh mesh;
    SurfaceTriangleMesh mesh;
    List<List<LatLon>> latLonNodes;

    public VertexWithNeighbors(SurfaceTriangleMesh mesh)
    {
        if (mesh == null)
        {
            String message = Logging.getMessage("nullError.三角网为空");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        this.mesh = mesh;
        latLonNodes = new ArrayList<>();
    }

    private void setLatLonNodes()
    {
        this.latLonNodes = parse();
    }

    public List<List<LatLon>> getLatLonNodes()
    {
        if (this.latLonNodes.size() == 0)
        {
            setLatLonNodes();
        }
        return latLonNodes;
    }

    private List<List<LatLon>> parse()
    {
        if (this.mesh == null)
        {
            return null;
        }

        Vertex vertex1,vertex2;

        List<LatLon> adjPoints;
        HashSet<LatLon> adjPointSet;
        HashSet<LatLon> pointSet = new HashSet<>();
        List<List<LatLon>> lattices = new ArrayList<>();
//        System.out.println("mesh: " + mesh);
        for (Mesh.CellNode cellNode :
            mesh.getCellNodes())
        {
//            MiddleArcSurfaceTriangle thisTriangle = (MiddleArcSurfaceTriangle) cellNode.getCell();
            SurfaceTriangle thisTriangle = (SurfaceTriangle) cellNode.getCell();
            List<Mesh.Neighbor> neighborList = cellNode.getNeighborList();
            List<LatLon> thisVertices = thisTriangle.getGeoVertices();
//            System.out.println("TriangleVertex: " + thisVertices);
//            System.out.println("NeighborCounter: " + cellNode.getNeighborList().size());
            for (LatLon p1 : thisVertices)
            {
                vertex1 = new Vertex(p1);
                if (!pointSet.add(vertex1))
                {
                    break;
                }
                adjPoints = new ArrayList<>();
                List<LatLon> tempPoints = new ArrayList<>();
                adjPointSet = new HashSet<>();
                adjPoints.add(p1);
                adjPointSet.add(vertex1);

                // 添加thisTriangle中的其他的点，
                // 他们必然相邻
//                 thisTriangle.getGeoVertices().size() == 3;
                for (LatLon latLon: thisVertices                )
                {
                    if (adjPointSet.add(new Vertex(latLon)))
                    {
                        tempPoints.add(latLon);
                    }
                }
//                for (int i = 0; i < 3; i++)
//                {
//                    if (adjPointSet.add(new Vertex(thisVertices.get(i))))
//                    {
//                        tempPoints.add(thisVertices.get(i));
//                    }
//                }

                // 添加thisTriangle的Neighbor中相邻的点，
                // 若在三角形中则可能添加其余的两个点
                //
                for (Mesh.Neighbor nb : neighborList)
                {
//                    MiddleArcSurfaceTriangle otherTriangle = (MiddleArcSurfaceTriangle) nb.getCellNode().getCell();
                    SurfaceTriangle otherTriangle = (SurfaceTriangle) nb.getCellNode().getCell();
                    List<LatLon> otherVertices = otherTriangle.getGeoVertices();

                    for (LatLon p2 : otherVertices)
                    {
                        // 如果p1的邻近点为p2：
                        vertex2 = new Vertex(p2);
                        if (vertex2.equals(vertex1))
                        {
                            // 判断两个点是否相等（p2、p1），若是则给出邻近点
                            for (int i = 0; i < 3; i++)
                            {
                                if (adjPointSet.add(new Vertex(otherVertices.get(i))))
                                {
                                    tempPoints.add(otherVertices.get(i));
                                }
                            }
                            break;
                        }
                    }
                }

//                System.out.println("BeforeSort: " + tempPoints);
                tempPoints.sort(new Comparator<LatLon>()
                {
                    @Override
                    public int compare(LatLon o1, LatLon o2)
                    {
                        if (o1.getLatitude().subtract(o2.getLatitude()).degrees>=0.0)
                        {
                            return Double.compare(o1.getLongitude().subtract(o2.getLatitude()).degrees, 0.0);
                        }
                        else return -1;
                    }
                });
//                System.out.println("AfterSort: " + tempPoints);
                adjPoints.addAll(tempPoints);
                lattices.add(adjPoints);
//                if (!adjPoints.isEmpty())
//                {
//
//                    lattices.add(adjPoints);
//                }
            }
        }
//        System.out.println("BeforeSort:");
//        System.out.println(lattices);
//        // 排序，按照方位角和经纬度排序
//        // 输出已排序list
//        for (List<LatLon> lls :
//            lattices)
//        {
//            Collections.sort(lls, new Comparator<LatLon>()
//            {
//                @Override
//                public int compare(LatLon o1, LatLon o2)
//                {
//                    LatLon p0 = lls.get(0);
//                    double theta1, theta2;
//                    Angle lat0, lon0, lat1, lat2, lon1, lon2, deltLon1, deltLon2;
//                    lat0 = p0.getLatitude();
//                    lon0 = p0.getLongitude();
//                    lat1 = o1.getLatitude();
//                    lon1 = o1.getLongitude();
//                    lat2 = o2.getLatitude();
//                    lon2 = o2.getLongitude();
//                    deltLon1 = lon1.subtract(lon0);
//                    deltLon2 = lon2.subtract(lon0);
//                    if (!p0.equals(o1) && !p0.equals(o2))
//                    {
//                        double tempAngle1 = Math.atan2(deltLon1.sin() * lat1.cos(),
//                            lat0.cos() * lat1.sin() - lat0.sin() * lat1.cos() * deltLon1.cos());
//                        theta1 = tempAngle1 < 0 ? tempAngle1 + 360 : tempAngle1;
//                        double tempAngle2 = Math.atan2(deltLon2.sin() * lat2.cos(),
//                            lat0.cos() * lat2.sin() - lat0.sin() * lat2.cos() * deltLon2.cos());
//                        theta2 = tempAngle2 < 0 ? tempAngle2 + 360 : tempAngle2;
//                        return theta1 - theta2 < 0 ? -1 : theta1 - theta2 > 0 ? 1 : 0;
//                    }
//
//                    if (p0.equals(o1))
//                        return 1;
//                    if (p0.equals(o2))
//                        return -1;
//                    return 0;
//                }
//            });
//
//        }
//        System.out.println("AfterSort:");
//        System.out.println(lattices);
        return lattices;
    }

    public List<Angle> surroundedAzimuth(LatLon node, List<LatLon> neighbors)
    {
        List<Angle> perigon = new ArrayList<>();
        for (LatLon neighbor : neighbors)
        {
            Angle temp = LatLon.greatCircleAzimuth(node, neighbor);
            if (temp.degrees < 0.0)
                perigon.add(temp.add(Angle.fromDegrees(360.0)));
            else
                perigon.add(temp);
        }
//        if (size < 2)
//        {
//            perigon.add(Angle.ZERO);
//        }
        // 已排序数列
//        for (int i = 0; i < size - 1; i++)
//        {
//            LatLon p1 = neighbors.get(i);
//            LatLon p2 = neighbors.get(i + 1);
//            SurfaceTriangle triangle = new SurfaceTriangle(node, p1, p2, "");
//            perigon.add(triangle.innerAngle().get(0));
//        }

        // 格式化，6个数值
        int length = 6 - perigon.size();
        for (int i = 0; i < length; i++)
        {
            perigon.add(Angle.NEG360);
        }
        return perigon;
    }

    public List<Angle> surroundedDistances(LatLon node, List<LatLon> neighbors)
    {
        // length = 6;
        List<Angle> distances = new ArrayList<>();
        for (LatLon ll : neighbors)
        {
            distances.add(LatLon.greatCircleDistance(node, ll));
        }
        // 格式化，6数值
        int length = 6 - distances.size();
        for (int i = 0; i < length; i++)
        {
            distances.add(Angle.ZERO);
        }
        return distances;
    }
}
