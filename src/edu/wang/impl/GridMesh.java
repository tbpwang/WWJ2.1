/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import edu.wang.model.FibonacciLattice;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwindx.examples.*;
import gov.nasa.worldwindx.examples.lineofsight.PointGrid;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/11/19
 * @description
 */
public class GridMesh extends ApplicationTemplate
{
    public static class GridMeshAppFrame extends ApplicationTemplate.AppFrame
    {
        public GridMeshAppFrame()
        {
            super(true,true,false);
            //
            int number = (int) 1E10;
            List<Position> positions = FibonacciLattice.getFibonacci(number);
            PointGrid grid = new PointGrid(positions,positions,positions.size());


            RenderableLayer layer = new RenderableLayer();
            layer.addRenderable(grid);
            insertBeforeCompass(getWwd(),layer);
        }
    }

    public static void main(String[] args)
    {
        start("GridMeshAppFrame",GridMeshAppFrame.class);
    }
}
