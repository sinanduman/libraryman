package com.sinanduman.library.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer bookId;

    @Size(max = 100)
    @NotBlank(message = "Name field can't be empty!")
    private String isbn;

    @Size(max = 100)
    @NotBlank(message = "Title field can't be empty!")
    private String title;

    @Size(max = 100)
    @NotBlank(message = "Author field can't be empty!")
    private String author;

    @Size(max = 100)
    @NotBlank(message = "Category field can't be blank!")
    private String category;

    @Size(max = 100)
    @NotBlank(message = "Publisher field can't be blank!")
    private String publisher;

    @NotNull(message = "Date must be in yyyy-mm-ddthh:mi:ss format!")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime publishingDate;

    @Size(max = 100)
    @NotBlank(message = "Edition field can't be blank!")
    private String edition;
}
