@Entity
@Data
public class ExperimentRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String experimentId;  // ChaosBlade 实验ID
    private String name;         // 实验名称
    private String type;         // 故障类型
    
    @Convert(converter = JpaJsonConverter.class)
    private Map<String, Object> params;  // 实验参数
    
    private String status;      // 实验状态
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    
    @Column(columnDefinition = "TEXT")
    private String result;      // 实验结果
} 