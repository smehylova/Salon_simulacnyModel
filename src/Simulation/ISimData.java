package Simulation;

import Agent.Cosmetitian;
import Agent.Customer;
import Agent.HairStylist;
import Agent.Receptionist;

public interface ISimData {
    public void refresh(SimCore simulation);
    public void createCustomer(Customer customer);
    public void updateCustomer(Customer customer);
    public void updateReceptionist(Receptionist receptionist);
    public void updateHairstylist(HairStylist hairStylist);
    public void updateCosmetitian(Cosmetitian cosmetitian);
    public void newSimulation();
}

