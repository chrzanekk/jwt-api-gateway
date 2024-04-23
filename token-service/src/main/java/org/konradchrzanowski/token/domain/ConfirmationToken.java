package org.konradchrzanowski.token.domain;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "confirmation_tokens")
public class ConfirmationToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @Column(name = "confirmation_token")
    private String confirmationToken;

    @JoinColumn(name = "user_id")
    private Long userId;

    @JoinColumn(name = "user_email")
    private String userEmail;

    @Column(name = "creation_date")
    private LocalDateTime createDate;

    @Column(name = "expire_date")
    private LocalDateTime expireDate;

    @Column(name = "confirm_date")
    private LocalDateTime confirmDate;
}
