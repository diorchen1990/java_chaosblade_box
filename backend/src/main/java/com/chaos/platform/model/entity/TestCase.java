@Entity
@Data
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String name;
    private String description;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;  // 用例所有者
    
    @Column(name = "parent_id")
    private Long parentId;  // 复制/复用来源ID
    
    private boolean shared;  // 是否共享
    
    @Column(columnDefinition = "TEXT")
    private String config;  // 用例配置JSON
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
} 