package org.example;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Main {

    static Jokes jokes = new Jokes();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(Main::createTray);
    }

    private static void createTray() {
        if (!SystemTray.isSupported()) {
            System.out.println("System tray is not supported here.");
            return;
        }
        PopupMenu menu = new PopupMenu();
        MenuItem jokeItem = new MenuItem("Tell me a joke");
        jokeItem.addActionListener(e -> showJoke());
        menu.add(jokeItem);
        menu.addSeparator();
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
        try (InputStream in = Main.class.getResourceAsStream("/icon.png")) {
            if (in == null) {
                System.out.println("icon.png not found, using fallback.");
                return createFallbackIcon();
            }
            BufferedImage original = ImageIO.read(in);
            Dimension size = SystemTray.getSystemTray().getTrayIconSize();
            return original.getScaledInstance(size.width, size.height, Image.SCALE_SMOOTH);
        } catch (IOException e) {
            System.out.println("Could not load icon: " + e.getMessage());
            return createFallbackIcon();
        }
    }

    private static Image createFallbackIcon() {
        BufferedImage img = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = img.createGraphics();
        g.setColor(Color.BLUE);
        g.fillOval(0, 0, 16, 16);
        g.dispose();
        return img;
    }
}