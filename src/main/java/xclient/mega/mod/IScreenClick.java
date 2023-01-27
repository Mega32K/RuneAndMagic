package xclient.mega.mod;

import xclient.mega.BmMain;
import xclient.mega.Main;
import xclient.mega.mod.Module;
import xclient.mega.mod.bigmodule.ActionBmC;
import xclient.mega.mod.bigmodule.BigModuleBase;
import xclient.mega.screen.XScreen;

public interface IScreenClick {
    default void click(double x, double y, int code) {
        for (BigModuleBase bm : BmMain.CREATED) {
            if (bm.isInRange_asModule())
                bm.click(code);
            for (Module<?> module : Module.every) {
                if (module.getFather_Bm() instanceof ActionBmC actionBmC && actionBmC.pushed && module.getFather_Bm().getName().equals(bm.getName()))
                    if (XScreen.isInRange(module, (int) x, (int) y)) {
                        if (code == 0) {
                            module.left();
                        }
                        if (code == 1) {
                            module.right();
                        }
                    }
            }
        }
        Main.setModules();
    }
}
