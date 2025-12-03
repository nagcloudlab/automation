package com.npci.mini;

import java.util.*;


class Owner {
    String name;
    String address;

    public Owner(String name, String address) {
        this.name = name;
        this.address = address;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Owner owner)) return false;
        return Objects.equals(name, owner.name) && Objects.equals(address, owner.address);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, address);
    }

    @Override
    public String toString() {
        return "Owner{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}

class Vehicle {
    String regNo;
    String model;

    public Vehicle(String regNo, String model) {
        this.regNo = regNo;
        this.model = model;
    }

    @Override
    public String toString() {
        return "Vehicle{" +
                "regNo='" + regNo + '\'' +
                ", model='" + model + '\'' +
                '}';
    }
}

public class Map_Colln_Demo {
    public static void main(String[] args) {

        // List -> index
        // Set  -> no index, no duplicate values

        /*

            MAP real life example:
            Key         Value
            -----------------------
            PAN         Customer Details
            Aadhar      Customer Details
            Voter ID    Customer Details
            DL          Customer Details
            -----------------------

         */

        Owner owner1 = new Owner("Riya", "123 Main St");
        Owner owner2 = new Owner("Diya", "456 Park Ave");

        Vehicle vehicle1 = new Vehicle("KA01AB1234", "Toyota");
        Vehicle vehicle2 = new Vehicle("KA02CD5678", "Honda");

//        Map<Owner, Vehicle> vehicleRegistry = new TreeMap<>((o1,o2)->{
//            return o1.name.compareTo(o2.name);
//        });
        Map<Owner, Vehicle> vehicleRegistry = new HashMap<>();
        vehicleRegistry.put(owner1, vehicle1);
        vehicleRegistry.put(owner2, vehicle2);
        vehicleRegistry.put(owner2, vehicle2);

        displayMap(vehicleRegistry);

//        Vehicle vehicle=vehicleRegistry.get(owner1);
//        System.out.println("Vehicle owned by "+owner1.name+" is: "+vehicle);


    }

    static void displayMap(Map<Owner, Vehicle> map) {
        Set<Owner> owners = map.keySet();
        for (Owner owner : owners) {
            Vehicle vehicle = map.get(owner);
            System.out.println(owner + " -> " + vehicle);
        }
    }
}
