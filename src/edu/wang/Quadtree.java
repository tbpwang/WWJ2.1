/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import java.io.Serializable;
import java.util.List;

/**
 * @author Zheng WANG
 * @create 2019/4/30 11:11
 * @description
 * @parameter
 */
public abstract class Quadtree implements Serializable
{
    // TODO
    private List<Cell> data;
    private Quadtree first,second,third, fourth;
    private int depth;
    private boolean isLeaf;
}
