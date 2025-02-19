package com.apartment.management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class ManagementApplication {

	public static void main(String[] args) {
		SpringApplication.run(ManagementApplication.class, args);
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

		// รหัสผ่านที่ต้องการทดสอบ
		String rawPassword = "nan123";
		String storedPassword = "$2a$10$Y1Bix4GpeFT.KyLCFfp1YOZTf4ySt4dBzUsn1wH7nIx8nQq9N1sW6";

		// ทดสอบการ match
		boolean isMatch = encoder.matches(rawPassword, storedPassword);
		System.out.println("🔍 passwordEncoder.matches: " + isMatch);

		// ทดสอบเข้ารหัสรหัสผ่านใหม่ ว่ามันแปลงแล้วจับคู่กันได้ไหม
	
		String encodedPassword = encoder.encode(rawPassword);

		System.out.println("🔐 Encoded Password: " + encodedPassword);

		
	}

}
