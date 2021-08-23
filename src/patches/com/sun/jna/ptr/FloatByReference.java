// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.ptr;

public class FloatByReference extends ByReference
{
    public FloatByReference() {
        this(0.0f);
    }
    
    public FloatByReference(final float value) {
        super(4);
        this.setValue(value);
    }
    
    public void setValue(final float value) {
        this.getPointer().setFloat(0L, value);
    }
    
    public float getValue() {
        return this.getPointer().getFloat(0L);
    }
}
