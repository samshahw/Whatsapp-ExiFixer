package it.ss.exifixer;

public class ImageDateFetch {

  // Fetches the date of the photo from the file name of the image, since
  // it abides to the following format, "IMG-YYYYMMDD-WAXXXX.jpeg/jpg"
  public static String getImageDate(String fileName) {

    // Checks if it can fetch the date from the file name, since
    // some images might be stored with other file names format
    if (fileName.matches("^IMG-[0-9]{8}-WA[0-9]{4}.(jpg|jpeg)$")) {

      // The following code returns the fetched date as specified
      // in the EXIF format, that is "yyyy:MM:dd HH:mm:ss", and
      // returns it as a string as requested by the EXIF Tag
      StringBuilder builder = new StringBuilder();

      builder.append(fileName, 4, 8);
      builder.append(":");
      builder.append(fileName, 8, 10);
      builder.append(":");
      builder.append(fileName, 10, 12);
      builder.append(" ");
      builder.append("00:00:00");

      return builder.toString();
    }

    return null;
  }

}
