package com.ll.backend.global.jpa.entity

import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType.IDENTITY
import jakarta.persistence.Id
import jakarta.persistence.MappedSuperclass
import org.springframework.data.domain.Persistable

@MappedSuperclass
abstract class BaseEntity : Persistable<Long> {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    var id: Long = 0

    override fun getId(): Long = id

    override fun isNew(): Boolean = id == 0L

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as BaseEntity

        if (id == null) return false

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}