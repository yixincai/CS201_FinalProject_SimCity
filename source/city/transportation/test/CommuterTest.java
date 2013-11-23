package city.transportation.test;

import city.transportation.CommuterRole;
import city.transportation.mock.MockBus;
import junit.framework.TestCase;

public class CommuterTest extends TestCase{
	
	CommuterRole commuter;
	MockBus mockBus;
	
	public void setUp() throws Exception{
		super.setUp();
		mockBus = new MockBus("MockBus");
	}
	
	public void testZeroNormalCashierScenario(){
		
	}
}
