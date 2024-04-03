import javax.swing.JFrame;

public class WorldFrame extends JFrame implements Runnable {

    private WorldPanel p;
    private Thread windowThread;

    public WorldFrame(String display) {
        super(display);
        int frameWidth = 1300;
        int frameHeight = 800;
        p = new WorldPanel();
        this.add(p);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(frameWidth, frameHeight);
        this.setLocation(0, 0);
        this.setVisible(true);
        startThread();

    }

    public void startThread() {
        windowThread = new Thread(this);
        windowThread.start();
    }

    public void run() {
        while (true) {
            p.repaint();
        }
    }
}


