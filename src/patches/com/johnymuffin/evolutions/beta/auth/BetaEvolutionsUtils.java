// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.auth;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.net.UnknownHostException;
import patches.com.johnymuffin.evolutions.beta.simplejson.parser.ParseException;
import java.io.IOException;
import java.io.InputStream;
import patches.com.johnymuffin.evolutions.beta.simplejson.parser.JSONParser;
import java.nio.charset.Charset;
import java.io.Reader;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import patches.com.johnymuffin.evolutions.beta.simplejson.JSONObject;
import java.util.Iterator;
import java.util.HashMap;

public class BetaEvolutionsUtils
{
    private boolean debug;
    private static HashMap<String, BEVersion> beServers;
    
    public BetaEvolutionsUtils() {
        this.debug = false;
    }
    
    public BetaEvolutionsUtils(final boolean debug) {
        this.debug = debug;
    }
    
    public VerificationResults authenticateUser(final String username, final String sessionID) {
        final String ip = this.getExternalIP();
        if (ip == null) {
            this.log("Can't authenticate with any nodes, can't fetch external IP address. Your internet is probably offline!");
            return new VerificationResults(0, 0, BetaEvolutionsUtils.beServers.size());
        }
        final VerificationResults verificationResults = new VerificationResults();
        for (final String node : BetaEvolutionsUtils.beServers.keySet()) {
            final Boolean result = this.authenticateWithBetaEvolutions(username, node, BetaEvolutionsUtils.beServers.get(node), sessionID, ip);
            if (result == null) {
                verificationResults.setErrored(verificationResults.getErrored() + 1);
            }
            else if (result) {
                verificationResults.setSuccessful(verificationResults.getSuccessful() + 1);
            }
            else {
                if (result) {
                    continue;
                }
                verificationResults.setFailed(verificationResults.getFailed() + 1);
            }
        }
        return verificationResults;
    }
    
    public VerificationResults verifyUser(final String username, final String userIP) {
        final VerificationResults verificationResults = new VerificationResults();
        for (final String node : BetaEvolutionsUtils.beServers.keySet()) {
            final Boolean result = this.verifyUserWithNode(username, userIP, node, BetaEvolutionsUtils.beServers.get(node));
            if (result == null) {
                verificationResults.setErrored(verificationResults.getErrored() + 1);
            }
            else if (result) {
                verificationResults.setSuccessful(verificationResults.getSuccessful() + 1);
            }
            else {
                if (result) {
                    continue;
                }
                verificationResults.setFailed(verificationResults.getFailed() + 1);
            }
        }
        return verificationResults;
    }
    
    private Boolean verifyUserWithNode(final String username, final String userIP, final String node, final BEVersion beVersion) {
        if (beVersion == BEVersion.V1) {
            final String stage1URL = node + "/serverAuth.php?method=1&username=" + this.encodeString(username) + "&userip=" + this.encodeString(userIP);
            final JSONObject stage1Object = this.getJSONFromURL(stage1URL);
            if (stage1Object == null) {
                this.log("Authentication with node: " + node + " has failed to respond when queried.");
                return null;
            }
            if (!this.verifyJSONArguments(stage1Object, "result", "verified")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            return Boolean.valueOf(String.valueOf(stage1Object.get("verified")));
        }
        else {
            if (beVersion != BEVersion.V2_PLAINTEXT) {
                return null;
            }
            final String stage1URL = node + "/server/getVerification?username=" + this.encodeString(username) + "&userip=" + this.encodeString(userIP);
            final JSONObject stage1Object = this.getJSONFromURL(stage1URL);
            if (stage1Object == null) {
                this.log("Authentication with node: " + node + " has failed to respond when queried.");
                return null;
            }
            if (!this.verifyJSONArguments(stage1Object, "verified", "error")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            return Boolean.valueOf(String.valueOf(stage1Object.get("verified")));
        }
    }
    
    private Boolean authenticateWithMojang(final String username, final String sessionID, final String serverID) {
        try {
            final String authURL = "http://session.minecraft.net/game/joinserver.jsp?user=" + this.encodeString(username) + "&sessionId=" + this.encodeString(sessionID) + "&serverId=" + this.encodeString(serverID);
            final URL url = new URL(authURL);
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openStream()));
            final String response = bufferedReader.readLine();
            bufferedReader.close();
            if (response.equalsIgnoreCase("ok")) {
                return true;
            }
            return false;
        }
        catch (Exception e) {
            if (this.debug) {
                this.log("An error occurred contacting Mojang.");
                e.printStackTrace();
            }
            return null;
        }
    }
    
