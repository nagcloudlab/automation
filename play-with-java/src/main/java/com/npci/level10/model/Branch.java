package com.npci.level10.model;

import java.util.Objects;

/**
 * Level 10: Streams API - Branch Model
 */
public class Branch {

    private String branchCode;
    private String branchName;
    private String ifscCode;
    private String city;
    private String state;
    private String region;  // NORTH, SOUTH, EAST, WEST
    private String branchType;  // METRO, URBAN, SEMI_URBAN, RURAL
    private int employeeCount;
    private double totalDeposits;
    private int accountCount;

    public Branch(String branchCode, String branchName, String ifscCode,
                  String city, String state, String region, String branchType) {
        this.branchCode = branchCode;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
        this.city = city;
        this.state = state;
        this.region = region;
        this.branchType = branchType;
    }

    // Getters and Setters
    public String getBranchCode() { return branchCode; }
    public String getBranchName() { return branchName; }
    public String getIfscCode() { return ifscCode; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getRegion() { return region; }
    public String getBranchType() { return branchType; }
    public int getEmployeeCount() { return employeeCount; }
    public void setEmployeeCount(int count) { this.employeeCount = count; }
    public double getTotalDeposits() { return totalDeposits; }
    public void setTotalDeposits(double deposits) { this.totalDeposits = deposits; }
    public int getAccountCount() { return accountCount; }
    public void setAccountCount(int count) { this.accountCount = count; }

    public boolean isMetro() { return "METRO".equals(branchType); }
    public boolean isUrban() { return "URBAN".equals(branchType); }

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
    public String toString() {
        return String.format("Branch[%s, %s, %s, %s]",
                branchCode, branchName, city, region);
    }
}