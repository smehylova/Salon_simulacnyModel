package Event;

import Agent.Customer;
import Agent.HairStylist;
import Agent.Receptionist;
import Simulation.Salon;

public class EventStartHair extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();
        if (salon.getQueueHair().size() > 0) {
            HairStylist actualHairStaylist = null;
            double actualOrders = Double.MAX_VALUE;
            for (int i = 0; i < salon.getHairStylists().size(); i++) {
                if (!salon.getHairStylists().get(i).isOccupied() &&
                        salon.getHairStylists().get(i).getWork() < actualOrders) {
                    actualHairStaylist = salon.getHairStylists().get(i);
                    actualOrders = actualHairStaylist.getWork();
                }
            }

            if (actualHairStaylist != null) {
                actualHairStaylist.setOccupied(true);
                salon.updateHairstylist(actualHairStaylist);
                actualHairStaylist.setStartWork(getTime());
                salon.setOccupiedHairStylists(salon.getOccupiedHairStylists() + 1);
                salon.getStatAverageQueueLengthHair().addValue(salon.getQueueHair().size() * (getTime() - salon.getLastTimeQueueLengthHair()), getTime() - salon.getLastTimeQueueLengthHair(), salon.getQueueHair().size() * (getTime() - salon.getLastTimeQueueLengthHair()) - 1);
                salon.setLastTimeQueueLengthHair(getTime());
                Customer customer = salon.getQueueHair().remove(0);
                salon.getStatAverageTimeQueueHair().addStatistic(getTime() - customer.getStartTimeQueue());

                EventEndHair event = new EventEndHair(customer, actualHairStaylist);
                event.setSimCore(getSimCore());
                double randHair = salon.getGeneratorHair().nextDouble();
                int duration;
                if (randHair < 0.4) {
                    duration = salon.getGeneratorEasyHair().nextValueInt() * 60;
                    customer.setStatus("Process of easy hair");
                    salon.updateCustomer(customer);
                } else if (randHair < 0.8) {
                    duration = salon.getGeneratorHardHair().nextValueInt() * 60;
                    customer.setStatus("Process of hard hair");
                    salon.updateCustomer(customer);
                } else {
                    duration = salon.getGeneratorWeddingHair().nextValueInt() * 60;
                    customer.setStatus("Process of wedding hair");
                    salon.updateCustomer(customer);
                }
                salon.getStatAverageHairDuration().addStatistic(duration);
                event.setTime(getTime() + duration);
                salon.getCalendar().add(event);

                if (salon.getQueueHair().size() > 0) {
                    EventStartHair event2 = new EventStartHair();
                    event2.setSimCore(getSimCore());
                    event2.setTime(getTime());
                    event2.execute();
                }
            }
        }
    }
}
