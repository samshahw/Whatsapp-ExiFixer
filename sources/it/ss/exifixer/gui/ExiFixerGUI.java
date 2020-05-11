package it.ss.exifixer.gui;

import it.ss.exifixer.FilesInFolderIterator;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@SuppressWarnings("Duplicates")
public class ExiFixerGUI extends JFrame {

  private static ExiFixerGUI frame;
  private static JTextArea displayOutput;

  private static File sourceFolder;
  private static File outputFolder;


  public ExiFixerGUI() {

    // Basic frame properties
    super("WhatsApp ExiFixer");
    setSize(900, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setVisible(true);
  }


  // Swing's own look and feel
  private static void setLookAndFeel() {

    try {

      UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");

    } catch (Exception e) {

      e.printStackTrace();
    }
  }


  private static Font setFonts() {

    return new Font("Helvetica Neue", Font.PLAIN, 14);
  }


  public static void execute() {

    // Creates instance of frame
    frame = new ExiFixerGUI();

    setLookAndFeel();

    /* ----- UPPER REGION ----- */

    // Upper menu creation
    JMenuBar upperMenu = new JMenuBar();

    JMenu file = new JMenu("File");
    JMenu help = new JMenu("Help");

    upperMenu.add(file);
    upperMenu.add(help);

    // "File" submenu creation
    JMenuItem source = new JMenuItem("Source folder");
    JMenuItem destination = new JMenuItem("Destination");
    JMenuItem exit = new JMenuItem("Exit");

    file.add(source);
    file.add(destination);
    file.add(exit);

    // "Help" submenu creation
    JMenuItem github = new JMenuItem("Report a bug");

    help.add(github);

    /* ----- CENTRAL REGION ----- */

    // Displays verbose output of exif fix
    JPanel centralPanel = new JPanel();
    centralPanel.setLayout(new BorderLayout());

    // Adds text area to panel
    displayOutput = new JTextArea();

    displayOutput.setLineWrap(true);
    displayOutput.setWrapStyleWord(true);
    displayOutput.setEditable(false);
    displayOutput.setFont(setFonts());
    displayOutput.setText(
        "\nWelcome to Whatsapp ExiFixer!\n\nThis is an unofficial tool to fix the dates of the " +
            "images from a WhatsApp backup.\nTo start, go to \"File\" and then select the " +
            "folder where the original photos are stored, \"Source folder\", and the folder " +
            "where the new, fixed photos will be stored, \"Destination folder\".\n" +
            "Finally, press \"Start\".\n\nnb. Start and Stop buttons may not be displayed, if so," +
            " just resize the window.\n");

    centralPanel.add(displayOutput);

    // Adds scroll pane
    JScrollPane scroll = new JScrollPane(displayOutput);
    centralPanel.add(scroll, BorderLayout.CENTER);

    /* ----- LOWER REGION ----- */

    // Start and stop the exif fixer
    JPanel bottomPanel = new JPanel();

    JButton start = new JButton("Start");
    JButton stop = new JButton("Stop");

    bottomPanel.add(start);
    bottomPanel.add(stop);

    /* ----- FUNCTIONALITY ----- */

    // Appends the panels to the frame
    frame.getContentPane().add(BorderLayout.NORTH, upperMenu);
    frame.getContentPane().add(BorderLayout.CENTER, centralPanel);
    frame.getContentPane().add(BorderLayout.SOUTH, bottomPanel);

    // Adds action listeners to the upper menu items
    source.addActionListener(new SourceDirListener());
    destination.addActionListener(new OutputDirListener());
    exit.addActionListener(new ExitListener());

    github.addActionListener(new GithubListener());

    // Adds action listeners to the bottom panel items
    start.addActionListener(new StartListener());
    stop.addActionListener(new StopListener());
  }


  // Method used by external classes to display text through the GUI
  public static void printGui(String text) {

    displayOutput.append("\n" + text + "\n");
  }


  // "Source folder" button listener
  private static class SourceDirListener implements ActionListener {

    // Opens file explorer in order to get the folder paths needed
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      JFileChooser files = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      files.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      int returnValue = files.showSaveDialog(null);

      if (returnValue == JFileChooser.APPROVE_OPTION) {
        if (files.getSelectedFile().isDirectory()) {

          sourceFolder = files.getSelectedFile();
          printGui("Selected source folder: " + files.getSelectedFile());
        }
      }
    }

  }



  // "Destination" button listener
  private static class OutputDirListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      JFileChooser files = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
      files.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

      int returnValue = files.showSaveDialog(null);

      if (returnValue == JFileChooser.APPROVE_OPTION) {
        if (files.getSelectedFile().isDirectory()) {

          outputFolder = files.getSelectedFile();
          printGui("Selected destination folder: " + files.getSelectedFile());
        }
      }
    }

  }



  // "Exit" button listener
  private static class ExitListener implements ActionListener {

    // Performs exit and close action
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      System.exit(0);
    }

  }



  // "GitHub" button listener
  private static class GithubListener implements ActionListener {

    // Redirects to the Github page of the project
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      try {

        Desktop.getDesktop().browse(new URI("https://github.com/samshahw/Galaxy"));

      } catch (IOException | URISyntaxException e) {

        e.printStackTrace();
      }
    }

  }



  // "Start" button listener
  private static class StartListener implements ActionListener {

    // Begins exif fix
    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      /* If source and destination folders are specified, once
       * start is clicked it will start operations.
       * If either one or both folders are not specified, it
       * will ask you to do so.
       */

      if (sourceFolder == null || outputFolder == null) {

        printGui("Error: Either source or destination folder are not specified!");

      } else {

        printGui("Starting dates fix...");

        try {

          FilesInFolderIterator.iterateThroughFolder(sourceFolder, outputFolder);

          printGui("You will find the fixed images in " + outputFolder.getAbsolutePath());

        } catch (ImageWriteException | ImageReadException | IOException e) {

          e.printStackTrace();
        }
      }
    }

  }



  // "Stop" button listener
  private static class StopListener implements ActionListener {

    @Override
    public void actionPerformed(ActionEvent actionEvent) {

      printGui("Program execution stopped, exiting in 5 seconds...");

      int delay = 5000; // Milliseconds

      // Action listener method called by lambda
      ActionListener taskPerformer = evt -> System.exit(0);

      new Timer(delay, taskPerformer).start();
    }

  }

}
