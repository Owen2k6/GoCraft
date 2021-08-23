// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.nametag;

import java.awt.Color;

public class UsableTagColor
{
    private final boolean backLight;
    private final int color;
    private boolean isRainbow;
    
    public UsableTagColor(final TagColor color) {
        this.isRainbow = false;
        this.color = ColorsHandler.getColor(color);
        if (color.equals(TagColor.RAINBOW)) {
            this.isRainbow = true;
        }
        if (color.equals(TagColor.NORMAL)) {
            this.backLight = true;
        }
        else {
            this.backLight = false;
        }
    }
    
    public UsableTagColor(final int color, final boolean backLight) {
        this.isRainbow = false;
        this.color = color;
        this.backLight = backLight;
    }
    
    public boolean isBackLight() {
        return this.backLight;
    }
    
    public int getColor() {
        if (this.isRainbow) {
            return Color.HSBtoRGB(System.currentTimeMillis() % 1000L / 1000.0f, 0.55f, 0.95f);
        }
        return this.color;
    }
}
