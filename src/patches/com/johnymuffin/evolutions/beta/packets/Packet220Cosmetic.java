// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.packets;

import java.io.DataOutputStream;
import java.io.IOException;
import patches.com.johnymuffin.evolutions.beta.UpdateReceiver;
import java.io.DataInputStream;

public class Packet220Cosmetic extends ki
{
    public String message;
    
    @Override
    public void a(final DataInputStream var1) throws IOException {
        UpdateReceiver.processUpdates(this.message = ki.a(var1, 1000));
    }
    
    @Override
    public void a(final DataOutputStream var1) throws IOException {
    }
    
    @Override
    public void a(final ti var1) {
    }
    
    @Override
    public int a() {
        return 0;
    }
}
