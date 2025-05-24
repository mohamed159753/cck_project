package com.pfe.Reservation_Bill_Management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReservationBillManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ReservationBillManagementApplication.class, args);
	}

}
