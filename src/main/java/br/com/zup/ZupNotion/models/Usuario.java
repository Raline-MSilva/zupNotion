package br.com.zup.ZupNotion.models;

import br.com.zup.ZupNotion.models.enums.Role;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "usuarios")
@Data
public class Usuario {

    @Id
    @GeneratedValue(generator = "system-uuid")
    @GenericGenerator(name = "system-uuid", strategy = "uuid")
    private String id;
    @Column(nullable = false, unique = true)
    private String email;
    private String sobrenome;
    @Column
    private String nome;
    @Column(nullable = false)
    private String senha;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE})
    private List<Tarefa> tarefas;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Column(nullable = false)
    private String perguntaDeSeguranca;
    @Column(nullable = false)
    private String respostaDeSeguranca;

}
