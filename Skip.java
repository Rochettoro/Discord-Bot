package slack;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Skip implements Commands{
    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skip the current song";
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
            event.reply("I am not in a voice channel").queue();
        }
        if(selfVoiceState.getChannel() != voiceState.getChannel()) {
            event.reply("You are not in a voice channel with me").queue();
            return;
        }

        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        List<AudioTrack> queue = new ArrayList<>(musicManager.getTrackScheduler().getQueue());

        if(queue.isEmpty()){
            event.reply("There are no songs after the current one\nIf you want to stop use **/stop** instead").queue();
            return;
        }

        musicManager.getTrackScheduler().getPlayer().stopTrack();
        event.reply("Skipping song").queue();
    }
}
