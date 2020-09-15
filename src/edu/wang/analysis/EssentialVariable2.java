/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.*;
import edu.wang.impl.OctahedronInscribed;
import edu.wang.io.*;
import edu.wang.model.*;
import gov.nasa.worldwind.geom.*;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/4/29 16:02
 * @description essential variable
 * @parameter 面积、角度、边长、形状
 */
public class EssentialVariable2
{
    //
    public static void main(String[] args)
    {
        OctahedronInscribed octahedron = OctahedronInscribed.getInstance();
        //
        // facet-ID
        // 0-A,1-B,2-C,3-D,4-E,5-F,6-G,7-H
        int facet = 0;
        String ID = "A";

        MiddleArcSurfaceTriangle triangle = new MiddleArcSurfaceTriangle(
            IO.changeTriangleVertexes(octahedron.getFacetList().get(facet)), new Geocode(ID));

        // 从一个面开始
        List<MiddleArcSurfaceTriangle> triangleList = new ArrayList<>(Collections.singletonList(triangle));
//        MidArcTriangleMesh triangleMesh = new MidArcTriangleMesh(level);
//        MidArcTriangleMesh mesh = triangleMesh.cutMesh();

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmm");
        Date date = new Date(System.currentTimeMillis());
        String strTitle = dateFormat.format(date);
        int maxLevel = 10;
        for (int level = 1; level <= maxLevel; level++)
        {
//            int maxRow = (int) (Math.pow(2, level) + 1);
            String prefix = "SQT" ;
            String folder = prefix + strTitle;
            String fileName = prefix + triangle.getGeocode().getBaseID() + "_" + level;

            String title = "格元ID\t面积(km^2)\t角度A(°)\t角度B(°)\t角度C(°)\t边长a(m)\t边长b(m)\t边长c(m)";
            IO.write(folder, fileName, title);

            // 剖分一次
            refineTriangle(triangleList);

            // 单元按照行列号排序
            triangleList.sort((o1, o2) -> IO.sortByRow(o1.getGeocode(), o2.getGeocode()));

            StringBuilder content;
            // 记录格点
            // 借助在类LatticeAzimuth的做法
            // Mesh first
            List<LatLon> vertices = new ArrayList<>();
            Set<LatLon> set = new HashSet<>();
            for (MiddleArcSurfaceTriangle tri : triangleList)
            {
                if (tri.isUp())
                {
                    if (set.add(tri.getLeft()))
                    {
                        vertices.add(tri.getLeft());
                    }
                    if (set.add(tri.getRight()))
                    {
                        vertices.add(tri.getRight());
                    }
                }
                if (tri.getGeocode().getRow() == Math.pow(2, level))
                {
                    if (set.add(tri.getTop()))
                    {
                        vertices.add(tri.getTop());
                    }
                }

                content = new StringBuilder();
                content.append(tri.getGeocode().getID()).append("\t");
                content.append(IO.formatDouble(tri.computeArea())).append("\t");
                content.append(IO.formatDouble(tri.interiorAngle().get(0).degrees, 6)).append("\t");
                content.append(IO.formatDouble(tri.interiorAngle().get(1).degrees, 6)).append("\t");
                content.append(IO.formatDouble(tri.interiorAngle().get(2).degrees, 6)).append("\t");
                content.append(IO.formatDouble(tri.edgeLengths().get(0))).append("\t");
                content.append(IO.formatDouble(tri.edgeLengths().get(1))).append("\t");
                content.append(IO.formatDouble(tri.edgeLengths().get(2))).append("\t");
                IO.write(folder, fileName, content.toString());
            }
            if (facet == 0)
            {
//                String vertexTxtName = folder + "Vertex_" + i;
                String vertexTxtName = "spPoint_" + level;
                for (LatLon ver : vertices)
                {
                    Vec4 v = IO.latLonToVec4(ver).normalize3().multiply3(Cons.RADIUS);
                    String vecString = IO.formatDouble(v.getX(), 6) + "\t"
                        + IO.formatDouble(v.getY(), 6) + "\t"
                        + IO.formatDouble(v.getZ(), 6) + "\t";
                    IO.write(folder, vertexTxtName, vecString);
                }
            }
        }
    }

    public static void refineTriangle(List<MiddleArcSurfaceTriangle> triangles)
    {
        List<MiddleArcSurfaceTriangle> tempList = new ArrayList<>();

        for (MiddleArcSurfaceTriangle tri : triangles)
        {
            tempList.addAll(Arrays.asList(tri.refine()));
        }
        triangles.clear();
        triangles.addAll(tempList);
        tempList.clear();
    }
}
