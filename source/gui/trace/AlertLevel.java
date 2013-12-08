package gui.trace;

public enum AlertLevel {
        
        /** 
         * Errors are printed in red.
         */
        ERROR,
        
        /** 
    	 *	Warnings are printed normally.
         */
        WARNING,
        
        /**
      	 *	It is generally off by default.
         */
        INFO,

        /**
         * Most alerts will have this level.  This should be the normal 
         * logging level for trace panel or console messages.
         */
        MESSAGE,

        /**
         * This level could be used to print specific debug information.
         */
        DEBUG
}