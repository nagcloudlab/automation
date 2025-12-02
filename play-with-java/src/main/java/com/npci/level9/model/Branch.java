package com.npci.level9.model;

import java.util.Objects;

/**
 * Level 9: Collections Framework - Branch Model
 */
public class Branch implements Comparable<Branch> {

    private final String branchCode;
    private String branchName;
    private String ifscCode;
    private String city;
    private String state;
    private String region;  // NORTH, SOUTH, EAST, WEST
    private int accountCount;
    private double totalDeposits;

    public Branch(String branchCode, String branchName, String ifscCode,
                  String city, String state, String region) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
        this.city = city;
        this.state = state;
        this.region = region;
        this.accountCount = 0;
        this.totalDeposits = 0;
    }

    // Getters
    public String getBranchCode() { return branchCode; }
    public String getBranchName() { return branchName; }
    public String getIfscCode() { return ifscCode; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getRegion() { return region; }
    public int getAccountCount() { return accountCount; }
    public double getTotalDeposits() { return totalDeposits; }

    // Setters
    public void setBranchName(String name) { this.branchName = name; }
    public void setAccountCount(int count) { this.accountCount = count; }
    public void setTotalDeposits(double deposits) { this.totalDeposits = deposits; }

    public void incrementAccountCount() { accountCount++; }
    public void addDeposits(double amount) { totalDeposits += amount; }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Branch branch = (Branch) obj;
        return Objects.equals(branchCode, branch.branchCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchCode);
    }

    @Override
    public int compareTo(Branch other) {
        return this.branchCode.compareTo(other.branchCode);
    }

    @Override
    public String toString() {
        return String.format("Branch[%s, %s, %s, %s]",
                branchCode, branchName, city, region);
    }
}