package hello.jdbc.domain;

import lombok.Data;

@Data
public class Member {

    private String memberId; //회원 id
    private int money; //회원이 가지고 있는 금액

    public Member() { //기본 생성자
    }

    public Member(String memberId, int money) {
        this.memberId = memberId;
        this.money = money;
    }
}
