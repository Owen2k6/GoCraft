// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.ptr;

public class ByteByReference extends ByReference
{
    public ByteByReference() {
        this((byte)0);
    }
    
    public ByteByReference(final byte value) {
        super(1);
        this.setValue(value);
    }
    
    public void setValue(final byte value) {
        this.getPointer().setByte(0L, value);
    }
    
    public byte getValue() {
        return this.getPointer().getByte(0L);
    }
}
