package entity;

/**
 * Created by jecka on 10.05.2015.
 */
public abstract class ADaoEntity {
    private int id;

    public ADaoEntity() {

    }

    public ADaoEntity(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "";
    }
}
