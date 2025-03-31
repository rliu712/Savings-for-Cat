package ui;

import java.net.MalformedURLException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JWindow;
import javax.swing.SwingConstants;
import java.awt.*;
import java.net.*;

public class SplashScreen {
    
    private JWindow window;
    
    public SplashScreen() {
        window = new JWindow();
        
        try {
            window = new JWindow();
            JLabel splashLabel = new JLabel(new ImageIcon(new URL("https://i.postimg.cc/nLtKFcjX/Welcome-to-Whisker-Saves-1.jpg")));
            window.getContentPane().add(splashLabel, BorderLayout.CENTER);

            window.setSize(400, 300);
            window.setLocationRelativeTo(null);   // puts it to the center of the screen

            window.setVisible(true);

            // Thread -- sets the time it displays
            Thread.sleep(3000);
        } catch (MalformedURLException e) {
            System.err.println("Invalid URL: " + e.getMessage());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            window.setVisible(false);
            window.dispose();
        }
    }
}
