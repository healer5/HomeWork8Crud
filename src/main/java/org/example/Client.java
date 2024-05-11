package org.example;
public class Client {

    private int id;
    private String name;

    public Client() {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }


    @Override
    public String toString() {
        return "Client {" +
                "id = " + id +
                ", name = '" + name + '\'' +
                '}';
    }

    public void setId(int id) {

    }

    public void setName(String name) {
    }
}