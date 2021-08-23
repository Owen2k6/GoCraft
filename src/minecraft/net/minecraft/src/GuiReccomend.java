package minecraft.net.minecraft.src;

import org.lwjgl.input.Keyboard;

public class GuiReccomend extends GuiScreen {
    private GuiScreen parentScreen;
    

    public GuiReccomend(GuiScreen var1) {
        this.parentScreen = var1;
    }

    public void updateScreen() {
       
    }

    public void initGui() {
        StringTranslate var1 = StringTranslate.getInstance();
        Keyboard.enableRepeatEvents(true);
        this.controlList.clear();
        this.controlList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 48 + 12, var1.translateKey("RetroMC")));
        this.controlList.add(new GuiButton(2, this.width / 2 - 100, this.height / 4 + 72 + 12, var1.translateKey("Betalands")));
        this.controlList.add(new GuiButton(3, this.width / 2 - 100, this.height / 4 + 96 + 12, var1.translateKey("GoBeta")));
        this.controlList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + 12, var1.translateKey("Back to main menu")));
        String var2 = this.mc.gameSettings.lastServer.replaceAll("_", ":");
        ((GuiButton)this.controlList.get(0)).enabled = var2.length() > 0;
    }

    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    protected void actionPerformed(GuiButton var1) {
        if (var1.enabled) {
            if (var1.id == 1) {
                this.mc.displayGuiScreen(this.parentScreen);
            } else if (var1.id == 0) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiConnecting(this.mc, "mc.retromc.org", 25565));
            } else if (var1.id == 2) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiConnecting(this.mc, "betalands.com", 25565));
            } else if (var1.id == 3) {
                this.mc.gameSettings.saveOptions();
                this.mc.displayGuiScreen(new GuiConnecting(this.mc, "gobeta.goplex.me", 25565));
            }

        }
    }
    

    private int parseIntWithDefault(String var1, int var2) {
        try {
            return Integer.parseInt(var1.trim());
        } catch (Exception var4) {
            return var2;
        }
    }

    public void drawScreen(int var1, int var2, float var3) {
        StringTranslate var4 = StringTranslate.getInstance();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRenderer, var4.translateKey("Recomended servers"), this.width / 2, this.height / 4 - 60 + 20, 16777215);
        super.drawScreen(var1, var2, var3);
    }
}
