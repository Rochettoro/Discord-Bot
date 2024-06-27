package slack;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager instance;
    private Map<Long, GuildMusicManager> guildMusicManagers = new HashMap<>();
    private AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();

    public PlayerManager(){
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public static PlayerManager getInstance(){
        if(instance == null){
            instance = new PlayerManager();
        }
        return instance;
    }

    public GuildMusicManager getGuildMusicManager(Guild guild){
        return guildMusicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            GuildMusicManager guildMusicManager = new GuildMusicManager(audioPlayerManager);

            guild.getAudioManager().setSendingHandler(guildMusicManager.getAudioForwarder());

            return guildMusicManager;
        });
    }

    public void play(Guild guild, String trackUrl){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        audioPlayerManager.loadItemOrdered(guildMusicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack audioTrack) {
                guildMusicManager.getTrackScheduler().queue(audioTrack);
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {

            }

            @Override
            public void noMatches() {

            }

            @Override
            public void loadFailed(FriendlyException e) {

            }
        });
    }

}
