/**
 * @author Piotr Bednarz
 * @date 25.05.2015
 */
package pl.pbednarz.systemyrozproszone;
import com.google.common.collect.Multiset;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

public class Main extends JPanel
        implements ActionListener {
    private final static int GRAPH_LIMIT = 200;
    GraphPanel graphPanel;
    JButton openButton, saveButton;
    JLabel log;
    JFileChooser fc;
    JTable table;
    List<Multiset.Entry> list;

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
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                scrollPaneLeft, panelRight);
        splitPane.setContinuousLayout(true);
        splitPane.setOneTouchExpandable(true);
        fc = new JFileChooser();
        openButton = new JButton("Open a File...");
        openButton.addActionListener(this);
        saveButton = new JButton("Export a result...");
        saveButton.addActionListener(this);
        saveButton.setEnabled(false);

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

    private static void createAndShowGUI() {
        //Create and set up the window.
        JFrame frame = new JFrame("Zipf's law - Piotr Bednarz");
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

                    list = ZipfLawUtils.readFile(file);

                    table.setModel(new ZipfTableModel(list));

                    int[] dane = new int[list.size() > GRAPH_LIMIT ? GRAPH_LIMIT : list.size()];

                    for (int i = 0; i < dane.length && i < GRAPH_LIMIT; i++) {
                        dane[i] = list.get(i).getCount();
                    }
                    graphPanel.setScores(dane);
                    saveButton.setEnabled(true);
                    log.setText(file.getName() + " - " + list.size() + " words found.");
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
                if (list != null) {
                    //This is where a real application would save the file.
                    log.setText("Saving: " + file.getName() + ".");
                    try {
                        ZipfLawUtils.writeToFile(file, list);
                        log.setText("Saved.");
                    } catch (IOException e1) {
                        e1.printStackTrace();
                        log.setText("Couldn't export data: " + e1.getLocalizedMessage());
                    }
                }
            } else {
                log.setText("Export command cancelled by user.");
            }
        }
    }
}