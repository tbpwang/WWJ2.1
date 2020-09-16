/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import edu.wang.io.Const;

import java.io.Serializable;
import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/5/6 12:41
 * @description
 */
public abstract class Mesh implements Serializable
{
    private List<CellNode> cellNodes;
    private int level;
    private boolean nodeSorted;

    public class CellNode
    {
        private DGG cell;
        private boolean flag;
        private List<Neighbor> neighborList;

        private CellNode()
        {
            this(null);
        }

        private CellNode(DGG cell)
        {
            this(cell, false);
        }

        private CellNode(DGG cell, boolean flag)
        {
            this.cell = cell;
            this.flag = flag;
//            neighborList = null;
        }

        public DGG getCell()
        {
            return cell;
        }

        public boolean isFlag()
        {
            return flag;
        }

        private void setFlag(boolean flag)
        {
            this.flag = flag;
        }

        public List<Neighbor> getNeighborList()
        {
            if (neighborList == null)
                neighborList = new ArrayList<>();
            return neighborList;
        }

        public Neighbor asNeighbor()
        {
//            return new Neighbor();
            return new Neighbor(this);
        }

        public Neighbor asNeighbor(int nodeType)
        {
//            return new Neighbor();
            return new Neighbor(this, nodeType);
        }

        public void addNeighbor(Neighbor neighbor)
        {
            if (neighbor != null)
                this.getNeighborList().add(neighbor);
        }

//        public void addNeighbor(Node neighbor, int neighborType)
//        {
//            Neighbor n = asNeighbor();
////                n.asNeighbour(neighbor);
////                n.setType(type);
////                n.asNeighbour(neighbor,neighborType);
//            n.
//        }

        public void setNeighborList(List<Neighbor> neighborList)
        {
            if (neighborList != null)
            {
                this.neighborList = neighborList;
            }
        }
    }

    public class Neighbor
    {
        //    private DGG host;
        private CellNode cellNode;
        //        private Geocode neighborGeocode;
        private int type;

//        private Neighbor()
//        {
//            this(null);
//        }

        private Neighbor(CellNode cellNode)
        {
            this(cellNode, -1);
        }

        private Neighbor(CellNode cellNode, int type)
        {
            if (cellNode != null)
            {
                this.cellNode = cellNode;
            }
            else
            {
                this.cellNode = new CellNode();
            }
            if (type == Const.NEIGHBOR_TYPE_EDGE || type == Const.NEIGHBOR_TYPE_VERTEX)
            {
                this.type = type;
            }
            else
            {
                this.type = -1;
            }
        }

        public CellNode getCellNode()
        {
            return cellNode;
        }

//        public void setNode(Node node)
//        {
//            this.node = node;
//        }
//
//        public void setNode(Node node, int type)
//        {
//            setNode(node);
//            setType(type);
//        }

        public int getType()
        {
            // DGGS.NEIGHBOR_TYPE_EDGE = 1
            // DGGS.NEIGHBOR_TYPE_VERTEX = 0
            // Error = -1
            return type;
        }

        public void setType(int neighborType)
        {
//            this.type = neighborType;
            if (type == Const.NEIGHBOR_TYPE_VERTEX || type == Const.NEIGHBOR_TYPE_EDGE)
            {
                this.type = neighborType;
            }
            else
            {
                this.type = -1;
            }
        }

        @Override
        public String toString()
        {
            return getCellNode().getCell().getGeocode().toString() +
                ", \'" + (type == 1 ? "Ed" : type == 0 ? "Vt" : "Un") + "\'";
        }
    }

    public Mesh()
    {
        this(-1);
    }

    public Mesh(int level)
    {
        cellNodes = new ArrayList<>();
        this.level = level;
        nodeSorted = false;
//        pathTyppe = -1;
    }

    private boolean isNodeSorted()
    {
        return nodeSorted;
    }

    private void setNodeSorted()
    {
        nodeSorted = true;
    }

    public int getLevel()
    {
        return level;
    }

    public void setCellNodes(List<CellNode> cellNodes)
    {
        if (cellNodes != null)
        {
            this.cellNodes = cellNodes;
        }
    }

    public void addNode(DGG cell)
    {
        //Node node
        if (cell.getLevel() == this.getLevel())
        {
            cellNodes.add(new CellNode(cell));
        }
    }

    public void addNode(CellNode cellNode)
    {
        if (cellNode.getCell().getLevel() == this.getLevel())
        {
            cellNodes.add(cellNode);
        }
    }

    public CellNode search(int row, int column, String baseID)
    {
        // TODO 此处的查找算法有待优化，以便提高查找速度
        List<CellNode> temp = getCellNodes();
//        int listSize = temp.size();
        for (CellNode cellNode : temp)
        {
            Geocode geocode = cellNode.getCell().getGeocode();
            String base = geocode.getBaseID();
            if (base.equals(baseID) && geocode.getRow() == row && geocode.getColumn() == column)
            {
                return cellNode;
            }
        }
        // 新查找方法
        // TODO

        return null;
    }

    public CellNode search(Geocode geocode)
    {
        for (int i = 0; i < getCellNodes().size(); i++)
        {
            if (getCellNodes().get(i).getCell().getGeocode().equals(geocode))
            {
                return getCellNodes().get(i);
            }
        }
        return null;
    }

    public List<CellNode> getCellNodes()
    {
        if (cellNodes != null)
        {
            if (!isNodeSorted())
            {
                cellNodes.sort((o1, o2) -> {
                    Geocode g1, g2;
                    g1 = o1.getCell().getGeocode();
                    g2 = o2.getCell().getGeocode();
                    if (g1.getBaseID().equals(g2.getBaseID()))
                    {
                        if (g1.getRow() == g2.getRow())
                        {
                            return g1.getColumn() - g2.getColumn();
                        }
                        return g1.getRow() - g2.getRow();
                    }
                    return g1.getBaseID().compareTo(g2.getBaseID());
                });
                setNodeSorted();
            }
            return cellNodes;
        }
        return null;
    }

    public void setFlag(DGG cell, boolean flag)
    {
        CellNode cellNode = search(cell.getGeocode());
        if (cellNode != null)
        {
            cellNode.setFlag(flag);
        }
    }

//    public String getPathType()
//    {
//        return pathType;
//    }
//
//    protected void setPathType(String pathType)
//    {
//        this.pathType = pathType;
//    }

    @Override
    public String toString()
    {
        StringBuilder stringNodes = new StringBuilder();

        for (CellNode cellNode : cellNodes)
        {
            stringNodes.append(cellNode.getCell().toString()).append(System.lineSeparator());
        }

        return "Mesh{" +
            "level = " + level +
            ", nodes = " + System.lineSeparator() + stringNodes.toString() +
            '}';
    }
}
