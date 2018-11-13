import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import java.io.*;

import java.awt.*;
import java.util.jar.Manifest;
//import org.osbot.rs07.api.model.Item;

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 1.1, logo = "")

public final class Woodcutter extends Script {

    //////// VARIABLES ////////

    // Variables to store how many logs cut
    private long initialLogs;       // Logs you started with
    private long currentLogs;    // Current logs in inventory
    private long previousLogs;   // Previous log count (for comparison)
    private long logsCollected;     // Counter for total logs cut

    // Logging time
    private long startTime;
    private long runTime;

    // Logging level
    private int startLevel;

    // Auto Highest Tree
    private String highestTree;
    private String highestLog;
    int location = 0; // Reset location to 0

    //////// FUNCTIONS ////////

    // Converts a time in ms to HH MM SS etc.
    public final String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }

    private String getHighestTree(int curLevel){
        String treeType;
        log("NOTE: modified values to ignore willow for now");

        if(curLevel >= 0 && curLevel < 15){
            treeType = "Tree";
        } else if(curLevel >= 15 && curLevel < 50){
            treeType = "Oak";
        } else if(curLevel >= 50 && curLevel < 55){
            treeType = "Willow";
        } else if(curLevel >= 55 && curLevel < 65) {
            treeType = "Maple";
        } else {
            treeType = "Yew";
        }

        return treeType;
    }

    private String getHighestLog(String tree){
        String logOutput = "";

        switch(tree){
            case "Tree":    logOutput = "Logs";
                            break;
            case "Oak":     logOutput = "Oak logs";
                            break;
            case "Willow":  logOutput = "Willow logs";
                            break;
            case "Yew":     logOutput = "Yew logs";
                            break;
        }
        return logOutput;
    }

    private Area getTreeArea(String treeType) {
        Area treeLocation = new Area(0,0, 0, 0).setPlane(0);

        switch(treeType){
            case "Tree":        treeLocation = new Area(3202,3236, 3186, 3255).setPlane(0);
                                break;

            case "Oak":         int locations = 2; // Number of locations
                                if(location == 0){
                                    location = random(1,locations);
                                }

                                switch (location){
                                    case 1: treeLocation = new Area(3186,3246, 3193, 3252).setPlane(0);
                                            break;
                                    case 2: treeLocation = new Area(3202,3238, 3206, 3249).setPlane(0);
                                }
                                break;

            case "Willow": treeLocation = new Area(1,1,1,1); //Update with WILLOW at some point
                                break;

            case "Maple":  treeLocation = new Area(1,1,1,1); //Update with MAPLE at some point
                                break;

            case "Yew":    treeLocation = new Area(1,1,1,1); //Update with YEW at some point
                                break;
        }

        // Small check to see whether treeType was found
        if(treeLocation == new Area(0,0, 0, 0).setPlane(0)){
            log("Tree type not found; defaulting to normal tree.");
            treeLocation = new Area(2302,3236, 3186, 3255).setPlane(0);
        }

        return treeLocation;
    }

    private boolean chop(String treeType) {
        return getObjects().closest(treeType).interact("Chop down");
    }

    private void chopTree(String treeType){

        // Finds closest tree
        RS2Object treeObj = getObjects().closest(treeType);

        // Walks to tree area
        if(!getTreeArea(treeType).contains(myPlayer())){
            getWalking().webWalk(getTreeArea(treeType));
        } else {
            if(!myPlayer().isAnimating() && treeObj != null){
                // Sleeps for a random number of seconds
                try{
                    sleep(random(100,3000));
                } catch(InterruptedException e) {
                    log("Sleep Interrupted (for some reason???!)");
                }

                // Chops tree
                chop(treeType);

                log("Chopping " + treeObj.getName());

                new ConditionalSleep(7000){
                    @Override
                    public final boolean condition() throws InterruptedException {
                        return myPlayer().isAnimating();
                    }
                }.sleep();
            } else {
                try{
                    sleep(random(100,3000));
                } catch(InterruptedException e) {
                    log("Sleep Interrupted (for some reason???!)");
                }
            }
        }
    }

    private boolean shouldBank() {
        return getInventory().isFull();
    }

    private void bank() throws InterruptedException {
        if(!Banks.LUMBRIDGE_UPPER.contains(myPlayer())){
            getWalking().webWalk(Banks.LUMBRIDGE_UPPER);
        } else {
            if(!getBank().isOpen()){
                getBank().open();
            } else {
                getBank().depositAllExcept(axes -> axes.getName().contains(" axe"));
            }
        }
        previousLogs = 0;
    }

    //////// INITIALISATION ////////

    @Override
    public final void onStart() {
        log("RK Woodcutter started! Version test v1.1.");

        startLevel = skills.getStatic(Skill.WOODCUTTING);
        highestTree = getHighestTree(startLevel);
        log(highestTree);

        highestLog = getHighestLog(highestTree);
        log(highestLog);

        startTime = System.currentTimeMillis();
        initialLogs = getInventory().getAmount(highestLog);
        previousLogs = initialLogs;
    }

    // Loop
    @Override
    public final int onLoop() throws InterruptedException {
        // Main Loop
        currentLogs = getInventory().getAmount(highestLog);
        if(currentLogs > previousLogs){
            logsCollected++;
            log("You have collected " + logsCollected + " "  + highestTree + " logs.");
            previousLogs = currentLogs;
        }
        if(shouldBank()){
            bank();
        } else {
            chopTree(highestTree);
        }
        return random(100,500);
    }

    @Override
    public final void onExit() {
        log("Thanks for chopping! You chopped " + logsCollected + " "  + highestTree + " logs!");
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        log("Game log: " + message.getMessage());
    }

    @Override
    public void onPaint(final Graphics2D g) {
        //Paint code
        runTime = System.currentTimeMillis() - startTime;
        Font font = new Font("Arial", Font.BOLD, 12);
        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString("Runekeep Woodcutter",10,255);
        g.drawString("Run Time: " + formatTime(runTime),10,270);
        g.drawString("Logs Collected: " + logsCollected,10,285);
        g.drawString("Current Level: " + skills.getStatic(Skill.WOODCUTTING),10,300);
        g.drawString("Levels Gained: TBA",10,315);
        g.drawString("Next Level In: TBA",10,330);

        String treeType = ("Current Tree: " + highestTree);
        g.drawString(treeType,510 - treeType.length()*7, 20);
    }





}
