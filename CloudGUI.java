import javax.swing.*;
import java.awt.*;


public class CloudGUI extends JFrame
{

    private final JLabel message;
    private final JButton selectButton;
    private JTextField fileField;
    private final JButton submitButton;


    public CloudGUI()
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

    }



    public static void main(String[] args)
    {
       CloudGUI frame = new CloudGUI();
       frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 600);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
