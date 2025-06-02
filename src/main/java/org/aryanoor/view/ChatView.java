package org.aryanoor.view;

import org.aryanoor.model.OpenRouterChat;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

public class ChatView {
    ChatArea chatArea;
    JScrollPane scrollPane;
    TextFieldArea textFieldArea;
    OpenRouterChat chatBot;
    String separatorLine;

    public ChatView(OpenRouterChat chatBot) {
        initUI();
        this.chatBot = chatBot;
    }

    private void initUI() {

        MyFrame myFrame = new MyFrame("Chat Bot");
        myFrame.setLayout(new BorderLayout());

        //init chat area
        chatArea = new ChatArea();
        scrollPane = new JScrollPane(chatArea);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        myFrame.add(scrollPane , BorderLayout.CENTER);

        //init button and text field panel
        JPanel buttonAndTextFieldPanel = new JPanel();
        buttonAndTextFieldPanel.setLayout(new BorderLayout());

        //init text field area
        textFieldArea = new TextFieldArea();
        buttonAndTextFieldPanel.add(textFieldArea , BorderLayout.CENTER);

        //init send button
        SendButton sendButton = new SendButton();
        buttonAndTextFieldPanel.add(sendButton , BorderLayout.EAST);

        myFrame.add(buttonAndTextFieldPanel , BorderLayout.SOUTH);


        myFrame.setVisible(true);
    }

    private void sendMessageToBot() throws IOException, BadLocationException {
        String userInput = textFieldArea.getText();
        if (textFieldArea.getText().isEmpty()) return;

        StyledDocument doc = chatArea.getStyledDocument();

        SimpleAttributeSet attrBotThinking = new SimpleAttributeSet();
        styleToAttrBotThinking(attrBotThinking);

        SimpleAttributeSet attrUserQuestion = new SimpleAttributeSet();
        styleToAttrUserQuestion(attrUserQuestion);


        SimpleAttributeSet attrBotAnswer = new SimpleAttributeSet();
        styleToAttrBotAnswer(attrBotAnswer);

        SimpleAttributeSet attrSeparatorLine = new SimpleAttributeSet();
        styleToAttrSeparatorLine(attrSeparatorLine);

        separatorLine = "\n────────────────────────────────────────\n\n";



        doc.insertString(doc.getLength(), " You: "  , attrUserQuestion);
        StyleConstants.setBold(attrUserQuestion, false);
        doc.insertString(doc.getLength(),textFieldArea.getText()  + "\n" , attrUserQuestion);

        doc.insertString(doc.getLength() , separatorLine  ,attrSeparatorLine);
        doc.setParagraphAttributes(doc.getLength() - separatorLine.length() , separatorLine.length(), attrSeparatorLine, false);

        textFieldArea.setText("");
        int thinkingStart = doc.getLength();
        doc.insertString(thinkingStart, " Thinking...\n" , attrBotThinking);

        String response = chatBot.sendChatRequest(userInput);
        doc.remove(thinkingStart, " Thinking...\n".length());
        doc.insertString(doc.getLength() , " Bot:  " , attrBotAnswer);
        StyleConstants.setBold(attrBotAnswer, false);
        doc.insertString(doc.getLength(), response + "\n" , attrBotAnswer);


        separatorLine = "\n✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦✦\n\n";
        doc.insertString(doc.getLength() , separatorLine , attrSeparatorLine);
        doc.setParagraphAttributes(doc.getLength() - separatorLine.length() , separatorLine.length(), attrSeparatorLine, false);

    }

    private void styleToAttrSeparatorLine(SimpleAttributeSet attributeSetSeparatorLine) {
        StyleConstants.setFontFamily(attributeSetSeparatorLine, "Courier New");
        StyleConstants.setAlignment(attributeSetSeparatorLine, StyleConstants.ALIGN_CENTER);
        StyleConstants.setFontSize(attributeSetSeparatorLine, 14);
        StyleConstants.setForeground(attributeSetSeparatorLine, Color.BLACK);
    }

    private void styleToAttrBotAnswer(SimpleAttributeSet attributeSetBotAnswer) {
        StyleConstants.setFontFamily(attributeSetBotAnswer, "Segoe UI");
        StyleConstants.setFontSize(attributeSetBotAnswer, 15);
        StyleConstants.setForeground(attributeSetBotAnswer, new Color(0x3F51B5));
        StyleConstants.setBold(attributeSetBotAnswer, true);
    }

    private void styleToAttrUserQuestion(SimpleAttributeSet attrUserQuestion) {
        StyleConstants.setFontFamily(attrUserQuestion, "Arial");
        StyleConstants.setFontSize(attrUserQuestion, 16);
        StyleConstants.setBold(attrUserQuestion, true);
        StyleConstants.setForeground(attrUserQuestion, new Color(0x1E88E5));
    }

    private void styleToAttrBotThinking(SimpleAttributeSet attrBotThinking) {
        StyleConstants.setFontFamily(attrBotThinking, "Arial");
        StyleConstants.setFontSize(attrBotThinking, 14);
        StyleConstants.setItalic(attrBotThinking, true);
        StyleConstants.setAlignment(attrBotThinking, StyleConstants.ALIGN_LEFT);
        StyleConstants.setForeground(attrBotThinking, new Color(0x43A047));
    }

    private static class ChatArea extends JTextPane {
        ChatArea(){
            setPreferredSize(new Dimension(600, 600));
            setMaximumSize(new Dimension(600, Integer.MAX_VALUE));
            setLayout(new BoxLayout(this ,BoxLayout.Y_AXIS));


            setFocusable(false);
            setEditable(false);
            setBackground(new Color(0xE3F2FD));

            StyledDocument doc = getStyledDocument();

            SimpleAttributeSet attributeSet = new SimpleAttributeSet();

            StyleConstants.setFontFamily(attributeSet ,"Arial");
            StyleConstants.setFontSize(attributeSet, 14);

            doc.setParagraphAttributes(0, doc.getLength(), attributeSet, false);
        }

    }

    private class TextFieldArea extends JTextArea {
        TextFieldArea(){
            setLineWrap(true);
            setWrapStyleWord(true);
            setRows(2);
            setBackground(new Color(0xF0F4FF));
            setForeground(new Color(0x1A237E));
            setCaretColor(new Color(0x3949AB));
            setEditable(true);
            setFont(new Font("Arial", Font.PLAIN, 16));
            setBorder(BorderFactory.createLineBorder(new Color(0x3F51B5), 2, true));
            setSelectedTextColor(Color.BLUE);
            setSelectionEnd(getText().length());
            addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        try {
                            sendMessageToBot();
                        } catch (IOException | BadLocationException ex) {
                            throw new RuntimeException(ex);
                        }
                    }
                }
            });
        }
    }

    private class SendButton extends JButton {
        SendButton(){
            setText("Send");
            setBackground(new Color(0x1976D2));
            setForeground(Color.WHITE);
            setFont(new Font("Arial", Font.PLAIN, 15));
            setFocusable(false);
            setBorder(BorderFactory.createLineBorder(new Color(0x1565C0), 2, true));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            addActionListener(_-> {
                try {
                    if (!textFieldArea.getText().isEmpty())
                        sendMessageToBot();
                } catch (IOException | BadLocationException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    private static class MyFrame extends JFrame {

        public MyFrame(String title) {
            setTitle(title);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setLocationRelativeTo(null);
            setResizable(false);
            getContentPane().setBackground(new Color(0xB8D2ED));
            setSize(600, 600);
        }

    }
}
