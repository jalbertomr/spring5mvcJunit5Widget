## Spring Framework 5, Spring MVC, H2, Thymeleaf, JUnit5 Tests
Simple CRUD application with thymeleaf frontend for simple
entity

EndPoints for REST (@RestController) , and thymeleaf frontend (@Controller)

#### Junit5 Test Service, Controller, RESTController level

##### Test on Service Level

- use @SpringBootTest annotation on class to test.

      @SpringBootTest
- Inject service to test

      @Autowired
      FooService fooService;
  
- Mock the repository

      @MockBean
      BarRepository barRepository;
      
- In Function to test do the steps
  
      // Setup mocked repository      
         //declare and fill object to return
         ReturnObject returnObject = new ReturnObject( params );
         doReturn( returnObject ).when( mockedRepository ).functionOfMockedTocall( parametersOfFunction );
         
      // Call service under test
         returnObject = fooService.functionOfServiceToTest( parameters );

      // Assert the responses
         Assertions.assertThat( ... )
         Assertions.assertEquals( ... )
      // What ever of org.junit.jupiter.api.Assertions...
      
##### Test on RestController Level      

- use @SpringBootTest annotation on class to test.

      @SpringBootTest
      @AutoConfigureMockMvc
      
- Mock the service which use the RESTController

      @MockBean
      FooServiceImpl fooService;
  
- Autowired MockMvc 

      @Autowired
      MockMvc mockMvc;
      
- In Function to test the call of the RestController endpoint do the steps
  
      // Setup mocked service
         //declare and fill object to return
         ReturnObject returnObject = new ReturnObject( params );
         doReturn( returnObject ).when( mockedService ).functionOfMockedTocall( parametersOfFunction );
         
      // Excecute the Request 
      // execute POST request with content type and body
         mockMvc.perform(post("/rest/widget")
               .contentType(MediaType.APPLICATION_JSON)
               .content(asJsonString(widgetPost)))
               // validate response code
              .andExpect(status().isCreated())
              .andExpect(content().contentType(MediaType.APPLICATION_JSON))
              // validate headers
              .andExpect(header().string(HttpHeaders.LOCATION, "/rest/widget/1"))
              .andExpect(header().string(HttpHeaders.ETAG, "\"1\""))
              // validate return fields
              .andExpect(jsonPath("$.id", is(1)))
              .andExpect(jsonPath("$.name", is("name1")))
              .andExpect(jsonPath("$.description", is("Description1")))
              .andExpect(jsonPath("$.version", is(1)));
