package Event;

import Agent.Cosmetitian;
import Agent.Customer;
import Agent.HairStylist;
import Simulation.Salon;

public class EventStartCosmetic extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        Cosmetitian actualCosmetitian = null;
        double actualOrders = Double.MAX_VALUE;
        for (int i = 0; i < salon.getCosmetitians().size(); i++) {
            if (!salon.getCosmetitians().get(i).isOccupied() &&
                    salon.getCosmetitians().get(i).getWork() < actualOrders) {
                actualCosmetitian = salon.getCosmetitians().get(i);
                actualOrders = actualCosmetitian.getWork();
            }
        }

        if (actualCosmetitian != null) {
            actualCosmetitian.setOccupied(true);
            salon.updateCosmetitian(actualCosmetitian);
            actualCosmetitian.setStartWork(getTime());
            salon.setOccupiedCosmetitians(salon.getOccupiedCosmetitians() + 1);
            salon.getStatAverageQueueLengthCosmetic().addValue(salon.getQueueCosmetic().size() * (getTime() - salon.getLastTimeQueueLengthCosmetic()), getTime() - salon.getLastTimeQueueLengthCosmetic(), salon.getQueueCosmetic().size() * (getTime() - salon.getLastTimeQueueLengthCosmetic()) - 1);
            salon.setLastTimeQueueLengthCosmetic(getTime());
            Customer customer = salon.getQueueCosmetic().remove(0);
            salon.getStatAverageTimeQueueCosmetic().addStatistic(getTime() - customer.getStartTimeQueue());

            if (customer.isClean()) {
                EventEndCleaning event = new EventEndCleaning(customer, actualCosmetitian);
                customer.setStatus("Process of cleaning");
                salon.updateCustomer(customer);
                event.setSimCore(getSimCore());
                double duration = salon.getGeneratorCleaning().nextValue();
                salon.getStatAverageCleaningDuration().addStatistic(duration);
                event.setTime(getTime() + duration);
                salon.getCalendar().add(event);
            } else {
                EventEndCosmetic event = new EventEndCosmetic(customer, actualCosmetitian);
                event.setSimCore(getSimCore());
                double randCosmetic = salon.getGeneratorCosmetic().nextDouble();
                int duration;
                if (randCosmetic < 0.3) {
                    duration = salon.getGeneratorEasyCosmetic().nextValueInt() * 60;
                    customer.setStatus("Process of easy cosmetic");
                    salon.updateCustomer(customer);
                } else {
                    duration = salon.getGeneratorHardCosmetic().nextValueInt() * 60;
                    customer.setStatus("Process of hard cosmetic");
                    salon.updateCustomer(customer);
                }
                salon.getStatAverageConsmeticDuration().addStatistic(duration);
                event.setTime(getTime() + duration);
                salon.getCalendar().add(event);
            }

            if (salon.getQueueCosmetic().size() > 0) {
                EventStartCosmetic event = new EventStartCosmetic();
                event.setSimCore(getSimCore());
                event.setTime(getTime());
                event.execute();
            }
        }

    }
}
