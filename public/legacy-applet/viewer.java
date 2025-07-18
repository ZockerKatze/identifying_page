import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.dnd.*;
import java.awt.event.*;
import java.applet.Applet;
import java.applet.AppletStub;
import java.applet.AppletContext;
import java.io.*;
import java.lang.management.ManagementFactory;
import java.net.*;
import java.util.*;
import java.util.jar.*;

import com.sun.management.OperatingSystemMXBean;

public class viewer extends JFrame {

    private JPanel appletPanel;
    private PerformanceOverlay overlay;
    private javax.swing.Timer repaintTimer;
    private long jvmStartTime;
    private boolean overlayVisible = true;

    public viewer() {
        super("Applet Viewer");

        jvmStartTime = System.currentTimeMillis();

        // Main applet panel with black background and null layout for absolute positioning if needed
        appletPanel = new JPanel(null) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                setBackground(Color.BLACK);
            }
        };
        appletPanel.setPreferredSize(new Dimension(640, 480));
        appletPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(Color.WHITE),
                "Drop .class or .jar file here", 0, 0, null, Color.WHITE));
        appletPanel.setForeground(Color.WHITE);

        setLayout(new BorderLayout());
        add(appletPanel, BorderLayout.CENTER);

        // Create overlay (we will add it to glass pane, not appletPanel)
        overlay = new PerformanceOverlay();
        overlay.setOpaque(false);
        overlay.setSize(250, 140);

        // Drag & drop support
        new DropTarget(appletPanel, new FileDropTargetListener());

        // Timer to repaint overlay ~30 FPS
        repaintTimer = new javax.swing.Timer(33, e -> {
            if (overlayVisible) overlay.repaint();
        });
        repaintTimer.start();

        // Setup global Ctrl+D to toggle overlay visibility
        setupGlobalToggleOverlayKeyBinding();

        // Set JFrame properties
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        // Setup glass pane for overlay
        JComponent glass = (JComponent) getGlassPane();
        glass.setLayout(null);
        glass.add(overlay);
        positionOverlay();
        glass.setVisible(true);

        // Update overlay position on resize/move
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                positionOverlay();
            }

            @Override
            public void componentMoved(ComponentEvent e) {
                positionOverlay();
            }
        });
    }

    private void positionOverlay() {
        // Position overlay relative to appletPanel on glass pane
        Point loc = SwingUtilities.convertPoint(appletPanel.getParent(), appletPanel.getLocation(), getGlassPane());
        overlay.setLocation(loc.x + 5, loc.y + 25);
    }

    private void setupGlobalToggleOverlayKeyBinding() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(e -> {
            if (e.getID() == KeyEvent.KEY_PRESSED) {
                // Check if Ctrl+D pressed
                if ((e.getModifiersEx() & KeyEvent.CTRL_DOWN_MASK) != 0 && e.getKeyCode() == KeyEvent.VK_D) {
                    overlayVisible = !overlayVisible;
                    overlay.setVisible(overlayVisible);
                    repaint();
                    return true;  // consume event
                }
            }
            return false;
        });
    }

    private class FileDropTargetListener extends DropTargetAdapter {
        @Override
        public void drop(DropTargetDropEvent dtde) {
            try {
                dtde.acceptDrop(DnDConstants.ACTION_COPY);
                java.util.List<File> droppedFiles = (java.util.List<File>)
                        dtde.getTransferable().getTransferData(DataFlavor.javaFileListFlavor);

                for (File file : droppedFiles) {
                    if (file.getName().endsWith(".jar")) {
                        loadAppletFromJar(file);
                    } else if (file.getName().endsWith(".class")) {
                        loadAppletFromClassFile(file);
                    } else {
                        JOptionPane.showMessageDialog(viewer.this,
                                "Unsupported file type: " + file.getName());
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(viewer.this, "Error loading applet: " + ex.getMessage());
            }
        }
    }

    private void loadAppletFromJar(File jarFile) throws Exception {
        URLClassLoader loader = new URLClassLoader(new URL[]{jarFile.toURI().toURL()});
        String mainClassName = getMainAppletClassFromJar(jarFile);

        if (mainClassName == null)
            throw new Exception("Could not find Applet class in jar");

        Class<?> clazz = loader.loadClass(mainClassName);
        if (!Applet.class.isAssignableFrom(clazz))
            throw new Exception("Class " + mainClassName + " is not an Applet");

        Applet applet = (Applet) clazz.getDeclaredConstructor().newInstance();
        showApplet(applet);
    }

    private String getMainAppletClassFromJar(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            Manifest mf = jar.getManifest();
            if (mf != null) {
                String mainClass = mf.getMainAttributes().getValue("Applet-Class");
                if (mainClass == null)
                    mainClass = mf.getMainAttributes().getValue("Main-Class");
                if (mainClass != null) return mainClass;
            }

            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                if (entry.getName().endsWith(".class")) {
                    String className = entry.getName().replace('/', '.').replace(".class", "");
                    try (URLClassLoader cl = new URLClassLoader(new URL[]{jarFile.toURI().toURL()})) {
                        Class<?> clazz = cl.loadClass(className);
                        if (Applet.class.isAssignableFrom(clazz)) {
                            return className;
                        }
                    } catch (Throwable ignored) {}
                }
            }
        }
        return null;
    }

    private void loadAppletFromClassFile(File classFile) throws Exception {
        File parentDir = classFile.getParentFile();
        String className = getClassNameFromFile(classFile, parentDir);
        URLClassLoader loader = new URLClassLoader(new URL[]{parentDir.toURI().toURL()});
        Class<?> clazz = loader.loadClass(className);

        if (!Applet.class.isAssignableFrom(clazz))
            throw new Exception("Class is not an Applet");

        Applet applet = (Applet) clazz.getDeclaredConstructor().newInstance();
        showApplet(applet);
    }

    private String getClassNameFromFile(File classFile, File rootDir) {
        String absPath = classFile.getAbsolutePath();
        String rootPath = rootDir.getAbsolutePath();
        String classPath = absPath.substring(rootPath.length() + 1, absPath.length() - 6);
        return classPath.replace(File.separatorChar, '.');
    }

    private void showApplet(Applet applet) {
        appletPanel.removeAll();
        applet.setStub(new DummyAppletStub());
        applet.init();
        applet.start();
        applet.setSize(640, 480);
        appletPanel.add(applet);
        appletPanel.revalidate();
        appletPanel.repaint();
        overlay.startMonitoring();
    }

    private static class DummyAppletStub implements AppletStub {
        public boolean isActive() { return true; }
        public URL getDocumentBase() { return getCodeBase(); }
        public URL getCodeBase() {
            try {
                return new URI("file:///").toURL();
            } catch (Exception e) {
                return null;
            }
        }
        public String getParameter(String name) { return null; }
        public AppletContext getAppletContext() { return null; }
        public void appletResize(int width, int height) {}
    }

    private class PerformanceOverlay extends JComponent {
        private long lastTime = System.nanoTime();
        private float fps = 0f;
        private final Runtime runtime = Runtime.getRuntime();
        private final OperatingSystemMXBean osBean;
        private final long jvmStartTime;
        private final javax.swing.Timer repaintTimer;

        public PerformanceOverlay() {
            jvmStartTime = viewer.this.jvmStartTime;

            OperatingSystemMXBean bean;
            try {
                bean = (OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();
            } catch (Exception e) {
                bean = null;
            }
            osBean = bean;

            setFont(new Font("Monospaced", Font.PLAIN, 12));

            // repaint timer for smooth updates (~30 FPS)
            repaintTimer = new javax.swing.Timer(33, e -> {
                if (overlayVisible) repaint();
            });
            repaintTimer.start();
        }

        public void startMonitoring() {
            // No extra logic needed here for now
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            long now = System.nanoTime();
            fps = 1_000_000_000f / (now - lastTime);
            lastTime = now;

            long uptime = (System.currentTimeMillis() - jvmStartTime) / 1000;
            long totalMem = runtime.totalMemory() / (1024 * 1024);
            long freeMem = runtime.freeMemory() / (1024 * 1024);
            long usedMem = totalMem - freeMem;
            long maxMem = runtime.maxMemory() / (1024 * 1024);

            int threadCount = Thread.activeCount();
            int loadedClasses = ManagementFactory.getClassLoadingMXBean().getLoadedClassCount();

            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,
                    RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            // Semi-transparent black background for overlay box
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15);

            // White border around the box
            g2d.setColor(Color.WHITE);
            g2d.setStroke(new BasicStroke(2));
            g2d.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);

            g2d.setColor(Color.WHITE);
            int y = 15;
            g2d.drawString("JVM Uptime: " + uptime + " sec", 10, y); y += 15;
            g2d.drawString(String.format("Memory: Used %d MB / Total %d MB / Max %d MB", usedMem, totalMem, maxMem), 10, y); y += 15;
            g2d.drawString("Threads: " + threadCount, 10, y); y += 15;
            g2d.drawString("Loaded Classes: " + loadedClasses, 10, y); y += 15;

            if (osBean != null) {
                double cpuLoad = osBean.getProcessCpuLoad(); // 0..1 or -1 if unsupported
                if (cpuLoad >= 0) {
                    g2d.drawString(String.format("CPU Load: %.2f%%", cpuLoad * 100), 10, y);
                    y += 15;
                }
            }

            g2d.drawString(String.format("FPS: %.1f", fps), 10, y);
            g2d.drawString(String.format("Press Strg+D for Profiler!"),10,y += 20); //adj
        }

        @Override
        public Dimension getPreferredSize() {
            return new Dimension(250, 140);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(viewer::new);
    }
}

