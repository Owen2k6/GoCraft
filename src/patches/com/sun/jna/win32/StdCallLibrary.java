// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.sun.jna.win32;

import patches.com.sun.jna.Callback;
import patches.com.sun.jna.FunctionMapper;
import patches.com.sun.jna.Library;

public interface StdCallLibrary extends Library, StdCall
{
    public static final int STDCALL_CONVENTION = 63;
    public static final FunctionMapper FUNCTION_MAPPER = new StdCallFunctionMapper();
    
    public interface StdCallCallback extends Callback, StdCall
    {
    }
}
