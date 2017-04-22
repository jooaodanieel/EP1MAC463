package lib;

/**
 * Created by vwraposo on 22/04/17.
 */

public class Seminar {
    private Integer id;
    private String name;

    public Seminar(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
