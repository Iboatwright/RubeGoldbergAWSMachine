package com.amazonaws.lambda.demo;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.Scanner;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class LambdaFunctionHandler implements RequestHandler<S3Event, String> {

    private AmazonS3 s3 = AmazonS3ClientBuilder.standard().build();

    public LambdaFunctionHandler() {}

    // Test purpose only.
    LambdaFunctionHandler(AmazonS3 s3) {
        this.s3 = s3;
    }

    @Override
    public String handleRequest(S3Event event, Context context) {
        context.getLogger().log("Received event: " + event);

        // Get the object from the event and show its content type
        String bucket = event.getRecords().get(0).getS3().getBucket().getName();
        String key = event.getRecords().get(0).getS3().getObject().getKey();
        try {
        	Properties properties = new Properties();
			properties.load(new FileInputStream(new File("aws_sdk.properties")));

			AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("aws_access_key_id"),
					properties.getProperty("aws_secret_access_key"));
			
			ClientConfiguration clientConfig = new ClientConfiguration();
			clientConfig.setProtocol(Protocol.HTTP);
			AmazonS3 s3 = new AmazonS3Client(credentials, clientConfig);
			S3Object folder = new S3Object();
			
			folder = s3.getObject(new GetObjectRequest(bucket, key));
			InputStream test = folder.getObjectContent();

			Scanner s = new Scanner(test).useDelimiter("\\A");
			String output = s.hasNext() ? s.next() : "";
			s.close();
			test.close();
			folder.close();

			output = output.replaceAll("package", "// package ");

			s3.putObject("packagescrubber", "scrubbedtest1.java", output);
        	
//            S3Object response = s3.getObject(new GetObjectRequest(bucket, key));
//            String contentType = response.getObjectMetadata().getContentType();
//            context.getLogger().log("CONTENT TYPE: " + contentType);
//            return contentType;
        	return "Success";
        } catch (Exception e) {
            e.printStackTrace();
            context.getLogger().log(String.format(
                "Error getting object %s from bucket %s. Make sure they exist and"
                + " your bucket is in the same region as this function.", key, bucket));
           
        }
		return "Failed";
    }
}