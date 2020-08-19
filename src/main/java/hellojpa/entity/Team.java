package hellojpa.entity;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Team {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") // mappedBy로 연관관계의 주인 Owner를 지정 => 왜래키가 있는 곳을 주인으로 정해라
    // mappedBy 가 있는 이유: 객체와 테이블간 연관관계를 맺는 차이를 이해하라
    // 테이블은 양 방향이 존재하지만! 객체는 서로 다른 단방향 관계 2개이다
    // 양방향 연관관계에선 객체 둘중 하나만 Owner가 되어 update를 가능하게 한다. 주인 아닌 쪽은 읽기만 가능,
    // 주인은 mappedBy 속성 사용 ㄴㄴ, 주인 아닌 쪽에서 mappedBy로 주인 지정
    // 보통 먼저 단방향으로 설계하고, 이후에 양방향이 필요하다면 나중에 추가해라
    private List<Member> members = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Member> getMembers() {
        return members;
    }

    public void setMembers(List<Member> members) {
        this.members = members;
    }
}
