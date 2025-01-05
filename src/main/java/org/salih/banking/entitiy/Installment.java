package org.salih.banking.entitiy;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.salih.banking.enums.StatusEnum;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
@Table(name = "installment")
@NoArgsConstructor
@AllArgsConstructor
public class Installment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private BigDecimal amount;

    private BigDecimal paidAmount;

    private StatusEnum status;

    private LocalDate dueDate;

    private LocalDate paymentDate;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "loanId")
    private Loan loan;

    @Column(updatable = false)
    @CreationTimestamp
    private LocalDate createdAt;

    @UpdateTimestamp
    private LocalDate updatedAt;

    public Installment(StatusEnum statusEnum, LocalDate localDate) {
        this.status = statusEnum;
        this.dueDate = localDate;
    }
}
