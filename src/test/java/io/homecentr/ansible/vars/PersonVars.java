package io.homecentr.ansible.vars;

public class PersonVars {
  private final String firstName;
  private final String lastName;
  private final String email;
  private final String phoneNumber;

  public PersonVars(String firstName, String lastName, String email, String phoneNumber) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.phoneNumber = phoneNumber;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public String getEmail() {
    return this.email;
  }

  public String getPhoneNumber() {
    return this.phoneNumber;
  }
}