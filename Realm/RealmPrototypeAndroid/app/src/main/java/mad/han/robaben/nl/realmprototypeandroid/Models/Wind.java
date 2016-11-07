package mad.han.robaben.nl.realmprototypeandroid.Models;

import io.realm.RealmObject;

public class Wind extends RealmObject {
    private String speed;
    private String direction;

    public Wind(){

    }

    public Wind(String speed, String direction){
        this.speed = speed;
        this.direction = direction;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

}
