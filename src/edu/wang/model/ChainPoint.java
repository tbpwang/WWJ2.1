/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import gov.nasa.worldwind.geom.*;

import java.util.*;

/**
 * @author Zheng WANG
 * @create 2019/8/30
 * @description
 */
public class ChainPoint extends LatLon
{
    private List<ChainPoint> neighbors;
    private LatLon latLon;
    private String ID;

    public ChainPoint(LatLon latLon)
    {
        this(latLon, "");
    }

    public ChainPoint(LatLon latLon, String ID)
    {
        super(latLon);
        this.latLon = latLon;
        this.ID = ID;
        neighbors = new ArrayList<>();
    }

    public List<ChainPoint> getNeighbors()
    {
        return neighbors;
    }

    public void addNeighbor(ChainPoint chainPoint)
    {
        neighbors.add(chainPoint);
    }

    public void remove(int index)
    {
        neighbors.remove(index);
    }

    public void remove(ChainPoint chainPoint)
    {
        neighbors.remove(chainPoint);
    }

    public LatLon getLatLon()
    {
        return latLon;
    }

    public String getID()
    {
        return ID;
    }

    public String toString()
    {
        String prefix = ID.isEmpty() ? "" : (ID + ", ");
        return prefix + getLatLon().toString();
    }
}
