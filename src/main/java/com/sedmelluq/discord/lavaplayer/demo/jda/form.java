package com.sedmelluq.discord.lavaplayer.demo.jda;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;


public class form extends JFrame {
    public JPanel panel;
    private JTextField textField1;
    private JButton gönderButton;
    private JSpinner spinnerMax;
    private JSpinner spinnerMin;
    private JLabel textLabel;
    public JRadioButton autoModRadioButton;
    private JRadioButton radioButton1;


    public form(){
        JLabel jLabel = new DefaultListCellRenderer();
        add(panel);

        setSize(450,220);
        setTitle("Fenomen Duygu V1.0");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        getRootPane().setDefaultButton(gönderButton);

        spinnerMax.setValue(Main.getMaxTimer());
        spinnerMin.setValue(Main.getMinTimer());
















        gönderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


             GuildMessageReceivedEvent event= Main.getLastEvent();
             if (event!=null){
                 String message=textField1.getText();
                 String []message2=message.split(" ");
                 message ="";
                 if (message2[0].startsWith("@")){
                     for (int i = 1; i <message2.length ; i++) {
                        message = message+" "+message2[i];
                     }


                     event.getChannel().sendMessage("<"+message2[0]+">"+"   "+message).queue();
                     textField1.setText("");
                 return;
                 }



             event.getChannel().sendMessage(message).queue();
             textField1.setText("");
             }
             else
                 JOptionPane.showMessageDialog(null,"Bot Event Yakalayamadı");

            }
        });
        spinnerMax.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Main.setMaxTimer((Integer) spinnerMax.getValue());
            }
        });
        spinnerMin.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                Main.setMinTimer((Integer)spinnerMin.getValue());

            }
        });

        autoModRadioButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Main.autoMod=autoModRadioButton.isSelected();
            }
        });
    }


    public  void setRandomText(int randomText) {
        timer(randomText);

    }
    private void timer(int sure){


        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
              if (sure>0){
                  textLabel.setText(String.valueOf(sure));
                  timer(sure-1);
              }else if (sure==0 && !Main.autoMod){
                  Main.leaveChannel(Main.getLastEvent().getGuild().getAudioManager());
                  textLabel.setText("Bitti");
                  return;

              }
              else return;
            }
        }, 1000);

    }



}
