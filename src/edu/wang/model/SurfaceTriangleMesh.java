/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.*;
import edu.wang.io.Cons;
import gov.nasa.worldwind.util.Logging;

import java.util.Objects;

/**
 * @author Zheng WANG
 * @create 2019/5/6 16:49
 * @description
 * @parameter
 */
public class SurfaceTriangleMesh extends Mesh
{
    public SurfaceTriangleMesh(int level)
    {
        super(level);
    }

    public void fillNeighbors(CellNode host)
    {
        SurfaceTriangle t = (SurfaceTriangle) host.getCell();
        boolean isNorth = t.getCenter().getLatitude().degrees > 0;
        if (getLevel() != t.getLevel())
        {
            String message = Logging.getMessage("ValueError.单元格和格网不同层");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        Geocode geocode = t.getGeocode();
        int i, j;
        i = geocode.getRow();
        j = geocode.getColumn();
        String baseID = t.getGeocode().getBaseID();
//        int index = OctahedronBaseID.valueOf(baseID).getIndex();
//        List<Neighbor> neighborList = host.getNeighborList();
        // 超限对称操作，左边tempLeft，右边tempRight,底边tempEdge
//        List<Node> tempLeft, tempRight, tempEdge;
        if (geocode.isUp())
        {
            // 向上
            if (i == maxRow())
            {
                // 极点
                // j=1
//                Node n = search(i, j, baseID);
                host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(
                    Objects.requireNonNull(vertexToVertex(host)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                CellNode n = search(i - 1, j, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                n = search(i - 1, j + 1, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                n = search(i - 1, j + 2, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
            }
            else if (i == 1)
            {
                // 最底边
                if (j == 1)
                {
                    // 最左边
//                    Node n = search(i, j, baseID);
                    host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(
                        Objects.requireNonNull(vertexToVertex(host)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    CellNode n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i, j + 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    n = search(i + 1, j, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else if (j == maxColumn(i))
                {
                    // 最右边
                    CellNode n = search(i, j - 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
//                    n = search(i, j, baseID);//.asNeighbor(Constant.NEIGHBOR_TYPE_EDGE);
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    }
                    host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(
                        Objects.requireNonNull(vertexToVertex(host)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i + 1, j - 2, baseID);//.asNeighbor(Constant.NEIGHBOR_TYPE_VERTEX);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else
                {
                    // i
                    CellNode n1 = search(i, j - 2, baseID);
                    CellNode n2 = search(i, j - 1, baseID);
                    CellNode n3 = search(i, j + 1, baseID);
                    CellNode n4 = search(i, j + 2, baseID);
                    host.addNeighbor(n1.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(n2.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(n3.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(n4.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i-1
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n1)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n2)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n3)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n4)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n1)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n2)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(host)).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n3)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n4)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    // i+1
                    host.addNeighbor(search(i + 1, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
            }
            else
            {
                if (j == 1)
                {
                    // i-1
                    CellNode n = search(i - 1, j, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i - 1, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i - 1, j + 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i - 1, j + 3, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i
//                    n = search(i,j,baseID);
                    host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i, j + 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    //i+1
                    n = search(i + 1, j, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else if (j == maxColumn(i))
                {
                    // i-1
                    host.addNeighbor(search(i - 1, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i - 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    CellNode n = search(i - 1, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i - 1, j + 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i
                    host.addNeighbor(search(i, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // n =host
                    host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    // i+1
                    n = search(i + 1, j - 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else
                {
                    // i-1
                    for (int k = j - 1; k <= j + 3; k++)
                    {
                        if (k != j + 1)
                        {
                            host.addNeighbor(search(i - 1, k, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        }
                        else
                        {
                            host.addNeighbor(search(i - 1, k, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                        }
                    }
                    // i
                    for (int k = j - 2; k <= j + 2; k++)
                    {
                        if (k != j)
                        {
                            if (k == j - 1 || k == j + 1)
                            {
                                host.addNeighbor(search(i, k, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                            }
                            else
                            {
                                host.addNeighbor(search(i, k, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                            }
                        }
                    }
                    // i+1
                    for (int k = j - 2; k <= j; k++)
                    {
                        host.addNeighbor(search(i + 1, k, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                }
            }
        }
        else
        {
            // 向下
            if (i == 1)
            {
                if (j == 2 && j == maxColumn(i) - 1)
                {
                    CellNode n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(northGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(
                            Objects.requireNonNull(southGroundCross(n)).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    // i+1
                    n = search(i + 1, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else if (j == 2)
                {
                    CellNode n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    n = search(i, j + 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i+1
                    n = search(i + 1, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else if (j == maxColumn(i) - 1)
                {
                    // i && i-1
                    CellNode n = search(i, j - 2, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(northGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(southGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    // i+1
                    host.addNeighbor(search(i + 1, j - 3, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i + 1, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else
                {
                    // i && i-1
                    host.addNeighbor(search(i, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    CellNode n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    CellNode n1 = search(i, j + 1, baseID);
                    host.addNeighbor(n1.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    if (isNorth)
                    {
                        host.addNeighbor(northGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(northGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(northGroundCross(n1).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
                    else
                    {
                        host.addNeighbor(southGroundCross(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(southGroundCross(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                        host.addNeighbor(southGroundCross(n1).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    }
//                    n = search(i, j + 2, baseID);
                    host.addNeighbor(search(i, j + 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i+1
                    host.addNeighbor(search(i + 1, j - 3, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(search(i + 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
            }
            else if (i == maxRow() - 1)
            {
                // i = 1
                host.addNeighbor(search(i - 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(search(i - 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(search(i - 1, j + 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                // i
                CellNode n = search(i, j - 1, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                n = search(i, j + 1, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                // i+1
                n = search(i + 1, j - 1, baseID);
                host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
            }
            else
            {
                // i-1
                host.addNeighbor(search(i - 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(search(i - 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                host.addNeighbor(search(i - 1, j + 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                if (j == 2)
                {
                    // i
                    CellNode n = search(i, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(rightToLeft(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(search(i, j + 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));

                    // i+1
                    n = search(i + 1, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(rightToLeft(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else if (j == maxColumn(i) - 1)
                {
                    // i
                    host.addNeighbor(search(i, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(host).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    CellNode n = search(i, j + 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    //i+1
                    host.addNeighbor(search(i + 1, j - 3, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    n = search(i + 1, j - 1, baseID);
                    host.addNeighbor(n.asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(leftToRight(n).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
                else
                {
                    // i
                    host.addNeighbor(search(i, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(search(i, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(search(i, j + 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    // i+1
                    host.addNeighbor(search(i + 1, j - 3, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 2, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j - 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_EDGE));
                    host.addNeighbor(search(i + 1, j, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                    host.addNeighbor(search(i + 1, j + 1, baseID).asNeighbor(Cons.NEIGHBOR_TYPE_VERTEX));
                }
            }
        }

        host.getNeighborList().
            sort((o1, o2) ->
            {
                int id = ((SurfaceTriangle) (o1.getCellNode()).getCell()).getGeocode().getBaseID().compareTo(
                    ((SurfaceTriangle) (o2.getCellNode()).getCell()).getGeocode().getBaseID());
                int diff1 = ((SurfaceTriangle) (o1.getCellNode()).getCell()).getGeocode().getRow()
                    - ((SurfaceTriangle) (o2.getCellNode()).getCell()).getGeocode().getRow();
                int diff2 = ((SurfaceTriangle) (o1.getCellNode()).getCell()).getGeocode().getColumn()
                    - ((SurfaceTriangle) (o2.getCellNode()).getCell()).getGeocode().getColumn();
                if (id == 0)
                {
                    if (diff1 == 0)
                    {
                        return diff2;
                    }
                    return diff1;
                }
                return id;
            });
    }

    private int maxRow()
    {
        return (int) Math.pow(2, getLevel());
    }

    private int maxColumn(int row)
    {
        return 2 * (maxRow() - row) + 1;
    }

    private boolean rowExceeds(int row)
    {
        if (row < 1)
        {
            return true;
        }
        return maxRow() - row < 0;
    }

    private boolean columnExceeds(int row, int column)
    {
        if (rowExceeds(row))
        {
            row = 1;
        }
        return maxColumn(row) - column < 0;
    }

    private CellNode leftToRight(CellNode cell)
    {
        // 对称到右边
        // 行号i不变
        // j = max-j+1;(i,j,rightBaseID)
        Geocode geocode = ((SurfaceTriangle) cell.getCell()).getGeocode();
        int i = geocode.getRow();
        int j = geocode.getColumn();
        // baseID-1
        int index = OctahedronBaseID.valueOf(geocode.getBaseID()).getIndex();
        int rightIndex;
        if (index == 3)
        {
            rightIndex = 0;
        }
        else if (index == 7)
        {
            rightIndex = 4;
        }
        else
        {
            rightIndex = index + 1;
        }
        String baseID = Objects.requireNonNull(OctahedronBaseID.indexOf(rightIndex)).name();
        return search(i, maxColumn(i) - j + 1, baseID);
    }

    private CellNode rightToLeft(CellNode cell)
    {
        // 对称到 左边
        // 行号i不变
        // j = maxJ-j+1;(i,j,leftBaseID)
        Geocode geocode = ((SurfaceTriangle) cell.getCell()).getGeocode();
        int i = geocode.getRow();
        int j = geocode.getColumn();

        // baseID-1
        int index = OctahedronBaseID.valueOf(geocode.getBaseID()).getIndex();
        int leftIndex;
        if (index == 0)
        {
            leftIndex = 3;
        }
        else if (index == 4)
        {
            leftIndex = 7;
        }
        else
        {
            leftIndex = index - 1;
        }
        String baseID = Objects.requireNonNull(OctahedronBaseID.indexOf(leftIndex)).name();
        return search(i, maxColumn(i) - j + 1, baseID);
    }

    private CellNode northGroundCross(CellNode cell)
    {
        // 对称到南部
        // 行号i = 1不变
        // 列号j不变
        // baseID  上下对称，即0->4,1->5,2->6,3->7
        // (i, j, BaseID)
        Geocode geocode = ((SurfaceTriangle) cell.getCell()).getGeocode();
        int i = geocode.getRow();
        int j = geocode.getColumn();
        // baseID-1
        int index = OctahedronBaseID.valueOf(geocode.getBaseID()).getIndex() + 4;
        if (i == 1 && index > 3 && index < 8)
        {
            String baseID = Objects.requireNonNull(OctahedronBaseID.indexOf(index)).name();
            return search(1, j, baseID);
        }
        return null;
    }

    private CellNode southGroundCross(CellNode cell)
    {
        // 对称到北部
        // 行号i = 1不变
        // 列号j不变
        // baseID  上下对称，即0 <-- 4, 1 <-- 5, 2 <-- 6, 3 <-- 7
        // (i, j, BaseID)
        Geocode geocode = ((SurfaceTriangle) cell.getCell()).getGeocode();
        int i = geocode.getRow();
        int j = geocode.getColumn();
        // baseID-1
        int index = OctahedronBaseID.valueOf(geocode.getBaseID()).getIndex();
        int northIndex = index - 4;
        if (i == 1 && northIndex >= 0 && northIndex < 4)
        {
            String baseID = Objects.requireNonNull(OctahedronBaseID.indexOf(northIndex)).name();
            return search(1, j, baseID);
        }
        return null;
    }

    private CellNode vertexToVertex(CellNode cell)
    {
        // 顶点对称
        // 行号i  constant
        // 列号j 1<-->max
        // baseID
        // (i, j, BaseID)
        Geocode geocode = ((SurfaceTriangle) cell.getCell()).getGeocode();
        // baseID-1
        int index = OctahedronBaseID.valueOf(geocode.getBaseID()).getIndex();
        int vertexIndex = -1;
        int i = geocode.getRow();
        int j = vertexIndex;
        String baseID;
        if (i == maxRow() && geocode.getColumn() == 1)
        {
            if (index < 4)
            {
                vertexIndex = (index + 2) % 4;
            }
            else
            {
                vertexIndex = 4 + (index + 2) % 4;
            }
            j = 1;
        }
        if (i == 1 && geocode.getColumn() == 1)
        {
            if (index == 0)
            {
                vertexIndex = 7;
            }
            else if (index < 4)
            {
                vertexIndex = index + 3;
            }
            else if (index == 4)
            {
                vertexIndex = 3;
            }
            else
            {
                vertexIndex = index - 5;
            }
            j = maxColumn(i);
        }
        if (i == 1 && geocode.getColumn() == maxColumn(i))
        {
            if (index < 3)
            {
                vertexIndex = index + 5;
            }
            else if (index == 3)
            {
                vertexIndex = 4;
            }
            else if (index < 7)
            {
                vertexIndex = index - 3;
            }
            else
            {
                vertexIndex = 0;
            }
            j = 1;
        }
        if (vertexIndex != -1 && j != -1)
        {
            baseID = Objects.requireNonNull(OctahedronBaseID.indexOf(vertexIndex)).name();
            return search(i, j, baseID);
        }
        return null;
    }
}
