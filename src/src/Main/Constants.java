package Main;

public class Constants {
    public static final int    MAX_UPS               = 60;
    public static final int    MAX_FPS               = 250;
    public static final int    HEIGHT                = 750;
    public static final int    WIDTH                 = 750;
    public static final double SCREEN_SCALE          = 0.4;
    public static final int    SCREEN_HEIGHT         = (int) ( HEIGHT / SCREEN_SCALE );
    public static final int    SCREEN_WIDTH          = (int) ( WIDTH / SCREEN_SCALE );
    public static final int    NAME_SPACE_HEIGHT     = (int) ( 50 / SCREEN_SCALE );
    public static final int    CHIP_SPACE_HEIGHT     = (int) ( 100 / SCREEN_SCALE );
    public static final int    END_PLAYABLE_AREA     = Constants.SCREEN_HEIGHT - Constants.CHIP_SPACE_HEIGHT;
    public static final long   NANO_SEC_PER_SEC      = 1_000_000_000L;
    public static final long   NANO_SEC_PER_M_SEC    = 1_000_000L;
    public static final int    MAX_NUM_IO            = 8;
    public static final int    MAX_NUM_WIRE_NODES    = 32;
    public static final int    LINE_THICKNESS        = (int) ( 2.4 / SCREEN_SCALE );
    public static final int    LINE_GRAB_RADIUS      = (int) ( 8 / SCREEN_SCALE );
    public static final int    MIN_LINE_LENGTH       = 40;
    public static final int    NUM_NEXT_NODES        = 3;
    public static final String SAVE_PATH             = "src/saves/";
    public static final String FULL_SAVE_PATH        = "/Users/john/Documents/GitHub/poker/src/saves";
}