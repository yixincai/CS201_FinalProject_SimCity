package city.restaurant.omar.interfaces;

import city.restaurant.omar.CashierAgent;

public interface Customer {
	
        /**
         * @param total The cost according to the cashier
         *
         * Sent by the cashier prompting the customer's money after the customer has approached the cashier.
         */
        public abstract void msgHereIsCheck(CashierAgent cashier, double check);

        /**
         * @param total change (if any) due to the customer
         *
         * Sent by the cashier to end the transaction between him and the customer. total will be >= 0 .
         */
        public abstract void msgHereIsYourChange(double change);


        /**
         * @param remaining_cost how much money is owed
         * Sent by the cashier if the customer does not pay enough for the bill (in lieu of sending {@link #HereIsYourChange(double)}
         */
        public abstract void msgGoDie();

}