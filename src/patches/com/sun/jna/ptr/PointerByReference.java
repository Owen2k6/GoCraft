// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.ptr;

import patches.com.sun.jna.Pointer;

public class PointerByReference extends ByReference
{
    public PointerByReference() {
        this(null);
    }
    
    public PointerByReference(final Pointer value) {
        super(Pointer.SIZE);
        this.setValue(value);
    }
    
    public void setValue(final Pointer value) {
        this.getPointer().setPointer(0L, value);
    }
    
    public Pointer getValue() {
        return this.getPointer().getPointer(0L);
    }
}
