package com.sedmelluq.discord.lavaplayer.demo.jda;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_MESSAGES;
import static net.dv8tion.jda.api.requests.GatewayIntent.GUILD_VOICE_STATES;

public class Main extends ListenerAdapter {
  private static GuildMessageReceivedEvent lastEvent;
  private static int maxTimer =20;
  private static int minTimer =10;
  private static form form;


 public static boolean autoMod=false;
  public  int randomNumber = 20;

  public DSes[] ses3=Token.getDsesArray();



  /*
  * 1. biden
  * 2. bilmiyom
  * 3.HeeY
  * 4.dıt dı dıt
  * 5. teknoloji
  * 6.bilmillah
  * 7.şerefsiz
  * 8.
  * 9.yetmedimi
  * 10.sucuk
  *11.memet ali bey
  *
  * */

  public static void main(String[] args) throws Exception {
    JDABuilder.create(Token.getTOKEN(), GUILD_MESSAGES, GUILD_VOICE_STATES)
            .addEventListeners(new Main())
            .setStatus(OnlineStatus.ONLINE)
            .setActivity(Activity.watching("İbo Show"))
            .build();


    jFrame();






  }

  public static int getMaxTimer() {
    return maxTimer;
  }

  public static void setMaxTimer(int maxTimer) {
    Main.maxTimer = maxTimer;
  }

  public static int getMinTimer() {
    return minTimer;
  }

  public static void setMinTimer(int minTimer) {
    Main.minTimer = minTimer;
  }

  private static void jFrame() {
    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    } catch (InstantiationException e) {
      e.printStackTrace();
    } catch (IllegalAccessException e) {
      e.printStackTrace();
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }


