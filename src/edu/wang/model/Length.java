/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.*;
import gov.nasa.worldwind.util.Logging;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/3 23:48
 * @Param:
 * @Description： 计算边长, 空间直线长度，大圆弧长度，小圆弧长度
 */
public class Length
{
    public static double calculateLineLength(LatLon p1, LatLon p2)
    {
        if (p1 == null || p2 == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }

        Vec4 v1 = IO.latLonToVec4(p1).normalize3().multiply3(Const.RADIUS);
        Vec4 v2 = IO.latLonToVec4(p2).normalize3().multiply3(Const.RADIUS);

        return calculateLineLength(v1, v2);
    }

    public static double calculateLineLength(Vec4 v1, Vec4 v2)
    {
        if (v1 == null || v2 == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        return v1.distanceTo3(v2);
    }

    public static double calculateArcLength(String sideType, LatLon p1, LatLon p2)
    {
        return calculateArcLengthInUnit(sideType, p1, p2) * Const.RADIUS;
    }

    public static double calculateArcLengthInUnit(String sideType, LatLon p1, LatLon p2)
    {
        if (p1 == null || p2 == null)
        {
            String msg = Logging.getMessage("NullValue.空值");
            Logging.logger().severe(msg);
            throw new IllegalArgumentException(msg);
        }
        double length = -1.0;

        if (sideType.equals(AVKey.GREAT_CIRCLE))
        {
            length = LatLon.greatCircleDistance(p1, p2).radians;
        }
        if (sideType.equals(AVKey.LOXODROME) || sideType.equals(AVKey.RHUMB_LINE))
        {
            length = LatLon.rhumbDistance(p1, p2).radians;
        }
        if (length > Math.PI / 2)
        {
            length = Math.PI - length;
        }
        return length <= 0 ? 0.0 : length;
    }
}
