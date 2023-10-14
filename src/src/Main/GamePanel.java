package Main;

import Player.Player;

import java.awt.*;

import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    private       Thread           gameThread;
    private final MouseHandler     mouseHandler     = new MouseHandler();
    private final Player player           = new Player( mouseHandler );


    public GamePanel() {
        this.setPreferredSize( new Dimension( Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT ) );
        this.setBackground( Colors.EGGSHELL );
        this.setDoubleBuffered( true );
        this.addKeyListener( KeyHandler.get() );
        this.setFocusTraversalKeysEnabled( false ); // can receive tab inputs
        this.addMouseMotionListener( mouseHandler );
        this.addMouseListener( mouseHandler );
        this.setFocusable( true );
    }

    @Override
    public void run() {
        final long updateFPSInterval  = Constants.NANO_SEC_PER_SEC / 5;
        final long updateGameInterval = Constants.NANO_SEC_PER_SEC / Constants.MAX_UPS;
        final long repaintInterval    = Constants.NANO_SEC_PER_SEC / Constants.MAX_FPS;
        long       lastRepaintTime    = 0;
        long       lastUpdateTime     = 0;
        long       currentTime;
        long       lastFPSTime        = 0;
        long       numFrames          = 0;
        long       numUpdates         = 0;
        double     FPS;
        double     UPS;
        boolean repaint = false;

        // main game loop
        while ( gameThread != null) {
            currentTime = System.nanoTime();
            if ( currentTime - lastUpdateTime > updateGameInterval ) {
                update();
                numUpdates++;
                lastUpdateTime = currentTime;
            }
            if ( currentTime - lastFPSTime > updateFPSInterval ) {
                FPS = (double) ( numFrames  * Constants.NANO_SEC_PER_SEC ) / ( currentTime - lastFPSTime );
                UPS = (double) ( numUpdates * Constants.NANO_SEC_PER_SEC ) / ( currentTime - lastFPSTime );
                System.out.format("FPS: [%.1f] UPS: [%.1f]\r", FPS, UPS);
                numFrames = 0;
                numUpdates = 0;
                lastFPSTime = currentTime;
            }
            if ( currentTime - lastRepaintTime > repaintInterval ) {
                repaint();
                numFrames++;
                lastRepaintTime = currentTime;
                long sleepTime = repaintInterval - ( System.nanoTime() - currentTime) ;
                if ( sleepTime > 0 ) {
                    try {
                        Thread.sleep( sleepTime / Constants.NANO_SEC_PER_M_SEC );
                    } catch ( InterruptedException e ) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

    }

    protected void startGameThread() {
        gameThread = new Thread( this );
        gameThread.start();
    }

    protected void paintComponent ( final Graphics graphics ) {
        super.paintComponent( graphics );
        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setStroke( new BasicStroke( Constants.LINE_THICKNESS ) );
        graphics2D.scale( Constants.SCREEN_SCALE, Constants.SCREEN_SCALE);

        graphics2D.setFont( new Font( Font.MONOSPACED, Font.PLAIN, (int) (12 / Constants.SCREEN_SCALE) ) );

        player.repaint( graphics2D );


        graphics2D.dispose();
    }

    private void update() {
        player.update();
    }
}
