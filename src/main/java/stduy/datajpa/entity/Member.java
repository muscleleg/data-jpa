package stduy.datajpa.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "userName", "age"})
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String userName;
    private int age;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    public Member(String name) {
        this.userName = name;
    }

    public Member(String name, int age, Team team) {
        this.userName = name;
        this.age = age;
        if (team != null) {
            changeTeam(team);
        }
    }

    public Member(String name, int age) {
        this.userName = name;
        this.age = age;

    }

    public void changeTeam(Team team) {
        this.team = team;
        team.getMembers().add(this);
    }





}
