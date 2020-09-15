/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import java.io.Serializable;

/**
 * @author Zheng WANG
 * @create 2019/5/17
 * @description
 * @parameter
 */
public abstract class DGG implements Serializable
{
//    abstract String getID();
    abstract Geocode getGeocode();
    abstract int getLevel();
    abstract int getShape();
}
