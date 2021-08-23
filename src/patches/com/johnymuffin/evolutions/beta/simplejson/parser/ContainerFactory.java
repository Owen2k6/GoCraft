// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.simplejson.parser;

import java.util.List;
import java.util.Map;

public interface ContainerFactory
{
    Map createObjectContainer();
    
    List creatArrayContainer();
}
