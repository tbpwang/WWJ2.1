/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.analysis;

import edu.wang.impl.FileChooser;
import edu.wang.io.IO;
import gov.nasa.worldwind.awt.ViewInputAttributes;

import javax.swing.*;
import java.io.*;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/26 15:27
 * @Param: split a TXT to the size of 4^10 lines
 */
public class SplitAFile
{
    public static void main(String[] args) throws IOException
    {
        JFileChooser fileChooser = new JFileChooser();
        String pathName = "D:\\outData\\QTM8-12";
        fileChooser.setCurrentDirectory(new File(pathName));
        fileChooser.showOpenDialog(null);
        File file = fileChooser.getSelectedFile();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String tempStr = "";
        int lines = 0;
        int interval = (int) Math.pow(4, 10);
        StringBuilder contents = new StringBuilder();
        while (tempStr != null)
        {
            lines++;
            tempStr = reader.readLine();
            contents.append(tempStr).append(System.lineSeparator());
            if (lines % interval == 0)
            {
                int part = lines / interval;
                //String path = file.getParent();
                IO.write(file.getParent(), "Part" + part, contents.toString());
                contents.setLength(0);
            }
        }
    }
}
