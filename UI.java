package example;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import com.amazonaws.AmazonClientException;
import com.amazonaws.SdkClientException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.PutObjectRequest;


public class UI extends JFrame
{

    private final JLabel message;
    private final JButton selectButton;
    private JTextField fileField;
    private final JButton submitButton;

    static boolean filePath = false;

    static File file;
    
    private static String bucketName;
    private static String keyName;


    public UI()
    {
        super("Cloud Project");

        setLayout(null);

        message = new JLabel("Select a .zip file");
        message.setBounds(250, 100, 200, 50);
        add(message);

        selectButton = new JButton("Select File");
        selectButton.setBounds(100, 200, 125, 50);
//        selectButton.setBackground(Color.PINK);
//        selectButton.setOpaque(true);
//        selectButton.setBorderPainted(false);
        add(selectButton);

        fileField = new JTextField();
        fileField.setBounds(225, 200, 250, 50);
        add(fileField);

        submitButton = new JButton("Submit");
        submitButton.setBounds(200, 300, 200, 50);
        add(submitButton);

        selectButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFileChooser fc = new JFileChooser();
                int returnVal = fc.showOpenDialog(submitButton);

                if (returnVal == JFileChooser.APPROVE_OPTION)
                {
                    file = fc.getSelectedFile();
                    //this is where a real application would open the file
                    //System.out.println("File: " + file);
                    String fileString = file.toString();
                    fileField.setText(fileString);
                    filePath = true;
                }
            }
        });

        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (filePath)
                {
					try {
						pushToBucket();
					} catch (SdkClientException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                }
            }
        });
    }

    public static void pushToBucket() throws FileNotFoundException, IOException, SdkClientException
    {
    	
    		Properties properties = new Properties();
    		properties.load(new FileInputStream(new File("aws_sdk.properties")));
    	
    		AWSCredentials credentials = new BasicAWSCredentials(properties.getProperty("aws_access_key_id"), 
    			properties.getProperty("aws_secret_access_key"));
    	
    		AmazonS3 s3client = new AmazonS3Client(credentials);
    		bucketName = "ivan-s3-test";
    		
    		try {
    			s3client.putObject(new PutObjectRequest(
    					bucketName, file.getName(), file ));
    		}
    		catch (AmazonClientException ase) {
    			ase.printStackTrace();
    		}
    	
    }


    public static void main(String[] args)
    {
       UI frame = new UI();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
