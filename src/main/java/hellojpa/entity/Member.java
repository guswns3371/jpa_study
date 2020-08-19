package hellojpa.entity;

import javax.persistence.*;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    // 권장 : Long + 대체 키 + 키 생성전략 사용

    @Column(name = "USERNAME")
    private String name;
    private int age;

//    @Column(name = "TEAM_ID")
//    private Long teamId;

    @ManyToOne(fetch = FetchType.LAZY)
    // EAGER은 비추 => 왠만하면 지연로딩을 사용해라 => ManyToOne,OneToOne 기본이 EAGER이므로 LAZY로 설정해줘라
    // Lazy : 지연로딩 :권장한다 : member조회시 team은 같이 조회되지 않는다 => 대신 team은 proxy객체라는 가짜 객체로 대체된다
    // member.getTeam().getName() 할때! = 실제 team을 사용하는 시점에서 team객체가 초기화된다.
    @JoinColumn(name = "TEAM_ID") // jpa 에게 연관관계 매핑한다는걸 알려준다
    private Team team;

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

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

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", team=" + team +
                '}';
    }
}
