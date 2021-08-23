// 
// Decompiled by Procyon v0.5.36
// 

package patches.com.johnymuffin.evolutions.beta;

import patches.net.arikia.dev.drpc.DiscordUser;
import patches.net.arikia.dev.drpc.DiscordRichPresence;
import patches.net.arikia.dev.drpc.DiscordRPC;
import patches.net.arikia.dev.drpc.DiscordEventHandlers;

public class DiscordThread extends Thread
{
    private BetaEVO betaEVO;
    private Object presenceLock;
    
    public DiscordThread(final BetaEVO betaEVO) {
        this.presenceLock = new Object();
        this.betaEVO = betaEVO;
    }
    
    @Override
    public void run() {
        System.out.println("Starting Discord RPC thread.");
        final DiscordEventHandlers handlers = new DiscordEventHandlers.Builder().setReadyEventHandler(user -> System.out.println("Welcome " + user.username + "#" + user.discriminator + "!")).build();
        DiscordRPC.discordInitialize("780669183779536926", handlers, false);
        while (!Thread.currentThread().isInterrupted()) {
            DiscordRPC.discordRunCallbacks();
            try {
                Thread.sleep(2000L);
            }
            catch (InterruptedException ex) {}
        }
    }
    
    public void updateRichPresence(final DiscordRichPresence discordRichPresence) {
        synchronized (this.presenceLock) {
            DiscordRPC.discordUpdatePresence(discordRichPresence);
        }
    }
}
