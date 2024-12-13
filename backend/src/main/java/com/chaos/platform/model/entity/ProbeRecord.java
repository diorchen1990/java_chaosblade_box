@Entity
@Data
public class ProbeRecord {
    @Id
    private String probeId;
    
    private String target;
    private String targetType;
    private String status;
    private LocalDateTime installTime;
    private LocalDateTime lastHeartbeat;
    
    @ElementCollection
    private Map<String, String> metadata;
} 