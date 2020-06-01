package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.testing.calcul.domain.ConversionCalculator;
import com.openclassrooms.testing.calcul.domain.model.ConversionModel;
import com.openclassrooms.testing.calcul.domain.model.ConversionType;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(Alphanumeric.class)
public class ConversionQuestion6et7Test {
	@Mock
	ConversionCalculator calculator;

	ConversionCalculatorServiceImpl conversionCalculatorService;

	@BeforeEach
	public void init() {
		conversionCalculatorService = new ConversionCalculatorServiceImpl(calculator);
	}

	@Test
	public void calculate_shouldUseCalculator_forCelsius0ToFarenheit32() {
		// GIVEN
		final double resultatAttendu = 32.;
		when(calculator.celsiusToFahrenheit(0.0)).thenReturn(32.);

		// WHEN
		Double resultat = conversionCalculatorService.calculate(
				new ConversionModel(0., ConversionType.CELSIUS_TO_FARENHEIT)).getSolution();

		// THEN
		verify(calculator).celsiusToFahrenheit(0.0);
		assertThat(resultat).isEqualTo(resultatAttendu);

	}

	@Test
	public void calculate_shouldUseCalculator_forPreviousFarenheitToCelsius0() {

		// GIVEN
		final double resultatAttendu = 0.;
		when(calculator.fahrenheitToCelsius(32.)).thenReturn(0.);

		// WHEN
		Double resultatAppel = conversionCalculatorService.calculate(
				new ConversionModel(32., ConversionType.FARENHEIT_TO_CELSIUS)).getSolution();

		// THEN
		verify(calculator).fahrenheitToCelsius(32.0);
		assertThat(resultatAppel).isEqualTo(resultatAttendu);

	}
}
