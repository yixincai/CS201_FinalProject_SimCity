package role.market;
import java.util.*;

public class MarketCustomerRole {
	Market market;
	List<Item> order;
	List<Item> orderFulfillment;
	
	double money, payment;
	Boolean needPay, wantToBuy;

	void msgHereIsBill (double payment){
		this.payment = payment;
		needPay = true;
	}
	void msgHereIsGoodAndChange(List<Item> orderFulfillment, double change){
		this. orderFulfillment = orderFulfillment;
		//Active = false;
	}

	public boolean pickAndExecuteAnAction(){
		if (needPay){
			payMarket();
			needPay = false;
			return true;
		}
		if(wantToBuy){
			buyFromMarket();
			wantToBuy = false;
			return true;
		}
		return false;
	}
	
	public void payMarket(){
		market.MarketCashier.msgPay(this, money);
		money = 0;
	}
	
	public void buyFromMarket(){
		market.MarketCashier.msgPlaceOrder(this, order);
	}
}
