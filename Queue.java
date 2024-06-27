package slack;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Queue implements Commands{
    @Override
    public String getName() {
        return "queue";
    }

    @Override
    public String getDescription() {
        return "Add a song to the queue";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();

        //event.reply("1").queue();

        assert member != null;
        GuildVoiceState voiceState = member.getVoiceState();

        assert voiceState != null;
        //event.reply("2").queue();
        if(!voiceState.inAudioChannel()){
            event.reply("You are not in a voice channel").queue();
            return;
        }
        Member selfMember = Objects.requireNonNull(event.getGuild()).getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        assert selfVoiceState != null;

        if(!selfVoiceState.inAudioChannel()){
            event.reply("I'm not in a voice channel").queue();
            return;
        }

        if(selfVoiceState.getChannel() != voiceState.getChannel()) {
            event.reply("You are not in a voice channel with me").queue();
            return;
        }


        GuildMusicManager musicManager = PlayerManager.getInstance().getGuildMusicManager(event.getGuild());
        List<AudioTrack> queue = new ArrayList<>(musicManager.getTrackScheduler().getQueue());
        EmbedBuilder eb = new EmbedBuilder();
        eb.setTitle("Queue");

        if(queue.isEmpty()){
            eb.setDescription("There are no songs in the queue");
            event.replyEmbeds(eb.build()).queue();
            return;
        }

        event.reply("3").queue();
        int i=0;
        for(AudioTrack track : queue){
            AudioTrackInfo trackInfo = queue.get(i).getInfo();
            i++;
            eb.addField(i+") " + track.getInfo().author, trackInfo.title, false);
        }

        event.replyEmbeds(eb.build()).queue();


    }
}
