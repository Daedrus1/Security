INSERT INTO categories (id, name, description, is_deleted)
VALUES (1, 'cat-1', 'test category', false);

INSERT INTO books (id, title, author, description, price, isbn)
VALUES (1, 'Book Title', 'Author', 'Desc', 10.50, 'ISBN-1');

INSERT INTO books_categories (book_id, category_id)
VALUES (1, 1);
