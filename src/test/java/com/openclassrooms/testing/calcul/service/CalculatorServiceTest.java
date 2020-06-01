package com.openclassrooms.testing.calcul.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.openclassrooms.testing.calcul.domain.Calculator;
import com.openclassrooms.testing.calcul.domain.model.CalculationModel;
import com.openclassrooms.testing.calcul.domain.model.CalculationType;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class CalculatorServiceTest {
	@Mock
	Calculator calculator;
	@Mock
	SolutionFormatter solutionFormatter;
	CalculatorService classUnderTest;

	@BeforeEach
	public void setup() {
		// Calculator IS CALLED BY CalculatorService
		classUnderTest = new CalculatorServiceImpl(calculator,solutionFormatter);
	}
	@Test
	public void should_use_calculator_for_addition() {
		// GIVEN
		when(calculator.add(1, 2)).thenReturn(3);
		//WHEN
		final int result = classUnderTest.calculate(
				new CalculationModel(CalculationType.ADDITION, 1, 2)).getSolution();
		//THEN
		verify(calculator).add(1, 2);
		assertThat(result).isEqualTo(3);
	}
	@Test
	public void should_use_calculator_for_multiplication() {
		//GIVEN
		when(calculator.multiply(-3, 2)).thenReturn(-6);
		//WHEN
		final int result = classUnderTest.calculate(new CalculationModel(CalculationType.MULTIPLICATION,-3,2)).getSolution();
		//THEN
		verify(calculator).multiply(-3, 2);
		assertThat(result).isEqualTo(-6);
	}
	@Test
	public void should_use_calculator_fpr_division() {
		//GIVEN
		when(calculator.divide(6, 3)).thenReturn(2);
		//WHEN
		final int result = classUnderTest.calculate(new CalculationModel(CalculationType.DIVISION,6,3)).getSolution();
		//THEN
		verify(calculator).divide(6, 3);
		assertThat(result).isEqualTo(2);
	}
	@Test
	public void should_throw_divideBy0_exception() {
		//GIVEN
		when(calculator.divide(6, 0)).thenThrow(new ArithmeticException());
		//WHEN
		assertThrows(IllegalArgumentException.class,
				() -> classUnderTest.calculate(
						new CalculationModel(
								CalculationType.DIVISION,6,0))
						.getSolution());
		//THEN
		verify(calculator).divide(6, 0);
	}
	@Test
	public void should_use_formatted_solution_for_addition() {
		int chiffre1=10000;
		int chiffre2=3000;
		int chiffre3R=13000;
		// GIVEN
		when(calculator.add(chiffre1, chiffre2)).thenReturn(chiffre3R);
		when(solutionFormatter.format(chiffre3R)).thenReturn("13 000");
		//WHEN
		final String formattedResult = classUnderTest.calculate(
				new CalculationModel(CalculationType.ADDITION, chiffre1, chiffre2))
				.getFormattedSolution();
		//THEN
		verify(calculator).add(chiffre1, chiffre2);
		assertThat(formattedResult).isEqualTo("13 000");
	}
}

