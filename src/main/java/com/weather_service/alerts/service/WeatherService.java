package com.weather_service.alerts.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.weather_service.alerts.dto.AlertsCountResponse;
import com.weather_service.alerts.dto.AlertsCountResponseDTO;
import com.weather_service.alerts.entity.AlertsCount;
import com.weather_service.alerts.repository.WeatherRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class WeatherService {

	@Value("${weather.api.base-url}")
	private String baseUrl;

	@Value("${weather.api.zones-endpoint}")
	private String zonesEndPoint;

	@Autowired
	private WeatherRepository weatherRepository;

	@Autowired
	private RestTemplate restTemplate;

	public AlertsCountResponseDTO getAlerts() throws JsonProcessingException {
		UriComponents builder = UriComponentsBuilder.fromHttpUrl(baseUrl + zonesEndPoint).build();
		HttpEntity<String> requestEntity = new HttpEntity<>(null);

		ResponseEntity<AlertsCountResponse> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,
				requestEntity, AlertsCountResponse.class);
		AlertsCountResponse res = response.getBody();

		AlertsCount ac = weatherRepository.save(AlertsCount.builder().total(res.getTotal()).land(res.getLand())
				.marine(res.getMarine()).regions(new ArrayList(res.getRegions().keySet())).build());
		return AlertsCountResponseDTO.builder().total(ac.getTotal()).marine(ac.getMarine()).land(ac.getLand())
				.regions(ac.getRegions()).message("Success!").build();
	}

}
