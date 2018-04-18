package example;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLDecoder;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.event.S3EventNotification.S3EventNotificationRecord;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

public class PackageScrubber implements RequestHandler<S3Event, String> {

	public String handleRequest(S3Event s3event, Context context) {
		try {
			S3EventNotificationRecord record = s3event.getRecords().get(0);

			String srcBucket = record.getS3().getBucket().getName();
			// Object key may have spaces or unicode non-ASCII characters.
			String srcKey = record.getS3().getObject().getKey().replace('+', ' ');
			srcKey = URLDecoder.decode(srcKey, "UTF-8");

			String dstBucket = srcBucket + "resized";
			String dstKey = "resized-" + srcKey;

			// Sanity check: validate that source and destination are different
			// buckets.
			if (srcBucket.equals(dstBucket)) {
				System.out.println("Destination bucket must not match source bucket.");
				return "";
			}

			// Infer the file type.
			Matcher matcher = Pattern.compile(".*\\.([^\\.]*)").matcher(srcKey);
			if (!matcher.matches()) {
				System.out.println("Unable to infer file type for key " + srcKey);
				return "";
			}
			String fileType = matcher.group(1);
			if (!"java".equals(fileType)) {
				System.out.println("Skipping non-java file " + srcKey);
				return "";
			}

			// Download the file from S3 into a stream
			AmazonS3 s3Client = new AmazonS3Client();
			S3Object s3Object = s3Client.getObject(new GetObjectRequest(srcBucket, srcKey));
			InputStream objectData = s3Object.getObjectContent();

			// process file here -- Mitchell code activate!!!
			// File search = new File("test.java");
			Scanner scan = null;
			scan = new Scanner(objectData);
			StringBuilder fout = new StringBuilder();

			String line = "";
			while (scan.hasNext()) {
				line = scan.nextLine();
				if (line.contains("package")) {
					line = line.replaceAll("package", "// package ");
				}

				fout.append(line + "\n");
			}
			scan.close();
			// Uploading to S3 destination bucket
			System.out.println("Writing to: " + dstBucket + "/" + dstKey);
			s3Client.putObject(dstBucket, dstKey, new File(fout.toString()));
			System.out.println("Successfully resized " + srcBucket + "/" + srcKey + " and uploaded to " + dstBucket
					+ "/" + dstKey);
			return "Ok";
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
