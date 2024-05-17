package net.noel.ophelie.applicationatmostateliertest;

import java.util.Date;

public class Capteur {
    private String id_wasp;
    private String sensor;
    private double value;
    private Date timestamp;
    private double seuilMin;
    private double seuilMax;
    private boolean valideSeuilMin;
    private boolean valideSeuilMax;
    private boolean activeAlert;
    private String unite;
    private double seuilEchMin;
    private double seuilEchMax;


    public Capteur(String id_wasp, String sensor, double value, Date timestamp, double seuilMin, double seuilMax,
                   boolean valideSeuilMin, boolean valideSeuilMax, boolean activeAlert, String unite,
                   double seuilEchMin, double seuilEchMax) {
        this.id_wasp = id_wasp;
        this.sensor = sensor;
        this.value = value ;
        this.timestamp = timestamp;
        this.seuilMin = seuilMin;
        this.seuilMax = seuilMax;
        this.valideSeuilMin = valideSeuilMin;
        this.valideSeuilMax = valideSeuilMax;
        this.activeAlert = activeAlert;
        this.unite = unite;
        this.seuilEchMin = seuilEchMin;
        this.seuilEchMax = seuilEchMax;
    }

    public String getId_wasp() {
        return id_wasp;
    }

    public void setId_wasp(String id_wasp) {
        this.id_wasp = id_wasp;
    }

    public String getSensor() {
        return sensor;
    }

    public void setSensor(String sensor) {
        this.sensor = sensor;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public double getSeuilMin() {
        return seuilMin;
    }

    public void setSeuilMin(double seuilMin) {
        this.seuilMin = seuilMin;
    }

    public double getSeuilMax() {
        return seuilMax;
    }

    public void setSeuilMax(double seuilMax) {
        this.seuilMax = seuilMax;
    }

    public boolean isValideSeuilMin() {
        return valideSeuilMin;
    }

    public void setValideSeuilMin(boolean valideSeuilMin) {
        this.valideSeuilMin = valideSeuilMin;
    }

    public boolean isValideSeuilMax() {
        return valideSeuilMax;
    }

    public void setValideSeuilMax(boolean valideSeuilMax) {
        this.valideSeuilMax = valideSeuilMax;
    }

    public boolean getActiveAlert() {
        return activeAlert;
    }

    public void setActiveAlert(boolean activeAlert) {
        this.activeAlert = activeAlert;
    }

    public String getUnite() {
        return unite;
    }

    public void setUnite(String unite) {
        this.unite = unite;
    }

    public double getSeuilEchMin() {
        return seuilEchMin;
    }

    public void setSeuilEchMin(double seuilEchMin) {
        this.seuilEchMin = seuilEchMin;
    }

    public double getSeuilEchMax() {
        return seuilEchMax;
    }

    public void setSeuilEchMax(double seuilEchMax) {
        this.seuilEchMax = seuilEchMax;
    }

    @Override
    public String toString() {
        return "Capteur{" +
                "id_wasp='" + id_wasp + '\'' +
                ", sensor='" + sensor + '\'' +
                ", value=" + value +
                ", timestamp=" + timestamp +
                ", seuilMin=" + seuilMin +
                ", seuilMax=" + seuilMax +
                ", valideSeuilMin=" + valideSeuilMin +
                ", valideSeuilMax=" + valideSeuilMax +
                ", activeAlert=" + activeAlert +
                ", unite='" + unite + '\'' +
                ", seuilEchMin=" + seuilEchMin +
                ", seuilEchMax=" + seuilEchMax +
                '}';
    }
}

