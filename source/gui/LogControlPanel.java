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
    
    JButton enableBankButton;
    JButton enableMarketButton;
    JButton enableOmarRestaurantButton;
    JButton enableYixinRestaurantButton;
    JButton enableEricRestaurantButton;
    JButton enableRyanRestaurantButton;
    
    JButton filterClearButton;
    
    private final int WIDTH = 1024/3;
	private final int HEIGHT = 720;
	
	private final Dimension buttonDimension = new Dimension(340, 30);
	
    public LogControlPanel(final TracePanel tracePanel) {
            this.tp = tracePanel;
            
/*          enableAllLevelsButton = new JButton("Show All Levels");
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
            
            enableBankButton = new JButton("Hide Tag: BANK");
            enableBankButton.setMinimumSize(buttonDimension);
            enableBankButton.setMaximumSize(buttonDimension);
            
            enableMarketButton  = new JButton("Hide Tag: MARKET");
            enableMarketButton.setMinimumSize(buttonDimension);
            enableMarketButton.setMaximumSize(buttonDimension);
            
            enableOmarRestaurantButton  = new JButton("Hide Tag: OMAR_RESTAURANT");
            enableOmarRestaurantButton.setMinimumSize(buttonDimension);
            enableOmarRestaurantButton.setMaximumSize(buttonDimension);
            
            enableYixinRestaurantButton  = new JButton("Hide Tag: YIXIN_RESTAURANT");
            enableYixinRestaurantButton.setMinimumSize(buttonDimension);
            enableYixinRestaurantButton.setMaximumSize(buttonDimension);
            
            enableEricRestaurantButton  = new JButton("Hide Tag: ERIC_RESTAURANT");
            enableEricRestaurantButton.setMinimumSize(buttonDimension);
            enableEricRestaurantButton.setMaximumSize(buttonDimension);
            
            enableRyanRestaurantButton  = new JButton("Hide Tag: RYAN_RESTAURANT");
            enableRyanRestaurantButton.setMinimumSize(buttonDimension);
            enableRyanRestaurantButton.setMaximumSize(buttonDimension);
            
            filterClearButton = new JButton("Clear Filter Periodically");
            filterClearButton.setMinimumSize(buttonDimension);
            filterClearButton.setMaximumSize(buttonDimension);
            
            
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
            
            //places
            enableBankButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableBankButton.getText().equalsIgnoreCase("Hide Tag: BANK")){
                    	tracePanel.hideAlertsWithTag(AlertTag.BANK);
            			enableBankButton.setText("Show Tag: BANK");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.BANK);
            			enableBankButton.setText("Hide Tag: BANK");
            		}
                }
            });
            enableMarketButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableMarketButton.getText().equalsIgnoreCase("Hide Tag: MARKET")){
                    	tracePanel.hideAlertsWithTag(AlertTag.TIME);
            			enableMarketButton.setText("Show Tag: MARKET");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.TIME);
            			enableMarketButton.setText("Hide Tag: MARKET");
            		}
                }
            });
            enableOmarRestaurantButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableOmarRestaurantButton.getText().equalsIgnoreCase("Hide Tag: OMAR_RESTAURANT")){
                    	tracePanel.hideAlertsWithTag(AlertTag.OMAR_RESTAURANT);
            			enableOmarRestaurantButton.setText("Show Tag: OMAR_RESTAURANT");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.OMAR_RESTAURANT);
            			enableOmarRestaurantButton.setText("Hide Tag: OMAR_RESTAURANT");
            		}
                }
            });
            enableYixinRestaurantButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableYixinRestaurantButton.getText().equalsIgnoreCase("Hide Tag: YIXIN_RESTAURANT")){
                    	tracePanel.hideAlertsWithTag(AlertTag.YIXIN_RESTAURANT);
            			enableYixinRestaurantButton.setText("Show Tag: YIXIN_RESTAURANT");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.YIXIN_RESTAURANT);
            			enableYixinRestaurantButton.setText("Hide Tag: YIXIN_RESTAURANT");
            		}
                }
            });
            enableEricRestaurantButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableEricRestaurantButton.getText().equalsIgnoreCase("Hide Tag: ERIC_RESTAURANT")){
                    	tracePanel.hideAlertsWithTag(AlertTag.ERIC_RESTAURANT);
            			enableEricRestaurantButton.setText("Show Tag: ERIC_RESTAURANT");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.ERIC_RESTAURANT);
            			enableEricRestaurantButton.setText("Hide Tag: ERIC_RESTAURANT");
            		}
                }
            });
            enableRyanRestaurantButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(enableRyanRestaurantButton.getText().equalsIgnoreCase("Hide Tag: RYAN_RESTAURANT")){
                    	tracePanel.hideAlertsWithTag(AlertTag.TIME);
            			enableRyanRestaurantButton.setText("Show Tag: RYAN_RESTAURANT");
            		} else {
            			tracePanel.showAlertsWithTag(AlertTag.TIME);
            			enableRyanRestaurantButton.setText("Hide Tag: RYAN_RESTAURANT");
            		}
                }
            });
            
            filterClearButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(filterClearButton.getText().equalsIgnoreCase("Clear Filter Periodically")){
                    	tracePanel.setFlag(true);
            			filterClearButton.setText("Turn off Filter");
            		} else {
            			tracePanel.setFlag(false);
            			filterClearButton.setText("Clear Filter Periodically");
            		}
                }
            });
            
            this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
            this.add(enableTimeButton);
            this.add(enableMessagesButton);
            this.add(enableInfoButton);
            this.add(enableErrorButton);
            this.add(enableBusButton);
            this.add(enableBankButton);
            this.add(enableMarketButton);
            this.add(enableOmarRestaurantButton);
            this.add(enableYixinRestaurantButton);
            this.add(enableEricRestaurantButton);
            this.add(enableRyanRestaurantButton);
            this.add(filterClearButton);
            this.setMinimumSize(new Dimension(WIDTH, HEIGHT)); //dimensions should be fine
    }
}
