package Event;

import Agent.Customer;
import Agent.Receptionist;
import Simulation.Salon;

public class EventEndPayment extends SimEvent {
    private Customer customer;
    private Receptionist receptionist;

    public EventEndPayment(Customer _customer, Receptionist _receptionist) {
        customer = _customer;
        receptionist = _receptionist;
    }

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        this.receptionist.setOccupied(false);
        this.receptionist.setTypeOfWork('x');
        this.receptionist.addWord(getTime() - this.receptionist.getStartWork());
        salon.updateReceptionist(receptionist);
        salon.getStatAverageTimeSalon().addStatistic(getTime() - this.customer.getStartTime());
        salon.getIntervalTimeInSalon().addValue(getTime() - this.customer.getStartTime());
        salon.addCustomer();
        this.customer.setStatus("Away");
        salon.updateCustomer(customer);

        salon.setOccupiedReceptionist(salon.getOccupiedReceptionist() - 1);
        if (salon.getQueuePayment().size() > 0) {
            EventStartPaying event = new EventStartPaying();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        } else if (salon.getQueueReception().size() > 0) {
            EventStartReception event = new EventStartReception();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        }
    }
}
