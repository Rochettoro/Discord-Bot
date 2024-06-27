package slack;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;

public class Now implements Commands{
    @Override
    public String getName() {
        return "now";
    }

    @Override
    public String getDescription() {
        return "returns the current song";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        assert member != null;
        GuildVoiceState voiceState = member.getVoiceState();

        assert voiceState != null;
        if(!voiceState.inAudioChannel()){
            event.reply("You are not in a voice channel").queue();
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        assert selfVoiceState != null;
        if(!selfVoiceState.inAudioChannel()){
            event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
        }else{
            if(selfVoiceState.getChannel() != voiceState.getChannel()){
                event.reply("You are not in a voice channel with me").queue();
                return;
            }
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        AudioTrack track = musicManager.getTrackScheduler().getPlayer().getPlayingTrack();
        if(track==null){
            event.reply("There is no track playing").queue();
            return;
        }
        AudioTrackInfo trackInfo = track.getInfo();

        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Current song");
        eb.setDescription("Title: " + trackInfo.title);
        eb.appendDescription("Url: " + trackInfo.uri);
        eb.appendDescription("Time: "+ track.getDuration());
        //eb.appendDescription("Queue " + track.getPosition());
        eb.setThumbnail(trackInfo.author);

        event.replyEmbeds(eb.build()).queue();

    }
}
