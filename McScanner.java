package slack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.MessageCreateAction;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import org.json.JSONObject;

import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

public class McScanner implements Commands{
    @Override
    public String getName() {
        return "scan";
    }

    @Override
    public String getDescription() {
        return "Scan a player or a server";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "type","What you want to scan", true)
                //.addChoice("User","user")
                .addChoice("Server","server"));
        options.add(new OptionData(OptionType.STRING, "name","IGN for type=user and IP for type=server", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {

        //if(event.getOption("type").getAsString().equals("user")) {
        //    event.reply("hai scelto user").queue();
        //    return;
        //}
        if (event.getOption("type").getAsString().equals("server")) {
            //event.reply("hai scelto server").queue();
            String address = event.getOption("name").getAsString();
            String urlString = "https://api.mcsrvstat.us/3/" + address;

            try {
                URL url = new URL(urlString);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");

                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("HTTP GET Request Failed with Error code : " + conn.getResponseCode());
                }

                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
                StringBuilder response = new StringBuilder();
                String output;
                while ((output = br.readLine()) != null) {
                    response.append(output);
                }
                conn.disconnect();

                JSONObject jsonResponse = new JSONObject(response.toString());
                //System.out.println(jsonResponse.toString(2));

                boolean online = jsonResponse.getBoolean("online");
                String ip = jsonResponse.getString("ip");
                int port = jsonResponse.getInt("port");
                int onlinePlayer = jsonResponse.getJSONObject("players").getInt("online");
                int maxPlayer = jsonResponse.getJSONObject("players").getInt("max");
                String version = jsonResponse.getString("version");
                //System.out.println("Server online: " + online);

                String imageData = jsonResponse.getString("icon");

                byte[] imageBytes = Base64.getDecoder().decode(imageData.split(",")[1]);

                String filePath = "src/main/java/slack/tmpImg/output.png";
                try (FileOutputStream fos = new FileOutputStream(filePath)) {
                    fos.write(imageBytes);
                    fos.close();
                }

                File image = new File(filePath);

                event.reply("online: "+online+"\n"+"IP: "+ip+":"+port+"\n"+"Players: "+onlinePlayer+"/"+maxPlayer+"\n"+"Version: "+version)
                        .addFiles(AttachedFile.fromData(image,"logo.png")).queue();

                image.delete();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
    }


}
