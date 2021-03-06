package org.tbk.xchange.jsr354;

import org.javamoney.moneta.Money;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.knowm.xchange.Exchange;
import org.knowm.xchange.ExchangeFactory;
import org.knowm.xchange.kraken.KrakenExchange;

import javax.money.convert.*;
import java.math.BigDecimal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

public class XChangeExchangeRateProviderIntegrationTest {

    private XChangeExchangeRateProvider sut;

    @Before
    public void setUp() {
        Exchange exchange = ExchangeFactory.INSTANCE.createExchange(KrakenExchange.class);
        ProviderContext providerContext = MoreProviderContexts.createSimpleProviderContextBuilder(exchange).build();
        this.sut = new XChangeExchangeRateProvider(providerContext, exchange);
    }

    @Test
    public void itShouldFetchSupportedExchangeRateSuccessfully() {
        ExchangeRate exchangeRate = this.sut.getExchangeRate("BTC", "USD");

        assertThat(exchangeRate, is(notNullValue()));

        BigDecimal singleBitcoinInUsdValue = exchangeRate.getFactor().numberValue(BigDecimal.class);
        assertThat("bitcoin value in usd is zero or greater", singleBitcoinInUsdValue, is(greaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void itShouldNotFetchUnsupportedExchangeRate() {
        try {
            // hopefully kraken will never return factor "1" for btc/btc pair :D
            ExchangeRate ignoredOnPurpose = this.sut.getExchangeRate("BTC", "BTC");

            Assert.fail("Should have thrown exception");
        } catch (CurrencyConversionException e) {
            assertThat(e, is(notNullValue()));
        }
    }

    @Test
    public void itShouldFetchSupportedCurrencyConversion() {
        ConversionQuery conversionQuery = ConversionQueryBuilder.of()
                .setBaseCurrency("BTC")
                .setTermCurrency("USD")
                .build();

        CurrencyConversion currencyConversion = this.sut.getCurrencyConversion(conversionQuery);
        assertThat(currencyConversion, is(notNullValue()));

        ExchangeRateProvider exchangeRateProvider = currencyConversion.getExchangeRateProvider();
        assertThat(exchangeRateProvider.isAvailable("BTC", "USD"), is(true));
        assertThat(exchangeRateProvider.isAvailable(conversionQuery), is(true));

        ProviderContext context = exchangeRateProvider.getContext();
        assertThat(context, is(this.sut.getContext()));

        ExchangeRate exchangeRate = exchangeRateProvider.getExchangeRate(conversionQuery);

        BigDecimal singleBitcoinInUsdValue = exchangeRate.getFactor().numberValue(BigDecimal.class);
        assertThat("bitcoin value in usd is zero or greater", singleBitcoinInUsdValue, is(greaterThanOrEqualTo(BigDecimal.ZERO)));
    }

    @Test
    public void itShouldNotFetchUnsupportedCurrencyConversion() {
        // hopefully kraken will never return factor "1" for btc/btc pair :D
        ConversionQuery conversionQuery = ConversionQueryBuilder.of()
                .setBaseCurrency("BTC")
                .setTermCurrency("BTC")
                .build();

        CurrencyConversion currencyConversion = this.sut.getCurrencyConversion(conversionQuery);
        assertThat(currencyConversion, is(notNullValue()));

        ExchangeRateProvider exchangeRateProvider = currencyConversion.getExchangeRateProvider();
        assertThat(exchangeRateProvider.isAvailable("BTC", "BTC"), is(false));
        assertThat(exchangeRateProvider.isAvailable(conversionQuery), is(false));

        try {
            ExchangeRate exchangeRate = currencyConversion.getExchangeRate(Money.of(BigDecimal.ONE, "BTC"));

            Assert.fail("Should have thrown exception");
        } catch (CurrencyConversionException e) {
            assertThat(e, is(notNullValue()));
        }
    }
}