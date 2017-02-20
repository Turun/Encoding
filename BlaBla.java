import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Dimension;

import java.awt.GridLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.BorderLayout;
import java.awt.Insets;

public class BlaBla{
    JPanel input;
    JPanel encoded;
    JPanel output;
    JPanel buttons;
    JTextArea jtaIn;
    JTextArea jtaCode;
    JTextArea jtaOut;
    JScrollPane jspIn;
    JScrollPane jspCode;
    JScrollPane jspOut;
    JFrame f;
    JButton encode;
    JButton decode;
    JButton clear;
    JButton test;
    GridBagLayout gbl;
    GridBagConstraints gbc;
        
    String strIn = "abcdefghijkllmnopqrstuvwxyz\nABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
    String strOut = "";
    String strCode = "";
    int[] bin;
    
    
    public BlaBla(){
        makeFrame();
    }
    
    public void decodeText(){
        strOut = "";
        for(int i = 0; i<(bin.length*32); i += 8){
            strOut += Character.toString((char)((bin[i>>5] >> (24 - i%32)) & 255));
        }
        updateText();
        
        /**
        String returnString = ""
        for(int i = 0; i<512; i = i+8){
            intCode = bin[i>>5]
            intChar = intCode >> (24 - i % 32)
            intChar = intChar & 255   //to get only the 8 rightmost bits
            returnString = returnString + charFromCharcode(intChar)
        }
        return returnString;
        */
    }
    
    public void encodeText(){
        strIn = jtaIn.getText();
        if(strIn.length() % 4 ==0){
            bin = new int[(int)(strIn.length()*0.25)];
        }else{
            bin = new int[(int)(strIn.length()*0.25)+1];
        }
        
        for(int i = 0; i<strIn.length()*8; i +=8){
            bin[i>>5] |= ((int)(strIn.charAt((int)(i/8))) & 255) << (24 - i%32);
        }
        updateText();
        
        /**
         * function Magic(str) {
            
            var bin = Array();
            
            for(var i = 0; i < 512; i += 8) {
            
             bin[i>>5] |= (str.charCodeAt(i / 8) & 255) << (24 - i%32);
            
             }
            
            return bin;
            
            }
         */
    }
    
    public void updateText(){
        jtaIn.setText(strIn);
        jtaOut.setText(strOut);
        
        strCode = "";
        for(int i = 0; i<(bin.length*32); i += 8){
            //int Code = bin[i>>5];
            //Code = Code >> (24 - i%32);
            //Code = Code & 255;
            String localCode = String.valueOf((bin[i>>5] >> (24 - i%32)) & 255);
            strCode += localCode;
            /*
            if(localCode.length() == 1){strCode += "      "+"       ";}
            else if(localCode.length() == 2){strCode += "   "+"       ";}
            else if(localCode.length() == 3){strCode += ""+"       ";}
            else{System.out.println("That doesn't look good ---" + localCode);}*/
            if((i+8)%32 == 0){
                strCode += "\n";
            }else{
                strCode += "\t";
            }
        }
        
        jtaCode.setText(strCode);
        f.repaint();
    }
    
    public void clearText(){
        jtaIn.setText("");
        encodeText();
        decodeText();
    }
    
    public void testText(){
        clearText();
        
        bin = new int[64];
        for(int i = 0; i<2048; i +=8){
            bin[i>>5] |= ((i/8) & 255) << (24 - i%32);
        }
        decodeText();
    }
    
    public void makeFrame(){
        gbl = new GridBagLayout();
        gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(10,10,10,10);
        gbc.anchor = GridBagConstraints.FIRST_LINE_START;
        
        f = new JFrame("ASCII Encoder");
        f.setBounds(100,100,400,400);
        f.setLayout(gbl);
        f.setResizable(false);
        f.setVisible(false);
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        input = new JPanel(new BorderLayout());
        jtaIn = new JTextArea();
        jtaIn.setEditable(true);
        jspIn = new JScrollPane(jtaIn);
        jspIn.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspIn.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspIn.setPreferredSize(new Dimension(300,200));
        input.add(jspIn, BorderLayout.CENTER);
        input.add(new JLabel("Input:"), BorderLayout.NORTH);
        
        encoded = new JPanel(new BorderLayout());
        jtaCode = new JTextArea();
        jtaCode.setEditable(false);
        jspCode = new JScrollPane(jtaCode);
        jspCode.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspCode.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspCode.setPreferredSize(new Dimension(350,200));
        encoded.add(jspCode, BorderLayout.CENTER);
        encoded.add(new JLabel("Encoded:"), BorderLayout.NORTH);
        
        output = new JPanel(new BorderLayout());
        jtaOut = new JTextArea();
        jtaOut.setEditable(false);
        jspOut = new JScrollPane(jtaOut);
        jspOut.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        jspOut.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        jspOut.setPreferredSize(new Dimension(300,200));
        output.add(jspOut, BorderLayout.CENTER);
        output.add(new JLabel("Output:"), BorderLayout.NORTH);
        
        buttons = new JPanel(new GridLayout(1,2, 10,10));
        buttons.setPreferredSize(new Dimension(100,50));
        encode = new JButton("Encode");
        encode.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){encodeText();}});
        decode = new JButton("Decode");
        decode.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){decodeText();}});
        buttons.add(encode);
        buttons.add(decode);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbl.setConstraints(input, gbc);
        f.add(input);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbl.setConstraints(encoded, gbc);
        f.add(encoded);
        
        gbc.gridx = 2;
        gbc.gridy = 0;
        gbl.setConstraints(output, gbc);
        f.add(output);
        
        gbc.gridx = 1;
        gbc.gridy = 1;
        gbl.setConstraints(buttons, gbc);
        f.add(buttons);
        
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,50,10,50);
        clear = new JButton("Clear");
        clear.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){clearText();}});
        gbl.setConstraints(clear, gbc);
        f.add(clear);
        
        gbc.gridx = 2;
        gbc.gridy = 1;
        gbc.insets = new Insets(10,50,10,50);
        test = new JButton("Test");
        test.addActionListener(new ActionListener(){public void actionPerformed(ActionEvent e){testText();}});
        gbl.setConstraints(test, gbc);
        f.add(test);
        
        f.pack();
        jtaIn.setText(strIn);
        f.setVisible(true);
        f.repaint();
    }

    public static void main(String[] args){
        BlaBla bla = new BlaBla();
    }


}