    SwingUtilities.invokeLater(new Runnable() {
      @Override
      public void run() {

         form = new form();
        form.setVisible(true);



      }
    });




  }

  private final AudioPlayerManager playerManager;
  private final Map<Long, GuildMusicManager> musicManagers;

  private Main() {
    this.musicManagers = new HashMap<>();

    this.playerManager = new DefaultAudioPlayerManager();
    AudioSourceManagers.registerRemoteSources(playerManager);
    AudioSourceManagers.registerLocalSource(playerManager);
  }

  public static GuildMessageReceivedEvent getLastEvent() {
    return lastEvent;
  }

  private synchronized GuildMusicManager getGuildAudioPlayer(Guild guild) {
    long guildId = Long.parseLong(guild.getId());
    GuildMusicManager musicManager = musicManagers.get(guildId);

    if (musicManager == null) {
      musicManager = new GuildMusicManager(playerManager);
      musicManagers.put(guildId, musicManager);
    }

    guild.getAudioManager().setSendingHandler(musicManager.getSendHandler());

    return musicManager;
  }

  @Override
  public void onGuildMessageReceived(GuildMessageReceivedEvent event) {
    lastEvent=event;

    event.getMember().getVoiceState().getChannel();


    String[] command = event.getMessage().getContentRaw().split(" ", 2);

    if ("basla".equalsIgnoreCase(command[0])){

      autoMod=true;
      form.autoModRadioButton.setSelected(autoMod);

      startTimer( event);
    }else if("d".equalsIgnoreCase(command[0]) && command.length == 2){

        if (command[1].equalsIgnoreCase("8")){
            if (!event.getMember().getId().equalsIgnoreCase("429405344683524098") ||
                    !event.getMember().getId().equalsIgnoreCase("751901481728213112") ){
                event.getChannel().sendMessage("<@"+event.getMember().getId()+">"+" Bu Kod Seni Aşar").queue();
                return;

            }}


      loadAndPlay(event.getChannel(),ses3[Integer.parseInt(command[1])-1].link);
      TimerTask ts2 = new TimerTask() {
        @Override
        public void run() {
          leaveChannel(event.getGuild().getAudioManager());
        }
      };
      int sure = ses3[Integer.parseInt(command[1])-1].sure;
      long Ss=sure*1000;
      System.out.println("LONG SÜRE: "+ Ss);
      new Timer().schedule(ts2,Ss);


    }else if("durdur".equalsIgnoreCase(command[0])){
      autoMod=false;
      form.autoModRadioButton.setSelected(autoMod);

    }




   else if ("-play".equals(command[0]) && command.length == 2) {
      loadAndPlay(event.getChannel(), command[1]);
    } else if ("-skip".equals(command[0])) {
      skipTrack(event.getChannel());
    }

    super.onGuildMessageReceived(event);
  }

  public void loadAndPlay(final TextChannel channel, final String trackUrl) {
    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
    musicManager.player.stopTrack();

    playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
      @Override
      public void trackLoaded(AudioTrack track) {
      //  channel.sendMessage("Adding to queue " + track.getInfo().title).queue();

        play(channel.getGuild(), musicManager, track);
      }

      @Override
      public void playlistLoaded(AudioPlaylist playlist) {
        AudioTrack firstTrack = playlist.getSelectedTrack();

        if (firstTrack == null) {
          firstTrack = playlist.getTracks().get(0);
        }

        channel.sendMessage("Adding to queue " + firstTrack.getInfo().title + " (first track of playlist " + playlist.getName() + ")").queue();

        play(channel.getGuild(), musicManager, firstTrack);
      }

      @Override
      public void noMatches() {
        channel.sendMessage("Nothing found by " + trackUrl).queue();
      }

      @Override
      public void loadFailed(FriendlyException exception) {
        channel.sendMessage("Could not play: " + exception.getMessage()).queue();
      }
    });
  }

  private void play(Guild guild, GuildMusicManager musicManager, AudioTrack track) {
    connectToFirstVoiceChannel(guild.getAudioManager());

    musicManager.scheduler.queue(track);
  }

  private void skipTrack(TextChannel channel) {
    GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
    musicManager.scheduler.nextTrack();

    channel.sendMessage("Skipped to next track.").queue();
  }
  public static void leaveChannel(AudioManager audioManager){
    if (audioManager.isConnected())
      audioManager.closeAudioConnection();



  }
  private  void startTimer(GuildMessageReceivedEvent event){
    Timer timer = new Timer();
    TimerTask ts = new TimerTask() {
      @Override
      public void run() {
        if (!autoMod){
          leaveChannel(event.getGuild().getAudioManager());
          timer.cancel();

        }

        System.out.println("Timer Task");
        int r = new Random().nextInt(ses3.length);

        loadAndPlay(event.getChannel(),ses3[r].link);
        startTimer(event);

        TimerTask ts2 = new TimerTask() {
          @Override
          public void run() {

            leaveChannel(event.getGuild().getAudioManager());
            if (!autoMod){
              leaveChannel(event.getGuild().getAudioManager());
              timer.cancel();
              return;

            }
          }
        };
        int sure =ses3[r].sure;
        long Ss=sure*1000;
       Timer timer2 = new Timer();
      if (!autoMod){
        leaveChannel(event.getGuild().getAudioManager());
        System.out.println("Kapalı auto");
        return;

      }

       timer2.schedule(ts2,Ss);




      }
    };
    randomNumber = new Random().nextInt(maxTimer)+minTimer;
    System.out.println("Random = "+randomNumber);
    long rr = randomNumber*1000;
    form.setRandomText(randomNumber);
     if (!autoMod){
       leaveChannel(event.getGuild().getAudioManager());
       System.out.println("kapalı 2");
       return;

     }
    timer.schedule(ts,rr);


  }

  private static void connectToFirstVoiceChannel(AudioManager audioManager) {
    if (!audioManager.isConnected() && !audioManager.isAttemptingToConnect()) {
    /*  for (VoiceChannel voiceChannel : audioManager.getGuild().getVoiceChannels()) {

        audioManager.openAudioConnection(voiceChannel);
        break;
      }*/         //Connect first voice channel
    if (lastEvent.getMember().getVoiceState().inVoiceChannel()){
        if ((Math.random()*10) > 8){
        lastEvent.getChannel().sendMessage("<@"+lastEvent.getMember().getId()+">"+" Geliyom 2 dk").queue();}
        audioManager.openAudioConnection(lastEvent.getMember().getVoiceState().getChannel());

    }else
        lastEvent.getChannel().sendMessage("<@"+lastEvent.getMember().getId()+">"+" sesli gel sesli bişey dicem").queue();





    }
  }
}
