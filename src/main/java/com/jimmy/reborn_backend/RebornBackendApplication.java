package com.jimmy.reborn_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing // 추가: 생성 시간 자동 기록 활성화
@SpringBootApplication
public class RebornBackendApplication {
	public static void main(String[] args) {
		SpringApplication.run(RebornBackendApplication.class, args);
	}
}