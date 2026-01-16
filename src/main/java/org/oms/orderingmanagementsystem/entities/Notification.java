package org.oms.orderingmanagementsystem.entities;

import lombok.Data;
import java.time.LocalDateTime;
import jakarta.persistence.*;

@Entity
@Data // Dòng này sẽ tự tạo Getter/Setter cho id, title, body, createdAt
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