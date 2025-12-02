package ru.otus.entity;


import jakarta.annotation.Nonnull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("phone")
public record Phone(
        @Id
        @Column("id") Long id,
        @Nonnull String number


) {
}