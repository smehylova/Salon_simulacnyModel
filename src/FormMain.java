import Agent.Cosmetitian;
import Agent.Customer;
import Agent.HairStylist;
import Agent.Receptionist;
import Simulation.ISimData;
import Simulation.Salon;
import Simulation.SimCore;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class FormMain extends JFrame implements ISimData {
    private JLabel lCountReceptionists;
    private JLabel lCountHairStylists;
    private JLabel lCountCosmetitians;
    private JSpinner sCountReceptionists;
    private JSpinner sCountHairStylists;
    private JSpinner sCountCosmetitians;
    private JButton btnStartSimulation;
    private JTabbedPane tabbedPane1;
    private JLabel lQueueReception;
    private JLabel lQueueHair;
    private JLabel lQueueCosmetic;
    private JLabel lQueuePay;
    private JLabel lCountOccupiedR;
    private JLabel lCountOccupiedH;
    private JLabel lCountOccupiedC;
    private JPanel MainPanel;
    private JLabel lSimTime;
    private JLabel lAverageTimeInSalon;
    private JButton btnStopSimulation;
    private JLabel lAverageTimeQueueReception;
    private JLabel lCountCustomers;
    private JTextArea taMessages;
    private JSlider sliderSpeed;
    private JLabel lLengthQueueR;
    private JCheckBox cbSpeed;
    private JSpinner sReplications;
    private JLabel lReplicationCountCustomers;
    private JLabel lReplicationsTimeSalon;
    private JLabel lReplicationsTimeQueueReception;
    private JLabel lReplicationsLengthQueueReception;
    private JLabel lReplicationDurationReception;
    private JLabel lReplicationDurationHair;
    private JLabel lReplicationDurationClean;
    private JLabel lReplicationDurationCosmetic;
    private JLabel lReplicationDurationPayment;
    private JTable tableCustomers;
    private JLabel lActualReplication;
    private JLabel lReplicationsLengthQueueHair;
    private JLabel lReplicationsLengthQueueCosmetic;
    private JLabel lReplicationsLengthQueuePayment;
    private JButton btnPause;
    private JButton btnContinue;
    private JTable tableReceptionist;
    private JTable tableCosmetitian;
    private JTable tableHairStylist;
    private JLabel lAverageTimeQueueHair;
    private JLabel lAverageTimeQueueCosmetic;
    private JLabel lAverageTimeQueuePayment;
    private JLabel lLengthQueueH;
    private JLabel lLengthQueueC;
    private JLabel lLengthQueueP;
    private JLabel lReplicationsTimeQueueHair;
    private JLabel lReplicationsTimeQueueCosmetic;
    private JLabel lReplicationsTimeQueuePayment;
    private JLabel lReplicationOvertime;
    private JLabel lReplicationIntervalTimeSalon;
    private JPanel graphPanel;
    private JButton vykreslenieGrafuButton;
    private JLabel lReplicationLengthQueueReception17;
    private JLabel lReplicationLengthQueueHair17;
    private JLabel lReplicationLengthQueueCosmetic17;
    private JLabel lReplicationLengthQueuePayment17;
    private JLabel lReplicationUtilizationR;
    private JLabel lReplicationUtilizationH;
    private JLabel lReplicationUtilizationC;

    private Salon salon;
    private ISimData self;
    DefaultTableModel modelTableCustomers;
    DefaultTableModel modelTableReceptionists;
    DefaultTableModel modelTableHairstylists;
    DefaultTableModel modelTableCosmetitians;

    //GRAF
    private static JFreeChart chart;
    private static final XYSeriesCollection dataset = new XYSeriesCollection();
    private static final XYSeries series = new XYSeries("Strategy");


    public FormMain() {
        self = this;
        drawGraph();
        //drawHistogram();
        setContentPane(MainPanel);
        setTitle("Salon");
        setSize(800, 600);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);

        //nastavenie prostredia
        sReplications.setValue(100000);
        sCountReceptionists.setValue(5);
        sCountHairStylists.setValue(5);
        sCountCosmetitians.setValue(5);

        //zakaznici
        createTables();

        btnStartSimulation.setEnabled(true);
        btnStopSimulation.setEnabled(false);
        btnPause.setEnabled(false);
        btnContinue.setEnabled(false);


        btnStartSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Thread thread = new Thread(){
                    public void run(){
                        btnStartSimulation.setEnabled(false);
                        btnStopSimulation.setEnabled(true);
                        btnPause.setEnabled(true);
                        btnContinue.setEnabled(false);

                        if (cbSpeed.isSelected()) {
                            cbSpeed.setEnabled(false);
                        }

                        //spustenie simulacie
                        createWorkers();

                        salon = new Salon((int) sCountReceptionists.getValue(), (int) sCountHairStylists.getValue(), (int) sCountCosmetitians.getValue(), 8*60*60, cbSpeed.isSelected(), sliderSpeed.getValue(), 15*60);
                        salon.addGuiListener(self);
                        salon.simulate((int) sReplications.getValue());

                        btnStartSimulation.setEnabled(true);
                        btnStopSimulation.setEnabled(false);
                        btnPause.setEnabled(false);
                        btnContinue.setEnabled(false);

                        cbSpeed.setEnabled(true);
                    }
                };

                thread.start();
            }
        });
        btnStopSimulation.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salon.stopSimulation();
                btnStartSimulation.setEnabled(true);
                btnStopSimulation.setEnabled(false);
                btnPause.setEnabled(false);
                btnContinue.setEnabled(false);

                cbSpeed.setEnabled(true);
            }
        });
        sliderSpeed.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (salon != null) {
                    salon.setSpeed(sliderSpeed.getValue());
                }
            }
        });
        cbSpeed.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (salon != null) {
                    salon.setSpeeded(cbSpeed.isSelected());
                    if (cbSpeed.isSelected() && salon.isRunning()) {
                        cbSpeed.setEnabled(false);
                    }
                }
            }
        });
        btnPause.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salon.pauseSimulation();
                btnStartSimulation.setEnabled(false);
                btnStopSimulation.setEnabled(true);
                btnPause.setEnabled(false);
                btnContinue.setEnabled(true);
            }
        });
        btnContinue.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salon.continueSimulation();
                btnStartSimulation.setEnabled(false);
                btnStopSimulation.setEnabled(true);
                btnPause.setEnabled(true);
                btnContinue.setEnabled(false);
            }
        });
        vykreslenieGrafuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                series.clear();

                //spustenie simulacie
                Thread thread = new Thread(){
                    public void run(){
                        for (int i = 1; i <= 10; i++) {
                            int countHairStylists = i;
                            //spustenie simulacie
                            salon = new Salon((int) sCountReceptionists.getValue(), countHairStylists, (int) sCountCosmetitians.getValue(), 8*60*60, true, sliderSpeed.getValue(), 15*60);
                            salon.addGuiListener(self);
                            salon.simulate(10000);
                            series.add(countHairStylists, salon.getStatReplicationsQueueLengthReception17().getResult());

                        }
                    }
                };
                thread.start();

            }
        });
    }

    private void createTables() {
        modelTableCustomers = new DefaultTableModel();
        modelTableCustomers.addColumn("ID");
        modelTableCustomers.addColumn("Čas príchodu");
        modelTableCustomers.addColumn("Status");
        tableCustomers.setModel(modelTableCustomers);

        modelTableReceptionists = new DefaultTableModel();
        modelTableReceptionists.addColumn("ID");
        modelTableReceptionists.addColumn("Obsadenosť");
        modelTableReceptionists.addColumn("Utilizácia");
        tableReceptionist.setModel(modelTableReceptionists);

        modelTableHairstylists = new DefaultTableModel();
        modelTableHairstylists.addColumn("ID");
        modelTableHairstylists.addColumn("Obsadenosť");
        modelTableHairstylists.addColumn("Utilizácia");
        tableHairStylist.setModel(modelTableHairstylists);

        modelTableCosmetitians = new DefaultTableModel();
        modelTableCosmetitians.addColumn("ID");
        modelTableCosmetitians.addColumn("Obsadenosť");
        modelTableCosmetitians.addColumn("Utilizácia");
        tableCosmetitian.setModel(modelTableCosmetitians);
    }

    public void createWorkers() {
        modelTableReceptionists.setRowCount(0);
        modelTableHairstylists.setRowCount(0);
        modelTableCosmetitians.setRowCount(0);

        for (int i = 0; i < (int) sCountReceptionists.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableReceptionists.addRow(data);
        }

        for (int i = 0; i < (int) sCountHairStylists.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableHairstylists.addRow(data);
        }

        for (int i = 0; i < (int) sCountCosmetitians.getValue(); i++) {
            Object[] data = new Object[3];
            data[0] = i;
            data[1] = false;
            data[2] = 0;
            modelTableCosmetitians.addRow(data);
        }
    }

    @Override
    public void refresh(SimCore simulation) {
        Salon salon = (Salon) simulation;

        if (!salon.isSpeeded()) {

            lQueueReception.setText(String.valueOf(salon.getQueueReception().size()));
            lQueueHair.setText(String.valueOf(salon.getQueueHair().size()));
            lQueueCosmetic.setText(String.valueOf(salon.getQueueCosmetic().size()));
            lQueuePay.setText(String.valueOf(salon.getQueuePayment().size()));
            lCountOccupiedR.setText(String.valueOf(salon.getOccupiedReceptionist()));
            lCountOccupiedH.setText(String.valueOf(salon.getOccupiedHairStylists()));
            lCountOccupiedC.setText(String.valueOf(salon.getOccupiedCosmetitians()));

            //simulacny cas
            /*double second = time % 60;
            int time = (int) time / 60;
            int minute = time % 60;
            int hour = time / 60;*/

            double time = salon.getActualTime();
            double seconds = time % 60;
            int pomTime = (int) time / 60;
            int minutes = pomTime % 60;
            pomTime = pomTime / 60;
            int hour = (9 + pomTime) % 24;
            lSimTime.setText(hour + ":" + minutes + ":" + seconds);

            lAverageTimeInSalon.setText(toTimeFormat(salon.getStatAverageTimeSalon().getResult()));
            lCountCustomers.setText(String.valueOf(salon.getCountCustomers()));

            lAverageTimeQueueReception.setText(toTimeFormat(salon.getStatAverageTimeQueueReception().getResult()));
            lAverageTimeQueueHair.setText(toTimeFormat(salon.getStatAverageTimeQueueHair().getResult()));
            lAverageTimeQueueCosmetic.setText(toTimeFormat(salon.getStatAverageTimeQueueCosmetic().getResult()));
            lAverageTimeQueuePayment.setText(toTimeFormat(salon.getStatAverageTimeQueuePayment().getResult()));

            lLengthQueueR.setText(String.format("%.3f", salon.getStatAverageQueueLengthReception().getResult()));
            lLengthQueueH.setText(String.format("%.3f", salon.getStatAverageQueueLengthHair().getResult()));
            lLengthQueueC.setText(String.format("%.3f", salon.getStatAverageQueueLengthCosmetic().getResult()));
            lLengthQueueP.setText(String.format("%.3f", salon.getStatAverageQueueLengthPayment().getResult()));
        }

        lActualReplication.setText(salon.getActualReplication() + " z " + salon.getCountReplications());

        lReplicationCountCustomers.setText(String.format("%.3f", salon.getStatReplicationsCountCustomers().getResult()));
        lReplicationsTimeSalon.setText(toTimeFormat(salon.getStatReplicationsTimeSalon().getResult()));
        lReplicationOvertime.setText(toTimeFormat(salon.getStatReplicationsOvertime().getResult()));

        lReplicationsTimeQueueReception.setText(toTimeFormat(salon.getStatReplicationsTimeQueueReception().getResult()));
        lReplicationsTimeQueueHair.setText(toTimeFormat(salon.getStatReplicationsTimeQueueHair().getResult()));
        lReplicationsTimeQueueCosmetic.setText(toTimeFormat(salon.getStatReplicationsTimeQueueCosmetic().getResult()));
        lReplicationsTimeQueuePayment.setText(toTimeFormat(salon.getStatReplicationsTimeQueuePayment().getResult()));

        lReplicationsLengthQueueReception.setText(String.format("%.3f", salon.getStatReplicationsLengthQueueReception().getResult()));
        lReplicationsLengthQueueHair.setText(String.format("%.3f", salon.getStatReplicationsLengthQueueHair().getResult()));
        lReplicationsLengthQueueCosmetic.setText(String.format("%.3f", salon.getStatReplicationsLengthQueueCosmetic().getResult()));
        lReplicationsLengthQueuePayment.setText(String.format("%.5f", salon.getStatReplicationsLengthQueuePayment().getResult()));

        lReplicationDurationReception.setText(toTimeFormat(salon.getStatReplicationsReceptionDuration().getResult()));
        lReplicationDurationHair.setText(toTimeFormat(salon.getStatReplicationsHairDuration().getResult()));
        lReplicationDurationClean.setText(toTimeFormat(salon.getStatReplicationsCleaningDuration().getResult()));
        lReplicationDurationCosmetic.setText(toTimeFormat(salon.getStatReplicationsCosmeticDuration().getResult()));
        lReplicationDurationPayment.setText(toTimeFormat(salon.getStatReplicationsPayingDuration().getResult()));

        lReplicationUtilizationR.setText(String.format("%.3f", salon.getStatReplicationsAverageUtilizationReceptionist().getResult()));
        lReplicationUtilizationH.setText(String.format("%.3f", salon.getStatReplicationsAverageUtilizationHairstylist().getResult()));
        lReplicationUtilizationC.setText(String.format("%.3f", salon.getStatReplicationsAverageUtilizationCosmetitian().getResult()));

        lReplicationIntervalTimeSalon.setText("<" + toTimeFormat(salon.getIntervalReplicationsTimeInSalon().getInterval().getMin()) + ", " + toTimeFormat(salon.getIntervalReplicationsTimeInSalon().getInterval().getMax()) + ">");

        lReplicationLengthQueueReception17.setText(String.valueOf(salon.getStatReplicationsQueueLengthReception17().getResult()));
        lReplicationLengthQueueHair17.setText(String.valueOf(salon.getStatReplicationsQueueLengthHair17().getResult()));
        lReplicationLengthQueueCosmetic17.setText(String.valueOf(salon.getStatReplicationsQueueLengthCosmetic17().getResult()));
        lReplicationLengthQueuePayment17.setText(String.valueOf(salon.getStatReplicationsQueueLengthPayment17().getResult()));
    }

    @Override
    public void createCustomer(Customer customer) {
        Object[] data = new Object[3];
        data[0] = customer.getId();
        data[1] = customer.getStartTime();
        data[2] = customer.getStatus();
        modelTableCustomers.addRow(data);
    }

    @Override
    public void updateCustomer(Customer customer) {
        modelTableCustomers.setValueAt(customer.getStatus(), customer.getId(), 2);
    }

    @Override
    public void updateReceptionist(Receptionist receptionist) {
        modelTableReceptionists.setValueAt(receptionist.isOccupied(), receptionist.getId(), 1);
        modelTableReceptionists.setValueAt((receptionist.getWork() / (double) salon.getActualTime()) * 100 + "%", receptionist.getId(), 2);
    }

    @Override
    public void updateHairstylist(HairStylist hairStylist) {
        modelTableHairstylists.setValueAt(hairStylist.isOccupied(), hairStylist.getId(), 1);
        modelTableHairstylists.setValueAt(((hairStylist.getWork() / (double) salon.getActualTime()) * 100) + "%", hairStylist.getId(), 2);
    }

    @Override
    public void updateCosmetitian(Cosmetitian cosmetitian) {
        modelTableCosmetitians.setValueAt(cosmetitian.isOccupied(), cosmetitian.getId(), 1);
        modelTableCosmetitians.setValueAt((cosmetitian.getWork() / (double) salon.getActualTime()) * 100 + "%", cosmetitian.getId(), 2);
    }

    @Override
    public void newSimulation() {
        modelTableCustomers.setRowCount(0);
    }

    public String toTimeFormat(double seconds) {
        double second = seconds % 60;
        int time = (int) seconds / 60;
        int minute = time % 60;
        int hour = time / 60;
        if (minute == 0 && hour == 0) {
            return String.format("%.3f", second) + " s";
        } else if (hour == 0) {
            return minute + " min " + String.format("%.3f", second) + " s";
        } else {
            return hour + " hod " + minute + " min " + String.format("%.3f", second) + " s";
        }
    }

    public void drawGraph() {
        dataset.addSeries(series);
        chart = ChartFactory.createScatterPlot(
                "Závislosť priemernej dĺžky rady na recepcií od počtu kaderníčok",
                "Počet kaderníčok",
                "priemerná dĺžka radu na recepcií",
                dataset,
                PlotOrientation.VERTICAL,
                true,
                true,
                false
        );
        ChartPanel chartPanel = new ChartPanel(chart);
        graphPanel.add(chartPanel);
        graphPanel.validate();
        chartPanel.setVisible(true);
    }
}
