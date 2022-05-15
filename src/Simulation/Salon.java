package Simulation;

import Agent.Cosmetitian;
import Agent.Customer;
import Agent.HairStylist;
import Agent.Receptionist;
import Event.EvenWait;
import Event.EventArrival;
import Event.EventEndSalon;
import Generators.*;
import Statistics.StatAverage;
import Statistics.StatAverageWeighted;
import Statistics.StatInterval;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Random;

public class Salon extends SimCore {
    private int countReceptionist;
    private int countCosmetitians;
    private int countHairStylists;

    private int occupiedReceptionist;
    private int occupiedCosmetitians;
    private int occupiedHairStylists;

    private int countCustomers;
    private int arrivedCustomers;

    private LinkedList<Customer> queueReception = new LinkedList<>();
    private LinkedList<Customer> queueHair = new LinkedList<>();
    private LinkedList<Customer> queueCosmetic = new LinkedList<>();
    private LinkedList<Customer> queuePayment = new LinkedList<>();

    private ArrayList<Receptionist> receptionists;
    private ArrayList<HairStylist> hairStylists;
    private ArrayList<Cosmetitian> cosmetitians;

    private GeneratorExponential generatorArrival;
    private GeneratorContinuousUniform generatorReception;
    private GeneratorDiscreteUniform generatorEasyHair;
    private GeneratorEmpiric generatorHardHair;
    private GeneratorEmpiric generatorWeddingHair;
    private GeneratorDiscreteUniform generatorEasyCosmetic;
    private GeneratorDiscreteUniform generatorHardCosmetic;
    private GeneratorTriangular generatorCleaning;
    private GeneratorContinuousUniform generatorPaying;

    private Random generatorHairCosmetic;
    private Random generatorClean;
    private Random generatorHair;
    private Random generatorCosmetic;

    //STATISTIKY
    private StatAverage statAverageTimeSalon;

    private StatAverage statAverageTimeQueueReception;
    private StatAverage statAverageTimeQueueHair;
    private StatAverage statAverageTimeQueueCosmetic;
    private StatAverage statAverageTimeQueuePayment;

    private StatAverageWeighted statAverageQueueLengthReception;
    private StatAverageWeighted statAverageQueueLengthHair;
    private StatAverageWeighted statAverageQueueLengthCosmetic;
    private StatAverageWeighted statAverageQueueLengthPayment;

    private StatAverage statAverageReceptionDuration;
    private StatAverage statAverageHairDuration;
    private StatAverage statAverageCleaningDuration;
    private StatAverage statAverageConsmeticDuration;
    private StatAverage statAveragePayingDuration;

    private double lastTimeQueueLengthReception;
    private double lastTimeQueueLengthHair;
    private double lastTimeQueueLengthCosmetic;
    private double lastTimeQueueLengthPayment;

    //statistiky replikacii
    private StatAverage statReplicationsCountCustomers;
    private StatAverage statReplicationsTimeSalon;
    private StatAverage statReplicationsOvertime;

    private StatAverage statReplicationsTimeQueueReception;
    private StatAverage statReplicationsTimeQueueHair;
    private StatAverage statReplicationsTimeQueueCosmetic;
    private StatAverage statReplicationsTimeQueuePayment;

    private StatAverage statReplicationsLengthQueueReception;
    private StatAverage statReplicationsLengthQueueHair;
    private StatAverage statReplicationsLengthQueueCosmetic;
    private StatAverage statReplicationsLengthQueuePayment;

    private StatAverage statReplicationsReceptionDuration;
    private StatAverage statReplicationsHairDuration;
    private StatAverage statReplicationsCleaningDuration;
    private StatAverage statReplicationsCosmeticDuration;
    private StatAverage statReplicationsPayingDuration;

    private StatAverage statReplicationsAverageUtilizationReceptionist;
    private StatAverage statReplicationsAverageUtilizationHairstylist;
    private StatAverage statReplicationsAverageUtilizationCosmetitian;

    private StatInterval intervalTimeInSalon;
    private StatInterval intervalReplicationsTimeInSalon;

    //statistiky do 17
    private double statQueueLengthReception17;
    private double statQueueLengthHair17;
    private double statQueueLengthCosmetic17;
    private double statQueueLengthPayment17;

