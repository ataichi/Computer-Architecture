/*
 * This demonstration program is released without warranty or restrictions.
 * You may modify and use it as you wish.
 * Just don't complain to me if you have trouble.
 */
package splashdemo;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.SplashScreen;
import java.awt.geom.Rectangle2D;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Example for Splash Screen tutorial
 *
 * @author Joseph Areeda
 */
public class MIPSSplash {

    public static SplashScreen mySplash;                   // instantiated by JVM we use it to get graphics
    static Graphics2D splashGraphics;               // graphics context for overlay of the splash image
    static Rectangle2D.Double splashTextArea;       // area where we draw the text
    static Rectangle2D.Double authors;       // area where we draw the text
    static Rectangle2D.Double splashProgressArea;   // area where we draw the progress bar
    static Font font;
    static String lastText; // used to draw our text
    static boolean isFirstTime = true;
    static String[] proponents = {"Chua, N.", "Nieva, P.", "Pol, K."};
    public MIPSSplash(){
        
    }
//    public static void main(String[] args) {
//        splashInit();           // initialize splash overlay drawing parameters
//        appInit();              // simulate what an application would do before starting
//        if (mySplash != null) // check if we really had a spash screen
//        {
//            mySplash.close();   // we're done with it
//        }
//        // begin with the interactive portion of the program
//    }

    /**
     * just a stub to simulate a long initialization task that updates the text
     * and progress parts of the status in the Splash
     */
    public static void appInit() {
        for (int i = 1; i <= 3; i++) {   // pretend we have 10 things to do
            int pctDone = i * 15;       // this is about the only time I could calculate rather than guess progress
//            splashText("Doing task #" + i);     // tell the user what initialization task is being done
            // splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.CLEAR, 1));
            splashText("Simulator by " + proponents[i - 1]);     // tell the user what initialization task is being done
            splashProgress(pctDone);            // give them an idea how much we have completed
            try {
                Thread.sleep(1250);             // wait a second
            } catch (InterruptedException ex) {
                break;
            }
        }
        try {
            splashProgress(100);
            Thread.sleep(1050);
        } catch (InterruptedException ex) {
        }
    }

    /**
     * Prepare the global variables for the other splash functions
     */
    public static void splashInit() {
        // the splash screen object is created by the JVM, if it is displaying a splash image

        mySplash = SplashScreen.getSplashScreen();
        // if there are any problems displaying the splash image
        // the call to getSplashScreen will returned null

        if (mySplash != null) {
            // get the size of the image now being displayed
            System.out.println("#testing splash not null");
            Dimension ssDim = mySplash.getSize();
            int height = ssDim.height;
            int width = ssDim.width;

            // stake out some area for our status information
//            splashTextArea = new Rectangle2D.Double(15., height*0.90, width * .45, 32.);
            splashTextArea = new Rectangle2D.Double(160., 190, width * .45, 32.);
            splashProgressArea = new Rectangle2D.Double(width * .55, height * .92, width * .4, 12);

            // create the Graphics environment for drawing status info
            splashGraphics = mySplash.createGraphics();
            font = new Font("Dialog", Font.PLAIN, 32);
            splashGraphics.setFont(font);

            // initialize the status info
            splashText("b3h");
            splashProgress(0);
            try {
                Thread.sleep(200);
            } catch (InterruptedException ex) {
                Logger.getLogger(MIPSSplash.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Display text in status area of Splash. Note: no validation it will fit.
     *
     * @param str - text to be displayed
     */
    public static void splashText(String str) {

        if (mySplash != null && mySplash.isVisible() && !isFirstTime) {   // important to check here so no other methods need to know if there
            // really is a Splash being displayed

            // erase the last status text
//             Color myColour = new Color(1, 0,0, 0);
//            splashGraphics.setPaint(myColour);
//              splashGraphics.fill(splashTextArea);
            splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.DST_OUT, 1f));
            splashGraphics.drawString(lastText, (int) (splashTextArea.getX() + 10), (int) (splashTextArea.getY() + 15));
//            Color myColour = new Color(1, 0,0, 0);
//            splashGraphics.setPaint(myColour);
//            splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 0.0f));
//            splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1));
//            Color myColour = new Color(255, 0, 0, 0);
//            splashGraphics.setPaint(myColour);
//            splashGraphics.fill(splashTextArea);

            // draw the text
//            splashGraphics.setPaint(Color.BLACK);
//            splashGraphics.drawString(str, (int) (splashTextArea.getX() + 10), (int) (splashTextArea.getY() + 15));
//            // make sure it's displayed
//            mySplash.update();
        }
        if (mySplash != null && mySplash.isVisible()) {
        splashGraphics.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC, 1f));
        isFirstTime = false;
        lastText = str;
        splashGraphics.setPaint(Color.BLACK);
        splashGraphics.drawString(str, (int) (splashTextArea.getX() + 10), (int) (splashTextArea.getY() + 15));
        // make sure it's displayed
        mySplash.update();
        }
    }

    /**
     * Display a (very) basic progress bar
     *
     * @param pct how much of the progress bar to display 0-100
     */
    public static void splashProgress(int pct) {
        Graphics2D splashGraphics2 = mySplash.createGraphics();
        if (mySplash != null && mySplash.isVisible()) {

            // Note: 3 colors are used here to demonstrate steps
            // erase the old one
            splashGraphics2.setPaint(Color.LIGHT_GRAY);
            splashGraphics2.fill(splashProgressArea);

            // draw an outline
            splashGraphics2.setPaint(Color.BLUE);
            splashGraphics2.draw(splashProgressArea);

            // Calculate the width corresponding to the correct percentage
            int x = (int) splashProgressArea.getMinX();
            int y = (int) splashProgressArea.getMinY();
            int wid = (int) splashProgressArea.getWidth();
            int hgt = (int) splashProgressArea.getHeight();

            int doneWidth = Math.round(pct * wid / 100.f);
            doneWidth = Math.max(0, Math.min(doneWidth, wid - 1));  // limit 0-width

            // fill the done part one pixel smaller than the outline
            splashGraphics2.setPaint(Color.GREEN);
            splashGraphics2.fillRect(x, y + 1, doneWidth, hgt - 1);

            // make sure it's displayed
            mySplash.update();
        }
    }

}
