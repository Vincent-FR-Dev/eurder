package com.switchfully.eurder.domain;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;

public class CustomerTest {

    @Test
    void twoCustomersDontHaveSameId(){

          Address address = new Address("Rue des loups", "2", "1000", "Bruxelles");
          Customer rudy = new Customer("Rudy", "Switchfully", "rudy@switchfully.com", address, "0472548963");
          Customer karel = new Customer("Karel", "Switchfully", "karel@switchfully.com", address, "0472548123");

          assertThat(rudy.getId()).isNotNull();
          assertThat(rudy.getId()).isNotEqualTo(karel.getId());
    }

    @Test
    void customerIdIsNotNull(){

    }

}
