package gui;

import gui.trace.AlertLevel;
import gui.trace.AlertTag;
import gui.trace.TracePanel;

import javax.swing.*;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

class LogControlPanel extends JPanel {
    TracePanel tp;        //Hack so I can easily call showAlertsWithLevel for this demo.
    
   /* JButton enableAllLevelsButton;
    JButton enableAllTagsButton; */
    
    JButton enableMessagesButton;
    JButton enableErrorButton;  
    JButton enableInfoButton;
    JButton enableTimeButton;
    
    JButton enableBusButton;
    
    private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	
	private final Dimension buttonDimension = new Dimension(340, 30);
	
    public LogControlPanel(final TracePanel tracePanel) {
            this.tp = tracePanel;
            
/*            enableAllLevelsButton = new JButton("Show All Levels");
            enableAllLevelsButton.setMinimumSize(buttonDimension);
            enableAllLevelsButton.setMaximumSize(buttonDimension);
            
            enableAllTagsButton = new JButton("Show All Tags");
            enableAllTagsButton.setMinimumSize(buttonDimension);
            enableAllTagsButton.setMaximumSize(buttonDimension); */
            
            enableTimeButton = new JButton("Hide Tag: TIME");
            enableTimeButton.setMinimumSize(buttonDimension);
            enableTimeButton.setMaximumSize(buttonDimension);
            
            enableMessagesButton = new JButton("Hide Level: MESSAGE");
            enableMessagesButton.setMinimumSize(buttonDimension);
            enableMessagesButton.setMaximumSize(buttonDimension);
            
            enableInfoButton = new JButton("Hide Level: INFO");
            enableInfoButton.setMinimumSize(buttonDimension);
            enableInfoButton.setMaximumSize(buttonDimension);
            
            enableErrorButton = new JButton("Hide Level: ERROR");
            enableErrorButton.setMinimumSize(buttonDimension);
            enableErrorButton.setMaximumSize(buttonDimension);
            
            tracePanel.hideAlertsWithTag(AlertTag.BUS);
            enableBusButton = new JButton("Show Tag: BUS");
            enableBusButton.setMinimumSize(buttonDimension);
            enableBusButton.setMaximumSize(buttonDimension);
            
          /*  enableAllLevelsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            			tracePanel.showAlertsForAllLevels();
                }
            });
            enableAllTagsButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
            			tracePanel.showAlertsForAllTags();
                }
            }); */
            enableTimeButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableTimeButton.getText().equalsIgnoreCase("Hide Tag: Time")){
                    	tracePanel.hideAlertsWithTag(AlertTag.TIME);
            			enableTimeButton.setText("Show Tag: Time");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.TIME);
            			enableTimeButton.setText("Hide Tag: Time");
            		}
                }
            });
            enableMessagesButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                    		if(enableMessagesButton.getText().equalsIgnoreCase("Hide Level: Message")){
                    			tracePanel.hideAlertsWithLevel(AlertLevel.MESSAGE);
                    			enableMessagesButton.setText("Show Level: Message");
                    		} else {
                    			tracePanel.showAlertsWithLevel(AlertLevel.MESSAGE);
                    			enableMessagesButton.setText("Hide Level: Message");
                    		}
                    }
            });
            enableInfoButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                		if(enableInfoButton.getText().equalsIgnoreCase("Hide Level: INFO")){
                			tracePanel.hideAlertsWithLevel(AlertLevel.INFO);
                			enableInfoButton.setText("Show Level: INFO");
                		} else {
                			tracePanel.showAlertsWithLevel(AlertLevel.INFO);
                			enableInfoButton.setText("Hide Level: INFO");
                		}
                }
            });
            enableErrorButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
	                    if(enableErrorButton.getText().equalsIgnoreCase("Hide Level: Error")){
	                    	tracePanel.hideAlertsWithLevel(AlertLevel.ERROR);
	            			enableErrorButton.setText("Show Level: Error");
	            		} else {
	            			tracePanel.showAlertsWithLevel(AlertLevel.ERROR);
	            			enableErrorButton.setText("Hide Level: Error");
	            		}
                    }
            });
            enableBusButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                		if(enableBusButton.getText().equalsIgnoreCase("Hide Tag: BUS")){
                			tracePanel.hideAlertsWithTag(AlertTag.BUS);
                			enableBusButton.setText("Show Tag: BUS");
                		} else {
                			tracePanel.showAlertsWithTag(AlertTag.BUS);
                			enableBusButton.setText("Hide Tag: BUS");
                		}
                }
            });
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(enableTimeButton);
            this.add(enableMessagesButton);
            this.add(enableInfoButton);
            this.add(enableErrorButton);
            this.add(enableBusButton);
            this.setMinimumSize(new Dimension(WIDTH, HEIGHT)); //dimensions should be fine
    }
}
