
import java.io.Serializable;

public class Person implements Serializable {
    String address;
    int id;
    int age;
    String name;


    public Person(int id, int age, String name, String address) {
        this.id=id;
        this.age=age;
        this.name=name;
        this.address=address;
    }

    public Person() {

    }

    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }
    public String getAddress() {
        return address;
    }
    public int getAge() {
        return age;
    }
    public void setID(int id) {
        this.id = id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setAddress(String address) {
        this.address = address;
    }
    public void setAge(int age) {
        this.age = age;
    }

    public void setId(int personId) {
        this.id=id;
    }
}