package slack;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;
import java.util.Objects;

public class Stop implements Commands{
    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stop the bot";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
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
        TrackScheduler scheduler = musicManager.getTrackScheduler();
        scheduler.getQueue().clear();
        scheduler.getPlayer().stopTrack();
        event.getGuild().getAudioManager().closeAudioConnection();
        event.reply("Stopping the bot").queue();
    }
}
