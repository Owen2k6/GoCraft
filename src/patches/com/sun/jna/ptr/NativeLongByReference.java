// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.ptr;

import patches.com.sun.jna.NativeLong;

public class NativeLongByReference extends ByReference
{
    public NativeLongByReference() {
        this(new NativeLong(0L));
    }
    
    public NativeLongByReference(final NativeLong value) {
        super(NativeLong.SIZE);
        this.setValue(value);
    }
    
    public void setValue(final NativeLong value) {
        this.getPointer().setNativeLong(0L, value);
    }
    
    public NativeLong getValue() {
        return this.getPointer().getNativeLong(0L);
    }
}