    private StatAverage statReplicationsQueueLengthReception17;
    private StatAverage statReplicationsQueueLengthHair17;
    private StatAverage statReplicationsQueueLengthCosmetic17;
    private StatAverage statReplicationsQueueLengthPayment17;

    public Salon(int _countReceptionist, int _countHairStylists, int _countCosmetitians, double maxTime, boolean speeded, int speed, int speedTime) {
        countCosmetitians = _countCosmetitians;
        countHairStylists = _countHairStylists;
        countReceptionist = _countReceptionist;
        setMaxTime(maxTime);
        setSpeeded(speeded);
        setSpeed(speed);
        setSpeedTime(speedTime);
    }

    @Override
    protected void beforeReplications() {
        //statistiky replikacii
        statReplicationsCountCustomers = new StatAverage();
        statReplicationsTimeSalon = new StatAverage();
        statReplicationsOvertime = new StatAverage();

        statReplicationsTimeQueueReception = new StatAverage();
        statReplicationsTimeQueueHair = new StatAverage();
        statReplicationsTimeQueueCosmetic = new StatAverage();
        statReplicationsTimeQueuePayment = new StatAverage();

        statReplicationsLengthQueueReception = new StatAverage();
        statReplicationsLengthQueueHair = new StatAverage();
        statReplicationsLengthQueueCosmetic = new StatAverage();
        statReplicationsLengthQueuePayment = new StatAverage();

        statReplicationsReceptionDuration = new StatAverage();
        statReplicationsHairDuration = new StatAverage();
        statReplicationsCleaningDuration = new StatAverage();
        statReplicationsCosmeticDuration = new StatAverage();
        statReplicationsPayingDuration = new StatAverage();

        statReplicationsAverageUtilizationReceptionist = new StatAverage();
        statReplicationsAverageUtilizationHairstylist = new StatAverage();
        statReplicationsAverageUtilizationCosmetitian = new StatAverage();

        intervalReplicationsTimeInSalon = new StatInterval(1.65);

        statReplicationsQueueLengthReception17 = new StatAverage();
        statReplicationsQueueLengthHair17 = new StatAverage();
        statReplicationsQueueLengthCosmetic17 = new StatAverage();
        statReplicationsQueueLengthPayment17 = new StatAverage();
    }

    @Override
    protected void beforeReplication() {
        occupiedReceptionist = 0;
        occupiedCosmetitians = 0;
        occupiedHairStylists = 0;

        countCustomers = 0;
        arrivedCustomers = 0;

        //aktualizacia radov
        queueReception = new LinkedList<>();
        queueHair = new LinkedList<>();
        queueCosmetic = new LinkedList<>();
        queuePayment = new LinkedList<>();

        //aktualizacia zamestnancov
        receptionists = new ArrayList<>(countReceptionist);
        for (int i = 0; i < countReceptionist; i++) {
            receptionists.add(new Receptionist(i));
        }
        hairStylists = new ArrayList<>(countHairStylists);
        for (int i = 0; i < countHairStylists; i++) {
            hairStylists.add(new HairStylist(i));
        }
        cosmetitians = new ArrayList<>(countCosmetitians);
        for (int i = 0; i < countCosmetitians; i++) {
            cosmetitians.add(new Cosmetitian(i));
        }

        //aktualizacia generatorov
        generatorArrival = new GeneratorExponential(1/(3600/8.0));

        generatorReception = new GeneratorContinuousUniform(80, 320);

        generatorHairCosmetic = new Random();

        generatorHair = new Random();
        generatorEasyHair = new GeneratorDiscreteUniform(10, 30);

        ArrayList<EmpiricParameter> arrayHardHair = new ArrayList<>();
        arrayHardHair.add(new EmpiricParameter(30, 60, 0.4));
        arrayHardHair.add(new EmpiricParameter(61, 120, 0.6));
        generatorHardHair = new GeneratorEmpiric(arrayHardHair);

        ArrayList<EmpiricParameter> arrayWeddingHair = new ArrayList<>();
        arrayWeddingHair.add(new EmpiricParameter(50, 60, 0.2));
        arrayWeddingHair.add(new EmpiricParameter(61, 100, 0.3));
        arrayWeddingHair.add(new EmpiricParameter(101, 150, 0.5));
        generatorWeddingHair = new GeneratorEmpiric(arrayWeddingHair);

        generatorClean = new Random();
        generatorCleaning = new GeneratorTriangular(360, 900, 540);

        generatorCosmetic = new Random();
        generatorEasyCosmetic = new GeneratorDiscreteUniform(10, 25);
        generatorHardCosmetic = new GeneratorDiscreteUniform(20, 100);

        generatorPaying = new GeneratorContinuousUniform(130, 230);

        //aktualizacia simulacie
        setCalendar(new PriorityQueue<>());

        //statistiky
        statAverageTimeSalon = new StatAverage();

        statAverageTimeQueueReception = new StatAverage();
        statAverageTimeQueueHair = new StatAverage();
        statAverageTimeQueueCosmetic = new StatAverage();
        statAverageTimeQueuePayment = new StatAverage();

        statAverageQueueLengthReception = new StatAverageWeighted();
        statAverageQueueLengthHair = new StatAverageWeighted();
        statAverageQueueLengthCosmetic = new StatAverageWeighted();
        statAverageQueueLengthPayment = new StatAverageWeighted();

        statAverageReceptionDuration = new StatAverage();
        statAverageHairDuration = new StatAverage();
        statAverageCleaningDuration = new StatAverage();
        statAverageConsmeticDuration = new StatAverage();
        statAveragePayingDuration = new StatAverage();

        intervalTimeInSalon = new StatInterval(1.65);

        lastTimeQueueLengthReception = 0;
        lastTimeQueueLengthHair = 0;
        lastTimeQueueLengthCosmetic = 0;
        lastTimeQueueLengthPayment = 0;


        //vytvorenie prveho eventu
        EventArrival event = new EventArrival();
        event.setTime(0);
        event.setSimCore(this);
        getCalendar().add(event);

        if (!isSpeeded()) {
            EvenWait eventWait = new EvenWait();
            eventWait.setTime(0);
            eventWait.setSimCore(this);
            getCalendar().add(eventWait);
        }

        EventEndSalon event2 = new EventEndSalon();
        event2.setTime(28800);
        event2.setSimCore(this);
        getCalendar().add(event2);

        setActualTime(0);
    }

