package Event;

import Agent.Customer;
import Agent.Receptionist;
import Simulation.Salon;

public class EventEndReception extends SimEvent {
    private Customer customer;
    private Receptionist receptionist;

    public EventEndReception(Customer _customer, Receptionist _receptionist) {
        this.customer = _customer;
        this.receptionist = _receptionist;
    }

    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();

        double randHairCosmetic = salon.getGeneratorHairCosmetic().nextDouble();
        if (randHairCosmetic < 0.2) {
            customer.setHair(true);
        } else {
            if (randHairCosmetic < 0.35) {
                customer.setConsmetic(true);
            } else {
                customer.setHair(true);
                customer.setConsmetic(true);
            }
            double randClean = salon.getGeneratorClean().nextDouble();
            if (randClean < 0.35) {
                customer.setClean(true);
            }
        }

        this.receptionist.setOccupied(false);
        this.receptionist.setTypeOfWork('x');
        this.receptionist.addWord(getTime() - this.receptionist.getStartWork());
        salon.updateReceptionist(receptionist);
        salon.setOccupiedReceptionist(salon.getOccupiedReceptionist() - 1);

        if (customer.isHair()) {
            salon.getStatAverageQueueLengthHair().addValue(salon.getQueueHair().size() * (getTime() - salon.getLastTimeQueueLengthHair()), getTime() - salon.getLastTimeQueueLengthHair(), salon.getQueueHair().size() * (getTime() - salon.getLastTimeQueueLengthHair()) + 1);
            salon.setLastTimeQueueLengthHair(getTime());
            this.customer.setStartTimeQueue(getTime());
            salon.getQueueHair().add(customer);
            customer.setStatus("In queue (hair)");
            salon.updateCustomer(customer);
            EventStartHair event = new EventStartHair();
            event.setSimCore(getSimCore());
            event.setTime(getTime());
            event.execute();
        } else {
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
