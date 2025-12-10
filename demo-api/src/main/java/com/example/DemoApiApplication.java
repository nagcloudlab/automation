package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

enum UserType {
    ADMIN,
    CUSTOMER,
    GUEST
}

class Address {
    private String street;
    private String city;

    public Address(String street, String city) {
        this.street = street;
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public String getCity() {
        return city;
    }
}

class User {
    private String name;
    private int age;
    private UserType type;
    private Address address;

    public User(String name, int age, UserType type, Address address) {
        this.name = name;
        this.age = age;
        this.type = type;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public UserType getType() {
        return type;
    }

    public Address getAddress() {
        return address;
    }

}

@SpringBootApplication
@RestController
public class DemoApiApplication {


    static List<User> userList = new ArrayList<>();

    static {
        userList.add(new User("Alice", 30, UserType.ADMIN, new Address("123 Main St", "Springfield")));
        userList.add(new User("Bob", 25, UserType.CUSTOMER, new Address("456 Oak Ave", "Shelbyville")));
    }

    @GetMapping(value = "/api/v1/hello", produces = "text/html;charset=UTF-8")
    public String hello() {
        //return "Hello, World!";
        return "<html><body><h1>Hello, World!</h1></body></html>";
    }

    @GetMapping(value = "/api/v1/users", produces = "application/json;charset=UTF-8")
    public List<User> getUsers(
            @RequestParam(defaultValue = "CUSTOMER", name = "type") String userType
    ) {
        UserType type = UserType.valueOf(userType.toUpperCase());
        return userList.stream()
                .filter(user -> user.getType() == type)
                .toList();
    }

    @PostMapping(value = "/api/v1/users",
            consumes = "application/json;charset=UTF-8",
            produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> createUser(
            @RequestBody User user) throws InterruptedException {
        userList.add(user);
        TimeUnit.SECONDS.sleep(5);
        return ResponseEntity.status(201).body(user);
    }

    public static void main(String[] args) {
        SpringApplication.run(DemoApiApplication.class, args);
    }

}
