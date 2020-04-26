package it.ss.exifixer;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) {

    InputStreamReader keyboard = new InputStreamReader(System.in);
    BufferedReader reader = new BufferedReader(keyboard);

    System.out.println("Welcome to WhatsApp ExiFixer, a program intended to fix the wrong dates " +
                           "of the Whatsapp backups");

    try {

      System.out.println("Please, enter the absolute path of the source folder: ");
      String sourceFolder = reader.readLine();
      System.out.println();

      System.out.println("Please, enter the absolute path of the output folder: ");
      String outputFolder = reader.readLine();
      System.out.println();

      FilesInFolderIterator.iterateThroughFolder(sourceFolder, outputFolder);

      System.out.println("\nThe remaining images did not have the date in the file name...");
      System.out.println("Date fix completed successfully!");

    } catch (IOException | ImageWriteException | ImageReadException e) {

      e.printStackTrace();
    }
  }

}
