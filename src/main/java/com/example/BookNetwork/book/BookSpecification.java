package com.example.BookNetwork.book;

import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class BookSpecification {
    // Specification-->functional interface in Spring Data JPA used to define dynamic, type-safe queries.
    // Root--> Represents the root of the query (the entity being queried,Book)
    //criteriaBuilder.equal(...): Creates a predicate (a condition) that checks if the id of the owner matches the provided ownerId.

    public static Specification<Book> withOwner(BigDecimal ownerId){
        return ((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("owner").get("id"),ownerId));
    }
}
