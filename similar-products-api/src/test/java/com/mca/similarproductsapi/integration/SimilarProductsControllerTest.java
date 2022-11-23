package com.mca.similarproductsapi.integration;

import com.google.gson.Gson;
import com.mca.similarproductsapi.SimilarProductsApiApplication;
import com.mca.similarproductsapi.infrastructure.dto.SimilarProducts;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockserver.client.server.MockServerClient;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Delay;
import org.mockserver.model.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SimilarProductsApiApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
class SimilarProductsControllerTest {

  Gson gson = new Gson();
  @Autowired
  private MockMvc mockMvc;

  private static ClientAndServer mockServer;

  @BeforeAll
  public static void startServer() {
    mockServer = startClientAndServer(3301);
  }

  @AfterAll
  public static void stopServer() {
    mockServer.stop();
  }

  @BeforeEach
  void beforeEach() {
    MockServerClient mockServerClient = new MockServerClient("localhost", 3301);
    var headers = new Header("Content-Type", "application/json; charset=utf-8");
    // Similar ids
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1/similarids"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody("[2,3,4]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/2/similarids"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody("[3,100,1000]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/3/similarids"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody("[100,1000,10000]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/4/similarids"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody("[1,2,5]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/5/similarids"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody("[1,2,6]"));

    // Product
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
            "{\"id\":\"1\",\"name\":\"Shirt\",\"price\":9.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/2"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
            "{\"id\":\"2\",\"name\":\"Dress\",\"price\":19.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/3"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
            "{\"id\":\"3\",\"name\":\"Blazer\",\"price\":29.99,\"availability\":false}")
            .withDelay(Delay.milliseconds(100)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/4"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
            "{\"id\":\"4\",\"name\":\"Boots\",\"price\":39.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/5"))
        .respond(response().withHeaders(headers).withStatusCode(404).withBody(
            "{\"message\":\"Product not found\"}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/6"))
        .respond(response().withHeaders(headers).withStatusCode(500));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/100"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
            "{\"id\":\"100\",\"name\":\"Trousers\",\"price\":49.99,\"availability\":false}")
            .withDelay(Delay.milliseconds(1000)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1000"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
                "{\"id\":\"1000\",\"name\":\"Coat\",\"price\":89.99,\"availability\":true}")
            .withDelay(Delay.milliseconds(5000)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1000"))
        .respond(response().withHeaders(headers).withStatusCode(200).withBody(
                "{\"id\":\"10000\",\"name\":\"Leather jacket\",\"price\":89.99,\"availability\":true}")
            .withDelay(Delay.milliseconds(50000)));
  }

  @ParameterizedTest
  @CsvSource({"1", "2"})
  void getSimilarProductsOkTest(String productId) throws Exception {
    String jsonResult = this.mockMvc
        .perform(get("/product/" + productId + "/similar"))
        .andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    SimilarProducts similarProducts = gson.fromJson(jsonResult, SimilarProducts.class);

    SimilarProducts expectedSimilarProducts = getJsonFromFile("json/expectedProductSimilarResponse/" + productId + ".json", SimilarProducts.class);

    Assertions.assertEquals(expectedSimilarProducts, similarProducts);
  }

  @ParameterizedTest
  @CsvSource({"3", "4", "6"})
  void getSimilarProductsNotFoundTest(String productId) throws Exception {
    String jsonResult = this.mockMvc
        .perform(get("/product/" + productId + "/similar"))
        .andExpect(status().is(404)).andReturn().getResponse().getContentAsString();
  }

  @ParameterizedTest
  @CsvSource({"5"})
  void getSimilarProductsErrorTest(String productId) throws Exception {
    String jsonResult = this.mockMvc
        .perform(get("/product/" + productId + "/similar"))
        .andExpect(status().is(500)).andReturn().getResponse().getContentAsString();
  }

  private  <T> T  getJsonFromFile(String file, Class<?> clazz){
      Reader reader = new InputStreamReader(Objects.requireNonNull(SimilarProductsControllerTest.class.getClassLoader().getResourceAsStream(file)));
      return (T) gson.fromJson(reader, clazz);
  }


}
