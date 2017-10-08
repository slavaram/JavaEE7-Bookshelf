package ru.vramaz.bookshelf.web.mbeans.book;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;

import ru.vramaz.bookshelf.core.BookService;
import ru.vramaz.bookshelf.core.entity.Book;

@Named
@RequestScoped
public class BooksBean {

	@EJB
	BookService bService;

	private PaginationHelper ph;

	@PostConstruct
	private void init() {
		ph = new PaginationHelper();
		Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		if (params.containsKey("deleteId")) {
			int deleteId = Integer.parseInt(params.get("deleteId"));
			bService.removeBookCover(bService.findBookById(deleteId));
			bService.deleteBookById(deleteId);
		}
		if (params.containsKey("index")) {
			int index = Integer.parseInt(params.get("index"));
			ph.setPage(index);
		}
		if (params.containsKey("count")) {
			int count = Integer.parseInt(params.get("count"));
			ph.setCount(count);
		}
		ph.load();
	}

	public class PaginationHelper {
		private int page;
		private int count = 5;
		private List<Book> books;
		private List<Book> nextBooks;

		private void load() {
			if (page == Integer.MAX_VALUE) {
				int total = bService.getBooksCount();
				page = total%count == 0 ? total/count - 1 : (total-total%count)/count;
			}
			List<Book> curBooks = bService.findAllBooks(page*count, (page+1)*count + 1);
			books = curBooks.size() > count ? curBooks.subList(0, count) : curBooks.subList(0, curBooks.size());
			nextBooks = curBooks.size() > count ? curBooks.subList(count, curBooks.size()) : Arrays.asList();
		}
		public boolean hasPrevious() {
			return page > 0;
		}
		public boolean hasNext() {
			return nextBooks.size() > 0;
		}
		public int getPage() {
			return page;
		}
		public void setPage(int page) {
			this.page = page;
		}
		public int getCount() {
			return count;
		}
		public void setCount(int count) {
			this.count = count;
			load();
		}
		public List<Book> getBooks() {
			return books;
		}
		public void setBooks(List<Book> books) {
			this.books = books;
		}
	}

	public Book findBook(int id) {
		return bService.findBookById(id);
	}

	public List<Book> findBooks(int from, int count) {
		return bService.findAllBooks(from, count);
	}

	public PaginationHelper getPh() {
		return ph;
	}
	public void setPh(PaginationHelper ph) {
		this.ph = ph;
	}

}
