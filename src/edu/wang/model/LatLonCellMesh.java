/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.Rank;
import gov.nasa.worldwind.geom.LatLon;
import gov.nasa.worldwind.util.Logging;

import java.util.*;

/**
 * @Author: Joel Wang
 * @Time: 2020/10/20 11:26
 * @Param: 1/8 sphere
 */
public class LatLonCellMesh
{
    private List<LatLonCell> mesh;
    private int level;

    public LatLonCellMesh(int level)
    {
        if (level < 0)
            this.level = 0;
        else
            this.level = level;

        mesh = new ArrayList<>();
        fillMesh();
    }

    public int getLevel()
    {
        return level;
    }

    public List<LatLonCell> getMesh()
    {
        return mesh;
    }

    private void findNeighbor()
    {

    }

    private void fillMesh()
    {
        // ## default base cell
        LatLon topPole = LatLon.fromDegrees(90.0, 0.0);
        LatLon baseLeft = LatLon.fromDegrees(0.0, 0.0);
        LatLon baseRight = LatLon.fromDegrees(0.0, 90.0);
        LatLonCell baseCell = new LatLonCell(topPole, baseLeft, baseRight, topPole, "A");
        // ##

        for (int i = 0; i < this.level; i++)
        {
            if (i == 0)
            {
                mesh.addAll(Arrays.asList(baseCell.refine()));
                continue;
            }
            List<LatLonCell> subCells = new ArrayList<>();
            for (LatLonCell cell : mesh)
            {
                subCells.addAll(Arrays.asList(cell.refine()));
            }
            mesh.clear();
            mesh.addAll(subCells);
        }
    }

//    @Override
//    public String toString()
//    {
//        return super.toString();
//    }

    class Adjacency
    {
        private LatLonCell cell;
        private List<LatLonCell> neighbors;
        private final int edgeAdjacency = 1;
        private final int angleAdjacency = 2;
        private Rank rank;

        public Adjacency(LatLonCell cell)
        {
            String id;
            if (find(cell))
            {
                id = cell.getID();
                //int from = whereNumber(id);
//                if (from == 0)
//                {
//                    neighbors = null;
//                }
                ////////////
            }
            else
            {
                String message = Logging.getMessage("nullError.该单元不在默认格网内");
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
            }
        }

        private int[] id2RowCol(int level)
        {
            //
            return null;
        }

        private boolean find(LatLonCell host)
        {
            for (LatLonCell cell : mesh)
            {
                if (cell.equals(host))
                    return true;
            }
            return false;
        }
    }

    private class LatLonCellMeshRank implements Rank
    {
        private int row;
        private int col;
        private String ID;

        //  0   3
        //  1   2
        LatLonCellMeshRank(String ID)
        {
            this.ID = ID;
            int from = whereNumber(this.ID);
            if (from == 0)
            {
                setColumn(0);
                setRow(0);
            }
            char[] numbers = ID.substring(from).toCharArray();
            List<Character> temps = new ArrayList<>();

        }

        private int whereNumber(String string)
        {
            for (int i = 0; i < string.length(); i++)
            {
                char c = string.charAt(i);
                if (Character.isDigit(c))
                    return i;
            }
            return 0;
        }

        @Override
        public String getBaseID()
        {
            return this.ID;
        }

        @Override
        public void setRow(int row)
        {
            this.row = row;
        }

        @Override
        public int getRow()
        {
            return row;
        }

        @Override
        public void setColumn(int column)
        {
            this.col = column;
        }

        @Override
        public int getColumn()
        {
            return col;
        }
    }
}
