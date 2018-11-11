import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

import java.awt.*;
import java.util.jar.Manifest;
//import org.osbot.rs07.api.model.Item;

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 1.0, logo = "")

public final class Woodcutter extends Script {

    // Logging logs
    private long initialLogs;
    private long currentLogs;
    private long previousLogs;
    private long logsCollected;

    // Logging time
    private long startTime;
    private long runTime;

    @Override
    public final int onLoop() throws InterruptedException {
        // Main Loop
        currentLogs = getInventory().getAmount("Logs");
        if(currentLogs > previousLogs){
            logsCollected++;
            log("You have collected " + logsCollected + " logs.");
            previousLogs = currentLogs;
        }
        if(shouldBank()){
            bank();
        } else {
            chopTree();
        }
        return random(100,3000);
    }

    public final String formatTime(final long ms){
        long s = ms / 1000, m = s / 60, h = m / 60, d = h / 24;
        s %= 60; m %= 60; h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }

    @Override
    public final void onStart() {
        log("RK Woodcutter started! Version test v1.0.");
        startTime = System.currentTimeMillis();
        initialLogs = getInventory().getAmount("Logs");
        previousLogs = initialLogs;
    }

    @Override
    public final void onExit() {
        log("Thanks for chopping! You chopped " + logsCollected + " logs!");
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        log("New message: " + message.getMessage());
    }

    private void chopTree(){
        RS2Object tree = getObjects().closest("Tree");
        if(!getTreeArea().contains(myPlayer())){
            getWalking().webWalk(getTreeArea());
        } else {
            if(!myPlayer().isAnimating() && tree != null){
                try{
                    sleep(random(500,2000));
                } catch(InterruptedException e) {
                    log("Sleep Interrupted (for some reason???!)");
                }
                chop("Tree");
                log("Chopping tree");

                new ConditionalSleep(5000){
                    @Override
                    public final boolean condition() throws InterruptedException {
                        return myPlayer().isAnimating();
                    }
                }.sleep();
            }
        }
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

    @Override
    public void onPaint(final Graphics2D g) {
        //Paint code
        runTime = System.currentTimeMillis() - startTime;
        g.drawString("Run Time: " + formatTime(runTime),10,330);
    }

    private boolean chop(String treeType) {
        return getObjects().closest(treeType).interact("Chop down");
    }

    private boolean shouldBank() {
        return getInventory().isFull();
    }

    private Area getTreeArea() {
        Area treeLocation = new Area(2302,3236, 3186, 3255).setPlane(0);
        Area oakLocation = new Area(1,1,1,1); //Update with OAK at some point
        return treeLocation;
    }



}
