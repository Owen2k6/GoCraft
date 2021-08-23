// 
// Decompiled by Procyon v0.5.36
// 

package patches.net.arikia.dev.drpc;

public final class OSUtil
{
    public boolean isMac() {
        return this.getOS().toLowerCase().startsWith("mac");
    }
    
    public boolean isWindows() {
        return this.getOS().toLowerCase().startsWith("win");
    }
    
    public String getOS() {
        return System.getProperty("os.name").toLowerCase();
    }
}