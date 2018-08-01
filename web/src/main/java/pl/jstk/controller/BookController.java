package pl.jstk.controller;

import java.util.List;

import javax.annotation.security.RolesAllowed;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import pl.jstk.constants.ModelConstants;
import pl.jstk.constants.ViewNames;
import pl.jstk.service.BookService;
import pl.jstk.to.BookTo;

@Controller

public class BookController {

	@Autowired
	BookService bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String findAllBooks(Model model) {
		List<BookTo> listOfAllBooks = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, listOfAllBooks);
		return ViewNames.BOOKS;

	}

	@RolesAllowed("ADMIN")
	@RequestMapping(value = "/books/remove/{bookId}", method = RequestMethod.GET)
	public String deleteBook(@RequestParam("id") Long id, Model model) {
		bookService.deleteBook(id);
		return findAllBooks(model);

	}

	@RolesAllowed({ "ADMIN", "USER" })
	@RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
	public String showBook(@RequestParam(value = "id", defaultValue = "") Long id, Model model) {

		BookTo bookById = bookService.getBookById(id);

		model.addAttribute(ModelConstants.BOOK, bookById);

		return ViewNames.BOOK;
	}

	@RolesAllowed("ADMIN")
	@RequestMapping(value = "/books/add", method = RequestMethod.GET)
	public String addBookToCollection(Model model) {
		model.addAttribute("newBook", new BookTo());
		return "addBook";
	}

	@RolesAllowed("ADMIN")
	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String saveBook(@ModelAttribute BookTo bookTo, Model model) {
		model.addAttribute("newBook", new BookTo());
		bookService.saveBook(bookTo);
		return findAllBooks(model);

	}

	@RolesAllowed({ "ADMIN", "USER" })
	@RequestMapping(value = "/books/find", method = RequestMethod.GET)
	public String findBook(Model model) {
		model.addAttribute("foundBook", new BookTo());
		return "findBook";
	}

	@RolesAllowed("ADMIN")
	@RequestMapping(value = "findBook", method = RequestMethod.GET)
	public String findBook(@ModelAttribute("foundBook") BookTo foundBook, Model model) {
		model.addAttribute("books", bookService.findBookByTitleOrAuthor(foundBook));
		return ViewNames.FOUNDBOOK;
	}

}