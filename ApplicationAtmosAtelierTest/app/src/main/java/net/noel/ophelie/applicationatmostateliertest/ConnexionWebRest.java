package net.noel.ophelie.applicationatmostateliertest;

public class ConnexionWebRest {
    private String adresseIP;
    private int port;

    public ConnexionWebRest(String adresseIP, int port) {
        this.adresseIP = adresseIP;
        this.port = port;
    }

    // Getters
    public String getAdresseIP() {
        return adresseIP;
    }

    public int getPort() {
        return port;
    }

}
