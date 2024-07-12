package com.weather_service.alerts.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather_service.alerts.dto.AlertsCountResponseDTO;
import com.weather_service.alerts.service.WeatherService;

import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;

/**
 * This api is to retrieve alerts active count
 */
@RestController
@RequestMapping("/api/v1/weather")
@Slf4j
public class WeatherServiceController {

	@Autowired
	private WeatherService weatherService;

	@GetMapping("/alerts-count")
	@CircuitBreaker(name = "getAlerts", fallbackMethod = "fallbackGetAlertsResponse")
	public AlertsCountResponseDTO getAlerts() throws JsonProcessingException {
		return weatherService.getAlerts();
	}

	public AlertsCountResponseDTO fallbackGetAlertsResponse(RuntimeException exception) {
		log.error("Weather service is failed to connect!");
		return new AlertsCountResponseDTO().builder()
				.message("Something went wrong. Please place order after some time!").build();
	}
}
