package slack;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.utils.AttachedFile;
import net.dv8tion.jda.api.utils.FileUpload;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class GithubFileDownloader implements Commands{
    @Override
    public String getName() {
        return "gitDownloader";
    }

    @Override
    public String getDescription() {
        return "Downloads a file from github";
    }

    @Override
    public List<OptionData> getOptions() {
        List<OptionData> options = new ArrayList<>();
        options.add(new OptionData(OptionType.STRING, "link","The github reporisory link", true));
        return options;
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        String link = event.getOption("link").getAsString();
        byte[] fileBytes = null;

        if(event.getOption("link").getAsString().endsWith(".zip")) {
            try {
                fileBytes = downloadFile(link);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        link += "/archive/refs/heads/master.zip";
        try {
            fileBytes = downloadFile(link);
        } catch (IOException e) {
            link = event.getOption("name").getAsString() + "/archive/refs/heads/main.zip";
            try {
                fileBytes = downloadFile(link);
            } catch (IOException ex) {
                event.reply("Cant download the file from the link");
            }
        }

        event.reply("Here you go").addFiles((FileUpload.fromData(fileBytes,"source.zip"))).queue();

    }

    private byte[] downloadFile(String fileUrl) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet request = new HttpGet(fileUrl);
        HttpResponse response = httpClient.execute(request);
        InputStream inputStream = response.getEntity().getContent();

        // Read file content into byte array
        byte[] fileBytes = inputStream.readAllBytes();

        // Optional: Save file locally
        try (FileOutputStream fos = new FileOutputStream("downloaded.zip")) {
            fos.write(fileBytes);
        }

        return fileBytes;
    }
}
