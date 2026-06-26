package com.ynov.mediacity.model;

// Un adherent de la mediatheque.
// On compte ses "retards importants" de l'annee : a 3, il est suspendu.
public class Member {

    private final String id;
    private final String name;
    private boolean suspended;
    private int importantLateCount;

    public Member(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void suspend() {
        this.suspended = true;
    }

    public int getImportantLateCount() {
        return importantLateCount;
    }

    public void addImportantLate() {
        this.importantLateCount++;
    }
}
