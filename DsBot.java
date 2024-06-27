package slack;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class DsBot {
    static String token;
    static JDA jda;
    DsBot(String token){
        this.token = token;
        jda = JDABuilder.createDefault(token).build();
        CommandManager cmdmanager = new CommandManager();
        cmdmanager.add(new Play());
        cmdmanager.add(new Stop());
        cmdmanager.add(new Skip());
        cmdmanager.add(new Queue());
        cmdmanager.add(new Now());
        jda.addEventListener(cmdmanager);
        ConsoleToServer cts = new ConsoleToServer();
    }

    public static JDA getJDA(){
        return jda;
    }
}
