// 
// Decompiled by Procyon v0.5.36
// 

package patches.net.arikia.dev.drpc.callbacks;

import patches.net.arikia.dev.drpc.DiscordUser;
import com.sun.jna.Callback;

public interface JoinRequestCallback extends Callback
{
    void apply(final DiscordUser p0);
}
