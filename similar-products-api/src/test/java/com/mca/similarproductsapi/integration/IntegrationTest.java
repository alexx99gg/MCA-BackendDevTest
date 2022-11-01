package com.mca.similarproductsapi.integration;

import com.google.gson.Gson;
import com.mca.similarproductsapi.SimilarProductsApiApplication;
import com.mca.similarproductsapi.infrastructure.response.SimilarProducts;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockserver.client.MockServerClient;
import org.mockserver.model.Delay;
import org.mockserver.model.MediaType;
import org.mockserver.springtest.MockServerTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = {SimilarProductsApiApplication.class})
@ActiveProfiles("test")
@AutoConfigureMockMvc
@MockServerTest("server.url=http://localhost:${mockServerPort}")
class IntegrationTest {

  Gson gson = new Gson();
  @Value("${server.url}")
  private String serverUrl;
  @Autowired
  private MockMvc mockMvc;

  private MockServerClient mockServerClient;

  @BeforeEach
  void beforeAll() {
    // Similar ids
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1/similarids"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody("[2,3,4]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/2/similarids"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody("[3,10,100]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/3/similarids"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody("[100,1000,10000]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/4/similarids"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody("[1,2,5]"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/5/similarids"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody("[1,2,6]"));

    // Product
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"id\":\"1\",\"name\":\"Shirt\",\"price\":9.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/2"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"id\":\"2\",\"name\":\"Dress\",\"price\":19.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/3"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"id\":\"3\",\"name\":\"Blazer\",\"price\":29.99,\"availability\":false}")
            .withDelay(Delay.milliseconds(100)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/4"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"id\":\"4\",\"name\":\"Boots\",\"price\":39.99,\"availability\":true}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/5"))
        .respond(response().withStatusCode(404).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"message\":\"Product not found\"}"));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/6"))
        .respond(response().withStatusCode(500).withContentType(MediaType.APPLICATION_JSON));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/100"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
            "{\"id\":\"100\",\"name\":\"Trousers\",\"price\":49.99,\"availability\":false}")
            .withDelay(Delay.milliseconds(1000)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1000"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
                "{\"id\":\"1000\",\"name\":\"Coat\",\"price\":89.99,\"availability\":true}")
            .withDelay(Delay.milliseconds(5000)));
    mockServerClient
        .when(request().withMethod("GET").withPath("/product/1000"))
        .respond(response().withStatusCode(200).withContentType(MediaType.APPLICATION_JSON).withBody(
                "{\"id\":\"10000\",\"name\":\"Leather jacket\",\"price\":89.99,\"availability\":true}")
            .withDelay(Delay.milliseconds(50000)));
  }

  @Test
  void getSimilarProductsIntegrationTest() throws Exception {
    String jsonResult = this.mockMvc.perform(get("/product/1/similar")).andExpect(status().isOk()).andReturn().getResponse().getContentAsString();

    SimilarProducts similarProducts = gson.fromJson(jsonResult, SimilarProducts.class);


  }


}
