package org.oms.orderingmanagementsystem.entities;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Data
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String body;
    private LocalDateTime createdAt;

    public String getMessage() {
        return this.body; // Thêm dòng này vào để trả về nội dung thông báo
    }
}