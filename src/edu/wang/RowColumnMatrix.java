/*
 * Copyright (C) 2020 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang;

/**
 * @Author: Joel Wang
 * @Time: 2020/10/21 17:08
 * @Param:
 */
public class RowColumnMatrix
{
    private int row;
    private int column;
//    private List<RowColumn> matrix;

    public RowColumnMatrix(int level)
    {
//        matrix = new ArrayList<>();
        String[][] rowCols;
        String[][] temps;
        String[][] meta = new String[2][2];
        meta[0][0] = "1";
        meta[0][1] = "2";
        meta[1][0] = "0";
        meta[1][1] = "3";
        for (int i = 0; i < level; i++)
        {
            int thisLine = (int) (Math.pow(2, i));
            int expand = (int) (Math.pow(2, i + 1));
            rowCols = new String[expand][expand];
            temps = new String[thisLine][thisLine];
            if (i == 0)
            {
                temps[i][i] = "1";
                rowCols = meta;
            }
            else
            {
                for (int j = 0; j < 4; j++)
                {
                    for (int k = 0; k < 4; k++)
                    {
                        for (int l = 0; l < i; l++)
                        {
                            // TODO Z曲线编码与行列号的对应关系
                        }
                    }
                }
            }
        }
        int edge = (int) Math.pow(2, level);
        for (int i = 0; i < edge; i++)
        {
            for (int j = 0; j < edge; j++)
            {
                // matrix.add(new RowColumn(i, j));
            }
        }
    }

//    private String[][] expandMatrix(char[][] baseMeta2by2)
//    {
//        // 2*2
//        String first1 = String.valueOf(baseMeta2by2[0][0]);
//        String first2 = String.valueOf(baseMeta2by2[0][1]);
//        String second1 = String.valueOf(baseMeta2by2[1][0]);
//        String second2 = String.valueOf(baseMeta2by2[1][1]);
//
//        int next = (int) Math.pow(2,baseMeta2by2.length + 1);
//
//        char[][] expand = new char[next][next];
//
//        for (int i = 0; i < baseMeta2by2.length; i++)
//        {
//            for (int j = 0; j < baseMeta2by2.length; j++)
//            {
//                expand[i][j]=baseMeta2by2[i][j]
//            }
//        }
//
//    }

    public int getColumn()
    {
        return column;
    }

    public int getRow()
    {
        return row;
    }

    private class RowColumn
    {
        private int row;
        private int column;

        RowColumn(int row, int column)
        {
            if (row > 0)
            {
                this.row = row;
            }
            else
            {
                this.row = 0;
            }
            if (column > 0)
            {
                this.column = column;
            }
            else
            {
                this.column = 0;
            }
        }
    }
}
