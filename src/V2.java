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

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 1.2, logo = "")

public final class Woodcutter extends Script {
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

		return d > 0 ? String.format("%02d:%02d:%02d:%02d", d, h, m, s) : h > 0 ? String.format("%02d:%02d:%02d", h, m, s) : String.format("%02d:%02d", m, s);
    }
    
    private String getHighestTree(int curLevel) {
        private int tree = 0;
        private int oak = 15;
        private int willow = 99;
        private int maple = 99;
        private int yew = 99;
        
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
    
    privae String getHighestLog(String tree) {
        String logType = "Logs";
        
    		switch (tree) {
		case "Tree":
			logOutput = "Logs";
			break;
		case "Oak":
			logOutput = "Oak logs";
			break;
		case "Willow":
			logOutput = "Willow logs";
			break;
		case "Yew":
			logOutput = "Yew logs";
			break;
        
        return logType;
    }
    
    public final class TreeArea {
        public static final Area TREE_A = new Area(3202, 3236, 3186, 3255).setPlane(0);
        public static final Area TREE_B = new Area().setPlane(0);
        public static final Area OAK_A = new Area().setPlane(0);
        public static final Area OAK_B = new Area().setPlane(0);
        public static final Area WILLOW = new Area().setPlane(0);
        public static final Area MAPLE = new Area().setPlane(0);
        public static final Area YEW = new Area().setPlane(0);
    }
    
    //---------------------------------------------------------------------------------------
    end region
}