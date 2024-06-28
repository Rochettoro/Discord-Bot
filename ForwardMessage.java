package slack;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Role;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ForwardMessage implements Commands {
    @Override
    public String getName() {
        return "forward";
    }

    @Override
    public String getDescription() {
        return "forwards a message to all servers where the bot is present and where the sender has admin";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "message","The message you want to forward", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        Member member = event.getMember();
        System.out.println("a1");
        if (member.isOwner()){
            System.out.println("b1");
            for(Guild g : DsBot.getJDA().getGuilds()){
                System.out.println("c1");
                for(Role roleInOtherDS : g.getRoles()) {
                    System.out.println("d1");
                    if(roleInOtherDS.hasPermission(Permission.ADMINISTRATOR)){
                        System.out.println("e1");
                        if(g.getMemberById(event.getMember().getId()).getRoles().contains(roleInOtherDS) || g.getMemberById(event.getMember().getId()).isOwner()) {
                            System.out.println("f1");
                            g.getDefaultChannel().asTextChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).queue();
                            break;
                        }
                    }
                }
                if(g.getRoles().isEmpty() && g.getMemberById(event.getMember().getId()).isOwner()){
                    System.out.println("f1.2");
                    g.getDefaultChannel().asTextChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).queue();
                }
            }
            event.reply("Message forwarded successfully!").setEphemeral(true).queue();
            return;
        }else {
            for (Role roleInMainDS : member.getRoles()) {
                System.out.println("a2");
                if (roleInMainDS.hasPermission(Permission.ADMINISTRATOR)) {
                    System.out.println("b2");
                    for (Guild g : DsBot.getJDA().getGuilds()) {
                        System.out.println("c2");
                        for (Role roleInOtherDS : g.getRoles()) {
                            System.out.println("d2");
                            if (roleInOtherDS.hasPermission(Permission.ADMINISTRATOR)) {
                                System.out.println("e2");
                                if (g.getMemberById(event.getMember().getId()).getRoles().contains(roleInOtherDS) || g.getMemberById(event.getMember().getId()).isOwner()) {
                                    System.out.println("f2");
                                    g.getDefaultChannel().asTextChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).queue();
                                    break;
                                }
                            }
                        }
                        if(g.getRoles().isEmpty() && g.getMemberById(event.getMember().getId()).isOwner()){
                            System.out.println("f2.2");
                            g.getDefaultChannel().asTextChannel().sendMessage(Objects.requireNonNull(event.getOption("message")).getAsString()).queue();
                        }
                    }
                    event.reply("Message forwarded successfully!").setEphemeral(true).queue();
                    return;
                }
            }
        }
        event.reply("You cant perform this action!").setEphemeral(true).queue();
    }
}
