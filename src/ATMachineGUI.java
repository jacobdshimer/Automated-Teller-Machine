//ATMachineGUI.java by Shimer, Jacob D.
//This is the main file for the application. It includes the class that builds the frame,
//creates the buttons, creates the listener for the buttons, and the main class

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.apache.commons.lang3.*;

public class ATMachineGUI {
    private static Account checkingAcct;
    private static Account savingsAcct;

    /*Create the frame for the ATM Machine.  The ATMachineFrame class consists of a constructor specifying the
    title, width, and height of the Frame, a the display method to have the frame be displayed, and a setter
    for setting the frame up.*/
    class ATMachineFrame extends JFrame {
        ATMachineFrame(String title, int width, int height) {
            super(title);
            setFrame(width, height);
        }

        void display() {
            setVisible(true);
        }

        void setFrame(int width, int height) {
            setSize(width, height);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        }
    }

    /*This class creates and constructs the Button Panel and a separate Text Panel. I didn't like the way it
    looked having the Text Field being part of the Button Panel, which is why I separated them.  This class also
    contains an inner class, ClickListener.  This class implements the ActionListener. */

    public class ButtonPanel extends JPanel {
        //Create Buttons
        private JButton deposit = new JButton("Deposit");
        private JButton balance = new JButton("Balance");
        private JButton transferTo = new JButton("Transfer To");
        private JButton withdraw = new JButton("Withdraw");

        //Create Radio Buttons
        private JRadioButton checking = new JRadioButton("Checking");
        private JRadioButton savings = new JRadioButton("Savings");
        //Create Text Field
        private JTextField amount = new JTextField(20);

        ButtonPanel() {
            setLayout(new BorderLayout());
            setBackground(Color.lightGray);

            //Create the Button's Panel
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(3, 2, 5, 5));
            buttonPanel.add(deposit);
            buttonPanel.add(balance);
            buttonPanel.add(transferTo);
            buttonPanel.add(withdraw);
            buttonPanel.add(checking);
            buttonPanel.add(savings);
            checking.setHorizontalAlignment(SwingConstants.CENTER);
            savings.setHorizontalAlignment(SwingConstants.CENTER);
            ButtonGroup bg = new ButtonGroup();
            bg.add(checking);
            bg.add(savings);
            add(buttonPanel, BorderLayout.CENTER);

            //Adds the buttons to the ActionListener, ClickListener
            deposit.addActionListener(new ClickListener());
            balance.addActionListener(new ClickListener());
            transferTo.addActionListener(new ClickListener());
            withdraw.addActionListener(new ClickListener());

            //Create the text panel for amount
            JPanel textFieldPanel = new JPanel();
            textFieldPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            textFieldPanel.add(amount);
            add(textFieldPanel, BorderLayout.SOUTH);
        }

