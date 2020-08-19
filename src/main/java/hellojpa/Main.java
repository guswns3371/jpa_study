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
        // EntityManager 가 영속성 컨텍스트에 접근한다
        // 영속성 컨택스트

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {

            // 팀 저장
            Team team = new Team();
            team.setName("teamA");
            em.persist(team); // 영속성 컨텍스트에 접근

            // 회원 저장 : 객체를 생성한 상태(비영속:new/transient)
            Member member = new Member();
            member.setName("hello");

            // 객체를 저장한 상태(영속:managed) : 1차 캐시에 저장됨 : 아직 INSERT SQL 이 DB로 날아가지 않음, SQL이 누적된 상태로 남는다
            em.persist(member);

            team.getMembers().add(member); // 이렇게 하면 안된다는 것! : 주인에게 추가를 해준다
            member.setTeam(team); // : 주인에게만 추가를 해준다
            // 하지만 실제 코드에서는 양쪽 다 입력해준다 : 객체관계를 고려

            em.flush();
            // flush 할 떄 실제 쿼리들이 DB에 날라간다 , 변경감지(더티체킹)이 일어난다.
            // em.createQuery 하면 자동으로 flush 된다
            // 영속성 컨택스트를 비우는게 아니라 => 영속성 컨택스트의 변경내용을 디비에 동기화한다.
            em.clear(); // 영속성 컨택스트 완전히 초기화: 영속성 컨택스트에서 캐쉬를 다 지워버린다


            //조회 : 1차 캐시에서 조회
            Member findMember = em.find(Member.class, member.getId()); // 영속상태
//            em.close(); // 영속성 컨택스트 종료
            Team findTeam = findMember.getTeam(); // Member에서 Team은 LAZY로 fetch되어있으므로 findTeam 객체는 proxy객체(가짜객체)가 담긴다
//            System.out.println("findTeam = "+findTeam);
            //  em.close()하고 findTeam을 건들면 LazyInitializatioinException에러 터진다
            //  => findTeam.getName() 하는 순간에 findTeam 객체에 진짜 team 객체가 담긴다

//           em.detach(findMember); // 준영속 상태로 전환한다 => findMember : 더이상 디비에 반영 안된다, 변경감지가 다 안됨됨
//            findMember.setName("t아카데미"); // 이것만 하고나서, commit() 해주면 알아서 반영된다 ㅁㅊ

            //참조를 사용하여 연관관계 조회
//            Team findTeam = findMember.getTeam();
//            List<Member> members = findTeam.getMembers();
//            for (Member member1 : members) {
//                System.out.println("member1 = "+member1.toString());
//            }


            // 커밋하는 순간 디비에 INSERT SQL을 보낸다
            // commit : 자동으로 DB에에 flush 후 comit 한다 [트랜잭션 commit]
            // 자동으로 update쿼리가 나가므로 em.update(member)와 같은 코드가 필요 없다.
            tx.commit();

        }catch (Exception e){
            tx.rollback();
        }finally {
            em.close(); // 영속성 컨택스트 종료
        }

        emf.close();
    }
}
