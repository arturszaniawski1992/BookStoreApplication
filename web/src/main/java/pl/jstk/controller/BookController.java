package pl.jstk.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
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

	@RequestMapping(value = "/{title}", method = RequestMethod.GET)
	public String findBooksByTitle(@RequestParam("title") String title, Model model) {
		List<BookTo> booksFoundByTitle = bookService.findBooksByTitle(title);
		model.addAttribute(ModelConstants.BOOKBYTITLE, booksFoundByTitle);
		return ViewNames.BOOKS;
	}

	@RequestMapping(value = "/{author}", method = RequestMethod.GET)
	public String findBooksByAuthor(@RequestParam("author") String author, Model model) {
		List<BookTo> booksFoundByAuthor = bookService.findBooksByAuthor(author);
		model.addAttribute(ModelConstants.BOOKBYAUTHOR, booksFoundByAuthor);
		return ViewNames.BOOKS;

	}

}