package it.ss.exifixer;

import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.ImageWriteException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.jpeg.exif.ExifRewriter;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputDirectory;
import org.apache.commons.imaging.formats.tiff.write.TiffOutputSet;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

@SuppressWarnings("unused")
public class ExifDateFix {

  // Changes the date of the image from its EXIF, overwriting the
  // previous date with the new one fetched from its file name
  public static void changeExifMetadata(final File jpegImageFile, final File dst, final String date)
      throws IOException, ImageReadException, ImageWriteException {

    try (FileOutputStream fos = new FileOutputStream(dst);
         OutputStream os = new BufferedOutputStream(fos)) {

      TiffOutputSet outputSet = null;

      // Fetches the image metadata, note that metadata
      // might be null if no metadata is found
      final ImageMetadata metadata = Imaging.getMetadata(jpegImageFile);
      final JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

      // Checks whether metadata has been found
      if (jpegMetadata != null) {

        // Fetches exif, note that exif might be null if no exif is found
        final TiffImageMetadata exif = jpegMetadata.getExif();

        // Checks whether exif has been found
        if (exif != null) {

          // Gets a copy of the exif fields to work on, this is
          // the easiest way if we only want to work on a specific
          // field, since it preserves the others
          outputSet = exif.getOutputSet();
        }
      }

      // If the file does not contain any exif metadata, we create an empty set
      // of exif metadata. Otherwise, we keep all of the other existing tags
      if (outputSet == null) {

        outputSet = new TiffOutputSet();
      }

      // The following code removes the wrong date first, in order to avoid a
      // duplicate tag, then adds the correct data fetched before
      final TiffOutputDirectory exifDirectory = outputSet.getOrCreateExifDirectory();

      // nb: The following method will not fail if the tag does not exist
      exifDirectory.removeField(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

      // Adds the correct date as of "yyyy:MM:dd HH:mm:ss" as exif tag
      exifDirectory.add(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL, date);

      // Finally, prints the new values onto the metadata
      new ExifRewriter().updateExifMetadataLossless(jpegImageFile, os, outputSet);
    }
  }

}
