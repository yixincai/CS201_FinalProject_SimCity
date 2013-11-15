package agent;

public class PersonAgent extends Agent {

	@Override
	protected boolean pickAndExecuteAnAction() {
		return false;
	}
	
// ACTIONS
	
	/*
	private void actGoToRestaurant()
	{
		Restaurant r = chooseRestaurant();
		getToRestaurant(r);
		Role r = r.createNewCustRole();
		r.active = true;
		roles.add(r);
	}*/
}
