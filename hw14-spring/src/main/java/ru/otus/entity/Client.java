package ru.otus.entity;


import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;


@Table("client")
public record Client(
        @Id @Column("id") Long id,
        @Nonnull String name,
        @MappedCollection(idColumn = "client_id") Address address,

        @MappedCollection(idColumn = "client_id", keyColumn ="client_key") List<Phone> phones

) {
}