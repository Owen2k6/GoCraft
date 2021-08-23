// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.nametag;

public class ColorsHandler
{
    public static int getColor(final TagColor tagColor) {
        switch (tagColor) {
            case RED: {
                return 16711680;
            }
            case YELLOW: {
                return 16776960;
            }
            case BLUE: {
                return 255;
            }
            case GREEN: {
                return 32768;
            }
            case CYAN: {
                return 65535;
            }
            case ORANGE: {
                return 16753920;
            }
            case PURPLE: {
                return 6950317;
            }
            default: {
                return 553648127;
            }
        }
    }
}
