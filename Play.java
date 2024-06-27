package slack;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Play implements Commands{
    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play a song";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "name","The name of the song", true));
        return options;
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

        String link = Objects.requireNonNull(event.getOption("name")).getAsString();

        try {
            new URI(link);
        } catch (URISyntaxException e) {
            link = "ytsearch:" + link;
        }

        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.play(event.getGuild(), link);
        event.reply("Playing song").queue();

    }
}
