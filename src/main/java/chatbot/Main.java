package chatbot;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by BigMax on 02.04.16.
 */
public class Main {
    public static void main(String[] args) {

        SimpleDateFormat hms = new SimpleDateFormat("dd.MM.yy HH-mm-ss");
        String time = hms.format(new Date()); //Время запуска программы

        String g;
        try {g = args[0]; //Параметр, полученный при запуске из консоли
        }
        catch(Exception e){
            g = "null";   //Если параметр не был указан, то присваиваем String "null"
        }

        //Создаем экземпляр бота и передаем в конструктор :
        // 1) Время запуска программ
        // 2) Параметр из косноли
        ChatBot bot = new ChatBot(time,g);

    }
}
