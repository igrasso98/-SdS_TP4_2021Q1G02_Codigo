package ar.edu.itba.sds_2021_q1_g02.models;

import javafx.util.Pair;

import java.util.*;

public class Particle {
    private final int id;
    private final double radius;
    private final double mass;
    private final Set<Particle> neighbors;
    private Position position;
    private Velocity velocity;
    private ParticleCharge charge;

    public Particle(int id, double radius, double mass, Position position, Velocity velocity) {
        this(id, radius, mass, position, velocity, null);
    }

    public Particle(int id, double radius, double mass, Position position, Velocity velocity, ParticleCharge charge) {
        this.id = id;
        this.radius = radius;
        this.neighbors = new HashSet<>();
        this.mass = mass;
        this.position = position;
        this.velocity = velocity;
        this.charge = charge;
    }

    public int getId() {
        return this.id;
    }

    public double getRadius() {
        return this.radius;
    }

    public double getMass() {
        return this.mass;
    }

    public Position getPosition() {
        return this.position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Velocity getVelocity() {
        return this.velocity;
    }

    public void setVelocity(Velocity velocity) {
        this.velocity = velocity;
    }

    public Collection<Particle> getNeighbors() {
        return this.neighbors;
    }

    public ParticleCharge getCharge() {
        return this.charge;
    }

    public void setCharge(ParticleCharge charge) {
        this.charge = charge;
    }

    public Particle copy() {
        Particle particle = new Particle(this.id, this.radius, this.mass, this.position, this.velocity, this.charge);

        if (this.position != null)
            particle.setPosition(this.position.copy());
        if (this.position != null)
            particle.setVelocity(this.velocity.copy());

        return particle;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Particle)) return false;
        Particle particle = (Particle) o;
        return this.getId() == particle.getId();
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getId());
    }

    public double distanceTo(Particle other) {
        double ctr_dist = Math.sqrt(
                Math.pow(this.position.getX() - other.position.getX(), 2)
                        + Math.pow(this.position.getY() - other.position.getY(), 2)
        );

        return (ctr_dist - this.getRadius()) - other.getRadius();
    }
}
