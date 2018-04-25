import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Properties;
import java.util.Scanner;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

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

	    folder = s3.getObject(new GetObjectRequest("unzipped", key));
	    InputStream test = folder.getObjectContent();

	    Scanner s = new Scanner(test).useDelimiter("\\A");
	    String output = s.hasNext() ? s.next() : "";
	    s.close();
	    test.close();
	    folder.close();
	    
		output = output.replaceAll("package", "// package ");
		
		s3.putObject("packagescrubber", "scrubbedtest1.java", output);
	}
}