    @Override
    protected void afterReplication() {
        statReplicationsCountCustomers.addStatistic(countCustomers);
        statReplicationsTimeSalon.addStatistic(statAverageTimeSalon.getResult());
        statReplicationsOvertime.addStatistic(getActualTime() - 28800);

        statReplicationsTimeQueueReception.addStatistic(statAverageTimeQueueReception.getResult());
        statReplicationsTimeQueueHair.addStatistic(statAverageTimeQueueHair.getResult());
        statReplicationsTimeQueueCosmetic.addStatistic(statAverageTimeQueueCosmetic.getResult());
        statReplicationsTimeQueuePayment.addStatistic(statAverageTimeQueuePayment.getResult());

        statReplicationsLengthQueueReception.addStatistic(statAverageQueueLengthReception.getResultTime(getActualTime()));
        statReplicationsLengthQueueHair.addStatistic(statAverageQueueLengthHair.getResultTime(getActualTime()));
        statReplicationsLengthQueueCosmetic.addStatistic(statAverageQueueLengthCosmetic.getResultTime(getActualTime()));
        statReplicationsLengthQueuePayment.addStatistic(statAverageQueueLengthPayment.getResultTime(getActualTime()));

        statReplicationsReceptionDuration.addStatistic(statAverageReceptionDuration.getResult());
        statReplicationsHairDuration.addStatistic(statAverageHairDuration.getResult());
        statReplicationsCleaningDuration.addStatistic(statAverageCleaningDuration.getResult());
        statReplicationsCosmeticDuration.addStatistic(statAverageConsmeticDuration.getResult());
        statReplicationsPayingDuration.addStatistic(statAveragePayingDuration.getResult());

        intervalReplicationsTimeInSalon.addValue(intervalTimeInSalon.getAverage());

        //utilizacia
        StatAverage average = new StatAverage();
        for (int i = 0; i < this.receptionists.size(); i++) {
            average.addStatistic (this.receptionists.get(i).getWork() / getActualTime() * 100);
        }
        statReplicationsAverageUtilizationReceptionist.addStatistic(average.getResult());

        average = new StatAverage();
        for (int i = 0; i < this.hairStylists.size(); i++) {
            average.addStatistic (this.hairStylists.get(i).getWork() / getActualTime() * 100);
        }
        statReplicationsAverageUtilizationHairstylist.addStatistic(average.getResult());

        average = new StatAverage();
        for (int i = 0; i < this.cosmetitians.size(); i++) {
            average.addStatistic (this.cosmetitians.get(i).getWork() / getActualTime() * 100);
        }
        statReplicationsAverageUtilizationCosmetitian.addStatistic(average.getResult());
    }

