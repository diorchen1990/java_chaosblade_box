@Entity
@Data
public class SystemConfig {
    @Id
    private String key;
    
    @Column(columnDefinition = "TEXT")
    private String value;
    
    private String description;
    private String category;
    private String type;  // STRING, NUMBER, BOOLEAN, JSON
    private boolean editable;
    private LocalDateTime updatedAt;
    
    @Version
    private Long version;
} 