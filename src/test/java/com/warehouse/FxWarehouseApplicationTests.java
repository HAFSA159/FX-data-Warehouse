package com.warehouse;

import com.warehouse.models.Deal;
import com.warehouse.utils.CurrencyCsvReader;
import com.warehouse.validation.DealValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class FxWarehouseApplicationTests {


	@Mock
	private CurrencyCsvReader currencyUtil;

	@Mock
	private DealValidator dealValidator;


	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		dealValidator = new DealValidator(currencyUtil);

		when(currencyUtil.isValid(anyString())).thenReturn(true);
	}

	@Test
	void shouldReturnNullForValidDeal() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now().minusMinutes(1))
				.amount(100F)
				.build();

		String error = dealValidator.validate(deal);

		assertNull(error);
	}

	@Test
	void shouldReturnErrorWhenDealIdIsMissing() {
		Deal deal = Deal.builder()
				.dealId(null)
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(100F)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Deal ID is required", error);
	}

	@Test
	void shouldReturnErrorWhenDealIdIsBlank() {
		Deal deal = Deal.builder()
				.dealId("")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(100.0f)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Deal ID is required", error);
	}

	@Test
	void shouldReturnErrorWhenFromCurrencyIsInvalid() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("XXX")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(100.0f)
				.build();

		when(currencyUtil.isValid("XXX")).thenReturn(false);

		String error = dealValidator.validate(deal);

		assertEquals("Invalid fromCurrency code", error);
	}

	@Test
	void shouldReturnErrorWhenFromCurrencyIsBlank() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(100F)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Invalid fromCurrency code", error);
	}

	@Test
	void shouldReturnErrorWhenToCurrencyIsInvalid() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("ZZZ")
				.timestamp(LocalDateTime.now())
				.amount(100.0f)
				.build();

		when(currencyUtil.isValid("ZZZ")).thenReturn(false);

		String error = dealValidator.validate(deal);

		assertEquals("Invalid toCurrency code", error);
	}

	@Test
	void shouldReturnErrorWhenToCurrencyIsBlank() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("")
				.timestamp(LocalDateTime.now())
				.amount(100.0f)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Invalid toCurrency code", error);
	}

	@Test
	void shouldReturnErrorWhenTimestampIsNull() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(null)
				.amount(100.0f)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Invalid or future timestamp", error);
	}

	@Test
	void shouldReturnErrorWhenTimestampIsInFuture() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now().plusDays(1))
				.amount(100.0f)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Invalid or future timestamp", error);
	}

	@Test
	void shouldReturnErrorWhenAmountIsNull() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(null)
				.build();

		String error = dealValidator.validate(deal);
		assertEquals("Amount must be a positive number", error);
	}

	@Test
	void shouldReturnErrorWhenAmountIsZero() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(0.0F)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Amount must be a positive number", error);
	}

	@Test
	void shouldReturnErrorWhenAmountIsNegative() {
		Deal deal = Deal.builder()
				.dealId("123")
				.fromCurrency("USD")
				.toCurrency("EUR")
				.timestamp(LocalDateTime.now())
				.amount(-100F)
				.build();

		String error = dealValidator.validate(deal);

		assertEquals("Amount must be a positive number", error);
	}


}