    @Override
    protected void afterReplications() {

    }

    public void createCustomer(Customer customer) {
        if (!isSpeeded()) {
            guiListener.createCustomer(customer);
        }
    }
    public void updateCustomer(Customer customer) {
        if (!isSpeeded()) {
            guiListener.updateCustomer(customer);
        }
    }
    public void updateHairstylist(HairStylist hairStylist) {
        if (!isSpeeded()) {
            guiListener.updateHairstylist(hairStylist);
        }
    }
    public void updateCosmetitian(Cosmetitian cosmetitian) {
        if (!isSpeeded()) {
            guiListener.updateCosmetitian(cosmetitian);
        }
    }
    public void updateReceptionist(Receptionist receptionist) {
        if (!isSpeeded()) {
            guiListener.updateReceptionist(receptionist);
        }
    }

    public LinkedList<Customer> getQueueReception() {
        return queueReception;
    }

    public LinkedList<Customer> getQueueHair() {
        return queueHair;
    }

    public LinkedList<Customer> getQueueCosmetic() {
        return queueCosmetic;
    }

    public LinkedList<Customer> getQueuePayment() {
        return queuePayment;
    }

    public ArrayList<Receptionist> getReceptionists() {
        return receptionists;
    }

    public ArrayList<HairStylist> getHairStylists() {
        return hairStylists;
    }

    public ArrayList<Cosmetitian> getCosmetitians() {
        return cosmetitians;
    }

    public int getCountCustomers() {
        return countCustomers;
    }

    public void addCustomer() {
        this.countCustomers++;
    }

    public GeneratorExponential getGeneratorArrival() {
        return generatorArrival;
    }

    public Random getGeneratorHairCosmetic() {
        return generatorHairCosmetic;
    }

    public Random getGeneratorClean() {
        return generatorClean;
    }

    public GeneratorContinuousUniform getGeneratorReception() {
        return generatorReception;
    }

    public Random getGeneratorHair() {
        return generatorHair;
    }

    public GeneratorDiscreteUniform getGeneratorEasyHair() {
        return generatorEasyHair;
    }

    public GeneratorEmpiric getGeneratorHardHair() {
        return generatorHardHair;
    }

    public GeneratorEmpiric getGeneratorWeddingHair() {
        return generatorWeddingHair;
    }

    public Random getGeneratorCosmetic() {
        return generatorCosmetic;
    }

    public GeneratorDiscreteUniform getGeneratorEasyCosmetic() {
        return generatorEasyCosmetic;
    }

    public GeneratorDiscreteUniform getGeneratorHardCosmetic() {
        return generatorHardCosmetic;
    }

    public GeneratorTriangular getGeneratorCleaning() {
        return generatorCleaning;
    }

    public GeneratorContinuousUniform getGeneratorPaying() {
        return generatorPaying;
    }

    public int getCountReceptionist() {
        return countReceptionist;
    }

    public void setCountReceptionist(int countReceptionist) {
        this.countReceptionist = countReceptionist;
    }

    public int getCountCosmetitians() {
        return countCosmetitians;
    }

    public void setCountCosmetitians(int countCosmetitians) {
        this.countCosmetitians = countCosmetitians;
    }

    public int getCountHairStylists() {
        return countHairStylists;
    }

    public void setCountHairStylists(int countHairStylists) {
        this.countHairStylists = countHairStylists;
    }

    public int getOccupiedReceptionist() {
        return occupiedReceptionist;
    }

    public void setOccupiedReceptionist(int occupiedReceptionist) {
        this.occupiedReceptionist = occupiedReceptionist;
    }

    public int getOccupiedCosmetitians() {
        return occupiedCosmetitians;
    }

    public void setOccupiedCosmetitians(int occupiedCosmetitians) {
        this.occupiedCosmetitians = occupiedCosmetitians;
    }

    public int getOccupiedHairStylists() {
        return occupiedHairStylists;
    }

    public void setOccupiedHairStylists(int occupiedHairStylists) {
        this.occupiedHairStylists = occupiedHairStylists;
    }

    public StatAverage getStatAverageTimeSalon() {
        return statAverageTimeSalon;
    }

