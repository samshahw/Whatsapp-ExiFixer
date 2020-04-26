package it.ss.exifixer;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.File;
import java.io.IOException;

public class FilesInFolderIterator {

  public static void iterateThroughFolder(String pathToSource, String pathToOutput)
      throws ImageWriteException, ImageReadException, IOException {

    // The following code creates the output folder if it does not exist
    File outputFolder = new File(pathToOutput);

    if (!outputFolder.exists() || !outputFolder.isDirectory()) {

      System.out.println("There is no directory at the specified path, creating it...");
      boolean success = outputFolder.mkdir();

      if (success) {

        System.out.println("Directory at " + pathToOutput + " created successfully!");

      } else {

        System.err.println("There was a problem creating the output directory");
        System.exit(1);
      }
    }

    // Gets the source folder path and add its files to an array
    File sourceFolder = new File(pathToSource);
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
          File outputFile = new File(pathToOutput + "/" + outputFileName);

          // The following code creates the new output image file
          // and check if the operation succeeded
          boolean success = outputFile.createNewFile();

          if (!success) {

            System.err.println("There was a problem writing one or more output files");
            System.exit(1);
          }

          // Gets the file name in order to extrapolate the date out of it
          String fileName = imageFile.getName();
          String fileDate = ImageDateFetch.getImageDate(fileName);

          // Finally, edit the file metadata
          ExifDateFix.changeExifMetadata(imageFile, outputFile, fileDate);

          count++;
          System.out.printf("Image #%d of %d has been written successfully!\n",
                            count, listOfFiles.length);
        }
      }
    }
  }

}
