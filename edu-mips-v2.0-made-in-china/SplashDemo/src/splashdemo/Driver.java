/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package splashdemo;


/**
 *
 * @author joechua
 */
public class Driver {

    public static void main(String[]args) {
        MIPSSplash m = new MIPSSplash();
        m.splashInit();           // initialize splash overlay drawing parameters
        m.appInit();              // simulate what an application would do before starting
        if (m.mySplash != null) // check if we really had a spash screen
        {
            m.mySplash.close();   // we're done with it
        }
        // begin with the interactive portion of the program
    }
}
