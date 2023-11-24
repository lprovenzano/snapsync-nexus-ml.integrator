package com.snapsync.nexus.repository.rest;

import com.snapsync.nexus.entity.auth.Authorization;
import com.snapsync.nexus.entity.auth.Credential;
import com.snapsync.nexus.entity.auth.GrantType;
import com.snapsync.nexus.entity.payments.Payment;
import com.snapsync.nexus.exception.validation.OrderNotFoundException;
import com.snapsync.nexus.exception.validation.PayerNotFoundException;
import com.snapsync.nexus.interfaces.GenerateAuthorizationRepository;
import com.snapsync.nexus.interfaces.GetPaymentRepository;
import com.snapsync.nexus.repository.configuration.RestRepositoryTest;
import com.snapsync.nexus.utils.Util;
import com.snapsync.nexus.utils.unit.util.Json;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.method;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

class GetPaymentRestRepositoryTest extends RestRepositoryTest {
    @Autowired
    private GetPaymentRepository getPaymentRepository;

    private static final String PATH = "/v1/payments/%d";

    @Autowired
    public GetPaymentRestRepositoryTest(RestTemplate restTemplate) {
        super(restTemplate);
    }

    @Test
    @DisplayName("Given a payment, when has an order and payer, then ok")
    void test1() {
        //Arrange
        final String jsonResponse = Json.parse("/payments/responseOK.json");
        final Long paymentId = 65245438575L;
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, String.format(PATH, paymentId))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));
        //Act
        final Payment response = getPaymentRepository.execute(paymentId);

        //Assert
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getOrder());
        Assertions.assertEquals(2000006684847072L, response.getOrder().getId());
        Assertions.assertNotNull(response.getPayer());
        Assertions.assertEquals(1484081588L, response.getPayer().getId());

    }

    @ParameterizedTest
    @ValueSource(strings = {"/payments/responseOrderNull.json", "/payments/responseOrderIdNull.json"})
    @DisplayName("Given a payment, when has an order null and payer not null, then throw OrderNotFoundException")
    void test2(String json) {
        //Arrange
        final String jsonResponse = Json.parse(json);
        final Long paymentId = 65245438575L;
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, String.format(PATH, paymentId))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));
        //Act
        Assertions.assertThrows(OrderNotFoundException.class, () -> getPaymentRepository.execute(paymentId));
    }

    @ParameterizedTest
    @ValueSource(strings = {"/payments/responsePayerNull.json", "/payments/responsePayerIdNull.json"})
    @DisplayName("Given a payment, when has an order not null and payer null, then throw PayerNotFoundException")
    void test3(String json) {
        //Arrange
        final String jsonResponse = Json.parse(json);
        final Long paymentId = 65245438575L;
        mockServer.expect(requestTo(Util.getEndpoint(URL_ML, String.format(PATH, paymentId))))
                .andExpect(method(HttpMethod.GET))
                .andRespond(withSuccess(jsonResponse, MediaType.APPLICATION_JSON));
        //Act
        Assertions.assertThrows(PayerNotFoundException.class, () -> getPaymentRepository.execute(paymentId));
    }

}