    public StatAverage getStatAverageTimeQueueReception() {
        return statAverageTimeQueueReception;
    }

    public StatAverageWeighted getStatAverageQueueLengthReception() {
        return statAverageQueueLengthReception;
    }

    public double getLastTimeQueueLengthReception() {
        return lastTimeQueueLengthReception;
    }

    public void setLastTimeQueueLengthReception(double lastTimeQueueLengthReception) {
        this.lastTimeQueueLengthReception = lastTimeQueueLengthReception;
    }

    public StatAverage getStatAverageHairDuration() {
        return statAverageHairDuration;
    }

    public StatAverage getStatReplicationsCountCustomers() {
        return statReplicationsCountCustomers;
    }

    public StatAverage getStatReplicationsTimeSalon() {
        return statReplicationsTimeSalon;
    }

    public StatAverage getStatReplicationsTimeQueueReception() {
        return statReplicationsTimeQueueReception;
    }

    public StatAverage getStatReplicationsLengthQueueReception() {
        return statReplicationsLengthQueueReception;
    }

    public void setCountCustomers(int countCustomers) {
        this.countCustomers = countCustomers;
    }

    public void setQueueReception(LinkedList<Customer> queueReception) {
        this.queueReception = queueReception;
    }

    public void setQueueHair(LinkedList<Customer> queueHair) {
        this.queueHair = queueHair;
    }

    public void setQueueCosmetic(LinkedList<Customer> queueCosmetic) {
        this.queueCosmetic = queueCosmetic;
    }

    public void setQueuePayment(LinkedList<Customer> queuePayment) {
        this.queuePayment = queuePayment;
    }

    public void setReceptionists(ArrayList<Receptionist> receptionists) {
        this.receptionists = receptionists;
    }

    public void setHairStylists(ArrayList<HairStylist> hairStylists) {
        this.hairStylists = hairStylists;
    }

    public void setCosmetitians(ArrayList<Cosmetitian> cosmetitians) {
        this.cosmetitians = cosmetitians;
    }

    public void setGeneratorArrival(GeneratorExponential generatorArrival) {
        this.generatorArrival = generatorArrival;
    }

    public void setGeneratorReception(GeneratorContinuousUniform generatorReception) {
        this.generatorReception = generatorReception;
    }

    public void setGeneratorEasyHair(GeneratorDiscreteUniform generatorEasyHair) {
        this.generatorEasyHair = generatorEasyHair;
    }

    public void setGeneratorHardHair(GeneratorEmpiric generatorHardHair) {
        this.generatorHardHair = generatorHardHair;
    }

    public void setGeneratorWeddingHair(GeneratorEmpiric generatorWeddingHair) {
        this.generatorWeddingHair = generatorWeddingHair;
    }

    public void setGeneratorEasyCosmetic(GeneratorDiscreteUniform generatorEasyCosmetic) {
        this.generatorEasyCosmetic = generatorEasyCosmetic;
    }

    public void setGeneratorHardCosmetic(GeneratorDiscreteUniform generatorHardCosmetic) {
        this.generatorHardCosmetic = generatorHardCosmetic;
    }

    public void setGeneratorCleaning(GeneratorTriangular generatorCleaning) {
        this.generatorCleaning = generatorCleaning;
    }

    public void setGeneratorPaying(GeneratorContinuousUniform generatorPaying) {
        this.generatorPaying = generatorPaying;
    }

    public void setGeneratorHairCosmetic(Random generatorHairCosmetic) {
        this.generatorHairCosmetic = generatorHairCosmetic;
    }

    public void setGeneratorClean(Random generatorClean) {
        this.generatorClean = generatorClean;
    }

    public void setGeneratorHair(Random generatorHair) {
        this.generatorHair = generatorHair;
    }

    public void setGeneratorCosmetic(Random generatorCosmetic) {
        this.generatorCosmetic = generatorCosmetic;
    }

    public void setStatAverageTimeSalon(StatAverage statAverageTimeSalon) {
        this.statAverageTimeSalon = statAverageTimeSalon;
    }

    public void setStatAverageTimeQueueReception(StatAverage statAverageTimeQueueReception) {
        this.statAverageTimeQueueReception = statAverageTimeQueueReception;
    }

    public void setStatAverageQueueLengthReception(StatAverageWeighted statAverageQueueLengthReception) {
        this.statAverageQueueLengthReception = statAverageQueueLengthReception;
    }

