/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import gov.nasa.worldwind.geom.LatLon;

import java.io.Serializable;

/**
 * @author Zheng WANG
 * @create 2019/5/17
 * @description 正八面体的基面代码，北半球从左到右依次是： A，B，C，D， 南半球从左到右依次是：E，F，G，H，具有对称性
 * @parameter
 */
public enum OctahedronBaseID implements Serializable
{
    A(0),
    B(1),
    C(2),
    D(3),
    E(4),
    F(5),
    G(6),
    H(7);
//    A("A", 0),
//    B("B", 1),
//    C("C", 2),
//    D("D", 3),
//    E("E", 4),
//    F("F", 5),
//    G("G", 6),
//    H("H", 7);

    private int index;
//    private String name;

    OctahedronBaseID(int index)
    {
//        this.name = name;
        if (index >= 0 && index <= 7)
            this.index = index;
        else
            this.index = 0;
    }

    public int getIndex()
    {
        return index;
    }

    public static OctahedronBaseID indexOf(int index)
    {
        for (OctahedronBaseID cell : OctahedronBaseID.values())
        {
            if (cell.getIndex() == index % OctahedronBaseID.values().length)
            {
                return cell;
            }
        }
        return null;
    }

    public LatLon[] poles()
    {
        return this.poles(0);
    }
    public LatLon[] poles(int index)
    {
        // （下底边对应的）上极、（右侧边对应的）左极、（左侧边对应的）右极
        OctahedronBaseID id = indexOf(index);
        LatLon[] poles = new LatLon[3];
        assert id != null;
        switch (id.getIndex())
        {
            case 1:
                poles[0] = LatLon.fromDegrees(90, 90);
                poles[1] = LatLon.fromDegrees(0, 90);
                poles[2] = LatLon.fromDegrees(0, 180);
                break;
            case 2:
                poles[0] = LatLon.fromDegrees(90, -90);
                poles[1] = LatLon.fromDegrees(0, -180);
                poles[2] = LatLon.fromDegrees(0, -90);
                break;
            case 3:
                poles[0] = LatLon.fromDegrees(90, 0);
                poles[1] = LatLon.fromDegrees(0, -90);
                poles[2] = LatLon.fromDegrees(0, 0);
                break;
            case 4:
                poles[0] = LatLon.fromDegrees(-90, 0);
                poles[1] = LatLon.fromDegrees(0, 0);
                poles[2] = LatLon.fromDegrees(0, 90);
                break;
            case 5:
                poles[0] = LatLon.fromDegrees(-90, 90);
                poles[1] = LatLon.fromDegrees(0, 90);
                poles[2] = LatLon.fromDegrees(0, 180);
                break;
            case 6:
                poles[0] = LatLon.fromDegrees(-90, -90);
                poles[1] = LatLon.fromDegrees(0, -180);
                poles[2] = LatLon.fromDegrees(0, -90);
                break;
            case 7:
                poles[0] = LatLon.fromDegrees(-90, 0);
                poles[1] = LatLon.fromDegrees(0, -90);
                poles[2] = LatLon.fromDegrees(0, 0);
                break;
            case 0:
            default:
                poles[0] = LatLon.fromDegrees(90, 0);
                poles[1] = LatLon.fromDegrees(0, 0);
                poles[2] = LatLon.fromDegrees(0, 90);
                break;
        }
        return poles;
    }
}
