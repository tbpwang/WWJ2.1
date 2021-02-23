/*
 * Copyright (C) 2021 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package edu.wang.impl;

import javax.swing.*;
import java.awt.event.*;
import java.io.File;

/**
 * @Author: Joel Wang
 * @Time: 2021/2/23 10:30
 * @Param:
 */
public class FileChooser extends JFrame implements ActionListener
{
    JButton open;

    public static void main(String[] args)
    {
        new FileChooser();
    }

    public FileChooser()
    {
        open = new JButton("Open");
        this.add(open);
        this.setBounds(400,200,100,100);
        this.setVisible(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        open.addActionListener(this);
    }

    @Override
    public void actionPerformed(ActionEvent e)
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.showDialog(new JLabel(),"Choose");
        File file = chooser.getSelectedFile();
        if (file.isDirectory())
            System.out.println("Folder:\t" + file.getAbsolutePath());
        else if (file.isFile())
            System.out.println("File:\t"+file.getAbsolutePath());
        else
            System.out.println(chooser.getSelectedFile().getName());

    }
}
