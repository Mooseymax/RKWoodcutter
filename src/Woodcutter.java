import org.osbot.rs07.script.Script;
import org.osbot.rs07.script.ScriptManifest;

@ScriptManifest(author = "OSRunekeep", name = "RK Tutorial", info = "Tutorial Island Buster", version = 0.1, logo = "")

public final class Woodcutter extends Script {

    @Override
    public final int onLoop() throws InterruptedException {
        return 0;
    }

    @Override
    public final void onStart() {
        log("On Start Test");
    }

    @Override
    public final void OnExit() {
        log("On Exit Test");
    }

}
