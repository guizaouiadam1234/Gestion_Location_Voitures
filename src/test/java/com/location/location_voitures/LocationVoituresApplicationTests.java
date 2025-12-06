package com.location.location_voitures;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.data.mongodb.uri=mongodb://localhost:27017/location-voitures-test")
class LocationVoituresApplicationTests {

	@Test
	void contextLoads() {
	}

}