        public class ClickListener implements ActionListener{
            @Override
            public void actionPerformed (ActionEvent e){
                String text = amount.getText();
                //Checks if the source of the ActionEvent is the withdraw button.
                if (e.getSource() == withdraw) {
                    //Create a title for the JOptionPane that will be implemented for this button
                    String title = "Withdrawing from ";
                    if (StringUtils.isNumeric(text)) {
                        //Turns value into a double that way we can check if it is Divisible by 20.  I do this by
                        //using the modulo operator
                        double value = Double.parseDouble(text);
                        if (value % 20.0 == 0.0) {
                            //Check if checking is selected
                            if (checking.isSelected()) {
                                //Try-Catch statement for withdrawing funds from checking account
                                try {
                                    checkingAcct.withdraw(value);
                                    //Uses the getBalance getter from Account.java in order to let the user know that the withdrawal was
                                    //successful and what there new balance is
                                    String message = "Withdraw successful.  New balance is " + String.format("%.2f",checkingAcct.getBalance());
                                    JOptionPane.showMessageDialog(null,message, title + "Checking", JOptionPane.INFORMATION_MESSAGE);
                                } catch (InsufficientFundsException ex) {
                                    //For the JOptionPane message, it uses the exception message specified when it is thrown
                                    JOptionPane.showMessageDialog(null, ex.getMessage(),"Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                                }
                            } else if (savings.isSelected()) {
                                //The Exact same thing as above but for savings account
                                try {
                                    savingsAcct.withdraw(value);
                                    String message = "Withdraw successful.  New balance is " + String.format("%.2f",savingsAcct.getBalance());
                                    JOptionPane.showMessageDialog(null,message, title + "Savings", JOptionPane.INFORMATION_MESSAGE);
                                } catch (InsufficientFundsException ex) {
                                    JOptionPane.showMessageDialog(null,ex.getMessage(),"Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                                }
                            } //Puts out an JOptionPane.ERROR_MESSAGE if not an increment of 20
                        } else JOptionPane.showMessageDialog(null, "Amount must be in increments of 20","Input Error",JOptionPane.ERROR_MESSAGE);
                    } else { //Puts out an JOptionPane.ERROR_MESSAGE if the input is not a number
                        JOptionPane.showMessageDialog(null, "Amount must be Numeric","Input Error",JOptionPane.ERROR_MESSAGE);
                    }

                    //Checks which account is selected and then calls the deposit method from that account
                } else if (e.getSource() == deposit){
                    String title = "Depositing to ";
                    if (StringUtils.isNumeric(text)){
                       if (checking.isSelected()) {
                           checkingAcct.deposit(Double.parseDouble(text));
                           String message = "Deposit successful.  New balance is " + String.format("%.2f",checkingAcct.getBalance());
                           JOptionPane.showMessageDialog(null,message, title + "Checking", JOptionPane.INFORMATION_MESSAGE);
                       } else if (savings.isSelected()) {
                           savingsAcct.deposit(Double.parseDouble(text));
                           String message = "Deposit successful.  New balance is " + String.format("%.2f",savingsAcct.getBalance());
                           JOptionPane.showMessageDialog(null,message, title + "Savings", JOptionPane.INFORMATION_MESSAGE);
                       }
                    } else {
                        JOptionPane.showMessageDialog(null, "Amount must be Numeric","Input Error",JOptionPane.ERROR_MESSAGE);
                    }

                    //In order to properly set up transferring funds to another account, I had the program call the account
                    //transferTo method from the account that is being transferred from, which in turn calls the withdraw method
                    //for that account and then the deposit method in the account that is being transferred to
                } else if (e.getSource() == transferTo){
                    String title = "Transferring to ";
                    if (StringUtils.isNumeric(text)) {
                        if (checking.isSelected()) {
                            try {
                                checkingAcct.transferTo(Double.parseDouble(text));
                                savingsAcct.deposit(Double.parseDouble(text));
                                String message = "Transfer Successful \n" +
                                        "New Checking balance is: " + String.format("%.2f",checkingAcct.getBalance()) +
                                        "\nNew Savings balance is: " + String.format("%.2f",savingsAcct.getBalance());
                                JOptionPane.showMessageDialog(null,message,title + "Savings", JOptionPane.INFORMATION_MESSAGE);
                            } catch (InsufficientFundsException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(),"Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                            }
                        } if (savings.isSelected()) {
                            try {
                                savingsAcct.transferTo(Double.parseDouble(text));
                                checkingAcct.deposit(Double.parseDouble(text));
                                String message = "Transfer Successful \n" +
                                        "New Savings balance is: " + String.format("%.2f",savingsAcct.getBalance()) +
                                        "\nNew Checking balance is:  " + String.format("%.2f",checkingAcct.getBalance());
                                JOptionPane.showMessageDialog(null,message,title + "Checking", JOptionPane.INFORMATION_MESSAGE);
                            } catch (InsufficientFundsException ex) {
                                JOptionPane.showMessageDialog(null, ex.getMessage(),"Insufficient Funds", JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Amount must be Numeric","Input Error",JOptionPane.ERROR_MESSAGE);
                    }

                    //Instead of creating a whole new method just to get the balance, I called the getter in the account class
                }  else if (e.getSource() == balance) {
                    String title = "Balance";
                    if (checking.isSelected()) {
                        String message = "Current balance in Checking Account is: " + String.format("%.2f",checkingAcct.getBalance());
                        JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
                    } else if (savings.isSelected()) {
                        String message = "Current balance in Savings Account is: " + String.format("%.2f",savingsAcct.getBalance());
                        JOptionPane.showMessageDialog(null,message,title,JOptionPane.INFORMATION_MESSAGE);
                    }
                }

            }
        }
    }

    //Application class that extends the ATMachineFrame and makes the title, height, and width settable
    //Also adds the button panel to the window
    public class ATMachineGUIApplication extends ATMachineFrame {
        ATMachineGUIApplication(String title, int width, int height) {
            super(title, width, height);
            add(new ButtonPanel());
        }
    }

    //Main class, creates an instance of the ATMachineApplication called Application
    //creates two accounts, the savingsAcct and the checkingAcct and displays the window
    public static void main(String[] args) {
        ATMachineGUIApplication application = new ATMachineGUI().  new ATMachineGUIApplication("ATM Machine",500,200);
        savingsAcct = new Account(2000);
        checkingAcct = new Account(1000);
        application.display();
    }
}



