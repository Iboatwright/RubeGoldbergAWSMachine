import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    
    File search = new File("test.java");
    Scanner scan = null;
    try {
      scan = new Scanner(search);
    } catch (FileNotFoundException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    String out = "";
    while (scan.hasNext()) {
      out += scan.nextLine();
      out += "\n";
    }
    //System.out.println(out);
    try {
      scan = new Scanner(search);
    } catch (FileNotFoundException e1) {
      // TODO Auto-generated catch block
      e1.printStackTrace();
    }
    String stuff = "";
    while (scan.hasNext()) {
      stuff = scan.nextLine();
      if (stuff.contains("package")) {
        //System.out.println(stuff);
        
        out = out.replaceAll("package", "// package ");
        System.out.println(out);
        try {
          FileWriter newFile = new FileWriter("test.java");
          newFile.write(out);
          newFile.close();
        } catch (IOException e) {
          // TODO Auto-generated catch block
          e.printStackTrace();
        }
      }
    }
  
    // from stackoverflow
//    Path path = Paths.get("test.java");
//    Charset charset = StandardCharsets.UTF_8;
//
//    String content = "";
//    try {
//      content = new String(Files.readAllBytes(path), charset);
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
//    content = content.replaceAll("Mitchell Donaghy", "redacted");
//    try {
//      Files.write(path, content.getBytes(charset));
//    } catch (IOException e) {
//      // TODO Auto-generated catch block
//      e.printStackTrace();
//    }
    

  }

}
