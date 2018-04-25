import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {

    File search = new File("SourceBucket/test.java");
    Scanner scan = null;
    try {
      scan = new Scanner(search);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    String out = "";
    while (scan.hasNext()) {
      out += scan.nextLine();
      out += "\n";
    }
    out = out.replaceAll("package", "// package ");
    try {
      FileWriter newFile = new FileWriter("DestBucket/test.java");
      newFile.write(out);
      newFile.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    scan.close();

  }

}
