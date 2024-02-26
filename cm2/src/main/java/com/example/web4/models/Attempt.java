package com.example.web4.models;

import com.example.web4.dto.HitResult;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "attempts")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Attempt {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;
    @Column(name = "x")
    private Double x;
    @Column(name = "y")
    private Double y;
    @Column(name = "r")
    private Double r;
    @Column(name = "hit")
    private Integer hit;
    @Column(insertable=false, updatable=false)
    private Long user_id;
    @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
    @JoinColumn
    private User user;


    public Attempt(Double x, Double y, Double r, Integer hit, User user) {
        this.x = x;
        this.y = y;
        this.r = r;
        this.hit = hit;
        this.user_id = user.getId();
        this.user = user;
    }
    public HitResult toHitResult() {
        return new HitResult(x, y, r, hit.equals(1) ? true : false);
    }

}
