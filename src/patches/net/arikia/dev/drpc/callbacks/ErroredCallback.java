// 
// Decompiled by Procyon v0.5.36
// 

package patches.net.arikia.dev.drpc.callbacks;

import com.sun.jna.Callback;

public interface ErroredCallback extends Callback
{
    void apply(final int p0, final String p1);
}
