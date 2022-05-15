package Event;

import Agent.Customer;
import Agent.Receptionist;
import Simulation.Salon;

public class EventStartPaying extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        Receptionist actualReceptionist = null;
        double actualOrders = Double.MAX_VALUE;
        for (int i = 0; i < salon.getReceptionists().size(); i++) {
            if (!salon.getReceptionists().get(i).isOccupied() &&
                    salon.getReceptionists().get(i).getWork() < actualOrders) {
                actualReceptionist = salon.getReceptionists().get(i);
                actualOrders = actualReceptionist.getWork();
            }
        }

        if (actualReceptionist != null) {
            actualReceptionist.setOccupied(true);
            actualReceptionist.setTypeOfWork('p');
            salon.updateReceptionist(actualReceptionist);
            actualReceptionist.setStartWork(getTime());
            salon.setOccupiedReceptionist(salon.getOccupiedReceptionist() + 1);
            salon.getStatAverageQueueLengthPayment().addValue(salon.getQueuePayment().size() * (getTime() - salon.getLastTimeQueueLengthPayment()), getTime() - salon.getLastTimeQueueLengthPayment(), salon.getQueuePayment().size() * (getTime() - salon.getLastTimeQueueLengthPayment()) - 1);
            salon.setLastTimeQueueLengthPayment(getTime());
            Customer customer = salon.getQueuePayment().remove(0);
            salon.getStatAverageTimeQueuePayment().addStatistic(getTime() - customer.getStartTimeQueue());

            EventEndPayment event = new EventEndPayment(customer, actualReceptionist);
            customer.setStatus("Process of payment");
            salon.updateCustomer(customer);
            event.setSimCore(getSimCore());
            double duration = salon.getGeneratorPaying().nextValue();
            salon.getStatAveragePayingDuration().addStatistic(duration);
            event.setTime(getTime() + duration);
            salon.getCalendar().add(event);

            if (salon.getQueuePayment().size() > 0) {
                EventStartPaying event2 = new EventStartPaying();
                event2.setSimCore(getSimCore());
                event2.setTime(getTime());
                event2.execute();
            } else if (salon.getQueueReception().size() > 0) {
                EventStartReception event2 = new EventStartReception();
                event2.setSimCore(getSimCore());
                event2.setTime(getTime());
                event2.execute();
            }
        }
    }
}
