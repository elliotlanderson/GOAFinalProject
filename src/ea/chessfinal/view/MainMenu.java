package ea.chessfinal.view;

import ea.chessfinal.Main;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * Created by elliotanderson on 5/9/16.
 * This is the Main Menu class that will
 * basically just introduce the game to the player
 *
 * @note The reason I am making this a separate class is because if I
 * ever want to expand the functionality of this game, (i.e. fully implement
 * the network player object), then I will have more options, as opposed to adding this to the model.GUIPlayer
 * object where the main GUI is implemented
 *
 * @author Elliot Anderson <eanderson17@germantownfriends.org>
 */
public class MainMenu extends JPanel  {

    int width, height;

    private JFrame mainFrame;

    private JLabel headerLabel;
    private JLabel statusLabel;
    private JPanel controlPanel;
    private JLabel messageLabel;
    private JLabel msgLabel;

    /**
     * @var instane of the main class
     */
    Main mainClass;



    public MainMenu(int width, int height, Main mainClass) {
        this.width = width;
        this.height = height;

        this.mainClass = mainClass;

        this.prepareGUI();

    }

    private void prepareGUI() {
        mainFrame = new JFrame("Chess Game");
        mainFrame.setSize(this.width, this.height);
        mainFrame.setLayout(new GridLayout(3,1));
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });

        headerLabel = new JLabel("", JLabel.CENTER);
        statusLabel = new JLabel("", JLabel.CENTER);

        statusLabel.setSize(350, 100);

        controlPanel = new JPanel();
        controlPanel.setLayout(new FlowLayout());

        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusLabel);
        mainFrame.setVisible(true);
    }

    public void showJPanel() {
        headerLabel.setText("Welcome to my Chess Game");

        JPanel panel = new JPanel();
        panel.setBackground(Color.magenta);
        panel.setLayout(new FlowLayout());

        JButton startButton = new JButton("Start");

        startButton.setActionCommand("Start");
        startButton.addActionListener(new ButtonClickListener());

        panel.add(startButton);

        controlPanel.add(panel);
        mainFrame.setVisible(true);
    }

    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();

            if (command.equals("Start")) {
                mainClass.startGame();
                mainFrame.dispose();
            }
        }
    }

}


