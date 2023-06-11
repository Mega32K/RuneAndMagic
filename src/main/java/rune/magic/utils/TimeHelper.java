package rune.magic.utils;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.function.LongSupplier;

public class TimeHelper {
    public static LongSupplier timeSource = System::nanoTime;
    public int min_i;
    public int max_i;
    public double min_d;
    public double max_d;
    public int integer_time;
    public double double_time;
    public boolean integer_logic;
    public boolean double_logic;

    public TimeHelper(int min, int max) {
        min_i = min;
        max_i = max;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public TimeHelper(double min, double max) {
        min_d = min;
        max_d = max;
        MinecraftForge.EVENT_BUS.register(this);
    }

    public TimeHelper() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static TimeHelper create(TimeHelper timeHelper, int min, int max) {
        if (timeHelper == null)
            return new TimeHelper(min, max);
        return timeHelper;
    }

    public static long millis() {
        return nanos() / 1000000L;
    }

    public static long nanos() {
        return timeSource.getAsLong();
    }

    public boolean aSecond() {
        return integer_time % 100 == 0;
    }

    @SubscribeEvent
    public void renderTick(TickEvent.RenderTickEvent event) {
        if (max_i > min_i) {
            if (!integer_logic) {
                if (integer_time > min_i)
                    integer_time--;
                if (integer_time <= min_i)
                    integer_logic = true;
            }
            if (integer_logic) {
                if (integer_time < max_i)
                    integer_time++;
                if (integer_time >= max_i)
                    integer_logic = false;
            }
        } else integer_time++;
        if (max_d > min_d) {
            if (!double_logic) {
                if (double_time > min_d)
                    double_time--;
                if (double_time <= min_d)
                    double_logic = true;
            }
            if (double_logic) {
                if (double_time < max_d)
                    double_time++;
                if (double_time >= max_d)
                    double_logic = false;
            }
        } else double_time++;
    }
}
