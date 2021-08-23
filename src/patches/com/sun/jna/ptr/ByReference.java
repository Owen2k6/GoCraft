// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.ptr;

import patches.com.sun.jna.Pointer;
import patches.com.sun.jna.Memory;
import patches.com.sun.jna.PointerType;

public abstract class ByReference extends PointerType
{
    protected ByReference(final int dataSize) {
        this.setPointer(new Memory(dataSize));
    }
}
