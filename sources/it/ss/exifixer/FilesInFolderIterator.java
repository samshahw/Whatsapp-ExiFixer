package it.ss.exifixer;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.File;
import java.io.IOException;

import static it.ss.exifixer.ExiFixerGUI.printGui;

public class FilesInFolderIterator {

  public static void iterateThroughFolder(File sourceFolder, File outputFolder)
      throws ImageWriteException, ImageReadException, IOException {

    // The following code creates the output folder if it does not exist
    if (!outputFolder.exists() || !outputFolder.isDirectory()) {

      printGui("There is no directory at the specified path, creating it...");
      boolean success = outputFolder.mkdir();

      if (success) {

        printGui("Folder " + outputFolder.getAbsolutePath() + " created successfully!");

      } else {

        printGui("There was a problem creating the output directory");
      }
    }

    // Gets the source folder path and add its files to an array
    File[] listOfFiles = sourceFolder.listFiles();

    // The following code checks that the source folder exists and
    // it is not empty, then iterates through its files and checks
    // if they abide to the whatsapp image file name format
    if (listOfFiles != null && listOfFiles.length > 0) {

      int count = 0;
      for (File imageFile : listOfFiles) {

        if (imageFile.getName().matches("^IMG-[0-9]{8}-WA[0-9]{4}.(jpg|jpeg)$")) {

          // The following code writes the new output file to PNG, since
          // Apache Commons Imaging does not support writing to JPEG
          String outputFileName = imageFile.getName().replaceAll("(jpeg|JPEG|jpg|JPG)", "PNG");
          File outputFile = new File(outputFolder.getAbsolutePath() + "/" + outputFileName);

          // The following code creates the new output image file
          // and check if the operation succeeded
          boolean success = outputFile.createNewFile();

          if (!success) {

            printGui("There was a problem writing one or more output files");
            System.exit(1);
          }

          // Gets the file name in order to extrapolate the date out of it
          String fileName = imageFile.getName();
          String fileDate = ImageDateFetch.getImageDate(fileName);

          // Finally, edit the file metadata
          ExifDateFix.changeExifMetadata(imageFile, outputFile, fileDate);

          count++;
          printGui("Image " + count + " of " + listOfFiles.length + " has been fixed successfully");

        } else {

          printGui("One or more files do not match the required format");
        }
      }
    }
  }

}
