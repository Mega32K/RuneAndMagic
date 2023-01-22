package xclient.mega.mod;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import xclient.mega.Main;
import xclient.mega.mod.bigmodule.BigModuleBase;
import xclient.mega.utils.ColorPutter;
import xclient.mega.utils.Render2DUtil;
import xclient.mega.utils.RendererUtils;
import xclient.mega.utils.Vec2d;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Module<T> {

    public static List<Module<?>> every = new ArrayList<>();
    public Minecraft mc;
    public T value;
    public Font font;
    public Color color;
    public ModuleTodo left;
    public ModuleTodo right;
    public int x;
    public int y;
    public int width;
    public int height;
    public boolean enableColorPutter;
    public boolean hasChildren;
    public Module<?> FatherModule = null;
    public Set<Module<?>> children = new HashSet<>();
    private String name;
    private BigModuleBase Father_Bm = null;

    public Module(String name, T value, boolean enableColorPutter, Font font) {
        this.font = font;
        this.enableColorPutter = enableColorPutter;
        this.name = name;
        this.value = value;
        this.mc = Minecraft.getInstance();
        ModuleManager.addModule(this);
        every.add(this);
    }

    public Module(String name, T value, boolean enableColorPutter) {
        this(name, value, enableColorPutter, Minecraft.getInstance().font);
    }

    public boolean sameModule(Module<?> m) {
        return m.getName().equals(getName());
    }

    public Module(String name, T value) {
        this(name, value, false);
    }

    public Module(String name) {
        this(name, null);
    }

    public Module<T> setLeft(ModuleTodo left) {
        this.left = left;
        return this;
    }

    public Module<T> unaddToList() {
        ModuleManager.modules.remove(this);
        return this;
    }

    public Module<T> setRight(ModuleTodo right) {
        this.right = right;
        return this;
    }

    public BigModuleBase getFather_Bm() {
        return Father_Bm;
    }

    public Module<T> setFather_Bm(BigModuleBase father_Bm) {
        Father_Bm = father_Bm;
        return this;
    }

    public boolean sameBm(Object o) {
        return getFather_Bm() != null && getFather_Bm().getClass().isInstance(o);
    }

    public void left() {
        if (left != null) {
            left.run(this);
        } else System.out.println(getName() + " left module is NULL!");

    }

    public void right()  {
        if (right != null)
            right.run(this);
        else System.out.println(getName() + " right module is NULL!");

    }

    public String getName() {
        return enableColorPutter ? ColorPutter.rainbow(I18n.get(name.toLowerCase().replaceAll(" ","_"))) : I18n.get(name.toLowerCase().replaceAll(" ","_"));
    }

    public Module<T> setName(String name) {
        this.name = name;
        return this;
    }

    public String getPos() {
        return "x:" + x + " y:" + y + " width:" + width + " height" + height;
    }

    public String getInfo() {
        if (value instanceof Component component)
            return getName() + (value != null ? ":" + component.getString() : "");
        if (value instanceof Boolean b)
            return getName() + (value != null ? (b ? " " + Main.YES : "") : "");
        return getName() + (value != null ? ":" + value : "");
    }

    public Module<T> setFont(Font font) {
        this.font = font;
        return this;
    }

    public Module<T> setColor(Color color) {
        this.color = color;
        return this;
    }

    public Module<T> setValue(T value) {
        this.value = value;
        return this;
    }

    public void setPos(Vec2d value) {
        x = value.x;
        y = value.y;
    }

    public void render(PoseStack stack, int x, int y, boolean isMouseOver) {
        if (value instanceof Float f)
            value = (T) Float.valueOf(String.format("%.2f", f));
        int color = new Color(55, 71, 90, 150).getRGB();
        int color2 = new Color(0, 0, 0, Main.instance != null ? Main.base_timehelper.integer_time : 150).getRGB();
        this.x = x;
        this.y = y;
        Render2DUtil.drawRect(stack, x, y, (int) (width * 1.5F), height + 1, isMouseOver ? (this.color != null ? this.color.getRGB() : color2) : color);

        this.font.drawShadow(stack, getInfo(), x, y, RendererUtils.WHITE);
        if (width == 0)
            width = font.width(getInfo());
        if (height == 0)
            height = font.lineHeight;
    }

    public Module<T> addChild(Module<?>... modules) {
        children.addAll(Arrays.asList(modules));
        hasChildren = true;
        return this;
    }

    public Module<T> removeChild(Module<?>... modules) {
        for (Module<?> module : modules)
            children.remove(module);
        if (Arrays.asList(modules).get(1) == null)
            hasChildren = false;
        return this;
    }

    public Module<?> getFather() {
        return FatherModule;
    }

    public Module<T> setFather(Module<?> module) {
        FatherModule = module;
        return this;
    }

    public void setValueObj(Object o) {
        value = (T)o;
    }
}
