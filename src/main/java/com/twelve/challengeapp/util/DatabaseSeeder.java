package com.twelve.challengeapp.util;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.twelve.challengeapp.entity.User;
import com.twelve.challengeapp.entity.UserRole;
import com.twelve.challengeapp.repository.UserRepository;

@Component
public class DatabaseSeeder implements CommandLineRunner {

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Value("@qwer0828")
	private String adminPassword;

	public DatabaseSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
		this.userRepository = userRepository;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void run(String... arg) {
		if (userRepository.count() == 0) {
			User adminUser = User.builder()
				.username("admin")
				.password(passwordEncoder.encode(adminPassword))
				.nickname("admin")
				.introduce("admin account")
				.email("admin@example.com")
				.role(UserRole.ADMIN)
				.build();

			userRepository.save(adminUser);
		}
	}
}
