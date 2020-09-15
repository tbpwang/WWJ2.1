/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

/**
 * @author Zheng WANG
 * @create 2019/12/17
 * @description 邻近格点方位角
 * 同Latlon.greatCircleAzimuth
 */
public class Azimuth
{
    public static Angle azimuth(LatLon p1, LatLon p2)
    {
        if (p1 == null || p2 == null)
        {
            String message = Logging.getMessage("nullValue.输入空值");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        Angle lat1 = p1.getLatitude();
        Angle lon1 = p1.getLongitude();
        Angle lat2 = p2.getLatitude();
        Angle lon2 = p2.getLongitude();
        Angle deltaLon = lon2.subtract(lon1);
        if (LatLon.equals(p1,p2))
            return Angle.ZERO;
        if (lon1.equals(lon2))
            return lat1.degrees > lat2.degrees ? Angle.POS180 : Angle.ZERO;
         // sin⁡∆λ  cos⁡〖φ_2 〗
        double y = deltaLon.sin()*lat2.cos();
        // cos⁡〖φ_1 〗  sin⁡〖φ_2 〗-sin⁡〖φ_1 〗  cos⁡〖φ_2 〗  cos⁡∆λ
        double x = lat1.cos()*lat2.sin()-lat1.sin()*lat2.cos()*deltaLon.cos();
        double azimuthRadians = Math.atan2(y,x);
        return Double.isNaN(azimuthRadians) ? Angle.ZERO : Angle.fromRadians(azimuthRadians);

    }

}
