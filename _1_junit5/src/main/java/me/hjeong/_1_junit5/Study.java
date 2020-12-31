package me.hjeong._1_junit5;

public class Study {
    private int limit;
    private StudyStatus status;
    private String name;

    public Study(int limit) {
        if (limit < 0) {
            throw new IllegalArgumentException("limit은 0보다 커야 합니다.");
        }
        this.limit = limit;
    }

    public Study() {

    }

    public StudyStatus getStatus() {
        return status;
    }

    public int getLimit() {
        return limit;
    }

    public Study(int limit, String name) {
        this.limit = limit;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Study{" +
                "limit=" + limit +
                ", status=" + status +
                ", name='" + name + '\'' +
                '}';
    }
}
