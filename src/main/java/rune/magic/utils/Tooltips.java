package rune.magic.utils;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTextTooltip;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("null")
public class Tooltips {
    private static final TooltipColors DEFAULT_COLORS = new TooltipColors(TextColor.fromRgb(0xF0100010), TextColor.fromRgb(0xF0100010), TextColor.fromRgb(0x505000FF), TextColor.fromRgb(0x5028007F));
    private static final FormattedCharSequence SPACE = FormattedCharSequence.forward(" ", Style.EMPTY);

    public static List<ClientTooltipComponent> centerTitle(List<ClientTooltipComponent> components, Font font, int minWidth) {
        // Calculate tooltip width first.
        int tooltipWidth = minWidth;

        for (ClientTooltipComponent clienttooltipcomponent : components) {
            if (clienttooltipcomponent == null) {
                return components;
            }
            int componentWidth = clienttooltipcomponent.getWidth(font);
            if (componentWidth > tooltipWidth) {
                tooltipWidth = componentWidth;
            }
        }

        // TODO: If the title is multiple lines, we need to extend this for each one.
        // Find the title component, which is the first text component.
        int titleIndex = 0;
        for (ClientTooltipComponent clienttooltipcomponent : components) {
            if (clienttooltipcomponent instanceof ClientTextTooltip) {
                break;
            }
            titleIndex++;
        }

        if (titleIndex >= components.size()) {
            titleIndex = 0;
        }

        List<FormattedText> recomposedLines = StringRecomposer.recompose(Collections.singletonList(components.get(titleIndex)));
        if (recomposedLines.isEmpty()) {
            return components;
        }

        List<ClientTooltipComponent> result = new ArrayList<>(components);

        FormattedCharSequence title = Language.getInstance().getVisualOrder(recomposedLines.get(0));
        while (result.get(titleIndex).getWidth(font) < tooltipWidth) {
            title = FormattedCharSequence.fromList(Arrays.asList(SPACE, title, SPACE));
            if (title == null) {
                break;
            }

            result.set(titleIndex, ClientTooltipComponent.create(title));
        }
        return result;
    }

    public record TooltipColors(TextColor backgroundColorStart, TextColor backgroundColorEnd,
                                TextColor borderColorStart, TextColor borderColorEnd) {
    }
}