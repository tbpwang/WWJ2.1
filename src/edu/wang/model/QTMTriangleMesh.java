/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.Mesh;
import edu.wang.io.IO;
import gov.nasa.worldwind.geom.LatLon;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/7/17
 * @description
 */

public class QTMTriangleMesh extends SurfaceTriangleMesh
{
    public QTMTriangleMesh(int level)
    {
        super(level);
    }

    public QTMTriangleMesh cutMesh()
    {
        return cutMesh(1);
    }

    public QTMTriangleMesh cutMesh(int octant)
    {
        int level = this.getLevel();
//        MidArcTriangleMesh tempMesh = new MidArcTriangleMesh(level);
        QTMTriangleMesh mesh = new QTMTriangleMesh(level);
        List<QTMTriangle> basePolygons = new ArrayList<>();
        List<QTMTriangle> subQTMTriangles = new ArrayList<>();

        for (SphericalTriangleOctahedron triangle : SphericalTriangleOctahedron.values())
        {
            basePolygons.add(QTMTriangle.cast(triangle.baseTriangle()));
            //triangle.baseTriangle()
        }

        for (QTMTriangle triangle : basePolygons)
        {
            subQTMTriangles.add(triangle);
            for (int i = 0; i < level; i++)
            {
                List<QTMTriangle> temp = new ArrayList<>();
                for (QTMTriangle tri : subQTMTriangles)
                {
                    // refine
                    QTMTriangle[] trianglesRefine = tri.refine();
                    temp.addAll(Arrays.asList(trianglesRefine));
//                    temp.addAll(Arrays.asList(tri.refine()));
//                    count++;
                }
                if (!temp.isEmpty())
                {
                    subQTMTriangles.clear();
                    subQTMTriangles.addAll(temp);
                }
                temp.clear();
            }
            // mesh
            for (QTMTriangle t : subQTMTriangles)
            {
//                tempMesh.addNode(t);
                this.addNode(t);
            }
            subQTMTriangles.clear();
        }

        List<CellNode> cellNodeList = this.getCellNodes();

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
        for (CellNode aCell : cellNodeListTemp)
        {
//            tempMesh.fillNeighbors(n);
            this.fillNeighbors(aCell);
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
            QTMTriangle triangle = (QTMTriangle) cellNode.getCell();

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
            List<Mesh.Neighbor> neighbors = cellNode.getNeighborList();
            for (Mesh.Neighbor dgg : neighbors)
            {
                CellNode n = dgg.getCellNode();
                if (!n.isFlag())
                {
                    QTMTriangle arcTriangle = (QTMTriangle) n.getCell();
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
        // [0] avg
        // [1] sigma
        double avg = 0.0, sum = 0.0, sigma = 0.0;
        int length = data.size();
        for (Double var : data)
        {
            sum += var;
        }
        avg = sum / length;
        sum = 0.0;
        for (Double var :
            data)
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
