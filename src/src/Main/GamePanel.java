package Main;

import Gates.Input;
import Gates.OrGate;
import Gates.AndGate;
import Gates.Output;

import java.awt.*;

import javax.swing.*;

public class GamePanel extends JPanel implements Runnable {

    private       Thread           gameThread;
    private final KeyHandler       keyHandler       = new KeyHandler();
    private final MouseHandler     mouseHandler     = new MouseHandler();
    private OrGate  orGate = new OrGate( 550, 300);

    private AndGate andGate = new AndGate( 300, 300);
    private Input   input   = new Input( 50, 300);
    private Output  output   = new Output( 800, 300 );


    public GamePanel() {
        this.setPreferredSize( new Dimension( Constants.SCREEN_WIDTH, Constants.SCREEN_HEIGHT ) );
        this.setBackground( Colors.EGGSHELL );
        this.setDoubleBuffered( true );
        this.addKeyListener( keyHandler );
        this.setFocusTraversalKeysEnabled( false ); // can receive tab inputs
        this.addMouseMotionListener( mouseHandler );
        this.addMouseListener( mouseHandler );
        this.setFocusable( true );
    }

    @Override
    public void run() {
        final long updateFPSInterval  = Constants.NANO_SEC_PER_SEC / 10;
        final long updateGameInterval = Constants.NANO_SEC_PER_SEC / Constants.UPDATES_PER_SECOND;
        final long repaintInterval    = Constants.NANO_SEC_PER_SEC / Constants.MAX_FPS;
        long       sleepTime;
        long       lastRepaintTime    = 0;
        long       lastUpdateTime     = 0;
        long       currentTime;
        long       lastFPSTime        = 0;
        long       numFrames          = 0;
        double     FPS;

        // main game loop
        while ( gameThread != null) {
            currentTime = System.nanoTime();
            if ( currentTime - lastUpdateTime > updateGameInterval ) {
                update();
                lastUpdateTime = currentTime;
            }
            if ( currentTime - lastFPSTime > updateFPSInterval ) {
                FPS = (double) ( numFrames * Constants.NANO_SEC_PER_SEC ) / ( currentTime - lastFPSTime );
                System.out.format("FPS: [%.1f]\r", FPS);
                numFrames = 0;
                lastFPSTime = currentTime;
            }
            if ( currentTime - lastRepaintTime > repaintInterval ) {
                repaint();
                numFrames++;
                lastRepaintTime = currentTime;
                sleepTime = repaintInterval - ( System.nanoTime() - currentTime) ;
                if ( sleepTime > 0 ) {
                    try {
                        Thread.sleep(sleepTime / Constants.NANO_SEC_PER_M_SEC);
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
        double scale = 0.4;
        graphics2D.scale( scale, scale );

        orGate.repaint( graphics2D );
        andGate.repaint( graphics2D );
        input.repaint( graphics2D );
        output.repaint( graphics2D );

        graphics2D.dispose();
    }

    private void update() {
        if ( keyHandler.isEndGamePressed() ) {
            System.exit(0);
        }
        if ( keyHandler.numbersPressed[0] ) {
            keyHandler.numbersPressed[0] = false;
            input.flipState();
        }
    }
}
