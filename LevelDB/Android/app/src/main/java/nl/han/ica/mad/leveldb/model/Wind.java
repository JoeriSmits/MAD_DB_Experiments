package nl.han.ica.mad.leveldb.model;

/**
 * @author Niels Bokmans
 * @version 1.0
 * @since 30-10-2016
 */
public class Wind {
    public String direction;
    public String speed;

    public Wind(String direction, String speed) {
        this.direction = direction;
        this.speed = speed;
    }
}
