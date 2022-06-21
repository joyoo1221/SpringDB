package hello.jdbc.connection;

//생성 못 하게 abstract로 만들기
//상수를 모아놔서 쓴 것이기 때문에 객체를 생성하면 안 된다. -> abstract
public abstract class ConnectionConst {

    public static final String URL = "jdbc:h2:tcp://localhost/~/test";
    public static final String USERNAME = "sa";
    public static final String PASSWORD = "";
}
