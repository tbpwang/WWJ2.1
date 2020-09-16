/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.Const;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

/**
 * @author Zheng WANG
 * @create 2019/12/17
 * @description 计算两点之间的内蕴距离即计算球面距离 同Latlon.greatCircleDistance
 */
public class Distance
{
    public static double distance(LatLon p1, LatLon p2)
    {
        return distance(p1, p2, Const.RADIUS);
    }

    public static double distance(LatLon p1, LatLon p2, double radius)
    {
        if (p1 == null || p2 == null)
        {
            String message = Logging.getMessage("nullValue.输入空值");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }
        if (LatLon.equals(p1,p2))
            return 0.0;
        Angle lat1 = p1.getLatitude();
        Angle lon1 = p1.getLongitude();
        Angle lat2 = p2.getLatitude();
        Angle lon2 = p2.getLongitude();
        double deltaX, deltaY, deltaZ;
        // ∆X=cos⁡〖φ_2 〗  cos⁡〖λ_2 〗-cos⁡〖φ_1 〗  cos⁡〖λ_1 〗
        deltaX = lat2.cos() * lon2.cos() - lat1.cos() * lon1.cos();
        // ∆Y=cos⁡〖φ_2 〗  sin⁡〖λ_2 〗-cos⁡〖φ_1 〗  sin⁡〖λ_1 〗
        deltaY = lat2.cos() * lon2.sin() - lat1.cos() * lon1.sin();
        // ∆Z=sin⁡〖φ_2 〗-sin⁡〖φ_1 〗
        deltaZ = lat2.sin() - lat1.sin();
        // C=√(〖∆X〗^2+〖∆Y〗^2+〖∆Z〗^2 )
        // d=R*2 sin^(-1)⁡〖C/2〗
        double C = Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);
        return radius * 2 * Math.asin(C / 2);
    }
}
