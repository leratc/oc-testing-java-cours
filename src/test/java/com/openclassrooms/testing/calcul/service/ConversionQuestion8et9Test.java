package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import com.openclassrooms.testing.calcul.domain.model.ConversionType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.openclassrooms.testing.calcul.domain.model.ConversionModel;

@ExtendWith(MockitoExtension.class)
public class ConversionQuestion8et9Test {
	@Mock
	ConversionCalculatorService conversionService;

	BatchConversionService batchConversionService;

	@BeforeEach
	public void init() {
		batchConversionService = new BatchConversionServiceImpl(conversionService);
	}

	@Test
	public void givenConversionsList_whenbatchConvert_thenCallsServiceWithCorrectArguments() {
		// GIVEN
		final List<String> operationsList = Arrays.asList("32. °F->°C", "10. GAL->L");
		final Stream<String> operations = operationsList.stream();
		final ArgumentCaptor<ConversionModel> conversionModelCaptor = ArgumentCaptor.forClass(ConversionModel.class);

		// WHEN
		batchConversionService.batchConvert(operations);

		// THEN
		verify(conversionService, times(2)).calculate(conversionModelCaptor.capture());
		List<ConversionModel> conversionModels = conversionModelCaptor.getAllValues();
		assertThat(conversionModels)
				.extracting(ConversionModel::getArgument, ConversionModel::getConversionType)
				.containsExactly(tuple(32., ConversionType.FARENHEIT_TO_CELSIUS),
						tuple(10., ConversionType.GALLON_TO_LITRE));
			}
	@Test
	public void givenConversionsList_whenbatchConvert_thenCallsServiceAndReturnsAnswer() {
		// GIVEN
		final Stream<String> operations = Arrays.asList("10. °F->°C", "10. GAL->L", "35 L->GAL", "35. °C->°F")
				.stream();
		when(conversionService.calculate(any(ConversionModel.class)))
				.then(invocation -> {
					final ConversionModel model = invocation.getArgument(0, ConversionModel.class);
					switch (model.getConversionType()) {
					case CELSIUS_TO_FARENHEIT:
						model.setSolution(32.);
						break;
					case FARENHEIT_TO_CELSIUS:
						model.setSolution(0.);
						break;
					case LITRE_TO_GALLON:
						model.setSolution(1.0);
						break;
					case GALLON_TO_LITRE:
						model.setSolution(37.8);
						break;
					default:
					}
					return model;
				});

		// WHEN
		final List<ConversionModel> results = batchConversionService.batchConvert(operations);

		// THEN
		verify(conversionService, times(4)).calculate(any(ConversionModel.class));
		assertThat(results).extracting("solution").containsExactly(0., 37.8, 1.0, 32.);

	}
}
