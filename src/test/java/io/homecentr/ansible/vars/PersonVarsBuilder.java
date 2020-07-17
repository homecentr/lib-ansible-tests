package io.homecentr.ansible.vars;

public class PersonVarsBuilder {

    public static PersonVarsBuilder fromValid() {
        return new PersonVarsBuilder();
    }

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    private PersonVarsBuilder() {
        firstName = "John";
        lastName = "Doe";
        email = "john@doe.com";
        phoneNumber = "123456789";
    }

    public PersonVarsBuilder setFirstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PersonVarsBuilder setLastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PersonVarsBuilder setEmail(String email) {
        this.email = email;
        return this;
    }

    public PersonVarsBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public PersonVars build() {
        return new PersonVars(firstName, lastName, email, phoneNumber);
    }
}