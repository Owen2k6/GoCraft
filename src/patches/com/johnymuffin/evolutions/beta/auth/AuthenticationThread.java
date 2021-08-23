// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta.auth;

import patches.com.johnymuffin.evolutions.beta.BetaEVO;

public class AuthenticationThread extends Thread
{
    private String username;
    private String sessionID;
    private String mojangAuthURL;
    
    public AuthenticationThread(final String username, final String sessionID, final String mojangAuthURL) {
        this.username = username;
        this.sessionID = sessionID;
        this.mojangAuthURL = mojangAuthURL;
    }
    
    @Override
    public void run() {
        while (true) {
            try {
                final BetaEvolutionsUtils betaEvolutionsUtils = new BetaEvolutionsUtils(true);
                final BetaEvolutionsUtils.VerificationResults results = betaEvolutionsUtils.authenticateUser(this.username, this.sessionID);
                BetaEVO.getInstance().setAuthenticationEnabled(results.getSuccessful());
            }
            catch (Exception e) {
                System.out.println("An error occurred authing with Beta Evolutions: ");
                e.printStackTrace();
            }
            try {
                Thread.sleep(86400000L);
            }
            catch (InterruptedException ex) {}
        }
    }
}
