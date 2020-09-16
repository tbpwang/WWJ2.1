/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.Mesh;
import edu.wang.io.*;
import edu.wang.model.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/6/27
 * @description 统计MidArcTriangleMesh
 *              统计ZhaoTriangleMesh并与Goodchild面积公式做比较
 */
public class Comparison
{
    public static void main(String[] args)
    {
        // constructor
        int level = 3;
//        MidArcTriangleMesh fullMesh = new MidArcTriangleMesh(level);
        QTMTriangleMesh fullMesh = new QTMTriangleMesh(level);

        QTMTriangleMesh mesh = fullMesh.cutMesh();

        // only suitable for ZhaoTriangle
        // 以下分析部分仅适用于ZhaoTriangle
        List<Mesh.CellNode> list = mesh.getCellNodes();
        double kappa = AreaLatitudeRelated.coefficientKappa(level);
//        List<Double> area = new ArrayList<>();
        StringBuilder area = new StringBuilder();
        double halfPI = Math.PI / 2.0;
        for (Mesh.CellNode cellNode : list)
        {
            QTMTriangle triangle = (QTMTriangle) cellNode.getCell();
            double latitude = triangle.isUp() ? triangle.getLeft().getLatitude().radians
                : triangle.getTop().getLatitude().radians;
            double chi = halfPI - latitude;
            double sinc = AreaLatitudeRelated.coefficientSinc(chi);
            area.append(AreaLatitudeRelated.deltaArea(kappa, sinc)).append(",");
        }
        IO.write("ZhaoTriangleMesh", "phi_" + String.valueOf(level), area.toString());
//        System.out.println(area);
        //以上部分仅适用于ZhaoTriangle

        // statistics
        mesh.analysis();
    }

    private static class AreaLatitudeRelated
    {
        // 用来比较ZhaoTriangle计算的三角形面积
        // chi = pi/2 - latitude
        private static double coefficientKappa(int level)
        {
            // level > 4
            double radius = Const.RADIUS;
            return Math.pow(Math.PI, 3) * Math.pow(radius, 2) / Math.pow(2, 2 * level + 4);
        }

        // chi = pi/2 - latitude
        private static double coefficientSinc(double chi)
        {
            return chi == 0 ? 0.0 : Math.sin(chi) / chi;
        }

        private static double deltaArea(double kappa, double sinc)
        {
            return kappa * sinc;
        }
    }
}
