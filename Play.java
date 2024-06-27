package slack;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;

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
        GuildVoiceState voiceState = member.getVoiceState();

        if(!voiceState.inAudioChannel()){
            event.reply("You are not in a voice channel").queue();
            return;
        }
        Member selfMember = event.getGuild().getSelfMember();
        GuildVoiceState selfVoiceState = selfMember.getVoiceState();

        if(!selfVoiceState.inAudioChannel()){
            event.getGuild().getAudioManager().openAudioConnection(voiceState.getChannel());
        }else{
            if(selfVoiceState.getChannel() != voiceState.getChannel()){
                event.reply("You are not in a voice channel with me").queue();
                return;
            }
        }

        PlayerManager playerManager = PlayerManager.getInstance();
        playerManager.play(event.getGuild(), event.getOption("name").getAsString());
        event.reply("Playing song").queue();

    }
}
