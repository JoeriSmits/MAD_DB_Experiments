package coen.sqliteonderzoek.model;

/**
 * Created by coen on 8-11-2016.
 */

public class Wind
{

    private int id;
    private String direction;
    private String speed;

    public Wind(){}

    public Wind(String direction, String speed) {
        super();
        this.direction = direction;
        this.speed = speed;
    }

    //getters & setters

    @Override
    public String toString() {
        return "Wind [id=" + id + ", direction=" + direction + ", speed=" + speed
                + "]";
    }


    public String getDirection() {
        return direction;
    }

    public String getSpeed() {
        return speed;
    }

    public void setId(int id) {
        this.id = id;
    }


    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public int getId() {
        return id;
    }
}
