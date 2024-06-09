insert into authors(full_name)
values ('Author_First'), ('Author_Second'), ('Author_Third');

insert into genres(name)
values ('Genre_First'), ('Genre_Second'), ('Genre_Third');

insert into books(title, author_id, genre_id)
values ('BookTitle_1', 1, 1), ('BookTitle_2', 2, 2), ('BookTitle_3', 3, 3);

insert into book_comments(comment, book_id)
values ('good book', 1), ('i like', 2), ('already read it', 2), ('so so', 3);