/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.IO;
import gov.nasa.worldwind.geom.*;

public class GridEdge
{
    private GridPoint first, last;
    private boolean flag;

    public GridEdge(LatLon first, LatLon last)
    {
        this.first = new GridPoint(IO.check(first));
        this.last = new GridPoint(IO.check(last));
        flag = false;
    }
    public GridEdge(Vec4 first, Vec4 last)
    {
        this.first = new GridPoint(IO.check(first));
        this.last = new GridPoint(IO.check(last));
        flag = false;
    }

    public boolean isFlag()
    {
        return flag;
    }

    public void setFlag(boolean flag)
    {
        this.flag = flag;
    }

    public GridPoint getFirst()
    {
        return first;
    }

    public GridPoint getLast()
    {
        return last;
    }
    public GridEdge inverse()
    {
        return new GridEdge(last.latLon,first.latLon);
    }
}
