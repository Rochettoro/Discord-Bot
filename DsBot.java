package slack;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;

public class DsBot {
    static String token;
    static JDA jda;
    DsBot(String token){
        this.token = token;
        jda = JDABuilder.createDefault(token).build();
        CommandManager cmdManager = new CommandManager();
        cmdManager.add(new GithubFileDownloader());
        cmdManager.add(new ForwardMessage());
        cmdManager.add(new Play());
        cmdManager.add(new Stop());
        cmdManager.add(new Skip());
        cmdManager.add(new Queue());
        cmdManager.add(new Now());

        McScanner scan = new McScanner();
        scan.getOptions();

        cmdManager.add(scan);
        jda.addEventListener(cmdManager);
        ConsoleToServer cts = new ConsoleToServer();
    }

    public static JDA getJDA(){
        return jda;
    }
}
