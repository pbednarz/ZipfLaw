/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

import com.google.common.collect.Multiset;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

/*
 * FileChooserDemo.java uses these files:
 *   images/Open16.gif
 *   images/Save16.gif
 */
public class Main extends JPanel
        implements ActionListener {
    GraphPanel graphPanel;
    JButton openButton, saveButton;
    JLabel log;
    JFileChooser fc;
    JTable table;

    public Main() {
        super(new BorderLayout());
        log = new JLabel("status");
        log.setHorizontalAlignment(SwingConstants.LEFT);
        JPanel statusPanel = new JPanel();
        statusPanel.setBorder(new BevelBorder(BevelBorder.LOWERED));
        add(statusPanel, BorderLayout.SOUTH);
        statusPanel.setPreferredSize(new Dimension(getWidth(), 24));
        statusPanel.setLayout(new BoxLayout(statusPanel, BoxLayout.X_AXIS));
        statusPanel.add(log);
        table = new JTable();
        JScrollPane scrollPaneLeft = new JScrollPane(table);
        final JPanel panelRight = new JPanel();


        JScrollPane scrollPaneRight = new JScrollPane(panelRight);
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPaneLeft, scrollPaneRight);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        //Create a file chooser
        fc = new JFileChooser();
        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);
        //Create the save button.  We use the image from the JLF
        //Graphics Repository (but we extracted it from the jar).
        saveButton = new JButton("Save a File...");
        saveButton.addActionListener(this);

        //For layout purposes, put the buttons in a separate panel
        JPanel buttonPanel = new JPanel(); //use FlowLayout
        buttonPanel.add(openButton);
        buttonPanel.add(saveButton);

        //Add the buttons and the log to this panel.
        add(buttonPanel, BorderLayout.PAGE_START);
        add(splitPane, BorderLayout.CENTER);
        splitPane.setDividerLocation(300);

        graphPanel = new GraphPanel(new int[]{});
        graphPanel.setPreferredSize(new Dimension(getWidth() - 100, getHeight() - 100));
        panelRight.setLayout(new BoxLayout(panelRight, BoxLayout.Y_AXIS));
        panelRight.add(Box.createHorizontalGlue());
        panelRight.add(graphPanel);
        panelRight.add(Box.createHorizontalGlue());
    }

    /**
     * Returns an ImageIcon, or null if the path was invalid.
     */
    protected static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = Main.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event dispatch thread.
     */
    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("FileChooserDemo");
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        //Add content to the window.
        frame.add(new Main());
        //Display the window.
        frame.setPreferredSize(new Dimension(800, 600));
        frame.pack();
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        //Schedule a job for the event dispatch thread:
        //creating and showing this application's GUI.
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                //Turn off metal's use of bold fonts
                UIManager.put("swing.boldMetal", Boolean.FALSE);
                createAndShowGUI();
            }
        });
    }

    public void actionPerformed(ActionEvent e) {

        //Handle open button action.
        if (e.getSource() == openButton) {
            int returnVal = fc.showOpenDialog(Main.this);

            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would open the file.
                log.setText("Opening: " + file.getName() + ".");
                try {

                    List<Multiset.Entry> list = ZipfLawUtils.readFile(file);

                    table.setModel(new ZipfTableModel(list));

                    int[] dane = new int[list.size() > 50 ? 50 : list.size()];

                    for (int i = 0; i < dane.length && i < 40; i++) {
                        dane[i] = list.get(i).getCount();
                    }
                    graphPanel.setScores(dane);

                    log.setText(list.size() + " words found.");
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            } else {
                log.setText("Open command cancelled by user.");
            }

            //Handle save button action.
        } else if (e.getSource() == saveButton) {
            int returnVal = fc.showSaveDialog(Main.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                //This is where a real application would save the file.
                log.setText("Saving: " + file.getName() + ".");
            } else {
                log.setText("Save command cancelled by user.");
            }
        }
    }
}