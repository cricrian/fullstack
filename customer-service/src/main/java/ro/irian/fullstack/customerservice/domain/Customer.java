package ro.irian.fullstack.customerservice.domain;


public class Customer extends BaseEntity {
    private String name;
    private String email;
    private String phone;
    private String address;

    public Customer() {
    }

    public Customer(String customerId, String name, String email, String phone, String address) {
        this.set_id(customerId);
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}
