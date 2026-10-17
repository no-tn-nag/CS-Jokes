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
    private static TrayIcon trayIcon;

    private static final int MINUTES = 60;
    private static final boolean IS_MAC = System.getProperty("os.name").toLowerCase().contains("mac");

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
        jokeItem.addActionListener(e -> showJokeNotification());
        menu.add(jokeItem);
        menu.addSeparator();
        MenuItem quitItem = new MenuItem("Quit");
        quitItem.addActionListener(e -> System.exit(0));
        menu.add(quitItem);

        trayIcon = new TrayIcon(createIcon(), "Computer Science Jokes", menu);
        trayIcon.setImageAutoSize(true);
        trayIcon.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    showJoke();
                }
            }
        });

        try {
            SystemTray.getSystemTray().add(trayIcon);
        } catch (AWTException e) {
            System.out.println("Could not add tray icon: " + e.getMessage());
        }
        scheduleNextJoke();
    }

    private static void showJoke() {
        JOptionPane pane = new JOptionPane(jokes.getJoke(), JOptionPane.INFORMATION_MESSAGE);
        JDialog dialog = pane.createDialog("Computer Science Jokes");
        dialog.setAlwaysOnTop(true);

        if (Desktop.isDesktopSupported()
                && Desktop.getDesktop().isSupported(Desktop.Action.APP_REQUEST_FOREGROUND)) {
            Desktop.getDesktop().requestForeground(true);
        }

        dialog.setVisible(true);
        dialog.dispose();
    }


    private static Image createIcon() {
        try (InputStream in = Main.class.getResourceAsStream("/icon2.png")) {
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

    private static void scheduleNextJoke() {
        Timer timer = new Timer(MINUTES, e -> {
            showJokeNotification();
            scheduleNextJoke();
        });
        timer.setRepeats(false);
        timer.start();
    }

    private static void showJokeNotification() {
        String title = "Computer Science Jokes";
        String body = jokes.getJoke();
        if (IS_MAC) {
            notifyMac(title, body);
        } else {
            trayIcon.displayMessage(title, body, TrayIcon.MessageType.NONE);
        }
    }

    private static void notifyMac(String title, String body) {
        String script = "display notification \"" + escape(body) + "\"" + " with title \"" + escape(title) + "\"";
        try {
            new ProcessBuilder("osascript", "-e", script).start();
        } catch (IOException e) {
            System.out.println("Could not show notification: " + e.getMessage());
        }
    }


    private static String escape(String s) {
        return s.replace("\\", "\\\\").replace("\"", "\\\"");
    }
}