    public StatAverage getStatAverageReceptionDuration() {
        return statAverageReceptionDuration;
    }

    public void setStatAverageReceptionDuration(StatAverage statAverageReceptionDuration) {
        this.statAverageReceptionDuration = statAverageReceptionDuration;
    }

    public void setStatAverageHairDuration(StatAverage statAverageHairDuration) {
        this.statAverageHairDuration = statAverageHairDuration;
    }

    public StatAverage getStatAverageCleaningDuration() {
        return statAverageCleaningDuration;
    }

    public void setStatAverageCleaningDuration(StatAverage statAverageCleaningDuration) {
        this.statAverageCleaningDuration = statAverageCleaningDuration;
    }

    public StatAverage getStatAverageConsmeticDuration() {
        return statAverageConsmeticDuration;
    }

    public void setStatAverageConsmeticDuration(StatAverage statAverageConsmeticDuration) {
        this.statAverageConsmeticDuration = statAverageConsmeticDuration;
    }

    public StatAverage getStatAveragePayingDuration() {
        return statAveragePayingDuration;
    }

    public void setStatAveragePayingDuration(StatAverage statAveragePayingDuration) {
        this.statAveragePayingDuration = statAveragePayingDuration;
    }

    public StatAverage getStatReplicationsReceptionDuration() {
        return statReplicationsReceptionDuration;
    }

    public void setStatReplicationsReceptionDuration(StatAverage statReplicationsReceptionDuration) {
        this.statReplicationsReceptionDuration = statReplicationsReceptionDuration;
    }

    public StatAverage getStatReplicationsHairDuration() {
        return statReplicationsHairDuration;
    }

    public void setStatReplicationsHairDuration(StatAverage statReplicationsHairDuration) {
        this.statReplicationsHairDuration = statReplicationsHairDuration;
    }

    public StatAverage getStatReplicationsCleaningDuration() {
        return statReplicationsCleaningDuration;
    }

    public void setStatReplicationsCleaningDuration(StatAverage statReplicationsCleaningDuration) {
        this.statReplicationsCleaningDuration = statReplicationsCleaningDuration;
    }

    public StatAverage getStatReplicationsCosmeticDuration() {
        return statReplicationsCosmeticDuration;
    }

    public void setStatReplicationsCosmeticDuration(StatAverage statReplicationsCosmeticDuration) {
        this.statReplicationsCosmeticDuration = statReplicationsCosmeticDuration;
    }

    public StatAverage getStatReplicationsPayingDuration() {
        return statReplicationsPayingDuration;
    }

    public void setStatReplicationsPayingDuration(StatAverage statReplicationsPayingDuration) {
        this.statReplicationsPayingDuration = statReplicationsPayingDuration;
    }

    public void setStatReplicationsCountCustomers(StatAverage statReplicationsCountCustomers) {
        this.statReplicationsCountCustomers = statReplicationsCountCustomers;
    }

    public void setStatReplicationsTimeSalon(StatAverage statReplicationsTimeSalon) {
        this.statReplicationsTimeSalon = statReplicationsTimeSalon;
    }

    public void setStatReplicationsTimeQueueReception(StatAverage statReplicationsTimeQueueReception) {
        this.statReplicationsTimeQueueReception = statReplicationsTimeQueueReception;
    }

    public void setStatReplicationsLengthQueueReception(StatAverage statReplicationsLengthQueueReception) {
        this.statReplicationsLengthQueueReception = statReplicationsLengthQueueReception;
    }

    public void addArrivedCustomer() {
        arrivedCustomers++;
    }

    public int getArrivedCustomers() {
        return arrivedCustomers;
    }

    public void setArrivedCustomers(int arrivedCustomers) {
        this.arrivedCustomers = arrivedCustomers;
    }

    public StatAverageWeighted getStatAverageQueueLengthHair() {
        return statAverageQueueLengthHair;
    }

    public void setStatAverageQueueLengthHair(StatAverageWeighted statAverageQueueLengthHair) {
        this.statAverageQueueLengthHair = statAverageQueueLengthHair;
    }

    public StatAverageWeighted getStatAverageQueueLengthCosmetic() {
        return statAverageQueueLengthCosmetic;
    }

