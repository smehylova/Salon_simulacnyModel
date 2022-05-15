package Event;

import Agent.Customer;
import Agent.HairStylist;
import Simulation.Salon;

public class EventEndHair extends SimEvent {
    private Customer customer;
    private HairStylist hairStylist;

    public EventEndHair(Customer _customer, HairStylist _hairStylist) {
        customer = _customer;
        hairStylist = _hairStylist;
    }

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        this.customer.setHair(false);
        this.hairStylist.setOccupied(false);
        this.hairStylist.addWord(getTime() - this.hairStylist.getStartWork());
        salon.updateHairstylist(hairStylist);
        salon.setOccupiedHairStylists(salon.getOccupiedHairStylists() - 1);

        if (customer.isConsmetic()) {
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
        } else {
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

        if (salon.getQueueHair().size() > 0) {
            EventStartHair event = new EventStartHair();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        }
    }
}
