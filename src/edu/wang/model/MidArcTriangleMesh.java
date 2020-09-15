/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.IO;
import gov.nasa.worldwind.geom.LatLon;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/6/25
 * @description SQT剖分的分析计算
 */
public class MidArcTriangleMesh extends SurfaceTriangleMesh
{
    public MidArcTriangleMesh(int level)
    {
        super(level);
    }

    public MidArcTriangleMesh cutMesh()
    {
        return cutMesh(1);
    }

    public MidArcTriangleMesh cutMesh(int octant)
    {
        int level = this.getLevel();
//        MidArcTriangleMesh tempMesh = new MidArcTriangleMesh(level);
        MidArcTriangleMesh mesh = new MidArcTriangleMesh(level);
        List<MiddleArcSurfaceTriangle> basePolygons = new ArrayList<>();
        List<MiddleArcSurfaceTriangle> refineTriangles = new ArrayList<>();

        for (SphericalTriangleOctahedron triangle : SphericalTriangleOctahedron.values())
        {
            basePolygons.add(MiddleArcSurfaceTriangle.cast(triangle.baseTriangle()));
        }
//        int count = 0;
        for (MiddleArcSurfaceTriangle triangle : basePolygons)
        {
            refineTriangles.add(triangle);
            for (int i = 0; i < level; i++)
            {
                List<MiddleArcSurfaceTriangle> temp = new ArrayList<>();
                for (MiddleArcSurfaceTriangle tri : refineTriangles)
                {
                    // refine
                    temp.addAll(Arrays.asList(tri.refine()));
//                    count++;
                }
                if (!temp.isEmpty())
                {
                    refineTriangles.clear();
                    refineTriangles.addAll(temp);
                }
                temp.clear();
            }
            // mesh
            for (MiddleArcSurfaceTriangle t : refineTriangles)
            {
//                tempMesh.addNode(t);
                this.addNode(t);
            }
            refineTriangles.clear();
        }
//        System.out.println(count);
//        List<Mesh.Node> nodeList = tempMesh.getNodes();
        List<CellNode> cellNodeList = this.getCellNodes();
//        System.out.println("tempMesh " + tempMesh.getNodes().size());
//        int from = 0;
//        int to = nodeList.size() / 8;
        // octant = 1,2,3,4,5,6,7,8
//        int size = tempMesh.getNodes().size() / 8;
        int size = cellNodeList.size() / 8;
        int fromPosition, toPosition;
        if (octant >= 1 && octant <= 8)
        {
            fromPosition = (octant - 1) * size;
            toPosition = octant * size;
        }
        else
        {
            fromPosition = 0;
            toPosition = size;
        }
        List<CellNode> cellNodeListTemp = new ArrayList<>(cellNodeList.subList(fromPosition, toPosition));
        for (CellNode n : cellNodeListTemp)
        {
//            tempMesh.fillNeighbors(n);
            this.fillNeighbors(n);
        }

        mesh.setCellNodes(cellNodeListTemp);

        return mesh;
    }

    public void analysis()
    {
        // level, area,
        List<CellNode> cellNodes = new ArrayList<>(this.getCellNodes());

        StringBuilder analysisString = new StringBuilder();
//        int level = 0;
        for (CellNode cellNode : cellNodes)
        {
            // level
//            level++;
//            analysisString.append("level ").append(level).append(" ");
            MiddleArcSurfaceTriangle triangle = (MiddleArcSurfaceTriangle) cellNode.getCell();

            // area
            double area = triangle.computeArea();
            analysisString.append("area ").append(area).append(" ");

            // compact
            LatLon top, left, right, center;
            center = triangle.getCenter();
            top = triangle.getTop();
            left = triangle.getLeft();
            right = triangle.getRight();
            double a, b, c, d1, d2, d3;
            a = LatLon.greatCircleDistance(right, left).radians;
            b = LatLon.greatCircleDistance(top, right).radians;
            c = LatLon.greatCircleDistance(top, left).radians;
            List<Double> list = new ArrayList<>();
            list.add(a);
            list.add(b);
            list.add(c);
            double[] stats = statistics(list);

            analysisString.append("avg_compact ").append(stats[0]).append(" ");
            analysisString.append("sigma_compact^2 ").append(stats[1]).append(" ");
            list.clear();

            // in-distance
            d1 = LatLon.greatCircleDistance(center, top).radians;
            d2 = LatLon.greatCircleDistance(center, left).radians;
            d3 = LatLon.greatCircleDistance(center, right).radians;
            list.add(d1);
            list.add(d2);
            list.add(d3);
            stats = statistics(list);
            analysisString.append("avg_distance ").append(stats[0]).append(" ");
            analysisString.append("sigma_distance^2 ").append(stats[1]).append(" ");
            list.clear();

            // adj-distance
            List<Neighbor> neighbors = cellNode.getNeighborList();
            for (Neighbor dgg : neighbors)
            {
                CellNode n = dgg.getCellNode();
                if (!n.isFlag())
                {
                    MiddleArcSurfaceTriangle arcTriangle = (MiddleArcSurfaceTriangle) n.getCell();
                    LatLon adjCenter = arcTriangle.getCenter();
                    list.add(LatLon.greatCircleDistance(center, adjCenter).radians);
                }
            }
            setFlag(cellNode.getCell(), true);
            stats = statistics(list);
            analysisString.append("adj_distance ").append(stats[0]).append(" ");
            analysisString.append("sigma_adjDist^2 ").append(stats[1]).append(" ");
            list.clear();
            analysisString.append(System.lineSeparator());
        }

        String fileName = this.getClass().getSimpleName();
        String level = String.valueOf(this.getLevel());
        IO.write(fileName, level, analysisString.toString());
    }

    private double[] statistics(List<Double> data)
    {
        // [0] avg 平均值
        // [1] sigma 方差（标准差的平方）
        double avg = 0.0, sum = 0.0, sigma = 0.0;
        int length = data.size();
        for (Double var : data)
        {
            sum += var;
        }
        avg = sum / length;
        sum = 0.0;
        for (Double var : data)
        {
            sum += Math.pow(var - avg, 2);
        }
        sigma = sum / length;
        double[] avg_sigma = new double[2];
        // [0] avg 
        // [1] sigma
        avg_sigma[0] = avg;
        avg_sigma[1] = sigma;
        return avg_sigma;
    }
}
