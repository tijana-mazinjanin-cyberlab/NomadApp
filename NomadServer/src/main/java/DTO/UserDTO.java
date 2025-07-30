package DTO;

import model.enums.UserType;

import java.util.List;

public class UserDTO {
        private Long id;
        private String firstName;
        private String lastName;
        private String address;
        private String username;
        private String password;
        private String phoneNumber;
        private boolean suspended;
        private boolean verified;
        private long cancellationNumber;
        private List<UserType> roles;

    // Constructor
    public UserDTO(Long id, String firstName, String lastName,
                   String address, String username, String password, boolean verified, boolean suspended,
                   String phoneNumber,  List<UserType> roles, long cancellationNumber) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.username = username;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.suspended = suspended;
        this.verified = verified;
        this.roles = roles;
        this.cancellationNumber = cancellationNumber;
    }

    public long getCancellationNumber() {
        return cancellationNumber;
    }

    public void setCancellationNumber(long cancellationNumber) {
        this.cancellationNumber = cancellationNumber;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }

    public List<UserType> getRoles() {
        return roles;
    }

    public void setRoles(List<UserType> roles) {
        this.roles = roles;
    }

    public UserDTO(){}
    // Getters and setters for each attribute

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void suspend(){
        this.suspended = true;
    }
    public void unsuspend(){
        this.suspended = false;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isSuspended() {
        return suspended;
    }

    public void setSuspended(boolean suspended) {
        this.suspended = suspended;
    }
}
