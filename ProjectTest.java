import java.util.Scanner;
import java.util.zip.*;
import java.io.*;
import java.util.*;

public class ProjectTest {
  /* Author: Cristian Benejan */

  public static void main(String[] args)
      throws FileNotFoundException, IOException {

    System.out.println("File(s) to be Un-Zipped:");
    System.out.println("=========================");

    File sourceDirectory = new File(
        System.getProperty("user.dir") + "/FileToBeUnzipped");
    File[] sourceDirectoryListing = sourceDirectory.listFiles();

    File destDirectory = new File(
        System.getProperty("user.dir") + "/UnzippedFiles");

    // Lists the contents of a source folder containing ZIP files & their size
    for (File f : sourceDirectoryListing) {
      // Gets & outputs file name/size/path info
      System.out.printf("%s\t%.1fkb\t%s%n", f.getName(),
          (float) f.length() / 1000, f.getAbsolutePath());
    } // End for.

    // Iterates through every ZIP file & extracts their contents to the
    // destination directory
    for (File f : sourceDirectoryListing) {
      try {
        ZipFile zipFile = new ZipFile(f.getAbsolutePath());

        System.out.printf("%n***%s Contained the following (%d) file(s):%n",
            f.getName(), zipFile.size());

        Enumeration<? extends ZipEntry> contents = zipFile.entries();

        // Creates folder with same name in destination directory
        String folderName = f.getName().substring(0, f.getName().length() - 4);
        String folderPath = destDirectory.getAbsolutePath() + File.separator
            + folderName;

        // Iterates through each element (file) in the ZIP file
        while (contents.hasMoreElements()) {

          ZipEntry singleItem = contents.nextElement();
          System.out.print(singleItem.getName());
          System.out.printf("\t\t%.1fkb%n",
              (float) singleItem.getSize() / 1000);
          
          File filePath = new File(folderPath);
          filePath.mkdirs();

          // Code from website
          if (singleItem.getName().contains("/")) { //Item is in a subfolder
            int slash = singleItem.getName().indexOf("/");
            File subFolder = new File(filePath + File.separator
                + singleItem.getName().substring(0, slash));
            subFolder.mkdir();

            int BUFFER_SIZE = 4096;
            BufferedInputStream zipIn = new BufferedInputStream(zipFile.getInputStream(singleItem));
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(
                    filePath + File.separator + singleItem.getName()));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
              bos.write(bytesIn, 0, read);
            }
            bos.flush();
            bos.close();
            zipIn.close();
            
          } else { //Item is just a file
            int BUFFER_SIZE = 4096;
            BufferedInputStream zipIn = new BufferedInputStream(
                zipFile.getInputStream(singleItem));
            BufferedOutputStream bos = new BufferedOutputStream(
                new FileOutputStream(
                    filePath + File.separator + singleItem.getName()));
            byte[] bytesIn = new byte[BUFFER_SIZE];
            int read = 0;
            while ((read = zipIn.read(bytesIn)) != -1) {
              bos.write(bytesIn, 0, read);
            }
            bos.flush();
            bos.close();
            zipIn.close();
          } //End if-else.
        } // End while.

      } catch (IOException e) {
        System.out.println("Error: Could not open file.");
        e.printStackTrace();
      } // End try-catch.

    } // End for.
    System.out.println();

  } // End main.
} // End class.