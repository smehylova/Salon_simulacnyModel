package Event;

import Agent.Cosmetitian;
import Agent.Customer;
import Simulation.Salon;

public class EventEndCleaning extends SimEvent {
    private Customer customer;
    private Cosmetitian cosmetitian;

    public EventEndCleaning(Customer _customer, Cosmetitian _cosmetitian) {
        customer = _customer;
        cosmetitian = _cosmetitian;
    }

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        this.customer.setClean(false);
        this.cosmetitian.setOccupied(false);
        this.cosmetitian.addWord(getTime() - this.cosmetitian.getStartWork());
        salon.updateCosmetitian(cosmetitian);
        salon.setOccupiedCosmetitians(salon.getOccupiedCosmetitians() - 1);
        if (salon.getQueueCosmetic().size() > 0) {
            EventStartHair event = new EventStartHair();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        }

        salon.getStatAverageQueueLengthCosmetic().addValue(salon.getQueueCosmetic().size() * (getTime() - salon.getLastTimeQueueLengthCosmetic()), getTime() - salon.getLastTimeQueueLengthCosmetic(), salon.getQueueCosmetic().size() * (getTime() - salon.getLastTimeQueueLengthCosmetic()) + 1);
        salon.setLastTimeQueueLengthCosmetic(getTime());
        this.customer.setStartTimeQueue(getTime());
        salon.getQueueCosmetic().add(customer);
        customer.setStatus("In queue (cosmetic)");
        salon.updateCustomer(customer);
        EventStartCosmetic event = new EventStartCosmetic();
        event.setSimCore(getSimCore());
        event.setTime(getTime());
        event.execute();
    }
}
