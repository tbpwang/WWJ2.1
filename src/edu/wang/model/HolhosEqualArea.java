/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.model;

import edu.wang.io.*;
import gov.nasa.worldwind.geom.*;

/**
 * @author Zheng WANG
 * @create 2019/10/18
 * @description According to Holhoş et.al,2014, "An octahedral equal area partition of the sphere and near optimal
 * configurations of points"
 */
public class HolhosEqualArea
{
    // P'(X,Y,Z) in the plane, called planePoint or point3D
    // P(x,y,z) on the sphere, called surfacePoint or inversePoint
    // theta is a co-latitude in radians, and phi is a longitude in radians.
    public static Vec4 toPlanePoint(LatLon spherePoint)
    {
        double colatitude = Math.signum(spherePoint.latitude.radians) * (Math.PI / 2 - Math.abs(
            spherePoint.latitude.radians));
        return toPlanePoint(colatitude, spherePoint.longitude.radians);
    }

    public static Vec4 toPlanePoint(Vec4 spherePoint)
    {
        return toPlanePoint(spherePoint.getX(), spherePoint.getY(), spherePoint.getZ());
//        double x,y,z;
//        x = spherePoint.getX();
//        y = spherePoint.getY();
//        z = spherePoint.getZ();
//        double r = Constant.radius;
//        if (spherePoint.getLength3()>1)
//        {
//            x = x/r;
//            y = y/r;
//            z = z/r;
//        }
//        return planePoint(x,y,z);

    }

    public static Vec4 toPlanePoint(double sphereX, double sphereY, double sphereZ)
    {
        double R = Const.RADIUS;
        if (!isUnitSphere(sphereX, sphereY, sphereZ))
        {
            sphereX = sphereX / R;
            sphereY = sphereY / R;
            sphereZ = sphereZ / R;
        }
        double L = Math.sqrt(2 * Math.PI / Math.sqrt(3));
        double sqrt1subabsz = Math.sqrt(1 - Math.sqrt(sphereZ));
        double temp = L * sqrt1subabsz / Math.sqrt(2) / Math.PI;
        double arctanyx = Math.atan2(Math.abs(sphereY), Math.abs(sphereX));
        double X = Math.signum(sphereX) * temp * (Math.PI - 2 * arctanyx);
        double Y = Math.signum(sphereY) * temp * 2 * arctanyx;
        double Z = Math.signum(sphereZ) * L * (1 - sqrt1subabsz) / Math.sqrt(2);
        Vec4 vec4 = new Vec4(X, Y, Z);
        return vec4.multiply3(R);
    }

    public static Vec4 toPlanePoint(double coLatitude, double longitude)
    {
        double L = Math.sqrt(2 * Math.PI / Math.sqrt(3));
        double ldivsqrt2 = L / Math.sqrt(2);
        double sqrt1subabscosphi = Math.sqrt(1 - Math.abs(Math.cos(coLatitude)));
        double arctanabstantheta = Math.atan(Math.abs(Math.tan(longitude)));
        double X = Math.signum(Math.cos(longitude)) * ldivsqrt2 * sqrt1subabscosphi * (Math.PI - 2 * arctanabstantheta)
            / Math.PI;
        double Y = Math.signum(Math.sin(longitude)) * ldivsqrt2 * sqrt1subabscosphi * 2 * arctanabstantheta / Math.PI;
//        double Z = Math.signum(Math.cos(coLatitude)) * ldivsqrt2 * (1 - sqrt1subabscosphi);
        double Z = Math.signum(Math.sin(coLatitude)) * ldivsqrt2 * (1 - sqrt1subabscosphi);
        Vec4 vec4 = new Vec4(X, Y, Z);
        return vec4.multiply3(Const.RADIUS);
    }

    public static LatLon toSpherePoint(Vec4 planePoint)
    {
        return toSpherePoint(planePoint.getX(), planePoint.getY(), planePoint.getZ());
    }

    public static LatLon toSpherePoint(double planeX, double planeY, double planeZ)
    {
        double R = Const.RADIUS;
//        double X = Math.abs(planeX) >= R ? planeX / R : planeX;
//        double Y = Math.abs(planeY) >= R ? planeY / R : planeY;
//        double Z = Math.abs(planeZ) >= R ? planeZ / R : planeZ;

        if (!isUnitSphere(planeX, planeY, planeZ))
        {
            planeX = planeX / R;
            planeY = planeY / R;
            planeZ = planeZ / R;
        }
        double L = Math.sqrt(2 * Math.PI / Math.sqrt(3));
//        double sqrt1subpowZ2 = Math.sqrt(1 - Math.pow(planeZ, 2));
        double sqrt1subpowZ2 = Math.abs(planeZ) >= 1 ? Math.sqrt(Math.pow(planeZ, 2) - 1) : Math.sqrt(1 - Math.pow(planeZ, 2));
        double piYdivXaddYdiv2 = Math.PI * planeY / (2.0 * (Math.signum(planeY / planeX) * planeX + planeY));
        if (Double.isNaN(piYdivXaddYdiv2) && planeY == 0)
        {
            piYdivXaddYdiv2 = 0.0;
        }
        double x = Math.signum(planeX) == 0 ? 0 : Math.signum(planeX) * sqrt1subpowZ2 * Math.cos(piYdivXaddYdiv2);
        double y = Math.signum(planeY) == 0 ? 0 : Math.signum(planeY) * sqrt1subpowZ2 * Math.sin(piYdivXaddYdiv2);
        double z = Math.signum(planeZ) == 0 ? 0 : 2.0 * planeZ * (Math.sqrt(2) * L - Math.abs(planeZ)) / Math.pow(L, 2);
        if (Math.signum(planeX) == 0 && Math.signum(planeY) == 0 && Math.signum(planeZ) != 0)
        {
            z = Math.signum(planeZ) > 0 ? 1 : -1;
        }
        Vec4 vec4 = new Vec4(x, y, z);
        return IO.vec4ToLatLon(vec4);
    }

    private static boolean isUnitSphere(double x, double y, double z)
    {
        return IO.check(Math.abs(Math.sqrt(x * x + y * y + z * z) - 1.0)) <= Const.EPSILON;
    }
}
