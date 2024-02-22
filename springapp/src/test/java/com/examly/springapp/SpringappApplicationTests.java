package com.examly.springapp;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.web.util.NestedServletException;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import java.io.File;
import java.util.NoSuchElementException;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = SpringappApplication.class)
@AutoConfigureMockMvc
class SpringappApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@Test
	@Order(1)
	void testaddShirt() throws Exception {
		String st = "{\"shirtId\": 123, \"shirtSize\": 457, \"shirtColor\": \"browndemo\"}";
		mockMvc.perform(MockMvcRequestBuilders.post("/")
				.contentType(MediaType.APPLICATION_JSON)
				.content(st)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.shirtSize").value("457"))
				.andReturn();
	}

	@Test
	@Order(2)
	void testgetAll() throws Exception {
		mockMvc.perform(get("/")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$[?(@.shirtColor == 'browndemo')]").exists())
				.andReturn();
	}

	@Test
	@Order(3)
	void testgetByID() throws Exception {
		mockMvc.perform(get("/123")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(jsonPath("$[?(@.shirtColor == 'browndemo')]").exists())
				.andReturn();
	}

	@Test
	@Order(4)
	void testupdateDetails() throws Exception {
		String st = "{\"shirtId\": 123, \"shirtSize\": 345,\"shirtColor\": \"black\"}";
		mockMvc.perform(MockMvcRequestBuilders.put("/123")
				.contentType(MediaType.APPLICATION_JSON)
				.content(st)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(jsonPath("$.shirtSize").value(345))
				.andReturn();
	}

	@Test
	@Order(5)
	void testdeleteById() throws Exception {
		mockMvc.perform(MockMvcRequestBuilders.delete("/123")
				.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").value(true))
				.andReturn();

		try {
			mockMvc.perform(MockMvcRequestBuilders.get("/123")
					.accept(MediaType.APPLICATION_JSON))
					.andExpect(status().isNotFound());
		} catch (NestedServletException e) {
			if (e.getCause() instanceof NoSuchElementException) {
			} else {
				throw e;
			}
		}

	}

}