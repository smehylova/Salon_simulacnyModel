package Event;

import Simulation.Salon;

public class EvenWait extends SimEvent {
    @Override
    public void execute() {
        Salon salon = (Salon) getSimCore();
        if (!salon.isSpeeded()) {
            try {
                getSimCore().getGuiListener().refresh(getSimCore());
                Thread.sleep(getSimCore().getSpeed());
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (getSimCore().getCalendar().size() != 0) {
                EvenWait event = new EvenWait();
                event.setTime(getTime() + getSimCore().getSpeedTime());
                event.setSimCore(getSimCore());
                getSimCore().getCalendar().add(event);
            }
        }
    }
}
