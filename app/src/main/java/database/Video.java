package database;

public class Video {

    private int id;
    private String name;
    private String route;

    public Video() {
    }

    public Video(String name, String route, int isLiked) {
        this.name = name;
        this.route = route;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

}
