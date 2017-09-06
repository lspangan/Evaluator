
package evaluator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent; 
import java.awt.event.ActionListener;


public class Project1_GUI extends JFrame implements ActionListener {
    final private TextField txField = new TextField();
    final private Panel buttonPanel = new Panel();
    final private Button buttons[] = new Button[20]; 
    //total 20 buttons on the calculator, numbered from left to right, up to down
    //bText[] array contains text on corresponding buttons
    private static final String bText[] = {"7", "8", "9", "0", "4", "5", "6",
                      "3 ", "1", "2", "(", ")", "+", "^",
                      "*", "/", "-", "=", "C", "CE"};

    public static void main(String argv[]) {
        Project1_GUI calc = new Project1_GUI();
    }

    public Project1_GUI() {
        // edits the appearance of the calculator
        // splits calculator into two panels
        // adds a text field and buttons
        setLayout(new BorderLayout());
            add(txField, BorderLayout.NORTH);
            txField.setEditable(false);
            add(buttonPanel, BorderLayout.CENTER);
            buttonPanel.setLayout(new GridLayout(5, 4));
            for (int i = 0; i < 20; i++)  
            //create 20 buttons with corresponding text in buttons[i] = new Button(bText[i]);
                buttons[i] = new Button(bText[i]);
            for (int i = 0; i < 20; i++) 
                buttonPanel.add(buttons[i]);
            //set up buttons to listen for mouse input buttons[i].addActionListener(this);
            for (int i = 0; i < 20; i++)
                buttons[i].addActionListener(this);
                       
            
        setTitle("Calculator");
        setSize(400, 400);
        setLocationByPlatform(true);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {        
        Evaluator eval = new Evaluator(); // used to evaluate expression
        // if numbers and operators then append to textbox
        if (arg0.getSource() == buttons[0]) {
            txField.setText(txField.getText() + 7);
        } else if (arg0.getSource() == buttons[1]) {
            txField.setText(txField.getText() + 8);
        } else if (arg0.getSource() == buttons[2]) {
            txField.setText(txField.getText() + 9);
        } else if (arg0.getSource() == buttons[3]) {
            txField.setText(txField.getText() + 0);
        } else if (arg0.getSource() == buttons[4]) {
            txField.setText(txField.getText() + 4);
        } else if (arg0.getSource() == buttons[5]) {
            txField.setText(txField.getText() + 5);
        } else if (arg0.getSource() == buttons[6]) {
            txField.setText(txField.getText() + 6);
        } else if (arg0.getSource() == buttons[7]) {
            txField.setText(txField.getText() + 3);
        } else if (arg0.getSource() == buttons[8]) {
            txField.setText(txField.getText() + 1);
        } else if (arg0.getSource() == buttons[9]) {
            txField.setText(txField.getText() + 2);
        } else if (arg0.getSource() == buttons[10]) {
            txField.setText(txField.getText().concat("("));
        } else if (arg0.getSource() == buttons[11]) {
            txField.setText(txField.getText().concat(")"));
        } else if (arg0.getSource() == buttons[12]) {
            txField.setText(txField.getText().concat("+"));
        } else if (arg0.getSource() == buttons[13]) {
            txField.setText(txField.getText().concat("^"));
        } else if (arg0.getSource() == buttons[14]) {
            txField.setText(txField.getText().concat("*"));
        } else if (arg0.getSource() == buttons[15]) {
            txField.setText(txField.getText().concat("/"));
        } else if (arg0.getSource() == buttons[16]) {
            txField.setText(txField.getText().concat("-"));
        // if = then call on Evaluator.eval()
        // converts int to string
        } else if (arg0.getSource() == buttons[17]) {
            String expr = Integer.toString(eval.eval(txField.getText()));
            txField.setText(expr);
        // if C, removes one character from text field
        } else if (arg0.getSource() == buttons[18]) {
            String expr = txField.getText();
            if(!txField.getText().equals("")) {
                txField.setText(expr.substring(0, expr.length() - 1));
            }
        // if CE, clears text field
        } else if (arg0.getSource() == buttons[19]){
            txField.setText("");
        }
    }
}
