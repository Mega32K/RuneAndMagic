package xclient.mega.mod.bigmodule;

import xclient.mega.Main;
import xclient.mega.screen.XScreen;
import xclient.mega.utils.Vec2d;

public interface ActionBm {
    default void update(BigModuleBase base) {
        if (base.isPressing) {
            if (base.asModule) {
                base.pos = new Vec2d(XScreen.mouseX, XScreen.mouseY);
            } else {
                Main._x_ = XScreen.mouseX;
                Main._y_ = XScreen.mouseY;
            }
        }
    }

    void click(int code);
}
