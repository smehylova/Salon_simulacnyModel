package Event;

import Simulation.Salon;

public class EventEndSalon extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();
        salon.getStatReplicationsQueueLengthReception17().addStatistic(salon.getStatAverageQueueLengthReception().getResultTime(getTime()));
        salon.getStatReplicationsQueueLengthHair17().addStatistic(salon.getStatAverageQueueLengthHair().getResultTime(getTime()));
        salon.getStatReplicationsQueueLengthCosmetic17().addStatistic(salon.getStatAverageQueueLengthCosmetic().getResultTime(getTime()));
        salon.getStatReplicationsQueueLengthPayment17().addStatistic(salon.getStatAverageQueueLengthPayment().getResultTime(getTime()));

        //System.out.println(salon.getStatAverageQueueLengthReception().getResultTime(getTime()));
        //System.out.println("b" + salon.getStatAverageQueueLengthReception().getResult());
    }
}