    public void setStatAverageQueueLengthCosmetic(StatAverageWeighted statAverageQueueLengthCosmetic) {
        this.statAverageQueueLengthCosmetic = statAverageQueueLengthCosmetic;
    }

    public StatAverageWeighted getStatAverageQueueLengthPayment() {
        return statAverageQueueLengthPayment;
    }

    public void setStatAverageQueueLengthPayment(StatAverageWeighted statAverageQueueLengthPayment) {
        this.statAverageQueueLengthPayment = statAverageQueueLengthPayment;
    }

    public StatAverage getStatReplicationsLengthQueueHair() {
        return statReplicationsLengthQueueHair;
    }

    public void setStatReplicationsLengthQueueHair(StatAverage statReplicationsLengthQueueHair) {
        this.statReplicationsLengthQueueHair = statReplicationsLengthQueueHair;
    }

    public StatAverage getStatReplicationsLengthQueueCosmetic() {
        return statReplicationsLengthQueueCosmetic;
    }

    public void setStatReplicationsLengthQueueCosmetic(StatAverage statReplicationsLengthQueueCosmetic) {
        this.statReplicationsLengthQueueCosmetic = statReplicationsLengthQueueCosmetic;
    }

    public StatAverage getStatReplicationsLengthQueuePayment() {
        return statReplicationsLengthQueuePayment;
    }

    public void setStatReplicationsLengthQueuePayment(StatAverage statReplicationsLengthQueuePayment) {
        this.statReplicationsLengthQueuePayment = statReplicationsLengthQueuePayment;
    }

    public double getLastTimeQueueLengthHair() {
        return lastTimeQueueLengthHair;
    }

    public void setLastTimeQueueLengthHair(double lastTimeQueueLengthHair) {
        this.lastTimeQueueLengthHair = lastTimeQueueLengthHair;
    }

    public double getLastTimeQueueLengthCosmetic() {
        return lastTimeQueueLengthCosmetic;
    }

    public void setLastTimeQueueLengthCosmetic(double lastTimeQueueLengthCosmetic) {
        this.lastTimeQueueLengthCosmetic = lastTimeQueueLengthCosmetic;
    }

    public double getLastTimeQueueLengthPayment() {
        return lastTimeQueueLengthPayment;
    }

    public void setLastTimeQueueLengthPayment(double lastTimeQueueLengthPayment) {
        this.lastTimeQueueLengthPayment = lastTimeQueueLengthPayment;
    }

    public StatAverage getStatAverageTimeQueueHair() {
        return statAverageTimeQueueHair;
    }

    public void setStatAverageTimeQueueHair(StatAverage statAverageTimeQueueHair) {
        this.statAverageTimeQueueHair = statAverageTimeQueueHair;
    }

    public StatAverage getStatAverageTimeQueueCosmetic() {
        return statAverageTimeQueueCosmetic;
    }

    public void setStatAverageTimeQueueCosmetic(StatAverage statAverageTimeQueueCosmetic) {
        this.statAverageTimeQueueCosmetic = statAverageTimeQueueCosmetic;
    }

    public StatAverage getStatAverageTimeQueuePayment() {
        return statAverageTimeQueuePayment;
    }

    public void setStatAverageTimeQueuePayment(StatAverage statAverageTimeQueuePayment) {
        this.statAverageTimeQueuePayment = statAverageTimeQueuePayment;
    }

    public StatAverage getStatReplicationsTimeQueueHair() {
        return statReplicationsTimeQueueHair;
    }

    public void setStatReplicationsTimeQueueHair(StatAverage statReplicationsTimeQueueHair) {
        this.statReplicationsTimeQueueHair = statReplicationsTimeQueueHair;
    }

    public StatAverage getStatReplicationsTimeQueueCosmetic() {
        return statReplicationsTimeQueueCosmetic;
    }

    public void setStatReplicationsTimeQueueCosmetic(StatAverage statReplicationsTimeQueueCosmetic) {
        this.statReplicationsTimeQueueCosmetic = statReplicationsTimeQueueCosmetic;
    }

    public StatAverage getStatReplicationsTimeQueuePayment() {
        return statReplicationsTimeQueuePayment;
    }

    public void setStatReplicationsTimeQueuePayment(StatAverage statReplicationsTimeQueuePayment) {
        this.statReplicationsTimeQueuePayment = statReplicationsTimeQueuePayment;
    }

    public StatAverage getStatReplicationsOvertime() {
        return statReplicationsOvertime;
    }

