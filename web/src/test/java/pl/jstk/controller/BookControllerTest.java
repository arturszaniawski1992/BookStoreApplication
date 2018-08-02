package pl.jstk.controller;

import static org.hamcrest.core.StringContains.containsString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.testSecurityContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.enumerations.BookStatus;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@AutoConfigureMockMvc

public class BookControllerTest {
	@Mock
	BookService bookService;

	@Autowired
	private FilterChainProxy filterProxy;

	@Autowired
	private WebApplicationContext webApplicationContext;

	@Autowired
	BookController bookController;

	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(new BookController()).build();
		MockitoAnnotations.initMocks(bookService);
		Mockito.reset(bookService);
		this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).addFilters(filterProxy).build();
		ReflectionTestUtils.setField(bookController, "bookService", bookService);
	}

	@Test
	public void shouldTestGetForBooks() throws Exception {
		// given
		List<BookTo> list = new ArrayList<>();
		when(bookService.findAllBooks()).thenReturn(list);
		// when
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		resultActions.andExpect(view().name("books")).andExpect(model().attribute(ModelConstants.BOOKLIST, list));
		verify(bookService, times(1)).findAllBooks();
	}

	@Test
	@WithMockUser(username = "user", roles = { "ADMIN" })
	public void shouldTestGetBookById() throws Exception {
		// given
		BookTo book = new BookTo();
		book.setId(1L);

		when(bookService.getBookById(1L)).thenReturn(book);
		// when
		ResultActions resultActions = mockMvc.perform(get("/books/book/?id=1").with(testSecurityContext()));
		// then
		resultActions.andExpect(view().name(ViewNames.BOOK)).andExpect(model().attribute(ModelConstants.BOOK, book));
		verify(bookService, times(1)).getBookById(1L);
	}

	@Test
	@WithMockUser(username = "user", roles = { "ADMIN" })
	public void shouldTestGetForAddBook() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/add"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.ADDBOOK)).andDo(print())
				.andExpect(content().string(containsString("")));
		;
	}

	@Test
	@WithMockUser(username = "user", roles = { "ADMIN" })
	public void shouldTestPostForAddBook() throws Exception {
		// given
		BookTo bookTo = new BookTo(1L, "title", "authors", BookStatus.FREE);
		when(bookService.saveBook(bookTo)).thenReturn(bookTo);
		// when
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", bookTo));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.ADDBOOK))
				.andExpect(model().attribute(ModelConstants.BOOK, bookTo)).andDo(print())
				.andExpect(content().string(containsString("")));

	}

	@Test
	public void shouldCatchExceptionTestPostForAddBook() throws Exception {
		// given
		BookTo bookTo = new BookTo(1L, "title", "authors", BookStatus.FREE);
		// when
		ResultActions resultActions = mockMvc.perform(post("/books/add").flashAttr("newBook", bookTo));
		// then
		resultActions.andExpect(view().name("addBook"))
				.andExpect(model().attribute(ModelConstants.ERRORMESSAGE, "You cant add book without all details"));
		verify(bookService, times(1)).saveBook(bookTo);
	}

	@Test
	public void shouldTestMethodGetForViewFind() throws Exception {
		// given when
		ResultActions resultActions = mockMvc.perform(get("/books/find"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOK)).andDo(print())
				.andExpect(content().string(containsString("")));
	}

	@Test
	public void shouldNotDeleteBookWhenBookDontExist() throws Exception {
		// given
		List<BookTo> books = new ArrayList<>();
		when(bookService.findAllBooks()).thenReturn(books);
		// when
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS)).andDo(print())
				.andExpect(content().string(containsString("")));

	}

	@Test
	public void shouldDeleteBookWhenBookExist() throws Exception {
		// given when
		BookTo book = new BookTo();
		book.setTitle("Title");
		Mockito.when(bookService.getBookById(Mockito.anyLong())).thenReturn(book);
		ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.delete("/books/remove?id=1"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS)).andDo(print())
				.andExpect(content().string(containsString("")));
	}

	@Test
	public void shouldTestGetForGetSpecificBookView() throws Exception {
		// given
		BookTo book = new BookTo();
		when(bookService.getBookById(1L)).thenReturn(book);
		// when
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS)).andDo(print())
				.andExpect(content().string(containsString("")));

	}

	@Test
	public void shouldTestBooksPage() throws Exception {
		// given
		// when
		ResultActions resultActions = mockMvc.perform(get("/books"));
		// then
		resultActions.andExpect(status().isOk()).andExpect(view().name(ViewNames.BOOKS)).andDo(print())
				.andExpect(content().string(containsString("")));
	}

}
