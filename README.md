## Spring Framework 5, Spring MVC, H2, Thymeleaf, JUnit5 Tests, DBUnit
Simple CRUD application with thymeleaf frontend for simple
entity with test Junit5, and DBUnit

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

#### DBUnit Test

Add the respective dependencies

     <!-- DBUnit -->
        <dependency>
            <groupId>org.dbunit</groupId>
            <artifactId>dbunit</artifactId>
            <version>2.7.0</version>
            <scope>test</scope>
        </dependency>
        <dependency>
            <groupId>com.github.database-rider</groupId>
            <artifactId>rider-core</artifactId>
            <version>1.10.0</version>
        </dependency>
        <dependency>
            <groupId>com.github.database-rider</groupId>
            <artifactId>rider-junit5</artifactId>
            <version>1.10.0</version>
        </dependency>
        <dependency>
            <groupId>com.github.springtestdbunit</groupId>
            <artifactId>spring-test-dbunit</artifactId>
            <version>1.3.0</version>
        </dependency>
        
###### Setup de Database connection

To set up de dataSource is used a class for configuration with the profile test

    @Configuration
    @Profile("test")
    public class WidgetRepositoryTestConfiguration {

      public DataSource dataSource(){
          DriverManagerDataSource dataSource = new DriverManagerDataSource();
          dataSource.setDriverClassName("org.h2.driver");
          dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1");
          dataSource.setUsername("sa");
          dataSource.setPassword("");
          return dataSource;
      }
    }

 When Run the test the profile mush be set in the environment variables
        
    spring.profile.active=test
    
 ###### The test for the Widget Repository
 
  To test the WidgetRepository, the class under test is annotated and in this case is activated over the profile
  test.
 
    @ExtendWith(DBUnitExtension.class)
    @SpringBootTest
    @ActiveProfiles("test")
    class WidgetRepositoryTest {
 
     @Autowired
     private DataSource dataSource;
 
     @Autowired
     private WidgetRepository widgetRepository;
 
     public ConnectionHolder getConnectionHolder(){
         return () -> dataSource.getConnection();
     }
 
     @Test
     @DataSet("widgets.yml")
     void findAll(){
         ArrayList<Widget> widgets = Lists.newArrayList(widgetRepository.findAll());
         Assertions.assertEquals(2, widgets.size(),"Widgets size should be 2");
     }
     
     ...   
     
###### Init the data of the table for DBUnit

The data of the table on database is inserted using a yml file. and specified on the functions to test

    widget:
    - id : 1
      name : "Widget 1"
      description : "Description Widget 1"
      version : 1
    - id : 2
      name : "Widget 2"
      description : "Description Widget 2"
      version : 4     