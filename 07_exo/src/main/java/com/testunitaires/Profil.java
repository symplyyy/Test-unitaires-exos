package com.testunitaires;

public enum Profil {
    STANDARD(0.0),
    PREMIUM(0.10),
    VIP(0.20);

    private final double remise;

    Profil(double remise) {
        this.remise = remise;
    }

    public double getRemise() {
        return remise;
    }
}
