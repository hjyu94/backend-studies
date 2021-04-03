package hellojpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try {
            /*// 1. 회원 등록
            Member member = new Member();
            member.setId(1L);
            member.setName("HelloA");

            em.persist(member);*/

            /*// 2. 회원 수정
            Member findMember = em.find(Member.class, 1L);
            System.out.println("findMember = " + findMember);*/

            /*// 3. 회원 삭제
            Member findMember = em.find(Member.class, 1L);
            em.remove(findMember);*/

            // 4. 회원 수정
            Member findMember = em.find(Member.class, 1L);
            findMember.setName("HelloJPA");
            // em.persist(findMember);
            // 안해줘도 jpa 를 통해서 엔티티를 가지고 오면 엔티티를 관리하고 있다가
            // 트랜잭션을 커밋하는 시점에 체크를 함
            // 뭔가 수정이 된게 있다면 알아서 업데이트 쿼리를 날려준다

            // * JPQL 객체 지향 쿼리
            // 젇대 테이블을 대상으로 쿼리를 짜는게 아니라 [객체]를 대상으로 쿼리를 작성한
            List<Member> result = em.createQuery("select m from Member as m", Member.class)
                    .getResultList();

            for(Member member : result) {
                System.out.println(member);
            }

            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        emf.close();
    }

}
