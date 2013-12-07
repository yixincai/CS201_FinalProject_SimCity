package gui;

import gui.trace.AlertLevel;
import gui.trace.AlertTag;
import gui.trace.TracePanel;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class ErrorControlPanel extends JPanel {
    TracePanel tp;        //Hack so I can easily call showAlertsWithLevel for this demo.
    
    JButton enableMessagesButton;                //You could (and probably should) substitute a JToggleButton to replace both
    JButton disableMessagesButton;                //of these, but I split it into enable and disable for clarity in the demo.
    JButton enableErrorButton;                
    JButton disableErrorButton;                
    JButton enableBankCustTagButton;                //You could (and probably should) substitute a JToggleButton to replace both
    JButton disableBankCustTagButton;                //of these, but I split it into enable and disable for clarity in the demo.
    
    private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	
	private final Dimension buttonDimension = new Dimension(340, 30);
	
    public ErrorControlPanel(final TracePanel tracePanel) {
            this.tp = tracePanel;
            enableMessagesButton = new JButton("Show Level: MESSAGE");
            enableMessagesButton.setMinimumSize(buttonDimension);
            enableMessagesButton.setMaximumSize(buttonDimension);
            
            disableMessagesButton = new JButton("Hide Level: MESSAGE");
            disableMessagesButton.setMinimumSize(buttonDimension);
            disableMessagesButton.setMaximumSize(buttonDimension);
            
            enableErrorButton = new JButton("Show Level: ERROR");
            enableErrorButton.setMinimumSize(buttonDimension);
            enableErrorButton.setMaximumSize(buttonDimension);
            
            disableErrorButton = new JButton("Hide Level: ERROR");
            disableErrorButton.setMinimumSize(buttonDimension);
            disableErrorButton.setMaximumSize(buttonDimension);
            
            enableBankCustTagButton = new JButton("Show Tag: BANK_CUSTOMER");
            enableBankCustTagButton.setMinimumSize(buttonDimension);
            enableBankCustTagButton.setMaximumSize(buttonDimension);
            
            disableBankCustTagButton = new JButton("Hide Tag: BANK_CUSTOMER");
            disableBankCustTagButton.setMinimumSize(buttonDimension);
            disableBankCustTagButton.setMaximumSize(buttonDimension);
            
            enableMessagesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This is how you make messages with a certain Level (normal MESSAGE here) show up in the trace panel.
                            tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
                            //================================================================================
                    }
            });
            disableMessagesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This is how you make messages with a certain Level not show up in the trace panel.
                            tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
                            //================================================================================
                    }
            });
            enableErrorButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This is how you make messages with a level of ERROR show up in the trace panel.
                            tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
                            //================================================================================
                    }
            });
            disableErrorButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This is how you make messages with a level of ERROR not show up in the trace panel.
                            tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
                            //================================================================================
                    }
            });
            enableBankCustTagButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This works the same way as AlertLevels, only you're using tags instead.
                            //In this demo, I generate message with tag BANK_CUSTOMER when you click in the 
                            //AnimationPanel somewhere.
                            tracePanel.showAlertsWithTag(AlertTag.BANK_CUSTOMER);
                            //================================================================================
                    }
            });
            disableBankCustTagButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            //============================ TUTORIAL ==========================================
                            //This works the same way as AlertLevels, only you're using tags instead.
                            tracePanel.hideAlertsWithTag(AlertTag.BANK_CUSTOMER);
                            //================================================================================
                    }
            });
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(enableMessagesButton);
            this.add(disableMessagesButton);
            this.add(enableErrorButton);
            this.add(disableErrorButton);
            this.add(enableBankCustTagButton);
            this.add(disableBankCustTagButton);
            this.setMinimumSize(new Dimension(WIDTH, HEIGHT)); //dimensions should be fine
    }
}
