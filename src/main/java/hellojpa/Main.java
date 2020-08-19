package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.MemberType;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory emf =
                Persistence.createEntityManagerFactory("hello");
        // persistence.xml 파일에서 <persistence-unit name="hello"> 의 hello를 가져온다다

        EntityManager em = emf.createEntityManager();
        // 하나만 만들어서 공유해야 한다, 다만 쓰레드간에 공유하면 안된다(사용 후 버려야한다)
        // JAP 의 모든 변경은 Transactional 어노테이션 안에서

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            // 팀 저장
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            // 회원 저장
            Member member = new Member();
            member.setName("hello");
//            member.setTeam(team); // 대박사건 사건대박
            em.persist(member);

//            team.getMembers().add(member); // 이렇게 하면 안된다는 것! : 주인에게만 추가를 해준다
            member.setTeam(team); // : 주인에게만 추가를 해준다

            //
            em.flush();
            em.clear();

            //조회
            Member findMember = em.find(Member.class, member.getId());

            //참조를 사용하여 연관관계 조회
//            Team findTeam = findMember.getTeam();
//
//            List<Member> members = findTeam.getMembers();
//            for (Member member1 : members) {
//                System.out.println("member1 = "+member1.toString());
//            }

            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close();
        }

        emf.close();

    }
}
