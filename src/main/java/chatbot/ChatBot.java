package chatbot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.io.*;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.logging.*;

/**
 * Created by BigMax on 02.04.16.
 */
public class ChatBot {

    private String answersPath = "answers.txt";
    private ArrayList<String> answers = new ArrayList();
    private boolean botOn = true;
    private boolean talkOn = true;
    public Logger logger = Logger.getLogger("MyLog");
    private String startTime;
    private boolean guiON = true;
    GUI gui;
    String g;


    ChatBot(String startTime, String g) {
        this.startTime = startTime;
        this.g = g;
        readAnswersPath(answersPath);
        createLogFile();
        String startPhrase = answers.get(0);
        System.out.println("Bot: "+startPhrase);
        if (guiON){
           gui = new GUI(this);
           gui.appendChatBox("Bot: "+startPhrase, Color.BLUE);
        }
        if (g.equals("g")){                //Если переданный при запуске параметр - g,
            gui.setVisible(true);          //То включаем графический интерфейс
        }

        logger.info("Bot: " + answers.get(0));
        startTalk();
    }

    //Метод считывает txt файл с answersPath
    private void readAnswersPath(String answersPath) {

        try (BufferedReader input = new BufferedReader(new FileReader(answersPath))) {
            String line;
            while ((line = input.readLine()) != null) { //до тех пор пока файл имеет строки, считываем их

                answers.add (line); //добавляем считанную строку в answers
            }
        }

        catch (Exception e) {
            System.out.println (e.getMessage());
        }
    }

    //Метод возвращает случайную фразу из answers
    private String getRandomPhrase(){
        Random rnd = new Random();
        int randomNumber = rnd.nextInt(answers.size());
        if (randomNumber == 0){                         // Запрещаем выбор первой фразы (приветствия)
            randomNumber = 1;
        }
        String randomPhrase = answers.get(randomNumber);

        return randomPhrase;
    }

    //Метод, запускаующий диалог
    private void startTalk() {

        String input;
      
        // Ожидаем ввода с консоли

        Scanner consoleReader = new Scanner(System.in);
        while (consoleReader.hasNext()) {
            try {
                input = (consoleReader.nextLine());

            } catch (Exception e) {

                System.out.println("Error while input");
                continue;
            }

            if(guiON) {
                gui.appendChatBox(input, Color.BLACK);
            }

            //Вызываем метод, отвечающий за действи бота

            makeAction(input);

            //Если бот отключен командой "Goodbye", то закрываем consoleReader
            if (botOn == false) {
                consoleReader.close();
                break;}
            }

        }

    //Метод, отвечающий за то, как бот реагирует на фразы пользователя
    public void makeAction(String input){

        int x = 5;

        //Команды для бота
            if (input.equals("\"Stop talking\"")){x=1;}
            if (input.equals("\"Start talking\"")){x=2;}
            if (input.equals("\"Goodbye\"")){x=3;}
            if (input.substring((input.length() - 1)).equals("\"") &&
                input.contains("\"Use another file:")
                    ){x=4;}
            if (input.equals("\"Use graphics\"")){x=6;}

        switch (x) {
            case  1:
                talkOn = false;            //Запрещаем боту говорить
                logger.info("User: "+input);
                break;
            case 2:
                talkOn = true;             //Разрешаем боту говорить
               logger.info("User: "+input);
                break;
            case 3:
                botOn = false;
                String lastPhrase = answers.get(answers.size() - 1);
                System.out.println("Bot: "+lastPhrase); //Выводим последнюю строчку из файла с ключевыми словами
                logger.info("User: " + input);
                logger.info("Bot : " +lastPhrase);
                logger.info("THE DIALOG ENDED");
                gui.dispatchEvent(new WindowEvent(gui, WindowEvent.WINDOW_CLOSING));
                break;
            case 4:
                String newAnswersPath = input.substring(18,(input.length() - 1));
                changeAnswersPath(newAnswersPath);      //Меняем файл, из которго считываются фразы

                logger.info("User: "+input);
                break;
            case 5:
                if(talkOn){

                String randomPhrase = getRandomPhrase();

                    if(guiON) {
                       gui.appendChatBox("Bot: "+randomPhrase, Color.BLUE);
                    }

                System.out.println("Bot: " +randomPhrase);  //Выводим случайную фразу
                logger.info("User: "+input);
                logger.info("Bot : "+randomPhrase);

                }
                else{
                    logger.info("User: "+input);
                }
                break;
            case 6:
                try {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                               gui.setVisible(true);   //Включаем графический интерфейс
                        }
                    });
                }
                catch(Exception e){
                    System.out.println(e);
                }
                break;
        }
    }

    //Метод, меняющий файл, из которого считываем фразы
    private void changeAnswersPath(String newPath){
        answers.clear();
        readAnswersPath(newPath);
    }

    //Метод, создающий лог файл
    private void createLogFile(){

        try {

            logger.setUseParentHandlers(false);
            FileHandler fh = new FileHandler(startTime+".log");
            LogFormatter format = new LogFormatter();
            fh.setFormatter(format);
            logger.addHandler(fh);
            logger.info("THE DIALOG BEGAN");

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
