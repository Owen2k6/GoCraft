// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna;

public class NativeLong extends IntegerType
{
    private static final long serialVersionUID = 1L;
    public static final int SIZE;
    
    public NativeLong() {
        this(0L);
    }
    
    public NativeLong(final long value) {
        this(value, false);
    }
    
    public NativeLong(final long value, final boolean unsigned) {
        super(NativeLong.SIZE, value, unsigned);
    }
    
    static {
        SIZE = Native.LONG_SIZE;
    }
}
