package Event;

import Agent.Customer;
import Agent.Receptionist;
import Simulation.Salon;

public class EventStartReception extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();
        int countActualOrders = 0;
        for (int i = 0; i < salon.getReceptionists().size(); i++) {
            if (salon.getReceptionists().get(i).isOccupied() && salon.getReceptionists().get(i).getTypeOfWork() == 'r') {
                countActualOrders++;
            }
        }

        if (salon.getActualTime() > salon.getMaxTime()) {
            for (int i = 0; i < salon.getQueueReception().size(); i++) {
                salon.getQueueReception().get(i).setStatus("Removed!");
                salon.updateCustomer(salon.getQueueReception().get(i));
            }
            salon.getStatAverageQueueLengthReception().addValue(salon.getQueueReception().size() * (getTime() - salon.getLastTimeQueueLengthReception()), getTime() - salon.getLastTimeQueueLengthReception(), 0);
            salon.setLastTimeQueueLengthReception(getTime());
            salon.getQueueReception().clear();
        }

        if ((salon.getQueueHair().size() + salon.getQueueCosmetic().size() + countActualOrders) < 11 &&
                salon.getQueueReception().size() > 0) {
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
                actualReceptionist.setTypeOfWork('r');
                salon.updateReceptionist(actualReceptionist);
                actualReceptionist.setStartWork(getTime());
                salon.setOccupiedReceptionist(salon.getOccupiedReceptionist() + 1);
                salon.getStatAverageQueueLengthReception().addValue(salon.getQueueReception().size() * (getTime() - salon.getLastTimeQueueLengthReception()), getTime() - salon.getLastTimeQueueLengthReception(), (salon.getQueueReception().size() - 1) * (getTime() - salon.getLastTimeQueueLengthReception()));
                salon.setLastTimeQueueLengthReception(getTime());
                Customer customer = salon.getQueueReception().remove(0);
                salon.getStatAverageTimeQueueReception().addStatistic(getTime() - customer.getStartTimeQueue());

                EventEndReception event = new EventEndReception(customer, actualReceptionist);
                customer.setStatus("Process of reception");
                salon.updateCustomer(customer);
                event.setSimCore(getSimCore());
                double duration = salon.getGeneratorReception().nextValue();
                salon.getStatAverageReceptionDuration().addStatistic(duration);
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
}
