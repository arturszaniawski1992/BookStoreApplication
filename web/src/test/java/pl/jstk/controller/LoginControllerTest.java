package pl.jstk.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import pl.jstk.constants.ViewNames;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class LoginControllerTest {

	@Autowired
	private MockMvc mockMvc;

	private static final String LOGGED = "You have been logged in";

	@Test
	public void shouldReturnLoginView() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(get("/login"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.LOGIN));

	}

	@Test
	public void shouldReturnWelcomeViewWhenLogged() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(post("/login"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.WELCOME))
				.andExpect(model().attribute("info", LOGGED));

	}

	@Test
	public void shouldReturnStatusOkWhenLogedAsAdmin() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "admin").param("password", "admin"));
		// then
		resultActions.andExpect(status().isOk());

	}

	@Test
	public void shouldReturnStatusOkWhenLogedAsUser() throws Exception {

		// when
		ResultActions resultActions = mockMvc.perform(get("/login").contentType(MediaType.APPLICATION_FORM_URLENCODED)
				.param("username", "user").param("password", "user"));
		// then
		resultActions.andExpect(status().isOk());

	}

	
	@Test
	public void shouldLoginCorrect() throws Exception {
		// given when
		ResultActions resultActions = mockMvc
				.perform(MockMvcRequestBuilders.get("/login").param("error", "false"));
		// then
		resultActions.andExpect(status().isOk())
				.andExpect(view().name(ViewNames.LOGIN));
	}

}