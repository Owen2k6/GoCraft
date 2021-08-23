// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import com.johnymuffin.evolutions.beta.simplejson.JSONObject;
import patches.net.arikia.dev.drpc.DiscordRichPresence;
import java.util.Collection;
import java.util.Arrays;
import com.johnymuffin.evolutions.beta.nametag.TagColor;
import java.util.ArrayList;
import java.util.HashMap;
import com.johnymuffin.evolutions.beta.nametag.UsableTagColor;

public class BetaEVO
{
    private static BetaEVO betaEVO;
    private UsableTagColor defaultUsernameColor;
    private HashMap<String, UsableTagColor> nameTagColorCache;
    private ArrayList<String> mouseEars;
    private String skinURL;
    private String cloakURL;
    private String resourceURL;
    private String authURL;
    private final String version = "1.3";
    private String newestVersion;
    private int authenticationCount;
    private Object authLock;
    private boolean allowFlying;
    private DiscordThread discordThread;
    
    private BetaEVO() {
        this.defaultUsernameColor = new UsableTagColor(TagColor.NORMAL);
        this.nameTagColorCache = new HashMap<String, UsableTagColor>();
        this.mouseEars = new ArrayList<String>(Arrays.asList(new String[0]));
        this.skinURL = "http://s3.amazonaws.com/MinecraftSkins/<username>.png";
        this.cloakURL = "http://s3.amazonaws.com/MinecraftCloaks/<username>.png";
        this.resourceURL = "http://s3.amazonaws.com/MinecraftResources/";
        this.authURL = "http://www.minecraft.net/game/joinserver.jsp?user=<username>&sessionId=<sessionid>&serverId=<serverId>";
        this.newestVersion = "1.3";
        this.authenticationCount = 0;
        this.authLock = new Object();
        this.allowFlying = false;
        try {
            final JSONObject jsonObject = JsonReader.readJsonFromUrl("https://api.johnymuffin.com/clientinfo.json");
            if (jsonObject.containsKey("skinURL")) {
                this.skinURL = String.valueOf(jsonObject.get("skinURL"));
            }
            if (jsonObject.containsKey("cloakURL")) {
                this.cloakURL = String.valueOf(jsonObject.get("cloakURL"));
            }
            if (jsonObject.containsKey("resourcesURL")) {
                this.resourceURL = String.valueOf(jsonObject.get("resourcesURL"));
            }
            if (jsonObject.containsKey("authURL")) {
                this.authURL = String.valueOf(jsonObject.get("authURL"));
            }
            if (jsonObject.containsKey("newestClient")) {
                this.newestVersion = String.valueOf(jsonObject.get("newestClient"));
            }
            System.out.println("Loaded BetaEvolutions Manifest.");
        }
        catch (Exception e) {
            System.out.println("An error occurred fetching the manifest: ");
            e.printStackTrace();
        }
        (this.discordThread = new DiscordThread(this)).start();
        final DiscordRichPresence discordRichPresence = new DiscordRichPresence.Builder("Starting Beta Evolutions").setBigImage("cart", "Version: 1.3").build();
        this.updateRichPresence(discordRichPresence);
    }
    
    public static String encodeURL(final String url) {
        try {
            return URLEncoder.encode(url, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Encode Error.");
        }
    }
    
    public void basicNameTagUpdate(final TagColor color, final String playerName) {
        this.nameTagColorCache.put(playerName, new UsableTagColor(color));
    }
    
    public void complexNameTagUpdate(final int color, final boolean backLight, final String playerName) {
        this.nameTagColorCache.put(playerName, new UsableTagColor(color, backLight));
    }
    
    public UsableTagColor getUsernameTagColor(final String playerName) {
        if (this.nameTagColorCache.containsKey(playerName)) {
            return this.nameTagColorCache.get(playerName);
        }
        return this.defaultUsernameColor;
    }
    
    public void setMouseEars(final boolean enabled, final String playerName) {
        if (this.mouseEars.contains(playerName)) {
            if (!enabled) {
                this.mouseEars.remove(playerName);
            }
        }
        else if (enabled) {
            this.mouseEars.add(playerName);
        }
    }
    
    public boolean hasMouseEars(final String playerName) {
        return this.mouseEars.contains(playerName);
    }
    
    public static BetaEVO getInstance() {
        if (BetaEVO.betaEVO == null) {
            BetaEVO.betaEVO = new BetaEVO();
        }
        return BetaEVO.betaEVO;
    }
    
    public String getSkinURL() {
        return this.skinURL;
    }
    
    public String getCloakURL() {
        return this.cloakURL;
    }
    
    public String getResourceURL() {
        return this.resourceURL;
    }
    
    public String getAuthURL() {
        return this.authURL;
    }
    
    public String getVersion() {
        return "1.3";
    }
    
    public String getNewestVersion() {
        return this.newestVersion;
    }
    
    public int getAuthenticationCount() {
        synchronized (this.authLock) {
            return this.authenticationCount;
        }
    }
    
    public void setAuthenticationEnabled(final int authenticationCount) {
        synchronized (this.authLock) {
            this.authenticationCount = authenticationCount;
        }
    }
    
    public boolean isFlyingAllowed() {
        return this.allowFlying;
    }
    
    public void setAllowFlying(final boolean flying) {
        this.allowFlying = flying;
    }
    
    public void wipeSessionData() {
        final DiscordRichPresence discordRichPresence = new DiscordRichPresence.Builder("In the menu").setDetails("Minecraft Beta 1.7.3").setBigImage("cart", "Version: 1.3").build();
        this.updateRichPresence(discordRichPresence);
        this.allowFlying = false;
        this.nameTagColorCache = new HashMap<String, UsableTagColor>();
        this.mouseEars = new ArrayList<String>(Arrays.asList(new String[0]));
    }
    
    public void updateRichPresence(final DiscordRichPresence discordRichPresence) {
        this.discordThread.updateRichPresence(discordRichPresence);
    }
}
