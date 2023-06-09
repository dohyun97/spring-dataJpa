package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"})
public class Member extends BaseEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;

    public Member(String username) {
        this(username,0);
    }

    public Member(String username,int age){
        this(username,age,null);
    }

    public Member(String username,int age, Team team){
        this.username = username;
        this.age = age;
//        if(team == null){
//            throw new IllegalArgumentException("Please choose team");
//        }else {
//            changeTeam(team);
//        }
        if(team != null){
            changeTeam(team);
        }
    }

    public void changeTeam(Team team){
        this.team =team;
        team.getMembers().add(this);
    }

    public void changeInfo(String username,int age){
        this.username = username;
        this.age =age;
    }
}
