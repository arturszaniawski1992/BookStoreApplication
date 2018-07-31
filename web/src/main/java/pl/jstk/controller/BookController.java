package pl.jstk.controller;

import java.util.List;

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
	protected static final String ADDBOOK = "Boos has been added!";
	protected static final String REMOVEDBOOK = "Book has been removed!";

	@Autowired
	BookService bookService;

	@RequestMapping(value = "/books", method = RequestMethod.GET)
	public String findAllBooks(Model model) {
		List<BookTo> listOfAllBooks = bookService.findAllBooks();
		model.addAttribute(ModelConstants.BOOKLIST, listOfAllBooks);
		return ViewNames.BOOKS;

	}

	@RequestMapping(value = "/books/remove/{bookId}", method = RequestMethod.GET)
	public String deleteBook(@RequestParam("id") Long id, Model model) {
		bookService.deleteBook(id);
		return findAllBooks(model);

	}
	
	@RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
	public String showBook(@RequestParam(value = "id", defaultValue = "") Long id, Model model) {

		BookTo bookById = bookService.getBookById(id);
		
		model.addAttribute(ModelConstants.BOOK, bookById);

		return ViewNames.BOOK;
	}

	/*@RequestMapping(value = "/books/{bookId}", method = RequestMethod.GET)
	public String findBooksById(@RequestParam("id") Long id, Model model) {
		bookService.getBookById(id);
		return ViewNames.BOOKS;


	}*/


	@RequestMapping(value = "/books/add", method = RequestMethod.GET)
	public String addBookToCollection(Model model) {
		model.addAttribute("newBook", new BookTo());
		return "addBook";
	}

	@RequestMapping(value = "/greeting", method = RequestMethod.POST)
	public String saveBook(@ModelAttribute BookTo bookTo, Model model) {
		model.addAttribute("newBook", new BookTo());
		bookService.saveBook(bookTo);

		return findAllBooks(model);

	}

	@RequestMapping(value = "/books/find", method = RequestMethod.GET)
	public String findBook(Model model) {
		model.addAttribute("findBook", new BookTo());
		return "findBook";
	}

	@RequestMapping(value = "/findBook", method = RequestMethod.GET)
	public List<BookTo> findBookByTitleOrAuthor(@RequestParam(value = "title", required = false) String title,
			@RequestParam(value = "author", required = false) String author) {
		List<BookTo> listOfAllBooks = bookService.findAllBooks();
		if (title != "" && author == "") {
			List<BookTo> listByTitle = bookService.findBooksByTitle(title);
			return listByTitle;
		} else if (title == " " && author != " ") {
			List<BookTo> listByAuthor=bookService.findBooksByAuthor(author);
			return listByAuthor;

		} else return listOfAllBooks;
	}
}
