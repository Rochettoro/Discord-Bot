package slack;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class GithubFileDownloader implements Commands{
    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        //example link https://github.com/Drawethree/X-Prison/archive/refs/heads/master.zip

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
