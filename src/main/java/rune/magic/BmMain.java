package rune.magic;

import net.minecraftforge.fml.common.Mod;
import rune.magic.utils.TimeHelper;

import java.util.Date;

@Mod.EventBusSubscriber
public class BmMain {
    public static Date date = new Date();
    public static TimeHelper timeHelper = new TimeHelper(20, 145);

    public BmMain() {

    }
}
