import org.osbot.rs07.api.model.Entity;
import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;
import org.osbot.rs07.utility.ConditionalSleep;

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 0.1, logo = "")

public final class Woodcutter extends Script {

    @Override
    public final int onLoop() throws InterruptedException {

        return 0;
    }

    @Override
    public final void onStart() {
        log("On Start Test.");
        log("Max sucks dick.");
        chopTree();
    }

    @Override
    public final void onExit() {
        log("On Exit Test");
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        log("New message: " + message.getMessage());
        // IDK what super.onMessage does lol
        //super.onMessage(message);
    }

    private void chopTree() {
        Entity tree = getObjects().closest("Tree");
        if(tree != null && tree.interact("Chop down")){
            // Chop down tree
            tree.interact("Chop down");
        }
    }

}
