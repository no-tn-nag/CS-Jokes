package org.example;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class Main {

    static Jokes jokes = new Jokes();

    static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createTray);
    }

    private static void createTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported here.");
            return;
        }
        PopupMenu menu = new PopupMenu();
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));
        menu.add(quitItem);

        TrayIcon trayIcon = new TrayIcon(createIcon(), "Computer Science Jokes", menu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showJoke();
                }
            }
        });

        // 3. Put it in the system tray
        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Could not add tray icon: " + e.getMessage());
        }
    }

    private static void showJoke() {
        JOptionPane.showMessageDialog(
                null,
                jokes.getJoke()
        );
    }


    private static Image createIcon() {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, 16, 16);
        g.dispose();
        return img;
    }
}