    public void setStatReplicationsOvertime(StatAverage statReplicationsOvertime) {
        this.statReplicationsOvertime = statReplicationsOvertime;
    }

    public StatInterval getIntervalTimeInSalon() {
        return intervalTimeInSalon;
    }

    public void setIntervalTimeInSalon(StatInterval intervalTimeInSalon) {
        this.intervalTimeInSalon = intervalTimeInSalon;
    }

    public StatInterval getIntervalReplicationsTimeInSalon() {
        return intervalReplicationsTimeInSalon;
    }

    public void setIntervalReplicationsTimeInSalon(StatInterval intervalReplicationsTimeInSalon) {
        this.intervalReplicationsTimeInSalon = intervalReplicationsTimeInSalon;
    }

    public double getStatQueueLengthReception17() {
        return statQueueLengthReception17;
    }

    public void setStatQueueLengthReception17(double statQueueLengthReception17) {
        this.statQueueLengthReception17 = statQueueLengthReception17;
    }

    public double getStatQueueLengthHair17() {
        return statQueueLengthHair17;
    }

    public void setStatQueueLengthHair17(double statQueueLengthHair17) {
        this.statQueueLengthHair17 = statQueueLengthHair17;
    }

    public double getStatQueueLengthCosmetic17() {
        return statQueueLengthCosmetic17;
    }

    public void setStatQueueLengthCosmetic17(double statQueueLengthCosmetic17) {
        this.statQueueLengthCosmetic17 = statQueueLengthCosmetic17;
    }

    public double getStatQueueLengthPayment17() {
        return statQueueLengthPayment17;
    }

    public void setStatQueueLengthPayment17(double statQueueLengthPayment17) {
        this.statQueueLengthPayment17 = statQueueLengthPayment17;
    }

    public StatAverage getStatReplicationsQueueLengthReception17() {
        return statReplicationsQueueLengthReception17;
    }

    public void setStatReplicationsQueueLengthReception17(StatAverage statReplicationsQueueLengthReception17) {
        this.statReplicationsQueueLengthReception17 = statReplicationsQueueLengthReception17;
    }

    public StatAverage getStatReplicationsQueueLengthHair17() {
        return statReplicationsQueueLengthHair17;
    }

    public void setStatReplicationsQueueLengthHair17(StatAverage statReplicationsQueueLengthHair17) {
        this.statReplicationsQueueLengthHair17 = statReplicationsQueueLengthHair17;
    }

    public StatAverage getStatReplicationsQueueLengthCosmetic17() {
        return statReplicationsQueueLengthCosmetic17;
    }

    public void setStatReplicationsQueueLengthCosmetic17(StatAverage statReplicationsQueueLengthCosmetic17) {
        this.statReplicationsQueueLengthCosmetic17 = statReplicationsQueueLengthCosmetic17;
    }

    public StatAverage getStatReplicationsQueueLengthPayment17() {
        return statReplicationsQueueLengthPayment17;
    }

    public void setStatReplicationsQueueLengthPayment17(StatAverage statReplicationsQueueLengthPayment17) {
        this.statReplicationsQueueLengthPayment17 = statReplicationsQueueLengthPayment17;
    }

    public StatAverage getStatReplicationsAverageUtilizationReceptionist() {
        return statReplicationsAverageUtilizationReceptionist;
    }

    public void setStatReplicationsAverageUtilizationReceptionist(StatAverage statReplicationsAverageUtilizationReceptionist) {
        this.statReplicationsAverageUtilizationReceptionist = statReplicationsAverageUtilizationReceptionist;
    }

    public StatAverage getStatReplicationsAverageUtilizationHairstylist() {
        return statReplicationsAverageUtilizationHairstylist;
    }

    public void setStatReplicationsAverageUtilizationHairstylist(StatAverage statReplicationsAverageUtilizationHairstylist) {
        this.statReplicationsAverageUtilizationHairstylist = statReplicationsAverageUtilizationHairstylist;
    }

    public StatAverage getStatReplicationsAverageUtilizationCosmetitian() {
        return statReplicationsAverageUtilizationCosmetitian;
    }

    public void setStatReplicationsAverageUtilizationCosmetitian(StatAverage statReplicationsAverageUtilizationCosmetitian) {
        this.statReplicationsAverageUtilizationCosmetitian = statReplicationsAverageUtilizationCosmetitian;
    }
}
