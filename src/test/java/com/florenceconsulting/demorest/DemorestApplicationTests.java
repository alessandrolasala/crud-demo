package com.florenceconsulting.demorest;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.florenceconsulting.demorest.model.User;
import com.florenceconsulting.demorest.service.UserService;

@SpringBootTest
@AutoConfigureMockMvc
class DemorestApplicationTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@Autowired
	private ObjectMapper objectMapper;

	@Test
	public void verifyCreatedUser() throws Exception {

		User user = new User();
		user.setCodiceFiscale("FLFZFC32B04F003R");
		user.setEmail("alessandro@lasala.it");
		user.setFirstName("Alessandro");
		user.setLastName("La Sala");

		given(userService.saveUser(any(User.class))).willAnswer((invocation) -> invocation.getArgument(0));

		ResultActions response = mockMvc.perform(post("/v1/api/users").contentType(MediaType.APPLICATION_JSON).content(objectMapper.writeValueAsString(user)));

		response.andDo(print()).andExpect(status().isOk()).andExpect(jsonPath("$.firstName", is(user.getFirstName()))).andExpect(jsonPath("$.lastName", is(user.getLastName()))).andExpect(jsonPath("$.email", is(user.getEmail())));
	}

	@Test
	public void verifyDeletedUser() throws Exception {
		User user = new User();
		user.setCodiceFiscale("FLFZFC32B04F003R");
		user.setEmail("alessandro@lasala.it");
		user.setFirstName("Alessandro");
		user.setLastName("La Sala");
		userService.saveUser(user);

		ResultActions response = mockMvc.perform(delete("/v1/api/users/{id}", user.getId()));

		response.andExpect(status().isOk()).andDo(print());
	}

}
