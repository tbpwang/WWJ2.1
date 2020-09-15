/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;

/**
 * @author Zheng WANG
 * @create 2020/1/29
 * @description 描述LatLon点， 只要两点的latitude和longitude各自相同就认为这两点相同
 */
public class Vertex extends LatLon
{
    public Vertex(Angle latitude, Angle longitude)
    {
        super(latitude, longitude);
    }

    public Vertex(LatLon latLon)
    {
        super(latLon);
    }

    public Vertex(double degreeLatitude, double degreeLongitude)
    {
        super(Angle.fromDegreesLatitude(degreeLatitude), Angle.fromDegreesLongitude(degreeLongitude));
    }

    public LatLon toLatLon()
    {
        return LatLon.fromDegrees(getLatitude().degrees, getLongitude().degrees);
    }

    public Vec4 toVec4()
    {
        return IO.latLonToVec4(toLatLon());
    }

    @Override
    public boolean equals(Object o)
    {
//        super.equals(o);
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        final gov.nasa.worldwind.geom.LatLon latLon = (gov.nasa.worldwind.geom.LatLon) o;

        double thisLatitude = Double.parseDouble(IO.formatDouble(getLatitude().degrees, 6));
        double thisLongitude = Double.parseDouble(IO.formatDouble(getLongitude().degrees % 360, 6));
        double oLatitude = Double.parseDouble(IO.formatDouble(latLon.getLatitude().degrees, 6));
        double oLongitude = Double.parseDouble(IO.formatDouble(latLon.getLongitude().degrees % 360, 6));

        if (thisLatitude == 90.0 && oLatitude == 90.0)
            return true;
        if (thisLatitude == -90.0 && oLatitude == -90.0)
            return true;

        return thisLatitude == oLatitude && thisLongitude == oLongitude;
    }
}
