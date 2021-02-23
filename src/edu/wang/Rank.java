/*
 * Copyright (C) 2019 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

import java.io.Serializable;

/**
 * @author Zheng WANG
 * @create 2019/5/16
 * @description row, column, ID, baseID
 * @parameter
 */
public interface Rank extends Serializable
{
    String getBaseID();
    void setRow(int row);
    int getRow();
    void setColumn(int column);
    int getColumn();
}
