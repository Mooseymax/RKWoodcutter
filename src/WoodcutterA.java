import org.osbot.rs07.api.map.Area;
import org.osbot.rs07.api.map.constants.Banks;
import org.osbot.rs07.api.model.RS2Object;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.api.ui.Skill;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;
import java.util.jar.Manifest;
import java.io. * ;
import java.awt. * ;

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 2.0, logo = "")

public final class WoodcutterA extends Script {
    //region Variables
    //---------------------------------------------------------------------------------------
    private long initialLogs;
    private long currentLogs;
    private long previousLogs;
    private long logsCollected;
    
    private long startTime;
    private long runTime;
    
    private int startLevel;
    
    private String highestTree;
    private String highestLog;
    private int locationChoice = 0;
    //---------------------------------------------------------------------------------------
    //end region
    
    //region Functions
    //---------------------------------------------------------------------------------------
    private final String formatTime(final long ms) {
        long s = ms / 1000,
		m = s / 60,
		h = m / 60,
		d = h / 24;
		s %= 60;
		m %= 60;
		h %= 24;

        return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) :
                h > 0 ? String.format("%02d:%02d:%02d", h, m, s) :
                        String.format("%02d:%02d", m, s);
    }
    
    private String getHighestTree(int curLevel) {
        int tree = 0;
        int oak = 15;
        int willow = 99;
        int maple = 99;
        int yew = 99;
        
        String treeType;
        
        if(curLevel >= yew) {
            treeType = "Yew";
        } else if (curLevel >= maple) {
            treeType = "Maple";
        } else if (curLevel >= willow) {
            treeType = "Willow";
        } else if (curLevel >= oak) {
            treeType = "Oak";
        } else {
            treeType = "Tree";
        }
        
        return treeType;
    }
    
    private String getHighestLog(String tree) {
        String logType = "Logs";

        switch (tree) {
            case "Tree":
                logType = "Logs";
                break;
            case "Oak":
                logType = "Oak logs";
                break;
            case "Willow":
                logType = "Willow logs";
                break;
            case "Yew":
                logType = "Yew logs";
                break;
        }
        return logType;
    }
    
    private Area getTreeArea(String treeType) {
        TreeArea Trees = new TreeArea();
        Area treeLocation = new Area(0,0,0,0).setPlane(0);
        int locations;

        switch(treeType){
        case "Tree":
            locations = 2;
            if(locationChoice == 0){
                locationChoice = random(1, locations);
            }
            switch(locationChoice){
                case 1:
                    treeLocation = Trees.TREE_A;
                case 2:
                    treeLocation = Trees.TREE_B;
            }
            break;

        case "Oak":
            locations = 2;
            if(locationChoice == 0){
                locationChoice = random(1, locations);
            }
            switch(locationChoice){
                case 1:
                    treeLocation = Trees.OAK_A;
                case 2:
                    treeLocation = Trees.OAK_B;
            }
            break;

        case "Willow":
            locations = 1;
            if(locationChoice == 0){
                locationChoice = random(1, locations);
            }
            switch(locationChoice){
                case 1:
                    treeLocation = Trees.WILLOW;
            }
            break;

        case "Maple":
            locations = 1;
            if(locationChoice == 0){
                locationChoice = random(1, locations);
            }
            switch(locationChoice){
                case 1:
                    treeLocation = Trees.MAPLE;
            }
            break;

        case "Yew":
            locations = 1;
            if(locationChoice == 0){
                locationChoice = random(1, locations);
            }
            switch(locationChoice){
                case 1:
                    treeLocation = Trees.YEW;
            }
            break;
        }

        return treeLocation;
    }

    private boolean chop(String treeType) {
        return getObjects().closest(treeType).interact("Chop down");
    }

    private void chopTree(String treeType){
        RS2Object treeObj = getObjects().closest(treeType);

        if(!getTreeArea(treeType).contains(myPlayer())){
            getWalking().webWalk(getTreeArea(treeType));
        } else {
            if(!myPlayer().isAnimating() && treeObj != null) {
                try {
                    sleep(random(100, 3000));
                } catch (InterruptedException e) {
                    log("Line 117: Sleep Interrupted.");
                }

                // Chop Tree
                chop(treeType);

                log("Chopping " + treeObj.getName());

                new ConditionalSleep(7000) {
                    @Override
                    public final boolean condition() throws InterruptedException {
                        return myPlayer().isAnimating();
                    }
                }.sleep();
            } else {
                try{
                    sleep(random(100,3000));
                } catch(InterruptedException e) {
                    log("Line 195: Sleep Interrupted.");
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
    //---------------------------------------------------------------------------------------
    //end region
    
    //region Classes
    //---------------------------------------------------------------------------------------
    public class TreeArea {
        public final Area TREE_A = new Area(3202, 3236, 3186, 3255).setPlane(0);
        public final Area TREE_B = new Area(0,0,0,0).setPlane(0);
        public final Area OAK_A = new Area(0,0,0,0).setPlane(0);
        public final Area OAK_B = new Area(0,0,0,0).setPlane(0);
        public final Area WILLOW = new Area(0,0,0,0).setPlane(0);
        public final Area MAPLE = new Area(0,0,0,0).setPlane(0);
        public final Area YEW = new Area(0,0,0,0).setPlane(0);
    }
    //---------------------------------------------------------------------------------------
    //end region

    //region Main
    //---------------------------------------------------------------------------------------
    @Override
    public final void onStart(){
        log("RK Woodcutter V2.0 Started!");

        startLevel = skills.getStatic(Skill.WOODCUTTING);
        highestTree = getHighestTree(startLevel);
        highestLog = getHighestLog(highestTree);
        log("Best tree: " + highestTree);
        log("Best log: " + highestLog);

        startTime = System.currentTimeMillis();

        initialLogs = getInventory().getAmount(highestLog);
        previousLogs = initialLogs;
        log("Found " + initialLogs + " in inventory.");
        log("All initial checks done.");
    }

    @Override
    public final int onLoop() throws InterruptedException {
        // Start by counting logs
        currentLogs = getInventory().getAmount(highestLog);
        if(currentLogs > previousLogs){
            logsCollected++;
            previousLogs = currentLogs;
            log("Total logs collected: " + logsCollected);
        }

        // Check if inventory full
        if(shouldBank()){
            bank();
            log("Inventory full; banking.");
        } else {
            chopTree(highestTree);
        }
        return (random(100,500));
    }

    @Override
    public final void onExit() {
        log("Thanks for using RK Woodcutter!");
        log("Logs cut: " + logsCollected);
        log("Levels gained: " + (skills.getStatic(Skill.WOODCUTTING) - startLevel));
        log("Run time: " + formatTime(runTime));
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        log("Game log: " + message.getMessage());
    }

    @Override
    public void onPaint(final Graphics2D g) {
        runTime = System.currentTimeMillis() - startTime;
        Font font = new Font("Arial", Font.BOLD, 12);
        g.setColor(Color.WHITE);
        g.setFont(font);

        g.drawString("Runekeep Woodcutter",10,255);
        g.drawString("Run Time: " + formatTime(runTime),10,270);
        g.drawString("Logs Collected: " + logsCollected,10,285);
        g.drawString("Current Level: " + skills.getStatic(Skill.WOODCUTTING),10,300);
        g.drawString("Levels Gained: " + (skills.getStatic(Skill.WOODCUTTING) - startLevel),10,315);
        g.drawString("Next Level In: TBA",10,330);

        String treeType = ("Current Tree: " + highestTree);
        g.drawString(treeType,510 - treeType.length()*7, 20);
    }
    //---------------------------------------------------------------------------------------
    //end region
}