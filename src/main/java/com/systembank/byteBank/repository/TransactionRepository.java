package com.systembank.byteBank.repository;

import com.systembank.byteBank.models.Transaction;
import com.systembank.byteBank.models.TransactionType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID>, JpaSpecificationExecutor<Transaction> {

    List<Transaction> findByType(TransactionType type);

    List<Transaction> findByDateTimeAfter(LocalDateTime date);
}
