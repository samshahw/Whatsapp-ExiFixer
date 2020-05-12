# Whatsapp-ExiFixer
GUI application to fix the dates of images restored from a WhatsApp backup.

Give the absolute path of the directory containing the images with the wrong dates, the "Source folder", and 
the directory to store the new fixed images into, the "Destination".  

**nb.** WhatsApp backups store the images with a .JPEG or .JPG extension. but since the Apache Commons Imaging library
cannot write in that format, the new images will have a .PNG extension; the file names will not be changed.  

This tool has been made possibile by an extensive use of the Apache Commons Imaging library.
