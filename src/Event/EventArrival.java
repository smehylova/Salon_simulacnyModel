package Event;

import Agent.Customer;
import Simulation.Salon;

public class EventArrival extends SimEvent {

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();
        Customer customer = new Customer(salon.getArrivedCustomers(), "In queue (reception)", getTime(), getTime());
        salon.createCustomer(customer);
        salon.addArrivedCustomer();
        getSimCore().setActualTime(getTime());

        salon.getStatAverageQueueLengthReception().addValue(salon.getQueueReception().size() * (getTime() - salon.getLastTimeQueueLengthReception()), getTime() - salon.getLastTimeQueueLengthReception(), salon.getQueueReception().size() * (getTime() - salon.getLastTimeQueueLengthReception()) + 1);
        salon.setLastTimeQueueLengthReception(getTime());
        salon.getQueueReception().add(customer);

        for (int i = 0; i < salon.getReceptionists().size(); i++) {
            if (!salon.getReceptionists().get(i).isOccupied()) {
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

        double newTime = (getTime() + salon.getGeneratorArrival().nextValue());
        if (newTime < salon.getMaxTime()) {
            EventArrival event = new EventArrival();
            event.setSimCore(getSimCore());
            event.setTime(newTime);
            getSimCore().getCalendar().add(event);
        }
    }
}
