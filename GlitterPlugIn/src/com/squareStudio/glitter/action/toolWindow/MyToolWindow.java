// Copyright 2000-2020 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.squareStudio.glitter.action.toolWindow;

import com.google.gson.Gson;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.squareStudio.glitter.action.util.Initial;

import javax.swing.*;
import java.io.File;
import java.util.Calendar;
import java.util.Map;

public class MyToolWindow {

  private JButton refreshToolWindowButton;
  private JButton hideToolWindowButton;
  private JLabel currentDate;
  private JLabel currentTime;
  private JLabel timeZone;
  private JPanel myToolWindowContent;
  private JComboBox comboBox1;
  private JTextField textField1;
  private JRadioButton androidRadioButton;
  private JRadioButton IOSRadioButton;
  private JButton selectFile;
  private JButton refrsh;
  private JButton buildAndriodButton;
  private JButton buildIOSButton;
  private Map obj=Initial.INSTANCE.getVersion();
  final JFileChooser fc = new JFileChooser();

  public MyToolWindow(ToolWindow toolWindow) {
    hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
    refreshToolWindowButton.addActionListener(e -> Initial.INSTANCE.initial(obj.get(comboBox1.getSelectedItem().toString()).toString(),textField1.getText().toString(),fc.getSelectedFile(),true));
    refrsh.addActionListener(e->{
      obj=Initial.INSTANCE.getVersion();
      comboBox1.removeAllItems();
      for (Object key : obj.keySet()) {
        comboBox1.addItem(key);
      }
    });
    for (Object key : obj.keySet()) {
      comboBox1.addItem(key);
    }
    selectFile.addActionListener(e -> {
      int returnVal = fc.showOpenDialog(myToolWindowContent);
      if (returnVal == JFileChooser.APPROVE_OPTION) {
        File file = fc.getSelectedFile();
        selectFile.setText(file.getName());
      }
    });
  }

  public JPanel getContent() {
    return myToolWindowContent;
  }

}
