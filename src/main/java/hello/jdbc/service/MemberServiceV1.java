package hello.jdbc.service;

import hello.jdbc.domain.Member;
import hello.jdbc.repository.MemberRepositoryV1;
import lombok.RequiredArgsConstructor;

import java.sql.SQLException;

@RequiredArgsConstructor
public class MemberServiceV1 {

    private final MemberRepositoryV1 memberRepository;

    //계좌이체 로직
    public void accountTransfer(String fromId, String toId, int money) throws SQLException {
        //트랜잭션 시작
        Member fromMember = memberRepository.findById(fromId);
        Member toMember = memberRepository.findById(toId);

        //from의 돈을 깎고
        memberRepository.update(fromId, fromMember.getMoney() - money);
        //! 중간에 오류 케이스 만들기 !
        validation(toMember);
        //to의 돈을 올리기(= 계좌이체)
        memberRepository.update(toId, toMember.getMoney() + money);

        //커밋, 롤백 판단
    }

    private void validation(Member toMember) {
        if (toMember.getMemberId().equals("ex")) {
            throw new IllegalStateException("이체 중 예외 발생");
        }
    }
}
