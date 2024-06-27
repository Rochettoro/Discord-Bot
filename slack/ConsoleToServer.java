package slack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;

import java.awt.*;
import java.util.Objects;
import java.util.Scanner;

public class ConsoleToServer extends Thread {
    ConsoleToServer(){
        start();
    }

    @Override
    public void run() {
        try {
            while (true) {
                Scanner scnr = new Scanner(System.in);
                JDA jda = DsBot.getJDA();
                EmbedBuilder eb = new EmbedBuilder();
                System.out.println("Inserisci il titolo: (String)");
                String titolo = scnr.nextLine();
                if(titolo.isEmpty()) {
                    titolo = "-";
                    System.out.println("titolo vuoto!");
                }
                //.setTitle("Annuncio","https://avatars.githubusercontent.com/u/130759213?v=4")
                //.setImage("https://avatars.githubusercontent.com/u/130759213?v=4")
                eb.setTitle(titolo);
                System.out.println("Inserisci il numero di field da inserire: (Int)");
                int numeroField = scnr.nextInt();
                for(int i = 0; i < numeroField; i++) {
                    scnr.nextLine();
                    System.out.println("Inserisci il titolo del Field"+(i+1)+": (String)");
                    String titoloField = scnr.nextLine();
                    if (titoloField.isEmpty()) {
                        titoloField = "-";
                        System.out.println("titoloField vuoto!");
                    }
                    System.out.println("Inserisci il testo del Field"+(i+1)+": (String)");
                    String testo = scnr.nextLine();
                    if (testo.isEmpty()) {
                        testo = "-";
                        System.out.println("testo vuoto!");
                    }
                    System.out.println("Inline?: (true/false)");
                    boolean inline = scnr.nextBoolean();
                    eb.addField(titoloField, testo, inline);
                }
                eb.setColor(Color.ORANGE);
                for (Guild guild : jda.getGuilds()) {
                    Objects.requireNonNull(guild.getDefaultChannel()).asTextChannel().sendMessageEmbeds(eb.build()).queue();
                    //Objects.requireNonNull(guild.getDefaultChannel()).asTextChannel().sendMessage(testo).queue();
                }
            }
        }catch (Exception e){
            System.out.println("Errore durante l'inserimento di un dato");
        }
    }
}
