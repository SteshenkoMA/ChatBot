package chatbot;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by BigMax on 23.03.16.
 */

//Класс, создающий графическую оболочку.
public class GUI extends JFrame {

    private JTextPane chatBox; //Окно диалога
    private JTextField input;  //Окно ввода
    private ChatBot bot;

    //Конструктор, в котором задаются основные параметры JFrame
    public GUI(ChatBot bot) {

        super("GUI ChatBot");
        this.bot=bot;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = makePanel();

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout(5, 5));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        mainPanel.add(panel, BorderLayout.CENTER);
        getContentPane().add(mainPanel);
        setPreferredSize(new Dimension(300, 300));
        pack();
        setLocationRelativeTo(null);
    }

    //Метод, создающий JPanel c окном диалога и окном ввода
    private JPanel makePanel(){

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout(5, 5));

        chatBox = new JTextPane();

        chatBox.setEditable(false);
        panel.add(new JScrollPane(chatBox), BorderLayout.CENTER);

        input = new JTextField();
        panel.add(input, BorderLayout.SOUTH);

        input.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                String userPhrase = input.getText();
                System.out.println(userPhrase);
                appendChatBox(userPhrase,Color.BLACK);

                bot.makeAction(userPhrase);
                input.setText("");
            }
        });

        return panel;

    }

    //Метод, добавляющий фразы в окно диалога
    public void appendChatBox(String s,Color color) {
        Style style = chatBox.addStyle("Style", null);
        StyleConstants.setForeground(style, color);

        try {
            Document doc = chatBox.getDocument();
            doc.insertString(doc.getLength(), s+"\n", style);
        } catch(BadLocationException exc) {
            exc.printStackTrace();
            System.out.println(exc);
        }
    }

}