    private Boolean authenticateWithBetaEvolutions(final String username, final String node, final BEVersion beVersion, final String sessionToken, final String ip) {
        if (beVersion == BEVersion.V1) {
            final String stage1URL = node + "/userAuth.php?method=1&username=" + this.encodeString(username);
            final JSONObject stage1Object = this.getJSONFromURL(stage1URL);
            if (stage1Object == null) {
                this.log("Authentication with node: " + node + " has failed as JSON can't be fetched.");
                return null;
            }
            if (!this.verifyJSONArguments(stage1Object, "result", "username", "userip", "serverId")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            final String serverID = String.valueOf(stage1Object.get("serverId"));
            final Boolean mojangAuthentication = this.authenticateWithMojang(username, sessionToken, serverID);
            if (mojangAuthentication == null) {
                this.log("Authentication with node: " + node + " has failed due to auth failure with Mojang.");
                return null;
            }
            if (!mojangAuthentication) {
                this.log("Authentication with node: " + node + " has failed. Token is probably incorrect, or user is cracked!");
                return false;
            }
            final String stage3URL = node + "/userAuth.php?method=2&username=" + this.encodeString(username) + "&serverId=" + this.encodeString(serverID);
            final JSONObject stage3Object = this.getJSONFromURL(stage3URL);
            if (stage3Object == null) {
                this.log("Authentication with node: " + node + " has failed as JSON can't be fetched.");
                return null;
            }
            if (!this.verifyJSONArguments(stage3Object, "result")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            final Boolean result = Boolean.valueOf(String.valueOf(stage3Object.get("result")));
            this.log("Node: " + node + " has returned the result: " + result);
            return result;
        }
        else {
            if (beVersion != BEVersion.V2_PLAINTEXT) {
                return null;
            }
            final String stage1URL = node + "/user/getServerID?username=" + this.encodeString(username) + "&userip=" + ip;
            final JSONObject stage1Object = this.getJSONFromURL(stage1URL);
            if (stage1Object == null) {
                this.log("Authentication with node: " + node + " has failed as JSON can't be fetched.");
                return null;
            }
            if (!this.verifyJSONArguments(stage1Object, "userIP", "error", "serverID", "username")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            final String serverID = String.valueOf(stage1Object.get("serverID"));
            final Boolean mojangAuthentication = this.authenticateWithMojang(username, sessionToken, serverID);
            if (mojangAuthentication == null) {
                this.log("Authentication with node: " + node + " has failed due to auth failure with Mojang.");
                return null;
            }
            if (!mojangAuthentication) {
                this.log("Authentication with node: " + node + " has failed. Token is probably incorrect, or user is cracked!");
                return false;
            }
            final String stage3URL = node + "/user/successfulAuth?username=" + this.encodeString(username) + "&serverid=" + this.encodeString(serverID) + "&userip=" + this.encodeString(ip);
            final JSONObject stage3Object = this.getJSONFromURL(stage3URL);
            if (stage3Object == null) {
                this.log("Authentication with node: " + node + " has failed as JSON can't be fetched.");
                return null;
            }
            if (!this.verifyJSONArguments(stage3Object, "result")) {
                this.log("Malformed response from: " + node + " using version " + beVersion);
                return null;
            }
            final Boolean result = Boolean.valueOf(String.valueOf(stage3Object.get("result")));
            this.log("Node: " + node + " has returned the result: " + result);
            return result;
        }
    }
    
    private String getExternalIP() {
        String ip = this.getIPFromAmazon();
        if (ip == null) {
            ip = this.getIPFromWhatIsMyIpAddress();
        }
        return ip;
    }
    
    private String getIPFromAmazon() {
        try {
            final URL myIP = new URL("http://checkip.amazonaws.com");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myIP.openStream()));
            return bufferedReader.readLine();
        }
        catch (Exception e) {
            this.log("Failed to get IP from Amazon, your internet is probably down.");
            if (this.debug) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    private String getIPFromWhatIsMyIpAddress() {
        try {
            final URL myIP = new URL("https://ipv4bot.whatismyipaddress.com/");
            final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(myIP.openStream()));
            return bufferedReader.readLine();
        }
        catch (Exception e) {
            this.log("Failed to get IP from WhatIsMyIpAddress, your internet is probably down.");
            if (this.debug) {
                e.printStackTrace();
            }
            return null;
        }
    }
    
    private static JSONObject readJsonFromUrl(final String url) throws IOException, ParseException, UnknownHostException {
        final InputStream is = new URL(url).openStream();
        try {
            final BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            final String jsonText = readAll(rd);
            final JSONParser jsonParser = new JSONParser();
            final JSONObject json = (JSONObject)jsonParser.parse(jsonText);
            return json;
        }
        finally {
            is.close();
        }
    }
    
    private static String readAll(final Reader rd) throws IOException {
        final StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char)cp);
        }
        return sb.toString();
    }
    
    private JSONObject getJSONFromURL(final String url) {
        try {
            final JSONObject jsonObject = readJsonFromUrl(url);
            return jsonObject;
        }
        catch (UnknownHostException e2) {
            this.log(url + " is offline, or your internet is offline.");
        }
        catch (Exception e) {
            if (this.debug) {
                this.log("An error occurred fetching JSON from: " + url);
                e.printStackTrace();
            }
        }
        return null;
    }
    
    private void log(final String info) {
        if (this.debug) {
            System.out.println("[Beta Evolutions] " + info);
        }
    }
    
    private String encodeString(final String string) {
        try {
            return URLEncoder.encode(string, StandardCharsets.UTF_8.toString());
        }
        catch (Exception e) {
            this.log("An error occurred encoding a string, this really shouldn't happen on modern JVMs.");
            e.printStackTrace();
            return null;
        }
    }
    
    private boolean verifyJSONArguments(final JSONObject jsonObject, final String... arguments) {
        for (final String s : arguments) {
            if (!jsonObject.containsKey(s)) {
                return false;
            }
        }
        return true;
    }
    
    static {
        (BetaEvolutionsUtils.beServers = new HashMap<String, BEVersion>()).put("https://auth.johnymuffin.com", BEVersion.V1);
        BetaEvolutionsUtils.beServers.put("https://auth1.evolutions.johnymuffin.com", BEVersion.V2_PLAINTEXT);
        BetaEvolutionsUtils.beServers.put("https://auth2.evolutions.johnymuffin.com", BEVersion.V2_PLAINTEXT);
        BetaEvolutionsUtils.beServers.put("https://auth3.evolutions.johnymuffin.com", BEVersion.V2_PLAINTEXT);
    }
    
    public enum BEVersion
    {
        V1, 
        V2_PLAINTEXT;
    }
    
    public class VerificationResults
    {
        private int successful;
        private int failed;
        private int errored;
        
        public VerificationResults() {
            this.successful = 0;
            this.failed = 0;
            this.errored = 0;
        }
        
        public VerificationResults(final int successful, final int failed, final int errored) {
            this.successful = 0;
            this.failed = 0;
            this.errored = 0;
            this.successful = successful;
            this.failed = failed;
            this.errored = errored;
        }
        
        public int getSuccessful() {
            return this.successful;
        }
        
        public void setSuccessful(final int successful) {
            this.successful = successful;
        }
        
        public int getFailed() {
            return this.failed;
        }
        
        public void setFailed(final int failed) {
            this.failed = failed;
        }
        
        public int getErrored() {
            return this.errored;
        }
        
        public void setErrored(final int errored) {
            this.errored = errored;
        }
        
        public int getTotal() {
            return this.errored + this.successful + this.failed;
        }
    }
}
