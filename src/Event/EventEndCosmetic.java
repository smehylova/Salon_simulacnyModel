package Event;

import Agent.Cosmetitian;
import Agent.Customer;
import Simulation.Salon;

public class EventEndCosmetic extends SimEvent {
    private Customer customer;
    private Cosmetitian cosmetitian;

    public EventEndCosmetic(Customer _customer, Cosmetitian _cosmetitian) {
        customer = _customer;
        cosmetitian = _cosmetitian;
    }

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        this.customer.setConsmetic(false);
        this.cosmetitian.setOccupied(false);
        this.cosmetitian.addWord(getTime() - this.cosmetitian.getStartWork());
        salon.updateCosmetitian(cosmetitian);
        salon.setOccupiedCosmetitians(salon.getOccupiedCosmetitians() - 1);
        if (salon.getQueueCosmetic().size() > 0) {
            EventStartCosmetic event = new EventStartCosmetic();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        }

        salon.getStatAverageQueueLengthPayment().addValue(salon.getQueuePayment().size() * (getTime() - salon.getLastTimeQueueLengthPayment()), getTime() - salon.getLastTimeQueueLengthPayment(), salon.getQueuePayment().size() * (getTime() - salon.getLastTimeQueueLengthPayment()) + 1);
        salon.setLastTimeQueueLengthPayment(getTime());
        this.customer.setStartTimeQueue(getTime());
        salon.getQueuePayment().add(customer);
        customer.setStatus("In queue (payment)");
        salon.updateCustomer(customer);
        EventStartPaying event = new EventStartPaying();
        event.setSimCore(getSimCore());
        event.setTime(getTime());
        event.execute();
    }
}
