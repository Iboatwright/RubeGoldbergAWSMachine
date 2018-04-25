import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLDecoder;
import java.util.List;
import java.util.Properties;
import java.util.Scanner;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ListObjectsV2Result;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.util.IOUtils;

public class Main {

	public static void main(String[] args) throws FileNotFoundException, IOException {

		Properties properties = new Properties();
	    properties.load(new FileInputStream(new File("aws_sdk.properties")));
	    
	    AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("aws_access_key_id"), 
	        properties.getProperty("aws_secret_access_key"));

	    ClientConfiguration clientConfig = new ClientConfiguration();
	    clientConfig.setProtocol(Protocol.HTTP);
	    AmazonS3 s3 = new AmazonS3Client(credentials, clientConfig);
	    S3Object folder = new S3Object();
	    
	    String key = "Test Java Files/test1.java".replace('+', ' ');
	    key = URLDecoder.decode(key, "UTF-8");

	    ListObjectsV2Result result = s3.listObjectsV2("unzipped");
	    List<S3ObjectSummary> objects = result.getObjectSummaries();
	    for (S3ObjectSummary os: objects) {
	        System.out.println("* " + os.getKey());
	    }

	    
	    folder = s3.getObject(new GetObjectRequest("unzipped", key));
	    InputStream test = folder.getObjectContent();
	    File search = new File("newTest1.java");
	    OutputStream outputStream = new FileOutputStream(search);
	    IOUtils.copy(test, outputStream);
	    outputStream.close();
	    System.out.println(search.getPath());
	    folder.close();
	    
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
		
		s3.putObject("packagescrubber", "scrubbedtest1.java", out);

				scan.close();

	}

}
