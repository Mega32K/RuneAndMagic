package xclient.mega.utils;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraftforge.registries.DeferredRegister;

public class ColorPutter {
    public static final ChatFormatting[] Rainbow = new ChatFormatting[]{ChatFormatting.RED, ChatFormatting.GOLD, ChatFormatting.YELLOW, ChatFormatting.GREEN, ChatFormatting.AQUA, ChatFormatting.BLUE, ChatFormatting.LIGHT_PURPLE};

    public static String Format(String Input, ChatFormatting[] Style, double Delay, int Step) {
        StringBuilder Builder = new StringBuilder(Input.length() * 3);
        int Offset = (int) Math.floor(Util.getMillis() / Delay) % Style.length;
        for (int i = 0; i < Input.length(); ++i) {
            char c = Input.charAt(i);
            int col = (i * Step + Style.length - Offset) % Style.length;
            Builder.append(Style[col]);
            Builder.append(c);
        }
        return Builder.toString();
    }

    /*
        public String getName() {
            return ColorPutter.rainbow("name");
        }
     */
    public static String rainbow(String Input) {
        return Format(Input, Rainbow, 80.0D, 1);
    }
}
