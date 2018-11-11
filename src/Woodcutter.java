import org.osbot.rs07.api.ui.Message;
import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "OSRunekeep", name = "RK Woodcutter", info = "Simple Woodcutter", version = 0.1, logo = "")

public final class Woodcutter extends Script {

    @Override
    public final int onLoop() throws InterruptedException {
        return 0;
    }

    @Override
    public final void onStart() {
        log("On Start Test.");
        log("Jed sucks dick.")
    }

    @Override
    public final void onExit() {
        log("On Exit Test");
    }

    @Override
    public void onMessage(Message message) throws InterruptedException {
        super.onMessage(message);
    }

    

}
